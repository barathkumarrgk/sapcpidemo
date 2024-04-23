import com.sap.gateway.ip.core.customdev.util.Message
import org.apache.log4j.Logger

Message processData(Message message) {
    
    def mapProperties = message.getProperties()
    String graylogMessage = mapProperties.get("graylogMessage")
    String URL = mapProperties.get("graylogUrl")
    int PORT = mapProperties.get("graylogPort").toInteger()

    DatagramSocket socket = new DatagramSocket();
    InetAddress address = InetAddress.getByName(URL)
    byte[] buf = graylogMessage.getBytes()

    try{
        socket.setSoTimeout(5000)
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, PORT)
        socket.send(packet)
    } catch (IOException e) {
        Logger logger = Logger.getLogger(getClass())
        logger.error("Error sending DatagramPacket: " + e.getMessage(), e)
    } finally {
        socket.close()
    }

    return message
}