
import com.sap.gateway.ip.core.customdev.util.Message
import groovy.util.XmlSlurper

def Message processData(Message message) {

    String xmlRecordPrefix = 'multimap:Message1'
    String NEW_LINE = "\n"
    String CSV_SEPARATOR = ","
    String DOUBLE_QUOTE = "\""

    List<String> excludeColumns = ["sequence","stop"]

	def body = message.getBody(java.lang.String) as String
    def writer = new StringWriter()

    // Parse input
    def slurper = new XmlSlurper(false, false)
    def messages = slurper.parseText(body)

    def headerNames = messages."${xmlRecordPrefix}".root[0].invoice[0].children().collect { it.name() }

    def headers = headerNames.collect { 
        DOUBLE_QUOTE.concat(it).concat(DOUBLE_QUOTE)
    }

    def csvContent = new StringBuilder()

    // Build CSV Header   
    csvContent.append(headers.join(CSV_SEPARATOR)).append(NEW_LINE)

    // Gather CSV Rows
    messages."${xmlRecordPrefix}".root.each { invoice ->
        def rowData = invoice.children().collect { it.text() }
        csvContent.append(rowData.join(CSV_SEPARATOR)).append(NEW_LINE)
    }
    // Set Body
    message.setBody(csvContent.toString())
	return message
}