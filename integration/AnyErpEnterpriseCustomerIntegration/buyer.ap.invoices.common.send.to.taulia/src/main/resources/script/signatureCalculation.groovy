import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import com.sap.gateway.ip.core.customdev.util.Message
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Base64;

def Message processData(Message message) {
  map = message.getHeaders();
  def secret = map.get("secret") as String;
  def contentType = map.get("x-tau-content-type") as String;
  def httpMethod = map.get("httpMethod") as String;
  def urlPath = map.get("httpUri") as String;
  def dateHeader = map.get("x-tau-date") as String;
  String HMAC_SHA1_ALGORITHM = "HmacSHA1";

  String signatureRaw = """${httpMethod}
${contentType}
${dateHeader}
${urlPath}""";
  SecretKeySpec signingKey = new SecretKeySpec(secret.getBytes('UTF8'),HMAC_SHA1_ALGORITHM);
  Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
  mac.init(signingKey);
  byte[] rawHmac = mac.doFinal(signatureRaw.getBytes("UTF8"));
  message.setHeader("x-tau-rest-signature", rawHmac.encodeBase64().toString());
  return message;
}