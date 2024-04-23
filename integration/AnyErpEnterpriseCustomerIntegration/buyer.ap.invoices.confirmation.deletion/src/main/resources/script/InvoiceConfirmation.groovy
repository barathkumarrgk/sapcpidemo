import com.sap.gateway.ip.core.customdev.util.Message
import java.util.HashMap
import groovy.json.JsonSlurper

def Message processData(Message message) {
        
    def body = message.getBody(java.lang.String) as String
    def jsonSlurper = new JsonSlurper()
    def jsonInput = jsonSlurper.parseText(body)
    
    // Set status header
    def staus = 'failure'
    if(jsonInput?.success == true) {
      staus = jsonInput.success
    } 
    message.setHeader("confirmation-status", staus)

    // Set invoice size
    def ticketSize = jsonInput?.tickets.size()
    message.setProperty('invoiceTotalSize', ticketSize)
    message.setProperty('invoiceCurrentSize', 0)


    // Parse request body and update with invoice details
    StringBuilder confirmationBody = new StringBuilder("[")
    List<String> reqBody = []
    jsonInput.tickets.each { data ->
        reqBody <<  "\"" +data.documentId.toString() + "\""
    }
    confirmationBody.append(reqBody.join(","))
    confirmationBody.append("]")
    message.setBody(confirmationBody.toString())
    
    return message
}