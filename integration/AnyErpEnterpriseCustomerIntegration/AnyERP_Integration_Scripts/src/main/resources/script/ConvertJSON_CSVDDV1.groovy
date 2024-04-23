/**
 * DD-V1 format for Early Payment JSON to CSV
 * Converts the incoming DD-V1 EP request JSON format to CSV 
 * NOTE: Hierarchy is 
 *   1. Base EP Attributes
 *   2. Empty Bank Attributes
 *   3. Finance date/type
 *   4. Netted positions (partial) 
**/

import com.sap.gateway.ip.core.customdev.util.Message
import java.util.HashMap
import groovy.json.JsonSlurper

def Message processData(Message message) {

    def body = message.getBody(java.lang.String) as String    
    def jsonSlurper = new JsonSlurper()
    def jsonInput = jsonSlurper.parseText(body)
    
    CsvFile csvFile = new CsvFile()     

    // "data" = 1 EP request
    jsonInput.data.eachWithIndex { data, index -> 
     
        // Count netted positions
        def nettedPositionList = data.documents
        int npCount = (nettedPositionList) ? nettedPositionList.size() : 0

        // Common EP attributes                       
        StringBuilder commonAttributes = concatenateCommonAttributes(data) 
        
        // Finance Date attributes
        StringBuilder financeAttributes = concatenateFinanceDateAndType(data)
        
        if (npCount > 0) { // Build CSV for each NP document
            data.documents.each { nettedPosition ->   
                csvFile.dataLines << new CsvDataLine(
                    index, 
                    commonAttributes, 
                    concatenateEmptyBankValues(), 
                    financeAttributes,
                    concatenateNettedPosition(data, nettedPosition)
                )            
            }            
        } else { // Build CSV line with empty document information 
            csvFile.dataLines << new CsvDataLine(
                index, 
                commonAttributes, 
                concatenateEmptyBankValues(), 
                financeAttributes,
                concatenateEmptyNettedPosition(data)                
            )          
        }   
    }
    
    boolean splitCsvByEP = splitCsvByEarlyPayment(message)
    message.setBody(csvFile.outputCsvFile(splitCsvByEP))  
    return message
}


def concatenateCommonAttributes(def data) {

    StringBuilder builder = new StringBuilder()
    builder.with {
        append("${data.autoRequested},")
        append("${data.creator.email},") 
        append("${data.customer.businessUnit},")
        append("${data.invoice.number},") // invoiceErpId
        append("${data.supplier.vendorNumber},") // supplierNumber
        append("${data.supplier.vendorNumber}").append("_").append("${data.customer.businessUnit},") // supplierSite - vendorNumber_businessUnit
        append("${data.status},")
        append("${data.transactionId},")
        append("${(data.platformSignature) ?: ''},")
    }

    builder
}  

def concatenateFinanceDateAndType(def data) {

    StringBuilder builder = new StringBuilder()
    builder.with {
        append("${(data.payDate) ? data.payDate.substring(0,10) : ''},") // financeDate 
        append("${data.financeType},") // SCF or DD  
    }

    builder
}

def concatenateNettedPosition(def data, def document) {  

    StringBuilder builder = new StringBuilder()
    builder.with {
        append("${document.type},") 
        append("${(document.originalMaturityDate) ? document.originalMaturityDate.substring(0,10) : ''},")
        append("${document.erpIdFi},")
        append("${(data.percent) ?: 0},") // discountPercent = percent value from EP root
        append("${(data.amount) ?: 0},") // amount = amount value from EP root
        append("${(document.amount) ?: 0},") // origamount = amount from netted document amount
        append("${document.currency},") 
    }
    
    builder
}

def concatenateEmptyNettedPosition(def data) {

    StringBuilder builder = new StringBuilder()
    builder.with {
        append(",")  // type
        append(",") // originalMaturityDate
        append(",") // erpIdFi
        append("${(data.percent) ?: 0},") // discountPercent = percent value from EP root
        append("${(data.amount) ?: 0},") // amount = amount value from EP root
        append(",") // origamount
        append(",") 
    }

    builder
}

def concatenateEmptyBankValues() {

    StringBuilder builder = new StringBuilder()
    builder.with {
        // DD use-case = empty bank attributes
        append(",") // bankName
        append(",") //bankBranchNumber
        append(",") //bankCity
        append(",") //bankRegion
        append(",") //bankStreet
        append(",") //bankCountry
        append(",") //accountNumber
        append(",") //bankNumber
        append(",") //controlKey
        append(",") //swiftNumber
        append(",") //ibanNumber  
    }

    builder   
}

boolean splitCsvByEarlyPayment(Message message) {
    Boolean.parseBoolean(message.headers?.get("splitCSVByEP"))
}

class CsvFile {

    // Optionally include sequence and stop attributes for iFlow splitting and processing.
    String headers = """\"autoRequested","creator","businessUnit","invoiceErpId","supplierNumber","supplierSite","status","transactionid","platformSignature","supplierBankName","supplierBankBranchNumber","supplierBankCity","supplierBankRegion","supplierBankStreet","supplierBankCountry","supplierBankaccountNumber","supplierBankNumber","supplierBankControlKey","supplierBankSwiftNumber","supplierBankIban","financeDate","financeType","type","originalMaturityDate","erpIdFi","discountPercent","amount","origamount","currency\""""    
    String seqStopHeaders = """,\"sequence","stop\""""
    String NEWLINE = "\n"
    
    List<CsvDataLine> dataLines = []

    String outputCsvFile(boolean splitCsvByEp) {

        StringBuilder csvFileBuilder = new StringBuilder()

        csvFileBuilder.append(headers)

        if (splitCsvByEp) {
            csvFileBuilder.append(seqStopHeaders).append(NEWLINE)
            dataLines.eachWithIndex { line, index ->
                boolean stop = (index < (dataLines.size() - 1)) ? false : true
                csvFileBuilder
                    .append(line.joinAttributes()).append(",")
                    .append(index + 1).append(",")
                    .append(stop.toString())                
                    .append(NEWLINE)
            }               
        } else {
            csvFileBuilder.append(NEWLINE)
            dataLines.eachWithIndex { line, index ->
                csvFileBuilder
                    .append(line.joinAttributes())
                    .append(NEWLINE)
            }               
        }
        
        return csvFileBuilder.toString()  
    }
}

class CsvDataLine {

    int epIndex
    StringBuilder commonAttributes
    StringBuilder bankAccountAttributes
    StringBuilder financeAttributes
    StringBuilder nettedPositionAttributes

    CsvDataLine(int epIndex, StringBuilder commonAttributes, StringBuilder bankAccountAttributes, 
      StringBuilder financeAttributes, StringBuilder nettedPositionAttributes) {
        this.epIndex = epIndex
        this.commonAttributes = commonAttributes
        this.bankAccountAttributes = bankAccountAttributes
        this.financeAttributes = financeAttributes
        this.nettedPositionAttributes = nettedPositionAttributes
    }

    String joinAttributes() {

        String commonAttributesString = commonAttributes.toString()
        String bankAccountAttributesString = bankAccountAttributes.toString()
        String financeAttributesString = financeAttributes.toString()
        String nettedPositionAttributesString = nettedPositionAttributes.toString()

        String dataLine = 
             commonAttributesString
            .concat(bankAccountAttributesString)    
            .concat(financeAttributesString)
            .concat(nettedPositionAttributesString)

        return dataLine.substring(0, (dataLine.size() - 1)) // Truncate last comma, return        
    }
}