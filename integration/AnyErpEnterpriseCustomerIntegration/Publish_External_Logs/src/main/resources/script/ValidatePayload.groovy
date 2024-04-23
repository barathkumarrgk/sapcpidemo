import com.sap.gateway.ip.core.customdev.util.Message;
import groovy.json.JsonSlurper;

def Message processData(Message message) {
    def body = message.getBody(java.lang.String) as String
    def props = message.getProperties()
    
    def payload
    def payloadToObfuscateLength = 0
    
    if(message.properties.get("payloadToObfuscate") != null){
        payload = props.get("payloadToObfuscate")
        
        def payloadList = new JsonSlurper().parseText(payload)
        
        payloadToObfuscateLength = payloadList.size()
        
        message.setProperty("payloadToObfuscateLength", payloadToObfuscateLength)
        message.setProperty("payload", payload)

    }
    return message
}