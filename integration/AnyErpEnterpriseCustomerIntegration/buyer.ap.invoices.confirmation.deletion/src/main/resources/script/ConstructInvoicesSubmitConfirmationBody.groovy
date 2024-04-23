import com.sap.gateway.ip.core.customdev.util.Message
import groovy.util.XmlSlurper

/**
 * Constructs Invoices Submit Confirmation JSON body in the form:
 *   [
         {
         "erpInvoiceStatus": "string",
         "erpStatusExplanation": "range [0,100]",
         "functionalMessages": [
         "string"
         ],
         "invoiceId": "string",
         "sapField": {
         "sapIdFi": "range [0,22]",
         "sapIdMm": "range [0,18]",
         "sapIdWf": "range [0,20]"
         },
         "status": "string",
         "ticketDocumentId": "string"
         }
    ]
 **/
def Message constructBody(Message message) {

    String xmlRecordPrefix = 'multimap:Message1'
    // Parse input
    def slurper = new XmlSlurper(false, false)
    def body = message.getBody(java.lang.String) as String
    def xmlInput = slurper.parseText(body)

    StringBuilder invoiceSubmitConfirmationBody = new StringBuilder("[")
    List<String> confirmations = []
    xmlInput."${xmlRecordPrefix}".root.each { data ->
        def functionalMessages = "${data.functionalMessages}".length() > 0 ? ${data.functionalMessages} : []
        confirmations <<
        """
        {
             "erpInvoiceStatus": "${data.erpInvoiceStatus}",
             "erpStatusExplanation": "${data.erpStatusExplanation}",
             "functionalMessages": ${functionalMessages},
             "invoiceId": "${data.id}",
             "sapField": {
                 "sapIdFi": "${data.sapField.sapIdFi}",
                 "sapIdMm": "${data.sapField.sapIdMm}",
                 "sapIdWf": "${data.sapField.sapIdWf}"
             },
             "status": "${data.status}",
             "ticketDocumentId": "${data.ticketDocumentId}"
         }
        """
    }
    invoiceSubmitConfirmationBody.append(confirmations.join(","))
    invoiceSubmitConfirmationBody.append("]")

    message.setBody(invoiceSubmitConfirmationBody.toString())
    return message
}
