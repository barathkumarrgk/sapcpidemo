import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
def Message processData(Message message) {
    def body = message.getBody(java.lang.String) as String;
    def props = message.getProperties();
    def sourceValue = props.get('sourceValue');
    def targetValue = props.get('targetValue');
    def bodyXML = new XmlSlurper().parseText(body);
    def vm_error = props.get('vm_error');
    
    //-EOE- will be replaced by line return
    message.setProperty('vm_error', vm_error + " Submitting the sourceValue: " + sourceValue + " and targetValue " + targetValue + " - resulted in the error: " + bodyXML.code + ": " + bodyXML.message + "-EOE-");
     
    return message;
}

