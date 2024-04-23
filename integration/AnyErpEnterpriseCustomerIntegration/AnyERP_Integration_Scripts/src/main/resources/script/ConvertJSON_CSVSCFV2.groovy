/**
 * SCF-V2 format for Early Payment JSON to CSV
 * Converts the incoming SCF-V2 EP request JSON format to CSV 
 * NOTE: Hierarcy is 
 *   1. Base EP Attributes
 *   2. Bank Accounts
 *   3. Finance date/type
 *   4. Netted positions
**/


import com.sap.gateway.ip.core.customdev.util.Message
import java.util.HashMap
import groovy.json.JsonSlurper

def Message processData(Message message) {
        
    def body = message.getBody(java.lang.String) as String
    def jsonSlurper = new JsonSlurper()
    def jsonInput = jsonSlurper.parseText(body)
    
    CsvFile csvFile = new CsvFile()    

    // EP data = jsonInput.data
    // EPC data = jsonInput.earlyPayments
    def earlyPayments = jsonInput.data ?: jsonInput.earlyPayments

    // "data" = 1 EP request
    earlyPayments.eachWithIndex { data, index ->  

        // Common EP attributes                       
        StringBuilder commonAttributes = concatenateCommonAttributes(data) 
        
        // Finance Date attributes
        StringBuilder financeAttributes = concatenateFinanceAttributes(data)

        // Count bank accounts
        def bankAccountList = data.supplier.bankAccounts        
        int bankCount = (bankAccountList) ? bankAccountList.size() : 0

        // Count netted positions
        def nettedPositionList = data.documents
        int documentCount = (nettedPositionList) ? nettedPositionList.size() : 0

        
        if (bankCount > 0) {  // Build CSV for each bank account
            bankAccountList.each { bankAccount ->
                if (documentCount > 0) {
                    nettedPositionList.each { document ->
                        csvFile.dataLines << new CsvDataLine(
                            index, 
                            commonAttributes, 
                            concatenatBankAccount(data, bankAccount), 
                            financeAttributes,
                            concatenateNettedPosition(data, document)
                        )
                    }     
                } else { // No netted positions - bank + empty NP.
                    csvFile.dataLines << new CsvDataLine(
                        index, 
                        commonAttributes, 
                        concatenatBankAccount(data, bankAccount), 
                        financeAttributes,
                        concatenateEmptyNettedPosition()
                    )
                }
            }                
        } else { // Build CSV line with empty bank + NP's
            if (documentCount > 0) {
                nettedPositionList.each { document ->
                    csvFile.dataLines << new CsvDataLine(
                        index, 
                        commonAttributes, 
                        concatenateEmptyBankValues(), 
                        financeAttributes,
                        concatenateNettedPosition(data, document)
                    )
                }     
            } else { // Nothing - empty bank + NP
                csvFile.dataLines << new CsvDataLine(
                    index, 
                    commonAttributes, 
                    concatenateEmptyBankValues(data, bankAccount), 
                    financeAttributes,
                    concatenateEmptyNettedPosition()
                )
            }        
        }        
    }

    boolean splitCsvByEP = splitCsvByEarlyPayment(message)
    message.setBody(csvFile.outputCsvFile(splitCsvByEP))  
    return message
}

boolean splitCsvByEarlyPayment(Message message) {
    Boolean.parseBoolean(message.headers?.get("splitCSVByEP"))
}

def concatenateCommonAttributes(def data) {
    StringBuilder builder = new StringBuilder()
    builder.with {
        append("${(data.autoRequested) ? data.autoRequested.toString().substring(0,1) : ''},") // single character
        append("${data.creator.email},") 
        append("${data.customer.businessUnit},")
        append("${data.invoice.number},") // invoiceErpId
        append("${data.supplier.vendorNumber},") // supplierNumber
        append("${data.supplier.vendorNumber}").append("_").append("${data.customer.businessUnit},") // supplierSite - vendorNumber_businessUnit
        if("${data.status}" == 'STARTED') {append("${data.status},")} else {append("\"CANCELLED\",")}
        append("${data.transactionId},")
        append("${(data.platformSignature) ?: ''},")
    }
    builder    
}  

def concatenateFinanceAttributes(def data) {
    StringBuilder builder = new StringBuilder()
    builder.with {       
        append("${(data.payDate) ? data.payDate.substring(0,10) : ''},") // financeDate 
        append("${data.financeType},") // financeType - SCF or DD  

    }
    builder
}

def concatenatBankAccount(def data, def bankAccount) {
    // SCF bank account attributes
    StringBuilder builder = new StringBuilder()
    builder.with {
        append("${(bankAccount.bankName).replaceAll(",","")},") 
        append("${bankAccount.bankBranchNumber},") 
        append("${bankAccount.bankCity},") 
        append("${(bankAccount.bankRegion) ?: ""},") 
        append("${bankAccount.bankStreet},") 
        append("${bankAccount.country},") // bankCountry
        append("${bankAccount.accountNumber},") 
        append("${bankAccount.bankNumber},") 
        append("${(bankAccount.controlKey) ?: ""},") 
        append("${bankAccount.swiftNumber},") 
        append("${(bankAccount.ibanNumber) ?: ""},")  
    }
    builder      
}

def concatenateNettedPosition(def data, def document) {  
    StringBuilder builder = new StringBuilder()
    builder.with {
        append("${document.type},") 
        append("${(document.originalMaturityDate) ? document.originalMaturityDate.substring(0,10) : ''},")
        append("${document.erpIdFi},")
        append("${(document.amount) ?: 0},") // "amount" from document
        append("${document.balanceResult},")
        append("${document.id},") // invoiceId 
        append("${(data.percent) ?: 0},") // discountPercent = percent value from EP root    
        append("${(data.amount) ?: 0},") // amount = discountAmount value from EP root
        append("${(data.collectionBankAccount?.erpId) ?: ''},")  // erpBankId
        append("${(data.collectionBankAccount?.hash) ?: ''},")  // erpBankHash        
    }
    builder
}

def concatenateEmptyBankValues() {
    StringBuilder builder = new StringBuilder()
    builder.with {
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

def concatenateEmptyNettedPosition() {  

    StringBuilder builder = new StringBuilder()
    builder.with {
        append(",") // type
        append(",") // originalMaturityDate
        append(",") // erpIdFi
        append(",") // amount
        append(",") // balanceResult
        append(",") // invoiceId
        append(",") // discountPercent 
        append(",") // discountAmount
        append(",") // erpBankId
        append(",") // erpBankHash
    }
    builder
}

class CsvFile {

    String headers = """\"autoRequested","creator","businessUnit","invoiceErpId","supplierNumber","supplierSite","status","transactionid","platformSignature","supplierBankName","supplierBankBranchNumber","supplierBankCity","supplierBankRegion","supplierBankStreet","supplierBankCountry","supplierBankaccountNumber","supplierBankNumber","supplierBankControlKey","supplierBankSwiftNumber","supplierBankIban","financeDate","financeType","type","originalMaturityDate","erpIdFi","amount","balanceResult","invoiceId","discountPercent","discountAmount","erpBankId","erpBankHash\""""   
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