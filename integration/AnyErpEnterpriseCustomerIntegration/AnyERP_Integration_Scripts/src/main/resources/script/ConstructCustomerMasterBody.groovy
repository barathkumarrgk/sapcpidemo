import com.sap.gateway.ip.core.customdev.util.Message;
import groovy.json.JsonSlurper
import groovy.json.JsonOutput

def Message processData(Message message) {
    def body = message.getBody(java.lang.String) as String
    def jsonSlurper = new JsonSlurper()
    def jsonInput = jsonSlurper.parseText(body)
    def payload = jsonInput.groupedCustomerMaster

  // need to check this for the edge case of number of paymentRecords landing right on the split
  // number, If 501 customerMasters, the second payload wont be the same format
    def customerMasters = standardizeArrayField(payload)

    customerMasters = customerMasters.collect { it ->
      def customerMasterGroup = standardizeArrayField(it.customerMaster)

      // set common values from first object in group
      def groupedPayload = customerMasterGroup?.first()
      def newBusinessUnits = []
      def newCustomFields = []
      def newSalesHierarchy = []
 
      // construct arrays
      customerMasterGroup.each { p ->
        def businessUnits = standardizeArrayField(p?.businessUnits)
        def customFields = standardizeArrayField(p?.customFields)
        def salesHierarchy = standardizeArrayField(p?.salesHierarchy)

        if (businessUnits) {
          businessUnits.each { bu ->
            newBusinessUnits.add(bu)
          }
        }

        if (customFields){
          customFields.each { cf ->
            if (customFieldIsComplete(cf)){
              newCustomFields.add(cf)
            }
          }
        }

        if (salesHierarchy) {
          salesHierarchy.each { sh ->
            if (salesHierarchyIsComplete(sh)){
              newSalesHierarchy.add(sh)
            }
          }
        }
      }

      // should remove any duplicates from the list
      groupedPayload.businessUnits = newBusinessUnits as Set
      groupedPayload.customFields = newCustomFields as Set
      groupedPayload.salesHierarchy = newSalesHierarchy as Set

      groupedPayload
    }
    
    message.setBody(JsonOutput.toJson(customerMasters))
    message
}

def standardizeArrayField(def field){
  def returnValue
  if (!field){
    returnValue = []
  }
  else {
    returnValue = field instanceof ArrayList ? field : [field]
  }
  returnValue
}

def customFieldIsComplete(def cf){
  (cf.name && cf.value)
}

def salesHierarchyIsComplete(def sales){
  (sales.distributionChannel || sales.division || sales.group || sales.office || sales.organization)
}