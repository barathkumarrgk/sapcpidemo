import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonBuilder
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

/**
 * This function processes the message by sorting the purchase orders and collating them into groups of two. 
 * It also sets the initial values for the `jsonSize`, `json`, and `numProcessed` properties, which are used
 * in the stack step to determine when to stop processing messages.
 * 
 * @param message The message to be processed
 * @return The processed message
 */
Message processData(Message message) {
  
  // Extract the message body as a string
  def body = message.getBody(java.lang.String) as String
  
  // Parse the message body as a JSON object
  def jsonSlurper = new JsonSlurper()
  def json = jsonSlurper.parseText(body)
  
  // Sort the purchase orders by number and vendor number, and collate them into groups of two
  def purchaseOrder = json.purchaseOrder.sort { a, b ->
    if (a.number == b.number) {
      a.vendorNumber <=> b.vendorNumber
    } else {
      a.number <=> b.number
    }
  }.collate(2)
  
  
  // Set the initial values for the `jsonSize`, `json`, and `numProcessed` properties
  message.setProperty("jsonSize", purchaseOrder.size()) // The number of batched purchaseOrder to be processed
  message.setProperty("json", JsonOutput.toJson(purchaseOrder)) // The list of purchaseOrder to be processed
  message.setProperty("numProcessed", 0) // The number of purchaseOrder that have been processed
  
  // Set the sorted purchaseOrder body to the message body
  message.setBody(JsonOutput.toJson(purchaseOrder))
  
  return message
}
