import com.sap.gateway.ip.core.customdev.util.Message
import java.util.HashMap
import groovy.json.JsonSlurper

/**
 * Constructs an EP acknowledgement JSON body in the form: 
 *   [{ "id":"","transactionId":"","invoiceId":"","status":""}, ... ]
 **/
def Message processData(Message message) {

    def body = message.getBody(java.lang.String) as String
    def headerMap = message.getHeaders();
    
    def jsonSlurper = new JsonSlurper()
    def jsonInput = jsonSlurper.parseText(body)
    
    // Determine finance type for acknowledgement status 
    String status = headerMap.get("processingResponse")
    
    StringBuilder epAcknowledgeBody = new StringBuilder("[")

    List<String> acknowledgements = []
    jsonInput.data.each { data -> 
        acknowledgements << 
        """ 
        { 
            "id":"${data.id}", 
            "transactionId":"${data.transactionId}",
            "invoiceId":"${data.invoice.id}",
            "status":"${status}"
        }    
        """
    }
    epAcknowledgeBody.append(acknowledgements.join(","))    
    epAcknowledgeBody.append("]")

    message.setBody(epAcknowledgeBody.toString()    )    
    return message
    
}