import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.json.JsonSlurper
import groovy.json.JsonOutput 

def Message processData(Message message) {
    def body = message.getBody(java.lang.String) as String;
    def props = message.getProperties()
    def poConfirmationIdsData = props.get("poConfirmationIdsBody")
    def poConfirmationIdsDataJson = new JsonSlurper().parseText(poConfirmationIdsData)
    List<String> poConfirmationsId = poConfirmationIdsDataJson.data

    def poConfirmationUpdateList = poConfirmationsId.collect{
        new PoConfirmationUpdateEntry(confirmationId: it, confirmationStatus: "Received")
    }
    println(JsonOutput.toJson(poConfirmationUpdateList))

    message.setBody(JsonOutput.toJson(poConfirmationUpdateList))
    return message;

}

class PoConfirmationUpdateEntry{
    String confirmationId
    String confirmationStatus
}
