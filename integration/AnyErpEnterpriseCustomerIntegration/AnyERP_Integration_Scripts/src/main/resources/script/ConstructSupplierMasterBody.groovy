import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

/**
 * Constructs a supplierMaster JSON body in the form: [{ "addresses":[{...}],"bankAccounts":[{...}, {...}],"contacts":[{...}, {...}]}, ... ]
 * The XML to JSON converter is not able to return just a json array so this method takes {supplierMaster:[...]} and converts it to
 * just [...]
 **/
def Message processData(Message message) {
    def body = message.getBody(java.lang.String) as String
    def jsonSlurper = new JsonSlurper()
    def jsonInput = jsonSlurper.parseText(body)

    def payload = jsonInput.groupedSupplierMaster
    def supplierMasters = standardizeArrayField(payload)

    supplierMasters = supplierMasters.collect { it ->
        def supMasterGroup = standardizeArrayField(it.supplierMaster)

        // set common values from first object in group
        def groupedPayload = supMasterGroup?.first()

        // construct arrays
        def newAddresses = []
        def newBankAccounts = []
        def newContacts = []
        def newCustomFields = []
        def newSupplierRelations = []
        def newTaxIdentifiers = []

        supMasterGroup.each { supMaster ->
            def addresses = standardizeArrayField(supMaster?.addresses)
            def bankAccounts = standardizeArrayField(supMaster?.bankAccounts)
            def contacts = standardizeArrayField(supMaster?.contacts)
            def customFields = standardizeArrayField(supMaster?.customFields)
            def supplierRelations = standardizeArrayField(supMaster?.supplierRelations)
            def taxIdentifiers = standardizeArrayField(supMaster?.taxIdentifiers)

            addresses?.each { addr ->
                if (addressIsComplete(addr)) {
                    newAddresses.add(addr)
                }
            }
            bankAccounts?.each { ba ->
                if (bankAccountIsComplete(ba)) {
                    if (ba.signature) {
                        ba.signature = [ba.signature.element]
                    }
                    def sigInfo = ba.signatureInfo
                    if (sigInfo) {
                        sigInfo.scheme = sigInfo.scheme ? new BigDecimal(sigInfo.scheme) : null
                        sigInfo.version = sigInfo.version ? new BigDecimal(sigInfo.version) : null
                    }
                    newBankAccounts.add(ba)
                }
            }
            contacts?.each { contact ->
                if (contactIsComplete(contact)) {
                    contact.invite = (contact.invite != null) ? Boolean.parseBoolean(contact.invite) : false
                    newContacts.add(contact)
                }
            }
            customFields?.each { cf ->
                if (customFieldIsComplete(cf)) {
                    cf.showInUi = (cf.showInUi != null) ? Boolean.parseBoolean(cf.showInUi) : false
                    newCustomFields.add(cf)
                }
            }
            supplierRelations?.each { sr ->
                if (supplierRelationIsComplete(sr)) {
                    sr.tedEligible = (sr.tedEligible != null) ? Boolean.parseBoolean(sr.tedEligible) : false
                    def paymentTerms = sr.paymentTerms
                    if (paymentTerms) {
                        paymentTerms.additionalMonth1 = paymentTerms.additionalMonth1 ? new BigDecimal(paymentTerms.additionalMonth1) : null
                        paymentTerms.additionalMonth2 = paymentTerms.additionalMonth2 ? new BigDecimal(paymentTerms.additionalMonth2) : null
                        paymentTerms.asap = (paymentTerms.asap != null) ? Boolean.parseBoolean(paymentTerms.asap) : false
                        paymentTerms.days1 = paymentTerms.days1 ? new BigDecimal(paymentTerms.days1) : null
                        paymentTerms.days2 = paymentTerms.days2 ? new BigDecimal(paymentTerms.days2) : null
                        paymentTerms.days3 = paymentTerms.days3 ? new BigDecimal(paymentTerms.days3) : null
                        paymentTerms.daysNet = paymentTerms.daysNet ? new BigDecimal(paymentTerms.daysNet) : null
                        paymentTerms.fixedDay1 = paymentTerms.fixedDay1 ? new BigDecimal(paymentTerms.fixedDay1) : null
                        paymentTerms.fixedDay2 = paymentTerms.fixedDay2 ? new BigDecimal(paymentTerms.fixedDay2) : null
                        paymentTerms.percent1 = paymentTerms.percent1 ? new BigDecimal(paymentTerms.percent1) : null
                        paymentTerms.percent2 = paymentTerms.percent2 ? new BigDecimal(paymentTerms.percent2) : null

                        def baselineDate = paymentTerms.baselineDate
                        if (baselineDate) {
                            baselineDate.additionalMonth = baselineDate.additionalMonth ? new BigDecimal(baselineDate.additionalMonth) : null
                            baselineDate.fixedDay = baselineDate.fixedDay ? new BigDecimal(baselineDate.fixedDay) : null
                        }
                    }
                    newSupplierRelations.add(sr)
                }
            }
            taxIdentifiers?.each { tax ->
                if (taxIdentifierIsComplete(tax)) {
                    newTaxIdentifiers.add(tax)
                }
            }

        }
        groupedPayload.addresses = newAddresses as Set
        groupedPayload.bankAccounts = newBankAccounts as Set
        groupedPayload.contacts = newContacts as Set
        groupedPayload.customFields = newCustomFields as Set
        groupedPayload.supplierRelations = newSupplierRelations as Set
        groupedPayload.taxIdentifiers = newTaxIdentifiers as Set

        groupedPayload
    }
    supplierMasters = recursivelyRemoveEmpties(supplierMasters)
    message.setBody(JsonOutput.prettyPrint(JsonOutput.toJson(supplierMasters)))
    return message;
}

def standardizeArrayField(def field) {
    def returnValue
    if (!field) {
        returnValue = []
    } else {
        returnValue = field instanceof ArrayList ? field : [field]
    }
    returnValue
}

def addressIsComplete(def addr) {
    (addr.addressLine1)
}

def bankAccountIsComplete(def ba) {
    ba.accountNumber ? true : false
}

def contactIsComplete(def c) {
    (c.transactionId && c.invite)
}

def customFieldIsComplete(def c) {
    (c.fieldName && c.fieldValue)
}

def supplierRelationIsComplete(def sr) {
    sr.businessUnit ? true : false
}

def taxIdentifierIsComplete(def t) {
    (t.type && t.value)
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