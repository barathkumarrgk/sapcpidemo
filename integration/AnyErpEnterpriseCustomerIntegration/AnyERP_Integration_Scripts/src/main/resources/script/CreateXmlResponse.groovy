import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.xml.*
import groovy.json.*
def Message processData(Message message) {
        //Parse XML Body 
        def body = message.getBody(String);
        def root = new XmlParser().parseText(body)
        //Get Properties 
        props = message.getProperties();
        identifiervalue = props.get("identifier");
        interfaceidvalue = props.get("interfaceid");
        //check if external indetifier exists
        if(identifiervalue)
        {
            identifier = identifiervalue;
        }else
        {
            identifier = root.tickets.referenceId[0].text();
        }
            //build XMl Response
            def writer = new StringWriter()
            def builder = new MarkupBuilder(writer)
             builder.confirmationMessage
            {
                'identifier'(identifier)
                'interface'(interfaceidvalue)
            }
        message.setBody(writer.toString())
        return message;
        }
    