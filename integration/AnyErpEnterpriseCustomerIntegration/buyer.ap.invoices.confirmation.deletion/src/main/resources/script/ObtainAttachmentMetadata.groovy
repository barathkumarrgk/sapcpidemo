import com.sap.gateway.ip.core.customdev.util.Message
import org.apache.camel.impl.DefaultAttachment
import javax.mail.util.ByteArrayDataSource
import java.util.HashMap
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

def Message processData(Message message)
{
    def body = message.getBody(String.class)
    Object jsonBody = new JsonSlurper().parseText(body)
    def data = jsonBody.data
    def id
    def link
    def attachmentType
    def attachedTo
    def fileName
    def mimeType

    if (data instanceof List) {
        data.collect {
            message.setProperty("id", it.id)
            message.setProperty("link", it.link)
            message.setProperty("attachmentType", it.attachmentType)
            message.setProperty("attachedTo", it.attachedTo)
            message.setProperty("fileName", it.fileName)
            message.setProperty("mimeType", it.mimeType)
        }
    }

    message.setProperty('data', body)
    return message
}