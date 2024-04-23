/* Refer the link below to learn more about the use cases of script.
https://help.sap.com/viewer/368c481cd6954bdfa5d0435479fd4eaf/Cloud/en-US/148851bf8192412cba1f9d2c17f4bd25.html

If you want to know more about the SCRIPT APIs, refer the link below
https://help.sap.com/doc/a56f52e1a58e4e2bac7f7adbf45b2e26/Cloud/en-US/index.html */
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message) {


    def map = message.getProperties();
    def vId = map.get("versionId");

    if(vId == "Draft"){
        vId = "1.0.0"
    }else{
        vId = setVersion(vId)
    }
    message.setProperty("newVersionId", vId);

  return message
}

def setVersion(String version) {
    def intVersion = version.replaceAll("[.]", "") as Integer;
    intVersion++;
    def strVersion = intVersion as String;
    def newVersion = strVersion.toCharArray().join('.');
    return newVersion;
}