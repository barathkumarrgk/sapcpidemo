import com.sap.gateway.ip.core.customdev.util.Message
import java.util.HashMap
import org.apache.commons.csv.*

def Message processData(Message message) {
    def lineNo = 0
    def columnCount = 0
    def rowCount = 0

    def lines = message.getBody(java.lang.String) as String
    
    // Parse the CSV content
    def parser = CSVParser.parse(lines, CSVFormat.DEFAULT)
    
    // Iterate through each CSV record
    for (CSVRecord record : parser) {
        rowCount++
        
        // The first record is considered as header, so it provides column count
        if (rowCount == 1) {
            columnCount = record.size()
        }
    }

    message.setProperty("csvRowCount", rowCount)
    message.setProperty("csvColumnCount", columnCount)
    
    return message
}
