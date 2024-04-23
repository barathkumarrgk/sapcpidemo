/*
Delay Message for 10 minutes
 */
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
def Message processData(Message message) {
    //Body 
       def body = message.getBody();
       sleep(600000); //10 minutes
       // sleep(30000); //30 seconds for testing
       message.setBody(body);
       return message;
}