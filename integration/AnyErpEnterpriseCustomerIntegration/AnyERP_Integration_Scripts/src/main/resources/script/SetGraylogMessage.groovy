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
    String logReferenceId = props.get("logReferenceIdGraylog")
    String logSapCiLink = props.get("logSapCiLinkGraylog")
    
    if(props.get("payloadToObfuscate")) {
        PlatformRequest platformRequest = new PlatformRequest(
            host : source,
            level : logLevel,
            iflow : logIflow,
            timestamp : logTimestamp,
            referenceId : logReferenceId,
            sapCiLink : logSapCiLink,
            short_message: message.getBody(java.lang.String) as String
            )
        props.put('graylogMessage', groovy.json.JsonOutput.toJson(platformRequest))

    } else {
        GraylogMessage graylogMessage = new GraylogMessage(
          host : source,
          short_message : logMessage,
          http_status_code : httpStatus,
          level : logLevel,
          iflow : logIflow,
          timestamp : logTimestamp,
          referenceId : logReferenceId,
          sapCiLink : logSapCiLink
        )
    
        props.put('graylogMessage', groovy.json.JsonOutput.toJson(graylogMessage))
    }
    
    return message;
}


class GraylogMessage {
    String host
    String short_message
    String http_status_code
    int level
    String iflow
    String timestamp
    String referenceId
    String sapCiLink
}

class PlatformRequest {
    String host
    String short_message
    int level
    String iflow
    String timestamp
    String referenceId
    String sapCiLink
}

