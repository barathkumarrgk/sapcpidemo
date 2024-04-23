import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message)
{
    // JPM-{country}-rates
    // JPM+{country}-rates
    // jpm-BR-rates@connect.taulia.com
    // jpm+BR-rates@connect.taulia.com

  map = message.getHeaders();
  def sender = map.get("From");
  def receiver = map.get("To");
  def name = map.get("fileName");

  String receiverEmail = receiver;
  
  receiverEmail = receiverEmail.contains("+") ? receiverEmail.replace("+","-") : receiverEmail
  //country = receiverEmail.split("-")[1]  

  String senderDomain = sender.toLowerCase().replaceAll(">","")

  String senderClean = senderDomain;
  int indexOfLessThan = senderClean.lastIndexOf("<") + 1;
  String senderFinal = senderClean.substring(indexOfLessThan);
  
  String getExtension = name;
  int indexOfPoint = getExtension.lastIndexOf(".") + 1;
  String extension = getExtension.substring(indexOfPoint).toUpperCase();

  message.setProperty("approvedSender",senderFinal).toString();
  message.setHeader("SAP_ApplicationID",name).toString();
  message.setHeader("extension",extension).toString();

  return message;
}