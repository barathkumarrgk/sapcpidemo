import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import com.sap.it.api.mapping.MappingContext

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
        messageLog.setStringProperty("Logging#1", "Printing Payload As Attachment")
        messageLog.addAttachmentAsString("Exception info", result, "text/plain");
    }
    
    return message;
}

def String getProperty(String propertyName, MappingContext context) {
    def propertyValue = context.getProperty(propertyName);
    return propertyValue;
}

def String getHeader(String headerName, MappingContext context) {
    def headerValue = context.getHeader(headerName);
    return headerValue;
}
