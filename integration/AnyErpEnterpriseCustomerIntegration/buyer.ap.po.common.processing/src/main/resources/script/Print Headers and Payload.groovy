import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
def Message processData(Message message) {
    def body = message.getBody(java.lang.String) as String;
    def heads = message.getHeaders().toMapString();
    def props
    if(message.getProperties()) {
        props = message.getProperties().toMapString();
    }

    def result = "Headers: " + heads + "\n" + "Properties: "  + props + "\n" + "Body:" + body;
    def messageLog = messageLogFactory.getMessageLog(message);
    if(messageLog != null){
        messageLog.setStringProperty("Logging#2", "Printing Payload As Attachment")
        messageLog.addAttachmentAsString("toStack:", result, "text/plain");
    }
    return message;
}
