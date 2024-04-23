import com.sap.gateway.ip.core.customdev.util.Message
import javax.activation.DataHandler

def Message processData(Message message) {
    // Create a new Map of attachments and store it in a property.
    def attachments = new HashMap<String, DataHandler>(message.getAttachments())
    message.setProperty('Attachments', attachments)
    // Store the number of attachments in a property.
    message.setProperty('AttachmentCount', attachments.size())
    // Clear the attachments and attachment wrappers.
    message.getAttachments().clear()
    message.getAttachmentWrapperObjects().clear()
    // All done.
    return message
}