import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {


    String reExp = "(?<=;filename=)(.*)(?=;creation-date)";
    def map = message.getHeaders();
    def conDisposition = map.get("content-disposition");

    def fileName = (conDisposition =~ reExp)[0][1]
    message.setHeader("fileName", fileName);

  return message
}