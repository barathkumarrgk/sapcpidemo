import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.json.JsonSlurper;

def Message processData(Message message) {
    
    Map<String, Object> props = message.properties
    
    def objects = props.get("objects")
    def objectList = new JsonSlurper().parseText(objects)
    
    props.put('objectSize', objectList.size())
    props.put('objectNumber', 0)
   
    return message
}