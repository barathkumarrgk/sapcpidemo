import com.sap.gateway.ip.core.customdev.util.Message;
import groovy.json.*;

def Message processData(Message message) {
    def body = message.getBody(java.lang.String) as String
    def fields = message.properties.get("payloadToObfuscate")
    def fieldsArray = new JsonSlurper().parseText(fields)
    def logOutput = [:]

    fieldsArray.each{ field -> 
      def value = findPropertyValue(body, field)  
      logOutput.put(field, obfuscateProperty(value))
    }
    
    def jsonLog = JsonOutput.toJson(logOutput)
    message.setBody(jsonLog)
    return message
}

def findPropertyValue(jsonBody, propertyName) {
    def json = new JsonSlurper().parseText(jsonBody)
    return traverseJson(json, propertyName)
}

def traverseJson(json, propertyName) {
    def finalValue
    if (json instanceof Map) {
        json.each { key, value ->
            if (key == propertyName) {
                finalValue = value
                return
            } else {
                def result = traverseJson(value, propertyName)
                if (result != null) {
                    finalValue = result
                    return
                }
            }
        }
    } else if (json instanceof List) {
        json.each { item ->
            def result = traverseJson(item, propertyName)
            if (result != null) {
                finalValue = result
                return
            }
        }
    }
    return finalValue
}

def obfuscateProperty(String property){
    if(property) {
        def len = property.length() / 2  as Integer
        def regExp = "^.{$len}"
        def mask = '*' * len
        return property.replaceAll(regExp, mask)
    }
    return property
} 
