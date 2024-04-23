import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message) {
    
    if(message?.headers?.get('CamelFileNameConsumed') != message?.properties?.previousFile){
        message.properties.put('previousFile', message.headers.get('CamelFileNameConsumed'))
    } else{
        message.properties.put('endLoop', 'true')
    }
    
    return message
}
