import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message) {

  Map<String, Object> props = message.properties

  props.put('objects', '["paymentRecord"]')
  props.put('fileType', '["csv"]')
  props.put('buyerName', 'someName')
  props.put('erpClientId', 'ANYERP_CI_CH_A')
  props.put('buyerCompanyId', '5599e78e0e4642d69c60f2a4f62faec3')
  props.put('emailNotificationFailure', 'someEmail')

  //SFTP Props
  props.put('sftpUser', 'SAP-CI-int-root')
  props.put('sftpPKey', 'sap-ci-int')
  props.put('sftpAddress', 'sftp.taulia.com')
  props.put('sftpPathDirectory', '/john.hughes.int/in/')

  //Process Flag
  props.put('processFiles', 'true')

  return message
}