import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message) {
    
    Map<String, Object> props = message.properties
    props.put('graylogUrl', 'graylog-inf-external.integration.taulia.com')
    props.put('graylogPort', '12201')
    props.put('graylogSource', 'sap-ci-buyer-integration')

    return message;
}