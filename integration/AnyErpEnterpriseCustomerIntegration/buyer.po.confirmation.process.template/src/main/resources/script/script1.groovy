import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message) 
{
	def body = message.getBody(java.lang.String) as String;
	def messageLog = messageLogFactory.getMessageLog(message);
	if(messageLog != null)
	{
	messageLog.addAttachmentAsString("Log Payload:", body, "text/plain");
     }
	return message;
}