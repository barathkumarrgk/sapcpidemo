import com.sap.gateway.ip.core.customdev.util.Message; 
import java.util.HashMap; 
def Message processData(Message message) { 
    def body = message.getBody(java.lang.String) as String;
    def heads = message.getHeaders().toMapString();
	def props
	if(message.getProperties()) {
props = message.getProperties().toMapString();
}

	def result = "Body:" + body + "Properties: "  + props + "Headers: " + heads;
    def messageLog = messageLogFactory.getMessageLog(message); 
    if(messageLog != null){ 
        messageLog.setStringProperty("Logging#1", "Printing Payload As Attachment") 
        messageLog.addAttachmentAsString("ResponsePayload:", result, "text/plain"); 
     } 
    return message; 
}