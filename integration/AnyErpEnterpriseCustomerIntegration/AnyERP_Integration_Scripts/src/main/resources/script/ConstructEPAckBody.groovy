import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import groovy.json.JsonBuilder

Message processData(Message message) {

    def body = message.getBody(String.class)
    def jsonSlurper = new JsonSlurper()
    //def jsonInput = jsonSlurper.parseText(body)
    def jsonObject = jsonSlurper.parseText(body)
    def earlyPaymentAcknowledgementGroups = jsonObject.groupedEarlyPaymentAcknowledgement

    earlyPaymentAcknowledgementGroups = earlyPaymentAcknowledgementGroups.collect {acks ->
        def newGroupedAck = [:]
        def newBankAccounts = []
        def earlyPaymentAcks = acks.earlyPaymentsAck
        if (earlyPaymentAcks){
            newGroupedAck = earlyPaymentAcks.first()
            earlyPaymentAcks.each {epa ->

                epa.erpDocumentId = epa.erpDocumentId ? new String(epa?.erpDocumentId) : null
                epa.errorCode = epa.errorCode ? new String(epa?.errorCode) : null
                epa.funderStatus = epa.funderStatus ? new String(epa?.funderStatus) : null
                epa.id = epa.id ? new String(epa?.id) : null
                epa.platformSignature = epa.platformSignature ? new String(epa?.platformSignature) : null
                epa.rejectReason = epa.rejectReason ? new String(epa?.rejectReason) : null
                epa.status = epa.status ? new String(epa?.status) : null
                epa.transactionId = epa.transactionId ? new String(epa?.transactionId) : null
                epa.invoiceId = epa.invoiceId ? new String(epa?.invoiceId) : null
                epa.ticketDocumentId = epa.ticketDocumentId ? new String(epa?.ticketDocumentId) : null

                def bankAccounts = epa?.bankAccounts
                if (epa.status == 'ACCEPTED'){
                    bankAccounts.each { ba ->

                        ba.accountNumber = ba.accountNumber ? new String(ba?.accountNumber) : null
                        ba.bankNumber = ba.bankNumber ? new String(ba?.bankNumber) : null
                        ba.bankBranchNumber = ba.bankBranchNumber ? new String(ba?.bankBranchNumber) : null
                        ba.bankName = ba.bankName ? new String(ba?.bankName) : null
                        ba.bankCity = ba.bankCity ? new String(ba?.bankCity) : null
                        ba.bankRegion = ba.bankRegion ? new String(ba?.bankRegion) : null
                        ba.bankStreet = ba.bankStreet ? new String(ba?.bankStreet) : null
                        ba.controlKey = ba.controlKey ? new String(ba?.controlKey) : null
                        ba.country = ba.country ? new String(ba?.country) : null
                        ba.currency = ba.currency ? new String(ba?.currency) : null
                        ba.externalAccountHolder = ba.externalAccountHolder ? new String(ba?.externalAccountHolder) : null
                        ba.externalAccountHolderId = ba.externalAccountHolderId ? new String(ba?.externalAccountHolderId) : null
                        ba.swiftNumber = ba.swiftNumber ? new String(ba?.swiftNumber) : null
                        ba.ibanNumber = ba.ibanNumber ? new String(ba?.ibanNumber) : null


                        if (ba.integrity) {
                            ba.integrity.entityFingerprint = ba.integrity.entityFingerprint ? new String(ba?.integrity?.entityFingerprint) : null
                            ba.integrity.localScheme = ba.integrity.localScheme ? new String(ba?.integrity?.localScheme) : null
                            ba.integrity.signature = ba.integrity.signature ? new String(ba?.integrity?.signature) : null
                            ba.integrity.signatureVersion = ba.integrity.signatureVersion ? new String(ba?.integrity?.signatureVersion) : null
                            ba.integrity.timestamp = ba.integrity.timestamp ? new String(ba?.integrity?.timestamp) : null

                        } else {
                            ba.integrity = null
                        }
                        if (bankAccountIsComplete(ba)){

                            newBankAccounts.add(ba)
                        }
                    }
                } else {
                    newGroupedAck.bankAccounts = null
                }
            }
        }
        if (newBankAccounts) {
            newGroupedAck.bankAccounts = newBankAccounts as Set
        }
        newGroupedAck
    }

    def cleanedObject = removeEmptyFields(earlyPaymentAcknowledgementGroups)
    def jsonBuilder = new JsonBuilder(cleanedObject) as Object

    //message.setBody(JsonOutput.prettyPrint(JsonOutput.toJson(earlyPaymentAcknowledgementGroups)))
    message.setBody(jsonBuilder.toPrettyString())
    return message
}

static def bankAccountIsComplete(def ba){
    def integrity = ba.integrity
    def integrityExists = integrity ? (integrity.entityFingerprint && integrity.localScheme && integrity.signature
            && integrity.signatureVersion && integrity.timestamp) : false
    (ba.accountNumber && integrityExists && ba.country)
}

def removeEmptyFields(def object) {
    if (object instanceof Map) {
        object.entrySet().removeAll { it.value == null || (it.value instanceof String && it.value.trim() == "") }
        object.collectEntries { [it.key, removeEmptyFields(it.value)] }
    } else if (object instanceof List) {
        object.collect { removeEmptyFields(it) }.findAll { it != null }
    } else {
        object
    }
}