import groovy.json.JsonSlurper
 
import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.MarkupBuilder
import java.time.LocalDate
import java.time.format.DateTimeFormatter

Message processData(Message message) {
    def messageString = message.getBody(String)
    def jsonSlurper = new JsonSlurper()
    def json = jsonSlurper.parseText(messageString)
    
    def jsonSize = json.size()
    
    message.setProperty("jsonSize", jsonSize.toString())
    
    return message
}