
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message) {
    try {
        throw new IOException(); //or anything that have declared checked exception
    } catch (Exception e) {
        throw e;
    }
}