import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
    def attachments = message.getProperty('Attachments')
    if (attachments.isEmpty()) {
        // This should never happen!
        throw new AssertionError('Empty attachments Map in property Attachments')
    }
    // Get the key of the next attachment to process.
    def nextKey = attachments.keySet().iterator().next()
    // Remove the attachment from the Map
    def attachment = attachments.remove(nextKey)
    // Replace the message body with the attachment's contents.
    message.setBody(attachment.getContent())
    // Update the AttachmentCount property.
    message.setProperty('AttachmentCount', attachments.size())
    // Set header of filename.
    message.setHeader("fileName", attachment.getName())
    // All done.
    return message
}