import com.sap.gateway.ip.core.customdev.util.Message
import java.nio.charset.StandardCharsets
import java.io.OutputStreamWriter
import groovy.xml.*

def Message processData(Message message) {
    def payload = message.getBody(java.lang.String.class)
    def root = new XmlParser().parseText(payload)
    def csv = new StringBuffer()

    // Write header row
  //  csv.append("\"")
    root.children().first().children().each { field ->
        csv.append(field.name())
        if (field != root.children().first().children().last()) {
            csv.append('|')
        }
    }
   // csv.append('"\n')
       csv.append('\n')

    // Write data rows    
    root.children().each { record -> 

        record.children().each { field ->
            csv.append(field.text())
            if (field != record.children().last()) {
                csv.append('|')
            }
        }
    csv.append('\n')
    }

    message.setBody(csv.toString())
    return message
}