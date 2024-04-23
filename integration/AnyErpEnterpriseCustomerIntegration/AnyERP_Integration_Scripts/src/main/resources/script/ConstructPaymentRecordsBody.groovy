import com.sap.gateway.ip.core.customdev.util.Message;
import groovy.json.JsonSlurper
import groovy.json.JsonOutput

/**
 * Constructs a paymentRecords JSON body in the form: [{ "amount":""],"businessUnit":""}]
 * The XML to JSON converter is not able to return just a json array so this method takes {paymentRecords:[...]} and converts it to
 * just [...]
 **/
def Message processData(Message message) {
    def body = message.getBody(java.lang.String) as String
    def jsonSlurper = new JsonSlurper()
    def jsonInput = jsonSlurper.parseText(body)
    
    def payload = jsonInput.groupedPayment
    
    // need to check this for the edge case of number of paymentRecords landing right on the split
    // number, If 501 paymentRecords, the second payload wont be the same format
    def payments = standardizeArrayField(payload)
    
    payments = payments.collect {it ->
    def paymentsGroup = standardizeArrayField(it.paymentRecord)

    // set common values from first object in group
    def groupedPayload = paymentsGroup?.first()
    def newPaymentRecordItems = []

    // construct arrays
    paymentsGroup.each { p ->
      if (!bankInformationIsComplete(p.payeeBankInformation)){
        p.payeeBankInformation = null
      }
      if (!bankInformationIsComplete(p.payerBankInformation)){
        p.payerBankInformation = null
      }
      def paymentRecordItems = standardizeArrayField(p?.paymentRecordItems)

      if (paymentRecordItems) {
        paymentRecordItems.each { pr ->
          if (paymentRecordItemIsComplete(pr)){
            pr.discountAmount = pr.discountAmount ? new BigDecimal(pr.discountAmount) : null
            pr.paymentAmount = pr.paymentAmount ? new BigDecimal(pr.paymentAmount) : null
            newPaymentRecordItems.add(pr)
          }
        }
      }
    }
    
    // should remove any duplicates from the list
    groupedPayload.paymentRecordItems = newPaymentRecordItems as Set

    groupedPayload.amount = groupedPayload.amount ? new BigDecimal(groupedPayload.amount) : null
    groupedPayload.cancelled = (groupedPayload.cancelled != null) ? Boolean.parseBoolean(groupedPayload.cancelled) : false
    groupedPayload.cashDiscountAmount = groupedPayload.cashDiscountAmount ? new BigDecimal(groupedPayload.cashDiscountAmount) : null
    groupedPayload.paidByCheck = (groupedPayload.paidByCheck != null) ? Boolean.parseBoolean(groupedPayload.paidByCheck) : false
    groupedPayload.paymentCount = groupedPayload.paymentRecordItems.size()

    groupedPayload
  }
    payments = recursivelyRemoveEmpties(payments)
    message.setBody(JsonOutput.prettyPrint(JsonOutput.toJson(payments)))
    return message;
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

def bankInformationIsComplete(def b){
  b ? (b.accountHolder || b.accountNumber || b.bankControlKey || b.bankCountry || b.bankDetails || b.bankName
      || b.bankNumber || b.city || b.companyName || b.country || b.postalCode || b.street || b.swiftNumber) : false
}

def paymentRecordItemIsComplete(def pr) {
  (pr.invoiceDocumentId && pr.paymentAmount)
}

def recursivelyRemoveEmpties(item) {
    switch (item) {
        case Map:
            return item.collectEntries { k, v ->
                [k, recursivelyRemoveEmpties(v)]
            }.findAll { k, v -> v }

        case List:
            return item.collect {
                recursivelyRemoveEmpties(it)
            }.findAll { v -> v }

        default:
            return item
    }
}