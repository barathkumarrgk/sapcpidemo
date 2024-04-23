import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message) {

  def body = message.getBody(java.lang.String) as String
  if(!body) {
    exchange.setRouteStop(true)
    def result = "Headers: " + heads + "\n" + "Properties: "  + props + "\n" + "Body:" + body
    def messageLog = messageLogFactory.getMessageLog(message)
    if(messageLog != null){
        messageLog.setStringProperty("Logging #1", "Printing Payload/Headers/Properties")
        messageLog.addAttachmentAsString("Log Info:", result, "text/plain")
    }
    return
  }

  return message
}
