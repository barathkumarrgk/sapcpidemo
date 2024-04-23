import com.sap.gateway.ip.core.customdev.util.Message
import java.util.HashMap
import groovy.json.JsonSlurper

def Message processData(Message message) {

    // Get invoiceCurrentSize
    Map<String, Object> props = message.properties
    def invoiceCurrentSize = props.get("invoiceCurrentSize").toInteger()
  
    // Set notification-id as header param
    def body = message.getBody(java.lang.String) as String
    def jsonSlurper = new JsonSlurper()
    def jsonInput = jsonSlurper.parseText(body)

    message.setHeader("notification-id", jsonInput[invoiceCurrentSize])

    // Increment current iteration
    invoiceCurrentSize += 1
    message.setProperty('invoiceCurrentSize', invoiceCurrentSize)

    // Set body without any changes
    def originalBody = body.toString()
    message.setBody(originalBody)
    return message
}