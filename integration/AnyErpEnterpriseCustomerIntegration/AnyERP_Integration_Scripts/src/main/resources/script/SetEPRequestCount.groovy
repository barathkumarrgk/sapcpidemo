import com.sap.gateway.ip.core.customdev.util.Message
import java.util.HashMap
import groovy.json.JsonSlurper


def Message processData(Message message) {
        
    def body = message.getBody(java.lang.String) as String
    def jsonSlurper = new JsonSlurper()
    def jsonInput = jsonSlurper.parseText(body)
    
    // Set record count for integration flow routing
    message.setProperty("epRequestCount", jsonInput.count)

    def notificationId = jsonInput.data.notificationId[0].toString()
    message.setProperty("notificationId", notificationId)

    return message
}