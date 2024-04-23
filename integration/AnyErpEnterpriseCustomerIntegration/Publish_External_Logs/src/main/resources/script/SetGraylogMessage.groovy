import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message) {
    
    Map<String, Object> props = message.properties
    
    String source = props.get("graylogSource")
    String logMessage = props.get("logMessageGraylog")
    String httpStatus = props.get("logHttpStatusCodeGraylog")
    int logLevel = props.get("logLevelGraylog").toInteger()
    String logIflow = props.get("logIflowGraylog")
    String logTimestamp = props.get("logTimestampGraylog")
    
    GraylogMessage graylogMessage = new GraylogMessage(
      host : source,
      short_message : logMessage,
      http_status_code : httpStatus,
      level : logLevel,
      iflow : logIflow,
      timestamp : logTimestamp
    )

    props.put('graylogMessage', groovy.json.JsonOutput.toJson(graylogMessage))

    return message;
}


class GraylogMessage {
    String host
    String short_message
    String http_status_code
    int level
    String iflow
    String timestamp
}