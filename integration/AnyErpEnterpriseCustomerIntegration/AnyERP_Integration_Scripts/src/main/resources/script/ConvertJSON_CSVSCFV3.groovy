/**
 * SCF-V3 format for Early Payment JSON to CSV-Tab delimeter
 * Converts the incoming SCF-V3 EP request JSON format to CSV 
 * NOTE: Hierarcy is 
 *   1. Base EP Attributes
 *   2. Bank Accounts
 *   3. Finance date
 *   4. Integrity  
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

        // Count bank accounts
        def bankAccountList = data.supplier.bankAccounts        
        int bankCount = (bankAccountList) ? bankAccountList.size() : 0

        // Common EP attributes                       
        StringBuilder commonAttributes = concatenateCommonAttributes(data) 
        
        // Finance Date attributes
        StringBuilder financeAttributes = concatenateFinanceAttributes(data)
        
        if (bankCount > 0) {  // Build CSV for each bank account
            data.supplier.bankAccounts.each { bankAccount ->
                csvFile.dataLines << new CsvDataLine(
                    index, 
                    commonAttributes, 
                    concatenatBankAccount(data, bankAccount), 
                    financeAttributes,
                    concatenatBankAccountIntegrity(bankAccount)
                )
            }            
        } else { // Build CSV line with empty document information 
            csvFile.dataLines << new CsvDataLine(
                index, 
                commonAttributes, 
                concatenateEmptyBankValues(), 
                financeAttributes,
                concatenatEmptyIntegrity()
            )          
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
        append("${data.autoRequested}\t")
        append("${data.creator.email}\t") 
        append("${data.customer.businessUnit}\t")
        append("${data.invoice.documentId}\t") // invoiceErpId
        append("${data.supplier.vendorNumber}\t") // supplierNumber
        append("${data.supplier.vendorNumber}\t") // supplierSite - vendorNumber_businessUnit
        if("${data.status}" == 'STARTED') {append("${data.status}\t")} else {append("CANCELLED\t")}
        append("${data.transactionId}\t")
        append("${(data.platformSignature) ?: ''}\t")
    }
    builder    
}  

def concatenateFinanceAttributes(def data) {
    StringBuilder builder = new StringBuilder()
    builder.with {
        builder.append("${(data.payDate) ? data.payDate.substring(0,10) : ''}\t") // financeDate 
    }
    builder
}

def concatenatBankAccount(def data, def bankAccount) {
    // SCF bank account attributes
    StringBuilder builder = new StringBuilder()
    builder.with {
        append("${(bankAccount.bankName) ?: ""}\t") 
        append("\t") 
        append("${(bankAccount.bankCity) ?: ""}\t") 
        append("\t") 
        append("${(bankAccount.bankStreet) ?: ""}\t") 
        append("${(bankAccount.country) ?: ""}\t") // bankCountry
        append("${(bankAccount.accountNumber) ?: ""}\t") 
        append("${(bankAccount.bankNumber) ?: ""}\t") 
        append("\t") 
        append("${(bankAccount.swiftNumber) ?: ""}\t") 
        append("${(bankAccount.ibanNumber) ?: ""}\t")  
    }
    builder      
}

def concatenateEmptyBankValues() {
    StringBuilder builder = new StringBuilder()
    builder.with {
        append("\t") // bankName
        append("\t") //bankBranchNumber
        append("\t") //bankCity
        append("\t") //bankRegion
        append("\t") //bankStreet
        append("\t") //bankCountry
        append("\t") //accountNumber
        append("\t") //bankNumber
        append("\t") //controlKey
        append("\t") //swiftNumber
        append("\t") //ibanNumber  
    }
    builder    
}

def concatenatBankAccountIntegrity(def bankAccount) {
    // Integrity
    StringBuilder builder = new StringBuilder()
    builder.with {
        append("${(bankAccount.integrity?.signatureVersion) ?: ""}\t")
        append("${(bankAccount.integrity?.localScheme) ?: ""}\t") 
        append("${(bankAccount.integrity?.entityFingerprint) ?: ""}\t")
        append("${(bankAccount.integrity?.timestamp) ?: ""}\t") 
        append("${(bankAccount.integrity?.signature) ?: ""}\t") 
    }    
    builder
}

def concatenatEmptyIntegrity() {
    // Integrity
    StringBuilder builder = new StringBuilder()
    builder.with {
        append("\t")  // signatureVersion
        append("\t")  // localScheme
        append("\t")  // entityFingerprint
        append("\t")  // timestamp
        append("\t")  // signature
    }    
    builder
}

class CsvFile {

    String headers = """autoRequested\tcreator\tbusinessUnit\tinvoiceErpId\tsupplierNumber\tsupplierSite\tstatus\ttransactionid\tplatformSignature\tsupplierBankName\tsupplierBankBranchNumber\tsupplierBankCity\tsupplierBankRegion\tsupplierBankStreet\tsupplierBankCountry\tsupplierBankaccountNumber\tsupplierBankNumber\tsupplierBankControlKey\tsupplierBankSwiftNumber\tsupplierBankIban\tfinanceDate\tsignatureVersion\tlocalScheme\tentityFingerPrint\ttimeStamp\tsignature"""     
    String seqStopHeaders = """\tsequence\tstop"""
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
                    .append(line.joinAttributes()).append("\t")
                    .append(index + 1).append("\t")
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
    StringBuilder integrityAttributes

    CsvDataLine(int epIndex, StringBuilder commonAttributes, StringBuilder bankAccountAttributes, 
      StringBuilder financeAttributes, StringBuilder integrityAttributes) {
        this.epIndex = epIndex
        this.commonAttributes = commonAttributes
        this.bankAccountAttributes = bankAccountAttributes
        this.financeAttributes = financeAttributes
        this.integrityAttributes = integrityAttributes
    }

    String joinAttributes() {

        String commonAttributesString = commonAttributes.toString()
        String bankAccountAttributesString = bankAccountAttributes.toString()
        String financeAttributesString = financeAttributes.toString()
        String integrityAttributesString = integrityAttributes.toString()

        String dataLine = 
             commonAttributesString
            .concat(bankAccountAttributesString)    
            .concat(financeAttributesString)
            .concat(integrityAttributesString)

        return dataLine.substring(0, (dataLine.size() - 1)) // Truncate last comma, return        
    }
}
