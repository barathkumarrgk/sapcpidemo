import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonBuilder
import groovy.json.JsonOutput
import groovy.json.JsonSlurper


Message processData(Message message) {

  // Extract the message body as a string
  def body = message.getBody(java.lang.String) as String

  // Parse the message body as a JSON object
  def jsonSlurper = new JsonSlurper()
  def payload = jsonSlurper.parseText(body)
  def purchaseOrderOutput = []

  payload.groupedPurchaseOrder.each { group ->
    def groupedLineItems = []
    //def groupedCustomFields = []
    def groupedObjectLinks = []
    def groupedTextList = []
    def currentPurchaseOrder
    group.purchaseOrder.each { purchaseOrder ->
      purchaseOrder.asnPossible = purchaseOrder.asnPossible.equals("true")
      purchaseOrder.customFields.each { customField ->
        if (customFieldIsComplete(customField)){
          customField.fieldName = customField?.fieldName? new String (customField?.fieldName) : null  
          customField.showInUi = customField?.showInUi? new String (customField?.showInUi) : null
          //groupedCustomFields.add(customField)
        } else {
            purchaseOrder.customFields = []
             }
      }
      purchaseOrder.objectLinks.each {ol ->
        if (objectLinkIsComplete(ol)){
          groupedObjectLinks.add(ol)
        }
      }
      purchaseOrder.textList.each{t ->
        if (textListIsComplete(t)){
          groupedTextList.add(t)
        }
      }
      purchaseOrder.exchangeRateFixed = purchaseOrder.exchangeRateFixed.equals("true")

      purchaseOrder.exchangeRate = purchaseOrder.exchangeRate? new BigDecimal(purchaseOrder.exchangeRate) : 0
      purchaseOrder.grossAmount = purchaseOrder.grossAmount? new BigDecimal(purchaseOrder.grossAmount) : 0
      purchaseOrder.lineItems.each { lineItem ->
        if (lineItemIsComplete(lineItem)){
          groupedLineItems.add(lineItem)
          lineItem.acknowledgementRequired = lineItem.acknowledgementRequired.equals("true")
          lineItem.asnPossible = lineItem.asnPossible.equals("true")
          lineItem.confirmationPossible = lineItem.confirmationPossible.equals("true")

          lineItem.evaluatedReceiptSettlement = lineItem.evaluatedReceiptSettlement.equals("true")

          lineItem.goodsReceiptBaseInvoiceVerification = lineItem.goodsReceiptBaseInvoiceVerification.equals("true")
          lineItem.hideLimitFlag = lineItem.hideLimitFlag.equals("true")
          lineItem.noInvoiceCreate = lineItem.noInvoiceCreate.equals("true")
          lineItem.taxableIndicator = lineItem.taxableIndicator.equals("true")
          lineItem.unlimited = lineItem.unlimited.equals("true")

          lineItem.customFields?.collect { customFieldLn ->
           if (customFieldIsComplete(customFieldLn)){
            customFieldLn.fieldName = customFieldLn?.fieldName? new String (customFieldLn?.fieldName) : null  
            customFieldLn.showInUi = customFieldLn?.showInUi? new String (customFieldLn?.showInUi) : null
        } else {
            lineItem.customFields = []
             }
          }

          lineItem.confirmations.each { confirmation ->
            confirmation.itemNumber = confirmation.itemNumber? new BigDecimal(confirmation.itemNumber) : 0
            confirmation.quantity = confirmation.quantity? new BigDecimal(confirmation.quantity) : 0
            confirmation.sequence = confirmation.sequence? new BigDecimal(confirmation.sequence) : 0
          }
          lineItem.deliveredQuantity = lineItem.deliveredQuantity? new BigDecimal(lineItem.deliveredQuantity) : 0
          lineItem.goodsReceipts?.collect { goodsReceipt ->
          if (goodsReceiptsIsComplete(goodsReceipt)){
              goodsReceipt.deliveryNote = goodsReceipt.deliveryNote? new String(goodsReceipt.deliveryNote): null
              goodsReceipt.netValue = goodsReceipt.netValue?new BigDecimal(goodsReceipt.netValue):0
              goodsReceipt.quantity = goodsReceipt.quantity?new BigDecimal(goodsReceipt.quantity): 0
              goodsReceipt.referenceDocumentItemNumber = goodsReceipt.referenceDocumentItemNumber?new BigDecimal(goodsReceipt.referenceDocumentItemNumber): 0
              goodsReceipt.referenceDocumentNumber = goodsReceipt.referenceDocumentNumber? new String(goodsReceipt.referenceDocumentNumber): null
              goodsReceipt.referenceDocumentYear = goodsReceipt.referenceDocumentYear?new BigDecimal(goodsReceipt.referenceDocumentYear): 0
          } else {
              lineItem.goodsReceipts = []
          }}
          lineItem.goodsReceiptValue = lineItem.goodsReceiptValue? new BigDecimal(lineItem.goodsReceiptValue): 0
          lineItem.invoiceableQuantity = lineItem.invoiceableQuantity?new BigDecimal(lineItem.invoiceableQuantity): 0
          lineItem.invoicedQuantity = lineItem.invoicedQuantity?new BigDecimal(lineItem.invoicedQuantity): 0
          lineItem.invoicedValue = lineItem.invoicedValue?new BigDecimal(lineItem.invoicedValue): 0
          lineItem.itemNumber = lineItem.itemNumber?new BigDecimal(lineItem.itemNumber): 0
          lineItem.netPrice = lineItem.netPrice?new BigDecimal(lineItem.netPrice): 0
          lineItem.netValue = lineItem.netValue?new String(lineItem.netValue): null
          lineItem.netWeight = lineItem.netWeight?new BigDecimal(lineItem.netWeight): null
          lineItem.overdeliveryTolerance = lineItem.overdeliveryTolerance?new BigDecimal(lineItem.overdeliveryTolerance): 0
          lineItem.orderPriceUnitConversionDenominator = lineItem.orderPriceUnitConversionDenominator?new BigDecimal(lineItem.orderPriceUnitConversionDenominator): 0
          lineItem.orderPriceUnitConversionNumerator = lineItem.orderPriceUnitConversionNumerator?new BigDecimal(lineItem.orderPriceUnitConversionNumerator): 0
          lineItem.priceUnit = lineItem.priceUnit?new BigDecimal(lineItem.priceUnit): 0
          lineItem.schedules = lineItem.schedules?.collect { schedule ->
            [
              //deliveryDate: schedule.deliveryDate,
              //deliveryDateType: schedule.deliveryDateType,
              quantity: schedule.quantity?new BigDecimal(schedule.quantity): 0
            ]
          }
          lineItem.serviceDetails?.collect { serviceDetail ->
          if (serviceDetailsIsComplete(serviceDetail)){
              serviceDetail.deleteIndicator = serviceDetail.deleteIndicator? new String(serviceDetail.deleteIndicator) : null
              serviceDetail.description = serviceDetail.description? new String(serviceDetail.servdescriptioniceNumber) : null
              serviceDetail.externalServiceNumber = serviceDetail.externalServiceNumber?new BigDecimal(serviceDetail.externalServiceNumber): null
              serviceDetail.grossPrice = serviceDetail.grossPrice?new BigDecimal(serviceDetail.grossPrice): null
              serviceDetail.lineNumber = serviceDetail.lineNumber?new BigDecimal( serviceDetail.lineNumber): null
              serviceDetail.netValue = serviceDetail.netValue?new BigDecimal(cserviceDetail.netValue): null
              serviceDetail.priceUnit = serviceDetail.priceUnit?new BigDecimal(serviceDetail.priceUnit): null
              serviceDetail.quantity = serviceDetail.quantity?new BigDecimal(serviceDetail.quantity): null
              serviceDetail.serviceNumber = serviceDetail.serviceNumber? new String(serviceDetail.serviceNumber) : null
              serviceDetail.unit = serviceDetail.unit? new String(serviceDetail.unit) : null
          } else {
              lineItem.serviceDetails = []
          }
          }

          lineItem.textList?.collect { textListsLn ->
          if (textListIsComplete(textListsLn)){
              textListsLn.type = textListsLn.type? new String(textListsLn.type) : null
              textListsLn.text = textListsLn.text? new String(textListsLn.text) : null
              } else {
                  lineItem.textList = []
              }
              }

          //lineItem.shipFromAddress.poItemNumber = lineItem.shipFromAddress.poItemNumber?new BigDecimal(lineItem.shipFromAddress.poItemNumber): 0
          //lineItem.shipToAddress.poItemNumber = lineItem.shipToAddress.poItemNumber?new BigDecimal(lineItem.shipToAddress.poItemNumber): 0
          lineItem.targetQuantity = lineItem.targetQuantity?new BigDecimal(lineItem.targetQuantity): 0
          lineItem.underdeliveryTolerance = lineItem.underdeliveryTolerance?new BigDecimal(lineItem.underdeliveryTolerance): 0
        }
      }

      purchaseOrder.objectLinks?.collect { objectLink ->
      if (objectLinkIsComplete(objectLink)){
          objectLink.linkType = objectLink.linkType? new String (objectLink.linkType) : null
          objectLink.erpId = objectLink.erpId? new String (objectLink.erpId) : null
          objectLink.erpType = objectLink.erpType? new String (objectLink.erpType) : null
      } else {
          purchaseOrder.objectLinks = []
          }
          }

      
      purchaseOrder.taxableIndicator = purchaseOrder.taxableIndicator.equals("true")

      purchaseOrder.textList?.collect { textLists ->
      if (textListIsComplete(textLists)){
              textLists.type = textLists.type? new String(textLists.type) : null
              textLists.text = textLists.text? new String(textLists.text) : null
      } else {
          purchaseOrder.textList = []
      }
      }

    }

    if(group.purchaseOrder.first()) {
      currentPurchaseOrder = group.purchaseOrder.first()
      currentPurchaseOrder.lineItems = groupedLineItems as Set

      //currentPurchaseOrder.customFields = groupedCustomFields as Set

      //currentPurchaseOrder.objectLinks = groupedObjectLinks as Set

      //currentPurchaseOrder.textList = groupedTextList as Set
    }
    purchaseOrderOutput.add(currentPurchaseOrder)
  }

  // Set the sorted purchaseOrder body to the message body
  message.setBody(JsonOutput.prettyPrint(JsonOutput.toJson(purchaseOrderOutput)))

  return message
}

def customFieldIsComplete(def c) {
  (c.fieldName && c.fieldValue)
}

def lineItemIsComplete(def line) {
  (line.unit && line.itemNumber && line.materialDescription && line.netPrice && line.schedules)
}

def objectLinkIsComplete(def link) {
  (link.linkType && link.erpId)
}

def textListIsComplete(def t) {
  (t.type && t.text)
}

def goodsReceiptsIsComplete(def gr) {
  (gr.deliveryNote && gr.quantity)
}

def serviceDetailsIsComplete(def sd) {
  (sd.description && sd.quantity)
}