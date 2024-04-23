import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
  def body = message.getBody(String) as String
  if (!body || body == "null") {
   message.setHeader('content-length', 0)
  }
  
  return message
}