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
            inv->
                'root' {
                    'additionalCharges' {
                        'element' {
                            'creditIndicator'(inv.VALUE[0].text()?.trim()?:null)
                            'description'(inv.VALUE[0].text()?.trim()?:null)
                            'fieldType'(inv.VALUE[0].text()?.trim()?:null)
                            'fieldValue'(inv.VALUE[0].text()?.trim()?:null)
                            'specialChargeName'(inv.VALUE[0].text()?.trim()?:null)
                            'blanketItemCategory'(inv.VALUE[0].text()?.trim()?:null)
                            'customFields' {
                                'element'(inv.VALUE[0].text()?.trim()?:null)
                            }
                            'deliveryNote'(inv.VALUE[0].text()?.trim()?:null)
                            'discount'(inv.VALUE[0].text()?.trim()?:null)
                            'freight'(inv.VALUE[0].text()?.trim()?:null)
                            'goodsReceiptSapItemNumber'(inv.VALUE[0].text()?.trim()?:null)
                            'goodsReceiptSapNumber'(inv.VALUE[0].text()?.trim()?:null)
                            'goodsReceiptSapYear'(inv.VALUE[0].text()?.trim()?:null)
                            'itemDescription'(inv.VALUE[0].text()?.trim()?:null)
                            'itemNumber'(inv.VALUE[0].text()?.trim()?:null)
                            'materialCode'(inv.VALUE[0].text()?.trim()?:null)
                            'materialCodeBuyer'(inv.VALUE[0].text()?.trim()?:null)
                            'materialCodeSupplier'(inv.VALUE[0].text()?.trim()?:null)
                            'netAmount'(inv.VALUE[0].text()?.trim()?:null)
                            'poItemDescription'(inv.VALUE[0].text()?.trim()?:null)
                            'poItemNumber'(inv.VALUE[0].text()?.trim()?:null)
                            'poNumber'(inv.VALUE[0].text()?.trim()?:null)
                            'pricePerUnit'(inv.VALUE[0].text()?.trim()?:null)
                            'priceUnit'(inv.VALUE[0].text()?.trim()?:null)
                            'quantity'(inv.VALUE[0].text()?.trim()?:null)
                            'serviceLineNumber'(inv.VALUE[0].text()?.trim()?:null)
                            'taxAmount'(inv.VALUE[0].text()?.trim()?:null)
                            'taxClearanceItemType'(inv.VALUE[0].text()?.trim()?:null)
                            'taxExemptReason'(inv.VALUE[0].text()?.trim()?:null)
                            'taxItems' {
                                'element'(inv.VALUE[0].text()?.trim()?:null)
                            }
                            'taxRate'(inv.VALUE[0].text()?.trim()?:null)
                            'name'(inv.VALUE[0].text()?.trim()?:null)
                            'reason'(inv.VALUE[0].text()?.trim()?:null)
                            'type'(inv.VALUE[0].text()?.trim()?:null)
                            'uiLabel'(inv.VALUE[0].text()?.trim()?:null)
                            'uiPosition'(inv.VALUE[0].text()?.trim()?:null)
                            'uiSection'(inv.VALUE[0].text()?.trim()?:null)
                            'value'(inv.VALUE[0].text()?.trim()?:null)
                            'financeId'(inv.VALUE[0].text()?.trim()?:null)
                            'apiKeyId'(inv.VALUE[0].text()?.trim()?:null)
                            'externalId'(inv.VALUE[0].text()?.trim()?:null)
                            'amount'(inv.VALUE[0].text()?.trim()?:null)
                            'rate'(inv.VALUE[0].text()?.trim()?:null)
                            'taxType'(inv.VALUE[0].text()?.trim()?:null)
                            'totalAmount'(inv.VALUE[0].text()?.trim()?:null)
                            'unit'(inv.VALUE[0].text()?.trim()?:null)
                        }
                    }
                    'accessType'(inv.VALUE[0].text()?.trim()?:null)
                    
                    
                    
                    
                    
                    
                    
                    
                    
                      <xs:element type="additionalChargesType" name="additionalCharges"/>
                      <xs:element type="attachmentsType" name="attachments"/>
                      <xs:element type="xs:string" name="buyerTimestamp"/>
                      <xs:element type="xs:string" name="comment"/>
                      <xs:element type="complianceType" name="compliance"/>
                      <xs:element type="creditType" name="credit"/>
                      <xs:element type="creditedInvoiceErpFieldsType" name="creditedInvoiceErpFields"/>
                      <xs:element type="xs:string" name="currency"/>
                      <xs:element type="customFieldsType" name="customFields"/>
                      <xs:element type="customerType" name="customer"/>
                      <xs:element type="xs:string" name="deliverDate"/>
                      <xs:element type="xs:string" name="deliveryNote"/>
                      <xs:element type="disableFinanceTypeType" name="disableFinanceType"/>
                      <xs:element type="xs:string" name="discountAmount"/>
                      <xs:element type="xs:string" name="discountBaseAmount"/>
                      <xs:element type="documentLinksType" name="documentLinks"/>
                      <xs:element type="xs:string" name="dueDate"/>
                      <xs:element type="earlyPaymentType" name="earlyPayment"/>
                      <xs:element type="xs:string" name="erpInvoiceStatus"/>
                      <xs:element type="xs:string" name="erpStatusExplanation"/>
                      <xs:element type="externalIdLinkObjectsType" name="externalIdLinkObjects"/>
                      <xs:element type="functionalMessagesType" name="functionalMessages"/>
                      <xs:element type="xs:string" name="grossAmount"/>
                      <xs:element type="xs:string" name="id"/>
                      <xs:element type="xs:string" name="invoiceDate"/>
                      <xs:element type="xs:string" name="invoiceIndicator"/>
                      <xs:element type="xs:string" name="invoiceSubOrigin"/>
                      <xs:element type="xs:string" name="lineItemTotalDiscountAmount"/>
                      <xs:element type="lineItemsType" name="lineItems"/>
                      <xs:element type="metadataType" name="metadata"/>
                      <xs:element type="xs:string" name="netAmount"/>
                      <xs:element type="xs:string" name="number"/>
                      <xs:element type="xs:string" name="origin"/>
                      <xs:element type="xs:string" name="otherCurrency"/>
                      <xs:element type="xs:string" name="otherCurrencyExchangeRate"/>
                      <xs:element type="xs:string" name="partialPayment"/>
                      <xs:element type="paymentType" name="payment"/>
                      <xs:element type="xs:string" name="paymentAmount"/>
                      <xs:element type="xs:string" name="paymentItemText"/>
                      <xs:element type="xs:string" name="poNumber"/>
                      <xs:element type="xs:string" name="postingDate"/>
                      <xs:element type="xs:string" name="receivingDate"/>
                      <xs:element type="xs:string" name="referenceNumber"/>
                      <xs:element type="sapFieldType" name="sapField"/>
                      <xs:element type="signatureType" name="signature"/>
                      <xs:element type="xs:string" name="status"/>
                      <xs:element type="xs:string" name="statusLastUpdated"/>
                      <xs:element type="supplierType" name="supplier"/>
                      <xs:element type="taxType" name="tax"/>
                      <xs:element type="xs:string" name="ticketDocumentId"/>
                      <xs:element type="xs:string" name="totalAddlChargesAmount"/>
                      <xs:element type="xs:string" name="totalDiscountAmount"/>
                    
                    
                    
                    
                    
                    
                    
                    
                    'accessType'(sm.accessType[0].text()?.trim()?:null)
                    'addresses'
                      {
                          'addressLine1'(sm.addressStreet1[0].text()?.trim()?:null)
                          'addressLine2'(sm.addressStreet2[0].text()?.trim()?:null)
                          'addressLine3'(sm.addressStreet3[0].text()?.trim()?:null)
                          'city'(sm.addressCity[0].text()?.trim()?:null)
                          'country'(sm.addressCountry[0].text()?.trim()?:null)
                          'email'(sm.addressEmail[0].text()?.trim()?:null)
                          'fax'(sm.addressFax[0].text()?.trim()?:null)
                          'language'(sm.addressLanguage[0].text()?.trim()?:null)
                          'name'(sm.name[0].text()?.trim()?:null)
                          'phone'(sm.addressPhone[0].text()?.trim()?:null)
                          'poBox'(sm.addressPoBox[0].text()?.trim()?:null)
                          'poBoxCity'(sm.addressPoBoxCity[0].text()?.trim()?:null)
                          'poBoxPostalCode'(sm.addressPoBoxZipCode[0].text()?.trim()?:null)
                          'postalCode'(sm.addressPostalCode[0].text()?.trim()?:null)
                          'region'(sm.addressRegion[0].text()?.trim()?:null)
                          'type'('IF')
                      }
                    'bankAccounts'
                      {
                          'accountNumber'(sm.bankAccountNumber[0].text()?.trim()?:null)
                          'bankBranchNumber'(sm.bankBranchNumber[0].text()?.trim()?:null)
                          'bankCity'(sm.bankCity[0].text()?.trim()?:null)
                          'bankName'(sm.bankName[0].text()?.trim()?:null)
                          'bankNumber'(sm.bankNumber[0].text()?.trim()?:null)
                          'bankRegion'(sm.bankRegion[0].text()?.trim()?:null)
                          'bankStreet'(sm.bankStreet[0].text()?.trim()?:null)
                          'controlKey'(sm.bankControlKey[0].text()?.trim()?:null)
                          'country'(sm.bankCountry[0].text()?.trim()?:null)
                          'currency'(sm.bankCurrency[0].text()?.trim()?:null)
                          'iban'(sm.bankIban[0].text()?.trim()?:null)
                          'externalAccountHolder'(sm.bankIban[0].text()?.trim()?:null)
                          'externalAccountHolderId'(sm.bankIban[0].text()?.trim()?:null)
                          'swiftNumber'(sm.bankIban[0].text()?.trim()?:null)
                          'signature'(sm.bankIban[0].text()?.trim()?:null)
                          'signatureInfo'{
                              'fingerprint'(sm.fingerPrint[0].text()?.trim()?:null)
                              'scheme'(sm.scheme[0].text()?.trim()?:null)
                              'timestamp'(sm.timestamp[0].text()?.trim()?:null)
                              'version'(sm.version[0].text()?.trim()?:null)
                              'value'(sm.value[0].text()?.trim()?:null)
                          }
                      }
                    'contacts'{
                        'department'(sm.department[0].text()?.trim()?:null)
                        'emailAddress'(sm.addressEmail[0].text()?.trim()?:null)
                        'erpId'(sm.erpId[0].text()?.trim()?:null)
                        'fax'(sm.fax[0].text()?.trim()?:null)
                        'firstName'(sm.firstName[0].text()?.trim()?:null)
                        'invite' ('true')
                        'lastName'(sm.lastName[0].text()?.trim()?:null)
                        'mobileNumber'(sm.mobileNumber[0].text()?.trim()?:null)
                        'notes'(sm.notes[0].text()?.trim()?:null)
                        'phone'(sm.phone[0].text()?.trim()?:null)
                        'role'(sm.role[0].text()?.trim()?:null)
                        'transactionId' ('need UUID logic here')
                    }
                    'customFields' {
                        'fieldName'(sm.fieldName[0].text()?.trim()?:null)
                        'fieldValue'(sm.fieldValue[0].text()?.trim()?:null)
                        'showInUi'(sm.showInUi[0].text()?.trim()?:null)
                    }
                    'name'(sm.name[0].text()?.trim()?:null)
                    'supplierLaunchId'(sm.supplierLaunchId[0].text()?.trim()?:null)
                    'supplierNumber'(sm.supplierNumber[0].text()?.trim()?:null)
                    'supplierRelations' {
                        'businessUnit'(sm.businessUnit[0].text()?.trim()?:null)
                        'erpId'(sm.erpId[0].text()?.trim()?:null)
                        'invoiceRemitToMandatory'(sm.invoiceRemitToMandatory[0].text()?.trim()?:null)
                        'paymentMethod'(sm.paymentMethod[0].text()?.trim()?:null)
                        'paymentTerms' {
                            'additionalMonth1'(sm.additionalMonth1[0].text()?.trim()?:null)
                            'additionalMonth2'(sm.additionalMonth2[0].text()?.trim()?:null)
                            'asap'(sm.asap[0].text()?.trim()?:null)
                            'baselineDate' {
                                'additionalMonth'(sm.additionalMonth[0].text()?.trim()?:null)
                                'baselineDate'(sm.baselineDate[0].text()?.trim()?:null)
                                'defaultDetermination'(sm.defaultDetermination[0].text()?.trim()?:null)
                                'defaultDeterminationDescription'(sm.defaultDeterminationDescription[0].text()?.trim()?:null)
                                'fixedDay'(sm.fixedDay[0].text()?.trim()?:null)
                            }
                            'days1'(sm.paymentTermsDays1[0].text()?.trim()?:null)
                            'days2'(sm.paymentTermsDays2[0].text()?.trim()?:null)
                            'days3'(sm.paymentTermsDays3[0].text()?.trim()?:null)
                            'daysNet'(sm.paymentTermsDaysNet[0].text()?.trim()?:null)
                            'definedDueDate'(sm.definedDueDate[0].text()?.trim()?:null)
                            'description'(sm.paymentTermsDescription[0].text()?.trim()?:null)
                            'fixedDay1'(sm.fixedDay1[0].text()?.trim()?:null)
                            'fixedDay2'(sm.fixedDay2[0].text()?.trim()?:null)
                            'paymentTermsKey'(sm.paymentTermsKey[0].text()?.trim()?:null)
                            'percent1'(sm.paymentTermsPercent1[0].text()?.trim()?:null)
                            'percent2'(sm.paymentTermsPercent2[0].text()?.trim()?:null)
                        }
                        'paymentTermsKey'(sm.paymentTermsKey[0].text()?.trim()?:null)
                        'tedEligible'(sm.tedEligible[0].text()?.trim()?:null)
                    }
                    'taxIdentifiers' {
                        'countryIso'(sm.countryIso[0].text()?.trim()?:null)
                        'type'(sm.taxType[0].text()?.trim()?:null)
                        'value'(sm.taxValue[0].text()?.trim()?:null)
                    }
                    'ticketDocumentId'(sm.ticketDocumentId[0].text()?.trim()?:null)
                }

        }}

    // Generate output
    message.setBody(writer.toString())
    return message
}