import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
def Message processData(Message message) {
    //Body
    def body = message.getBody();

    def headers = message.getHeaders();
    def value = headers.get("CamelFileName");
    message.setProperty("filename",value)
     
    String regexzip = /^.+\.(?:(?:[zZ][iI][pP]))$/
    String regexcsv = /^.+\.(?:(?:[cC][sS][vV]))$/
    String regexp7m = /^.+\.(?:(?:[pP][7][mM]))$/
    String regexedi = /^.+\.(?:(?:[eE][dD][iI]))$/
    String regextxt = /^.+\.(?:(?:[tT][xX][tT]))$/
    String regexx12 = /^.+\.(?:(?:[xX][1][2]))$/


    if (value ==~ regexzip)
    {
        message.setProperty("contenttype","application/zip");
    }
    else if(value ==~ regexcsv)
    {
        message.setProperty("contenttype","text/csv");
    }
    else if(value ==~ regexp7m)
    {
        message.setProperty("contenttype","application/pkcs7-mime");
    }
     else if(value ==~ regexedi)
    {
        message.setProperty("contenttype","application/edi");
    }
    else if(value ==~ regexx12)
    {
        message.setProperty("contenttype","application/edi");
    }
    else if(value ==~ regextxt)
    {
        message.setProperty("contenttype","application/edi");
    }
    else
    {
      message.setProperty("contenttype","application/octstream");  
    }

    return message;
}