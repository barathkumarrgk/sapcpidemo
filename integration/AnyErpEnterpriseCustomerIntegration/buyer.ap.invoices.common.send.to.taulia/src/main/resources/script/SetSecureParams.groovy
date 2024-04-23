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
    def apikey = ''
    def buyerCompanyId = ''
    def secret = ''
    def erpClientId = ''

    Map<String, Object> heads = message.headers

    try{
        def secureParameter = secureStorageService.getUserCredential('ApiKeyAlias')
        apikey = secureParameter.getPassword().toString()
        message.setHeaders()
    } catch(Exception e){
        throw new SecureStoreException("ApiKeyAlias Secure Parameter not available")
    }
    
    try{
        def secureParameter = secureStorageService.getUserCredential('SecretAlias')
        secret = secureParameter.getPassword().toString()
        heads.put('secret', secret)
    } catch(Exception e){
        throw new SecureStoreException("Secret Secure Parameter not available")
    }
    
    try{
        def secureParameter = secureStorageService.getUserCredential('ErpClientId')
        apikey = secureParameter.getPassword().toString()
        heads.put('x-tau-rest-apikey', apikey)
    } catch(Exception e){
        throw new SecureStoreException("ErpClientId Secure Parameter not available")
    }
    
    try{
        def secureParameter = secureStorageService.getUserCredential('BuyerCompanyId')
        secret = secureParameter.getPassword().toString()
        heads.put('secret', secret)
    } catch(Exception e){
        throw new SecureStoreException("Secret Secure Parameter not available")
    }
    
    return message
}