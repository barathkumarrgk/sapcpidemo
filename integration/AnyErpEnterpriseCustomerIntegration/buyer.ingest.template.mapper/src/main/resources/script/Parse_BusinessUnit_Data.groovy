import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.MarkupBuilder
import java.time.LocalDate
import java.time.format.DateTimeFormatter

Message processData(Message message) {
    // Access message body and properties
    Reader reader = message.getBody(Reader)
    Map properties = message.getProperties()

    // Define XML parser and builder
    def vendor = new XmlSlurper().parse(reader)
    def writer = new StringWriter()
    def builder = new MarkupBuilder(writer)

    // Define target payload mapping


    builder.payload {
        vendor.line.each{
            bu->
            'root' {
                'address' {
                    'city'(bu.city[0].text()?.trim()?:null)
                    'country'(bu.country[0].text()?.trim()?:null)
                    'email'(bu.VALUE[0].text()?.trim()?:null)
                    'erpTimeZone'(bu.VALUE[0].text()?.trim()?:null)
                    'fax'(bu.VALUE[0].text()?.trim()?:null)
                    'language'(bu.VALUE[0].text()?.trim()?:null)
                    'name'(bu.name[0].text()?.trim()?:null)
                    'phone'(bu.VALUE[0].text()?.trim()?:null)
                    'poBox'(bu.VALUE[0].text()?.trim()?:null)
                    'poBoxCity'(bu.VALUE[0].text()?.trim()?:null)
                    'poBoxPostalCode'(bu.VALUE[0].text()?.trim()?:null)
                    'postalCode'(bu.postalCode[0].text()?.trim()?:null)
                    'region'(bu.region[0].text()?.trim()?:null)
                    'street'(bu.street1[0].text()?.trim()?:null)
                    'street2'(bu.street2[0].text()?.trim()?:null)
                    'street3'(bu.VALUE[0].text()?.trim()?:null)
                }
                'businessUnitSuffix'(bu.VALUE[0].text()?.trim()?:null)
                'businessUnitsSuffixed' {
                    'element'(bu.VALUE[0].text()?.trim()?:null)
                }
                'currency'(bu.currency[0].text()?.trim()?:null)
                'name'(bu.name[0].text()?.trim()?:null)
                'number'(bu.businessUnit[0].text()?.trim()?:null)
                'payingNumber'(bu.VALUE[0].text()?.trim()?:null)
                'taxId'(bu.taxId[0].text()?.trim()?:null)
                'ticketDocumentId'(bu.VALUE[0].text()?.trim()?:null)
                'ticketDocumentIdParts' {
                    'element'(bu.VALUE[0].text()?.trim()?:null)
                }
            }
        }
    }
    

    // Generate output
    message.setBody(writer.toString())
    return message
}