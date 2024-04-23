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
        messageLog.setStringProperty("Logging #1", "Printing Headers/Properties/Payload")
        messageLog.addAttachmentAsString("Log Info", result, "text/plain");
    }
    
    return message;
}
