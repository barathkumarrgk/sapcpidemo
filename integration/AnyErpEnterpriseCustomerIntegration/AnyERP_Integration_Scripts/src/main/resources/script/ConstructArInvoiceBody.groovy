import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import groovy.json.JsonOutput

def Message processData(Message message) {
  def body = message.getBody(String) as String
  def jsonSlurper = new JsonSlurper()
  def jsonInput = jsonSlurper.parseText(body)
    
  def groups = jsonInput.groupedArInvoice
    
  def results = [:]

  groups.each { def group ->
    def items = group.arInvoice 
    items.each { def item ->
      def key = getKey(item)
      def existingItem = results.get(key)
      if (existingItem) {
        def newItem = formatInvoice(item)
        existingItem.customFields.addAll(newItem.customFields)
        existingItem.disableFinanceType.addAll(newItem.disableFinanceType)
        existingItem.lineItems.addAll(newItem.lineItems)
      } else {
        results.put(key, formatInvoice(item))
      }
    }
  }

  results.each { key, value ->
    if (value?.customFields) {
      value.customFields = (value.customFields- [null]) as Set
    }
    if (value?.disableFinanceType) {
      value.disableFinanceType = (value.disableFinanceType - [null]) as Set
    }
    //apply standard of date separator format 
    if(value?.erpTimestamp) {
        value.erpTimestamp = value.erpTimestamp.replaceAll("/","-")
    }
    if(value?.dueDate) {
        value.dueDate = value.dueDate.replaceAll("/","-")
    }
    if(value?.invoiceDate) {
        value.invoiceDate = value.invoiceDate.replaceAll("/","-")
    }
    if(value?.postingDate) {
        value.postingDate = value.postingDate.replaceAll("/","-")
    }
  }

  def finalJsonFormat = JsonOutput.toJson(results*.value)
  if (results) {
    message.setBody(finalJsonFormat)
  }
  message
}

Map formatInvoice(Map item) {
  item.creditIndicator = (item.creditIndicator != null) ? Boolean.parseBoolean(item.creditIndicator) : false
  item.customFields = customFieldIsComplete(item.customFields) ?  [item.customFields] : []
  item.disableFinanceType = disableFinanceTypeIsComplete(item.disableFinanceType) ? [item.disableFinanceType] : []
  item.discountAmount = item.discountAmount ? new BigDecimal(item.discountAmount) : null
  item.discountBaseAmount = item.discountBaseAmount ? new BigDecimal(item.discountBaseAmount) : null
  item.grossAmount = item.grossAmount ? new BigDecimal(item.grossAmount) : null
  if (item.lineItems) {
    item.lineItems = lineItemIsComplete(item.lineItems) ? [formatLineItem(item.lineItems)] : []
  }
  item.netAmount = item.netAmount ? new BigDecimal(item.netAmount) : null
  item.paymentAmount = item.paymentAmount ? new BigDecimal(item.paymentAmount) : null   
  item
}

Map formatLineItem(Map item) {
  item.customFields = [item.customFields]
  item.grossAmount = item.grossAmount ? new BigDecimal(item.grossAmount) : null
  item.itemNumber = item.itemNumber ? Integer.parseInt(item.itemNumber) : null
  item.netAmount = item.netAmount ? new BigDecimal(item.netAmount) : null
  item.taxAmount = item.taxAmount ? new BigDecimal(item.taxAmount) : null
  item.taxRate = item.taxRate ? new BigDecimal(item.taxRate) : null
  item
}

String getKey(def invoice) {
    "${invoice.businessUnit}|${invoice.customerNumber}|${invoice.creditIndicator}" + 
      "|${invoice.currency}|${invoice.discountAmount}|${invoice.dueDate}" + 
      "|${invoice.erpUniqueId}|${invoice.grossAmount}|${invoice.invoiceDate}" +
      "|${invoice.invoiceNumber}"
}

def customFieldIsComplete(def c) {
  (c.name && c.value)
}

def lineItemIsComplete(def line) {
  (line.itemNumber && line.netAmount && line.grossAmount)
}

def disableFinanceTypeIsComplete(def d) {
  (d.reason && d.type)
}

def setSeparatorFormatForDates(def date) {
    return date.replaceAll("/", "-")
}