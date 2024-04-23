import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import java.text.SimpleDateFormat;


def Message processData(Message message) {
    
    Map<String, Object> heads = message.headers
    String dateHeader = formatDate(new Date())
    
    heads.put('Date', dateHeader)
    heads.put('Content-Type','application/json')
    heads.put('x-tau-userId', '8a84931a8701ee440187025421420043')
    heads.put('x-tau-rest-external-reference-id', message.headers.SAP_MplCorrelationId)
    heads.put('secret', 'MMbWyq9soXofsnxHcnz0U3PvxaEf2Ybez8a4Mv3T5yFS2MpB3p')
    heads.put('x-tau-content-type', 'application/json')
    heads.put('x-tau-rest-apikey', '2BcxIDQk4p1IzPB6L29IKSx7T2hwq84Yjiqa')
    heads.put("x-tau-date", dateHeader)
    heads.put("x-tau-erp-client-id", 'ANYERP_CI_CH_A')
    heads.put('tauliaUrl', 'https://buyerintegration.integration.taulia.com')
    
    return message;
}

String formatDate(Date date) {
  def tz = TimeZone.getTimeZone("UTC");
  def df = new SimpleDateFormat("yyyy-MM-dd'T'00:00:00'Z'");
  df.setTimeZone(tz);
  df.format(date);
}