import com.sap.gateway.ip.core.customdev.util.Message;
import groovy.json.*;

def Message processData(Message message) {
    def body = message.getBody(java.lang.String) as String
    def fields = message.properties.get("payloadToObfuscate")
    def fieldsArray = new JsonSlurper().parseText(fields)
    def json = new JsonSlurper().parseText(body)

    fieldsArray.each{ field -> 
      findPropertyValue(json.json, field)  
    }
    JsonOutput.toJson(json)
    def finalOutput = JsonOutput.prettyPrint(JsonOutput.toJson(json))
    message.setBody(finalOutput)
    return message
}

def findPropertyValue(jsonBody, propertyName) {
    def fieldToObfuscateStructure = propertyName.split("\\.")
    println(fieldToObfuscateStructure)
    return traverseJson(jsonBody, fieldToObfuscateStructure, null)
}

def Object traverseJson(json, arrayWithNameHierarchy, parent) {
    if (json instanceof Map) {
        json.each { entry ->
            if(!parent) {
                println(arrayWithNameHierarchy)
               if (entry.key == arrayWithNameHierarchy[0] && !(entry.value instanceof List || entry.value instanceof Map)) {
                    println(entry.key)
                    entry.value = obfuscateProperty(entry.value)
                } else if (arrayWithNameHierarchy.size() > 1 && entry.key == arrayWithNameHierarchy[0]) {
                    println(entry.key)
                    traverseJson(entry.value, arrayWithNameHierarchy[1..-1] , null)
                } 
            } else {
                if (parent == arrayWithNameHierarchy[0] && entry.key == arrayWithNameHierarchy[1]) {
                    if(!(entry.value instanceof List || entry.value instanceof Map)) {
                        entry.value = obfuscateProperty(entry.value)
                    } else {
                        traverseJson(entry.value, arrayWithNameHierarchy[1..-1] , arrayWithNameHierarchy[1])
                    } 
                } 
            }
            
        }
    } else if (json instanceof List) {
        json.each { item ->
            item = traverseJson(item, arrayWithNameHierarchy, parent)
            return item
        }
    }
}

def obfuscateProperty(String property){
    def MAX_SIZE_STRING = 30
    if(property) {
        if(property.length() > MAX_SIZE_STRING) {
            property = property.substring(property.length() - 10)
        }
        def len = property.length() / 1.5  as Integer
        def regExp = "^.{$len}"
        def mask = '*' * len
        return property.replaceAll(regExp, mask)
    }
    return property
} 