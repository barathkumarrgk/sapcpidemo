/**
 * SCF-V1 format for Early Payment JSON to CSV
 * Converts the incoming SCF-V1 EP request JSON format to CSV 
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

    // "data" = 1 EP request
    jsonInput.data.eachWithIndex { data, index -> 

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

def concatenateFinanceAttributes(def data) {
    StringBuilder builder = new StringBuilder()
    builder.with {
        builder.append("${(data.payDate) ? data.payDate.substring(0,10) : ''},") // financeDate 
    }
    builder
}

def concatenatBankAccount(def data, def bankAccount) {
    // SCF bank account attributes
    StringBuilder builder = new StringBuilder()
    builder.with {
        append("${bankAccount.bankName},") 
        append("${bankAccount.bankBranchNumber},") 
        append("${bankAccount.bankCity},") 
        append("${bankAccount.bankRegion},") 
        append("${bankAccount.bankStreet},") 
        append("${bankAccount.country},") // bankCountry
        append("${bankAccount.accountNumber},") 
        append("${bankAccount.bankNumber},") 
        append("${bankAccount.controlKey},") 
        append("${bankAccount.swiftNumber},") 
        append("${bankAccount.ibanNumber},")  
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

def concatenatBankAccountIntegrity(def bankAccount) {
    // Integrity
    StringBuilder builder = new StringBuilder()
    builder.with {
        append("${bankAccount.integrity?.signatureVersion},")
        append("${bankAccount.integrity?.localScheme},") 
        append("${bankAccount.integrity?.entityFingerprint},")
        append("${bankAccount.integrity?.timestamp},") 
        append("${bankAccount.integrity?.signature},") 
    }    
    builder
}

def concatenatEmptyIntegrity() {
    // Integrity
    StringBuilder builder = new StringBuilder()
    builder.with {
        append(",")  // signatureVersion
        append(",")  // localScheme
        append(",")  // entityFingerprint
        append(",")  // timestamp
        append(",")  // signature
    }    
    builder
}

class CsvFile {

    String headers = """\"autoRequested","creator","businessUnit","invoiceErpId","supplierNumber","supplierSite","status","transactionid","platformSignature","supplierBankName","supplierBankBranchNumber","supplierBankCity","supplierBankRegion","supplierBankStreet","supplierBankCountry","supplierBankaccountNumber","supplierBankNumber","supplierBankControlKey","supplierBankSwiftNumber","supplierBankIban","financeDate","signatureVersion","localScheme","entityFingerPrint","timeStamp","signature\""""     
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
