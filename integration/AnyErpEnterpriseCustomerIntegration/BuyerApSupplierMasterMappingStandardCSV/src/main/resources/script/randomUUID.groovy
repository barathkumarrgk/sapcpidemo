import com.sap.it.api.mapping.*;

def String customFunction1(String generateUUIDString){

def randomID = UUID.randomUUID().toString().replace("-", "").toUpperCase();

return randomID

}