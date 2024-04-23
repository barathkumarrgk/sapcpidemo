import com.sap.gateway.ip.core.customdev.util.Message;
import groovy.json.JsonSlurper
import groovy.json.JsonOutput

/**
 * Constructs a businessUnit JSON body in the form: [{ "address":{},"currency":"","name":"","number":""}, ... ]
 * The XML to JSON converter is not able to return just a json array so this method takes {businessUnit:[...]} and converts it to
 * just [...]
 **/
def Message processData(Message message) {
    def body = message.getBody(java.lang.String) as String
    def jsonSlurper = new JsonSlurper()
    def jsonInput = jsonSlurper.parseText(body)
    
    def payload = jsonInput.get("businessUnit")

    payload.each { it ->
        if (!addressIsComplete(it.address)){
            it.address = null
        }
    }
    payload = recursivelyRemoveEmpties(payload)
    message.setBody(JsonOutput.prettyPrint(JsonOutput.toJson(payload)))
    return message;
}

def addressIsComplete(def addr) {
    addr.country ? true : false
}

def recursivelyRemoveEmpties(item) {
    switch (item) {
        case Map:
            return item.collectEntries { k, v ->
                [k, recursivelyRemoveEmpties(v)]
            }.findAll { k, v -> v }

        case List:
            return item.collect {
                recursivelyRemoveEmpties(it)
            }.findAll { v -> v }

        default:
            return item
    }
}