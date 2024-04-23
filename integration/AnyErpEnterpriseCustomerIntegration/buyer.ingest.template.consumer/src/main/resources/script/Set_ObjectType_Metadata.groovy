import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.json.JsonSlurper;

def Message processData(Message message) {

  Map<String, Object> props = message.properties

  def fileType = props.get("fileType")
  def fileTypeList = new JsonSlurper().parseText(fileType)
  def objectNumber = props.get("objectNumber").toInteger()
  def objects = props.get("objects")
  def objectList = new JsonSlurper().parseText(objects)
  def folder = objectList.get(objectNumber)

  objectNumber += 1

  props.put('fileTypeSize', fileTypeList.size())
  props.put('folder', folder)
  props.put('objectNumber', objectNumber)
  props.put('fileTypeNumber', 0)

  return message
}