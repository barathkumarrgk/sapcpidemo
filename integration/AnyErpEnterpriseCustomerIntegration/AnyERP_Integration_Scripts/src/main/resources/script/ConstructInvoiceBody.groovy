import com.sap.gateway.ip.core.customdev.util.Message; 
import java.util.HashMap; 
import groovy.json.JsonSlurper;
import groovy.json.JsonOutput;

def Message processData(Message message) { 
    def body = message.getBody(java.lang.String) as String;
    def jsonSlurper = new JsonSlurper()
    def invoicesInput = jsonSlurper.parseText(body) 
    
    def invoicesArray = invoicesInput.groupedInvoice.invoice
    
    message.setBody(JsonOutput.toJson(invoicesArray))
    return message; 
}