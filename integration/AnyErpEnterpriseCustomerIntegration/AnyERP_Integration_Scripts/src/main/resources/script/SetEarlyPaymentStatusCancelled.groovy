/**
 * Sets the "status" value for all EP records to "CANCELLED"
**/

import com.sap.gateway.ip.core.customdev.util.Message
import java.util.HashMap
import groovy.json.JsonSlurper
import groovy.json.JsonOutput

def Message processData(Message message) {
            
    String CANCELLED = "CANCELLED"

    def body = message.getBody(java.lang.String) as String
    def jsonSlurper = new JsonSlurper()
    def jsonMap = jsonSlurper.parseText(body)
    
    // "data" = 1 EP request
    jsonMap.data.each { earlyPayment -> 
        earlyPayment.status = "${CANCELLED}"
    }

    String jsonBody = JsonOutput.prettyPrint(JsonOutput.toJson(jsonMap))
    
    message.setBody(jsonBody)  
    return message
}
