import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {

    def body = message.getBody(java.lang.String) as String
    
    def lines = body.trim().split('\n')
    def header = lines[0].replaceAll("\\s+", "")
    def dataLines = lines[1..-1]
    println(dataLines)
    def modifiedCSV = [header] + dataLines

    modifiedCSV = modifiedCSV.join('\n')
    
    message.setBody(modifiedCSV)

    return message
    
}
