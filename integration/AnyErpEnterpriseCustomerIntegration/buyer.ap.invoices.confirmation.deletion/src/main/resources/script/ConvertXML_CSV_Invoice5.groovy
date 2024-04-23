
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

    def headerNames = messages."${xmlRecordPrefix}".root[0].invoice[0].children().collect { it.name() }
    def csvContent = new StringBuilder()

    // Build CSV Header   
    csvContent.append(headerNames.join(CSV_SEPARATOR)).append(NEW_LINE)

    // Gather CSV Rows
    messages."${xmlRecordPrefix}".root.invoice.each { invoiceRecord ->
        def rowData = invoiceRecord.children().collect { it.text() }
        csvContent.append(rowData.join(CSV_SEPARATOR)).append(NEW_LINE)
    }
    // Set Body
    message.setBody(csvContent.toString())
	return message
}