import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
  def finOpsEmail = message?.getHeader('finOpsEmail')
  if (!finOpsEmail || finOpsEmail == "null") {
    message.setProperty('toEmailNotification', finOpsEmail)
  }

  return message
}