import com.sap.gateway.ip.core.customdev.util.Message
import java.util.HashMap
import java.util.Map

/**
 * {"invoiceId":"i1","notificationId":"n1"}
 **/
def Message processData(Message message) {
    Map<String, Object> body = new HashMap<String, Object>()
	Map<String, Object> headerObjs = message.headers
	def notId = headerObjs.get('notificationId').toString()
	def invId = headerObjs.get('invoiceId').toString()
    notId = "\""+notId+"\""
    invId = "\""+invId+"\""

    StringBuilder invNotificationMapBody = new StringBuilder("{")
    invNotificationMapBody.append("\""+"invoiceId"+"\":" + invId)
    invNotificationMapBody.append(",")
	invNotificationMapBody.append("\""+"notificationId"+"\":" + notId)
    invNotificationMapBody.append("}")
    message.setBody(invNotificationMapBody.toString())
	return message
}