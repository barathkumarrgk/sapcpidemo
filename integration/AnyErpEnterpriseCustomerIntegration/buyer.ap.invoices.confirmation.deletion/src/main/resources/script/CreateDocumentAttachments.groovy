import com.sap.gateway.ip.core.customdev.util.Message
import org.apache.camel.impl.DefaultAttachment
import javax.mail.util.ByteArrayDataSource
import java.util.HashMap

def Message processData(Message message)
{
    // 1: Obtain Body and Properties from Message
    def body = message.getBody(java.lang.Object) as Object
    def map = message.getProperties();
    def head = message.getHeaders();
    def id = map.get("id");
    def link = map.get("link");
    def attachmentType = map.get("attachmentType");
    def attachedTo = map.get("attachedTo");
    def fileName = map.get("fileName")

    //def fileName = head.get("CamelFileName")
    def mimeType = map.get("mimeType");
    
    // 2: Create the dataSource with body in Message and type of file
    def dataSource = new ByteArrayDataSource(body, mimeType)

    // 3: Construct a DefaultAttachment object
    def attachment = new DefaultAttachment(dataSource)

    // Set header
	message.setHeader("CamelFileName", fileName);

    // 4: Add the attachment to the message
    message.addAttachmentObject(fileName, attachment)

    return message
}