import com.sap.gateway.ip.core.customdev.util.Message
import org.apache.camel.impl.DefaultAttachment
import javax.mail.util.ByteArrayDataSource
import java.util.HashMap


def Message processData(Message message)
{
  def body = message.getBody(String.class)
  def result = message.getProperties()
  def messageLog = messageLogFactory.getMessageLog(message)
    if(messageLog != null){
        messageLog.setStringProperty("Logging#3", "Printing Attachment")
        messageLog.addAttachmentAsString("buyer.ap.invoices.common.send.to.taulia", body, "text/plain")
    }  
  return message
}
