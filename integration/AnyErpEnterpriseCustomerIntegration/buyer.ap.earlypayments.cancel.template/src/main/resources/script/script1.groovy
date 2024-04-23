import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message) {

  Map<String, Object> headers = message.headers

  /******* Customer Properties *******/
  headers.put('buyerName', 'someName')
  headers.put('erpClientId', 'ANYERP_CI_CH_A')
  headers.put('buyerCompanyId', '5599e78e0e4642d69c60f2a4f62faec3')
  headers.put('userId', '8a84931a8701ee440187025421420043')
  
  
  /******* Early Payment and CSV Properties *******/
  headers.put('EPFilenamePrefix', 'EPR_OUT_')  // CSV file created will be prefixed with this value. 
  headers.put('splitCSVByEP', 'true') // One CSV file will be created for each Early Payment (grouped by Transaction ID)
  headers.put('financeType', 'DD') // CSV file format type and version
  headers.put('financeVersion', 'V1')
  headers.put('fileExtension', '.csv')


  /******* SFTP Properties *******/
  headers.put('sftpUser', 'SAP-CI-int-buyer-root')
  headers.put('sftpPKey', 'sap-ci-int-buyer')
  headers.put('sftpAddress', 'sftp.taulia.com')
  headers.put('sftpPathDirectory', '/charles.harward.int/in/')
  
  /******* Extra Processing *******/
  headers.put('zipping', 'false') // Will the payload be zipped before sending
  headers.put('pgp', 'false') // Will the payload be encrypted before sending
  headers.put('pgpUserId', 'changeMe') // Links to PGP userId

  return message
}