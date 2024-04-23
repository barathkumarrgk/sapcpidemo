
import com.sap.gateway.ip.core.customdev.util.Message
import groovy.util.XmlSlurper

/**
 id|additionalCharges|attachments|buyerTimestamp
 invoice1|||
 */
def Message processData(Message message) {

    String xmlRecordPrefix = 'multimap:Message1'
    String NEW_LINE = "\n"
    String CSV_SEPARATOR = "|"

    def body = message.getBody(java.lang.String) as String
    // Parse input
    def slurper = new XmlSlurper(false, false)
    def messages = slurper.parseText(body)

    def headerNames = messages."${xmlRecordPrefix}".root[0].children().collect { it.name() }
    def csvContent = new StringBuilder()

    // Build CSV Header
    csvContent.append(headerNames.join(CSV_SEPARATOR)).append(NEW_LINE)

    // Gather CSV Rows
    messages."${xmlRecordPrefix}".root.each { invoiceRecord ->
        def rowData = invoiceRecord.children().collect { it.text() }
        csvContent.append(rowData.join(CSV_SEPARATOR)).append(NEW_LINE)
    }
    // Set Body
    message.setBody(csvContent.toString())

    // Set attachment property
    StringBuilder attachments = new StringBuilder()
    String finalAttachmenIds = ""
    messages."${xmlRecordPrefix}".root.each { invoiceRecord ->
        invoiceRecord.attachments.each { attachmentId ->
            attachments.append(attachmentId).append(",")
        }
    }
    if(attachments.length() > 0) {
        finalAttachmenIds = attachments.substring(0, attachments.length() - 1)
    }
    message.setProperty("attachments", finalAttachmenIds)
    return message
}