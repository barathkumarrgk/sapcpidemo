import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message) {
    
    Map<String, Object> props = message.properties
    
    props.put('objects', '["in"]')
    props.put('fileType', '["xml"]')
    props.put('buyerName', 'someName')
    props.put('erpClientId', 'someErpClientId')
    props.put('buyerCompanyId', 'someBuyerCompanyId')
    props.put('emailNotificationFailure', 'someEmail')
    
    //SFTP Props
    props.put('sftpUser', 'SAP-CI-int-root')
    props.put('sftpPKey', 'sap-ci-int')
    props.put('sftpAddress', 'sftp.taulia.com')
    props.put('sftpPathDirectory', '/john.hughes.int/')
    
    //Validation Props
    props.put('endLoop', 'false')
   
    return message
}