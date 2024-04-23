import com.sap.gateway.ip.core.customdev.util.Message;
import groovy.json.JsonSlurper
import groovy.json.JsonOutput 
import java.text.SimpleDateFormat

def Message processData(Message message) {
    def body = message.getBody(java.lang.String) as String
    def bodyJson = new JsonSlurper().parseText(body)
    def heads = message.getHeaders()
    
    String tauliaConfirmationId = bodyJson.id
    String supplierNumber = bodyJson.supplier.vendorNumber
    String poNumber = bodyJson.purchaseOrder.number
    String lastUpdateOn = formatDate(new Date())

    heads.put('sourceValue',"$tauliaConfirmationId - $supplierNumber - $poNumber")
    heads.put('targetValue',"$lastUpdateOn")

    return message;
}

String formatDate(Date date) {
  def tz = TimeZone.getTimeZone("UTC");
  def df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
  df.setTimeZone(tz);
  df.format(date);
}
