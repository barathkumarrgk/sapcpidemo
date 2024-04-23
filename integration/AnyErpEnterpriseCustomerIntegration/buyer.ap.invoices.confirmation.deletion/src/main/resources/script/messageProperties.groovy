import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message) {

  Map<String, Object> props = message.properties
  props.put('buyerCompanyId', '5599e78e0e4642d69c60f2a4f62faec3')

  return message
}