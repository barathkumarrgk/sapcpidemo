import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message) {

  Map<String, Object> headers = message.headers

  /******* Customer Properties *******/
  headers.put('buyerName', 'someName')
  headers.put('erpClientId', 'ANYERP_CI_CH_A')
  headers.put('buyerCompanyId', '5599e78e0e4642d69c60f2a4f62faec3')
  headers.put('userId', '8a84931a8701ee440187025421420043')
  headers.put('filename', 'someName_invoice_')
  
  /******* SFTP Properties *******/
  headers.put('sftpUsername', 'SAP-CI-int-buyer-root')
  headers.put('privateKeyAlias', 'sap-ci-int-buyer')
  headers.put('address', 'sftp.taulia.com')
  headers.put('directory', '/john.hughes.int/out/')

  return message
}
