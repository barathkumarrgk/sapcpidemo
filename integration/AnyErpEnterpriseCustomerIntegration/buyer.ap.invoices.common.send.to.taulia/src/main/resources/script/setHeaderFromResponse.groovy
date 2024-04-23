import com.sap.gateway.ip.core.customdev.util.Message
import org.apache.camel.impl.DefaultAttachment
import javax.mail.util.ByteArrayDataSource
import java.util.HashMap
import groovy.json.JsonSlurper

def Message processData(Message message) {
  def body = message.getBody(String.class)
  def jsonSlurper = new JsonSlurper()
  def jsonObject = jsonSlurper.parseText(body)

  def batchId = jsonObject?.get("batchId") as String

  def messageLog = messageLogFactory.getMessageLog(message)
    if(messageLog != null && batchId != null){
        messageLog.addCustomHeaderProperty("batchId", batchId)
    }  
  return message
}