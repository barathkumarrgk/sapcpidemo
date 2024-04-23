import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message)
{
  map = message.getHeaders();
  def workingFileName = map.get("fileName");

  String fileNameNew = workingFileName.toUpperCase();
  int indexOfUnderscore = fileNameNew.indexOf("_");
  String folder = fileNameNew.substring(0, indexOfUnderscore);

  message.setHeader("folder",folder).toString();
  message.setProperty("folder",folder).toString();
  return message;
}