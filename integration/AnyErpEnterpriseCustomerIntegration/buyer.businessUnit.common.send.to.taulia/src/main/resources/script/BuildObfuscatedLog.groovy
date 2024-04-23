import com.sap.gateway.ip.core.customdev.util.Message
import org.apache.camel.impl.DefaultAttachment
import javax.mail.util.ByteArrayDataSource
import java.util.HashMap
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

def Message processData(Message message)
{
    // 1: Obtain Body and Properties from Message
    def body = message.getBody(java.lang.Object) as Object
    def props = message.getProperties();
   
    def bankAccount = props.get("bankAccount");
    def iban = props.get("iban");
    def swift = props.get("swift");
    
    // 2: Create the dataSource with body in Message and type of file
    Map<String, Object> obfValues = [:]
    obfValues.put('bankAccount', obfuscateProperty(bankAccount))
    obfValues.put('iban', obfuscateProperty(iban))
    obfValues.put('swift', obfuscateProperty(swift))
    
    // 3: Construct a json object
    def jsonString = JsonOutput.toJson(obfValues)

    // 4: Change the body
    message.setBody(jsonString)

    return message
}

def obfuscateProperty(String property){
    def len = property.length() / 2  as Integer
    def regExp = "^.{$len}"
    def mask = '*' * len
    def result = property.replaceAll(regExp, mask)
}  