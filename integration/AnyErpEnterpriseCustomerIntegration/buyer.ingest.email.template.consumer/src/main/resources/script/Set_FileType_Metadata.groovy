import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.json.JsonSlurper;

def Message processData(Message message) {

  Map<String, Object> props = message.properties

  def fileTypeNumber = props.get("fileTypeNumber").toInteger()
  def fileTypes = props.get("fileType")
  def fileTypeList = new JsonSlurper().parseText(fileTypes)
  def fileType = fileTypeList.get(fileTypeNumber)

  fileTypeNumber += 1

  props.put('fileTypeExtension', fileType)
  props.put('fileTypeNumber', fileTypeNumber)
  props.put('previousFile', '')

  return message
}