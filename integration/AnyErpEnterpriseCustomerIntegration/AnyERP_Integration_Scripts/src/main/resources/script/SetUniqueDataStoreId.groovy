import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import java.text.SimpleDateFormat;


def Message processData(Message message) {
    
    Map<String, Object> heads = message.headers
    
    // sapDataStoreId - set UUID for DataStore record storage / retrieval
    heads.put "sapDataStoreId", UUID.randomUUID().toString().replace("-", "")
    
    return message;
}