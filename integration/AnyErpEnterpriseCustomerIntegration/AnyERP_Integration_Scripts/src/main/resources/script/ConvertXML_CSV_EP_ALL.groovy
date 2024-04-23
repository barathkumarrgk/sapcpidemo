/**
 * DD-V1 format for Early Payment XML to CSV
 * Converts the incoming DD-V1 EP request XML format to CSV
 * NOTE: The CI Aggregator component wraps each earlyPayment with an "'multimap:Message1'" tag,
 *       and each resulting earlyPayment record is enclosed in an "<earlyPayments />" tag.
**/

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
    
    // Get element names from first earlyPayment
    def headerNames = messages."${xmlRecordPrefix}".earlyPayments.earlyPayment[0].children().findAll { 
        !excludeColumns.contains(it.name())
    }.collect { it.name() }

    def headers = headerNames.collect { 
        DOUBLE_QUOTE.concat(it).concat(DOUBLE_QUOTE)
    }

    def csvContent = new StringBuilder()

    // Build CSV Header   
    csvContent.append(headers.join(CSV_SEPARATOR)).append(NEW_LINE)

    // Gather CSV Rows
    messages."${xmlRecordPrefix}".earlyPayments.each { earlyPayments ->
        earlyPayments.earlyPayment.each { earlyPayment ->
            def rowData = earlyPayment.children().findAll { 
                !excludeColumns.contains(it.name())
            }.collect { it.text() }
            csvContent.append(rowData.join(CSV_SEPARATOR)).append(NEW_LINE)
        }
    }
    
    // Set Body
    message.setBody(csvContent.toString())
	return message
}