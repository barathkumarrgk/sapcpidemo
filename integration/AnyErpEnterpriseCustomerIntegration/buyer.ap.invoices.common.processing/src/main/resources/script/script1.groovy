import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
def Message processData(Message message) {
    //Body
    def body = message.getBody();

    //Properties
 
    def properties = message.getProperties();
    int value = properties.get("CamelSplitIndex") as Integer;
    int Split = value%10
    
    //validate on the iteration
    if(Split == 0 && value!=0)
    {
        message.setProperty("waitrequired","true");
    }
    else
    {
        message.setProperty("waitrequired","false");
    }
    
    return message;
}