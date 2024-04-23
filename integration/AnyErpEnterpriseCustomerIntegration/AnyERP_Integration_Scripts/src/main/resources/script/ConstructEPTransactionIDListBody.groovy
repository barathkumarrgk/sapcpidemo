import com.sap.gateway.ip.core.customdev.util.Message
import java.util.HashMap
import groovy.json.JsonSlurper

/**
 * Constructs a JSON body to retrieve Early Payments by bulk in the form: 
 *   [{ "transactionId", "transactionId", ... ] (POST /v1/earlyPaymentList)
 **/
def Message processData(Message message) {

    def body = message.getBody(java.lang.String) as String
    def headerMap = message.getHeaders()
    
    def jsonSlurper = new JsonSlurper()
    def jsonInput = jsonSlurper.parseText(body)
    

    StringBuilder transactionIdBody = new StringBuilder("[")

    List<String> transactionIdList = []
    jsonInput.data.each { epCancel -> 
        transactionIdList << "\"${epCancel.transactionId}\""
    }
    transactionIdBody.append(transactionIdList.join(",")).append("]")  

    message.setBody(transactionIdBody.toString())    
    return message
    
}
