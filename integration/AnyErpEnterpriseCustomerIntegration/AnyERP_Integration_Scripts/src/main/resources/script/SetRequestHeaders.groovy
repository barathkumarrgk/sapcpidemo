import com.sap.gateway.ip.core.customdev.util.Message
import java.util.HashMap
import java.text.SimpleDateFormat


def Message processData(Message message) {
    
    String dateHeader = formatDate(new Date())
    
    Map<String, Object> heads = message.headers
    Map<String, Object> props = message.properties
    
    heads.put('Date', dateHeader)
    heads.put('Content-Type','application/json')
    heads.put('x-tau-content-type', 'application/json')
    heads.put('x-tau-rest-external-reference-id', message.headers.SAP_MplCorrelationId)
    heads.put("x-tau-date", dateHeader)

    // Not needed - SetHeadersForTauliaRequest injects this into the header already.
    // heads.put('tauliaUrl', props.get('tauliaUrl'))
    // heads.put('secret', props.get('secret')) 
    // heads.put('x-tau-erp-client-id', props.get('erpClientId'))
    // heads.put('x-tau-userId', props.get('userId'))
    // heads.put('x-tau-rest-apikey', props.get('apiKey'))    
    
    return message
}

String formatDate(Date date) {
  def tz = TimeZone.getTimeZone("UTC")
  def df = new SimpleDateFormat("yyyy-MM-dd'T'00:00:00'Z'")
  df.setTimeZone(tz)
  df.format(date)
}