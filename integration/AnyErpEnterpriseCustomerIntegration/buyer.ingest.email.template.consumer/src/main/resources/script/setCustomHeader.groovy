import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
def Message processData(Message message)
{
       def map  = message.getProperties();
       def country   = map.get("country") as String;
       if (country != null)
       {
           def messageLog   = messageLogFactory.getMessageLog(message);
    if(messageLog != null){ 
        messageLog.addCustomHeaderProperty("Country: ", country);
     } 
       }
       return message;
}