import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import com.sap.it.api.ITApiFactory;
import com.sap.it.api.mapping.ValueMappingApi
import com.sap.it.api.securestore.SecureStoreService
import com.sap.it.api.securestore.UserCredential
import com.sap.it.api.securestore.exception.SecureStoreException
import java.text.SimpleDateFormat;


def Message processData(Message message) {
        
    def secureStorageService =  ITApiFactory.getService(SecureStoreService.class, null)
    def apikey = message.getHeaders().get('ApiKeyAlias')
    def secret = message.getHeaders().get('SecretAlias')

    Map<String, Object> heads = message.headers

    try{
        def secureParameter = secureStorageService.getUserCredential(apikey)
        apikey = secureParameter.getPassword().toString()
        heads.put('x-tau-rest-apikey', apikey)
    } catch(Exception e){
        throw new SecureStoreException("ApiKeyAlias Secure Parameter not available")
    }
    
    try{
        def secureParameter = secureStorageService.getUserCredential(secret)
        secret = secureParameter.getPassword().toString()
        heads.put('secret', secret)
    } catch(Exception e){
        throw new SecureStoreException("Secret Secure Parameter not available")
    }
    
    
    String dateHeader = formatDate(new Date())
    heads.put('Date', dateHeader)
    heads.put("x-tau-date", dateHeader)
    heads.put('x-tau-rest-external-reference-id', message.headers.SAP_MplCorrelationId)
    heads.put('Content-Type','application/json')
    heads.put('x-tau-content-type', 'application/json')
    heads.put('x-tau-erp-client-id', 'SAP_123')

    // URL setup
    Map<String, Object> props = message.properties
    props.put('tauliaUrl', 'https://extapi-buyer-integration.integration.taulia.com')
    return message;
}

String formatDate(Date date) {
  def tz = TimeZone.getTimeZone("UTC");
  def df = new SimpleDateFormat("yyyy-MM-dd'T'00:00:00'Z'");
  df.setTimeZone(tz);
  df.format(date);
}