import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message) {
    def map = message.getHeaders();
    def body = message.getBody(java.lang.String) as String;
    def filePath = map.get("CamelFileName") as String;
    if (filePath != null) {
        def messageLog = messageLogFactory.getMessageLog(message);
        if (messageLog != null) {
            messageLog.addCustomHeaderProperty("Consumed File: ", filePath);
            messageLog.setStringProperty(filePath, "Printing Payload As Attachment")
            messageLog.addAttachmentAsString(filePath, body, "text/plain");
        }
    }
    return message;
}