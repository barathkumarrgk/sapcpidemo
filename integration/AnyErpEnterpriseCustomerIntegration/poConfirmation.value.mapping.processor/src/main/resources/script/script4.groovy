import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
def Message processData(Message message) {
    def body = message.getBody(java.lang.String) as String;
    def props = message.getProperties();
    def vm_errors = props.get('vm_errors');

    def messageLog = messageLogFactory.getMessageLog(message);

    if(messageLog != null){
        //-EOE- replace with line breaks
        messageLog.addAttachmentAsString("Error", vm_errors.replaceAll("-EOE-", "\n"), "text/plain");
     }
     
     
    return message;
}
