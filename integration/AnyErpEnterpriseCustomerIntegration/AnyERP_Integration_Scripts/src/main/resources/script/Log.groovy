import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;


def Message printTauliaRequest(Message message) {
    printLog(message, "Taulia Request")
}

def Message printTauliaResponse(Message message) {
    printLog(message, "Taulia Response")
}

def Message FileBeforeSplit(Message message) {
    printLog(message, "File Before Split")
}

def Message FileAfterSplit(Message message) {
    printLog(message, "After Split Count ${message?.properties?.CamelSplitIndex}")
}

def Message printLog(Message message) {
    printLog(message, "Log")
}

def Message printInputMessage(Message message) {
    printLog(message, "Input Message")
}

def Message printXml(Message message) {
    printLog(message, "XML")
}

def Message endOfIflow(Message message) {
    printLog(message, "End of IFlow")
}

def Message printCSVContents(Message message) {
    printLog(message, "CSV Contents")    
}

def Message printNewFile(Message message) {
    printLog(message, "New file ${message?.properties?.csvFileName}")
}

def addAttachment(Message message, String nameOfAttachment, String content){
    def messageLog = messageLogFactory.getMessageLog(message);
    if(messageLog != null){
        messageLog.setStringProperty("Logging#1", "Printing Payload As Attachment")
        messageLog.addAttachmentAsString(nameOfAttachment, content, "text/plain");
    }
}

def Message printLog(Message message, String nameOfAttachment) {
    def body = message.getBody(java.lang.String) as String;
    def heads = message.getHeaders().toMapString();
    def props
    if(message.getProperties()) {
        props = message.getProperties().toMapString();
    }

    def result = "Headers: " + heads + "\n" + "Properties: "  + props + "\n" + "Body:" + body;
    
    addAttachment(message, nameOfAttachment, result)
    
    return message;
}

def Message printJmsMessage(Message message) {

    def map = message.getProperties()
    
    def body = message.getBody(java.lang.String) as String
    def messageLog = messageLogFactory.getMessageLog(message)

    if (messageLog != null) {
        messageLog.addAttachmentAsString("JMS Message", body, "text/xml")
    }

    return message
}

def Message unsupportedCSVFormatException(Message message) {
    
    def properties = message.getProperties()
    def csvFormat = properties.get("csvFormat")
    
    //Body  
    String body = "Unsupported CSV format specified - [${csvFormat}]"
    
    def messageLog = messageLogFactory.getMessageLog(message)
    if (messageLog != null) {
        messageLog.addAttachmentAsString('Exception Information', body, 'text/plain')
    }
    
    return message
}

def Message logSendToTaulia(Message message){
    def body = message.getBody(String.class)
    def properties = message.getProperties()
    def headers = message.getHeaders()
    def messageLog = messageLogFactory.getMessageLog(message)
        if(messageLog != null){
            messageLog.setStringProperty("Logging#3", "Printing Attachment")
            messageLog.addAttachmentAsString(headers.IFlowId, body, "text/plain")
        }  
    return message
}

def Message logGeneratedPayload(Message message){
    def body = message.getBody(String.class)
    def properties = message.getProperties()
    def headers = message.getHeaders()
    def messageLog = messageLogFactory.getMessageLog(message)
        if(messageLog != null){
            messageLog.addAttachmentAsString("Payload", body, "text/plain")
        }  
    return message
}
