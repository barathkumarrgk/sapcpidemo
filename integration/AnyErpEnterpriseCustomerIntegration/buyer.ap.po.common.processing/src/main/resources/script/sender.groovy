import groovy.json.JsonSlurper
import groovy.json.JsonBuilder
import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonOutput

/**
 * Processes the incoming message by parsing the JSON payload, logging it as an attachment,
 * processing each JSON object in the payload one by one, and returning the processed message.
 *
 * @param message The incoming message to process.
 * @return The processed message.
 */
Message processData(Message message) {
    // Get the message body as a String
    def messageString = message.getBody(String)

    // Parse the JSON payload into a Groovy object
    def jsonSlurper = new JsonSlurper()
    def jsonBody = jsonSlurper.parseText(message.getProperty("json"))
    
    // Process each JSON object in the payload one by one
    if (jsonBody && !jsonBody.isEmpty()) {
        // Get the first JSON object in the payload
        def first = jsonBody[0]
        // Drop the first JSON object from the payload
        jsonBody = jsonBody.drop(1)
    
        // Update the message properties and body for the processed JSON object
        message.setProperty("json", JsonOutput.toJson(jsonBody))
        message.setProperty("numProcessed", message.getProperty("numProcessed") + 1)
        message.setBody(new JsonBuilder(first).toString())
    }

    // Return the processed message
    return message
}
