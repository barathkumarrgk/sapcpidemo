import com.sap.gateway.ip.core.customdev.util.Message
import groovy.util.XmlSlurper

def Message processData(Message message) {
    def body = message.getBody(java.lang.String) as String
    // Parse input
    def slurper = new XmlSlurper(false, false)
    def messages = slurper.parseText(body)

    message.setBody(messages.toString())

    message.setBody(messages.row[0].contents.toString())
    return message
}