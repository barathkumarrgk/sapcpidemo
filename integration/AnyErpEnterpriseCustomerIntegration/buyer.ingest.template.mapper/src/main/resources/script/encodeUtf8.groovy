import com.sap.gateway.ip.core.customdev.util.Message

import java.nio.charset.StandardCharsets

def Message processData(Message message) {

def body = message.getBody(java.lang.String)

message.setBody(body.getBytes(StandardCharsets.UTF_8))

return message

}