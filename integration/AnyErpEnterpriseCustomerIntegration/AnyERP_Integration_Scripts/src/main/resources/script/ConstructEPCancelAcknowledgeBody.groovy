import com.sap.gateway.ip.core.customdev.util.Message
import java.util.HashMap
import groovy.json.JsonSlurper

/**
 * Constructs an EP cancel acknowledgement JSON body in the form: 
 *   [{ "id":"","transactionId":"","invoiceId":"","status":""}, ... ]
 * NOTE: EP Cancellations only apply to SCF, and as such, status is always "ACKNOWLEDGED"
 **/
def Message processData(Message message) {

    def body = message.getBody(java.lang.String) as String
    def headerMap = message.getHeaders();
    
    def jsonSlurper = new JsonSlurper()
    def jsonInput = jsonSlurper.parseText(body)
    
    StringBuilder epCancelAcknowledgeBody = new StringBuilder("[")

    List<String> acknowledgements = []
    jsonInput.earlyPayments.each { data -> 
        acknowledgements << 
        """ 
        { 
            "id":"${data.id}", 
            "transactionId":"${data.id}",
            "invoiceId":"${data.invoice.id}",
            "status":"ACKNOWLEDGED" 
        }    
        """
    }
    epCancelAcknowledgeBody.append(acknowledgements.join(","))    
    epCancelAcknowledgeBody.append("]")

    message.setBody(epCancelAcknowledgeBody.toString()    )    
    return message
    
}
