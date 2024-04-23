import com.sap.gateway.ip.core.customdev.util.Message; 
import java.util.HashMap; 
def Message processData(Message message) { 
	def props
	if(message.getProperties()) {
props = message.getProperties().toMapString();
    value = properties.get("CamelFileExchangeFile");
    value = properties.get("CamelCreatedTimestamp");
}

	def result = "Properties: "  + props + "\n";
    def messageLog = messageLogFactory.getMessageLog(message); 
    if(messageLog != null){ 
        messageLog.setStringProperty("Logging#1", "Printing Payload As Attachment") 
        messageLog.addAttachmentAsString("ResponsePayload2:", result, "text/plain"); 
     } 
    return message; 
}