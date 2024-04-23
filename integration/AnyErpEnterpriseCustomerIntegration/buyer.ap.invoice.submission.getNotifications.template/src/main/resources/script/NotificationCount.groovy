import com.sap.gateway.ip.core.customdev.util.Message
import java.util.HashMap
import groovy.json.JsonSlurper

def Message processData(Message message) {
        
    def body = message.getBody(java.lang.String) as String
    def jsonSlurper = new JsonSlurper()
    def jsonInput = jsonSlurper.parseText(body)
    
    def itemCount = 0
    if(jsonInput?.notifications) {
      itemCount = jsonInput.notifications.size()
    }
    message.setHeader("content-notification-count", itemCount.toString())
    return message
}