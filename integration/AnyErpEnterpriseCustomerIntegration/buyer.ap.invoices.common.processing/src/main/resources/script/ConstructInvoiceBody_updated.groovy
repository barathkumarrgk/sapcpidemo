import com.sap.gateway.ip.core.customdev.util.Message
import java.math.BigDecimal
import groovy.json.JsonSlurper
import groovy.json.JsonOutput

def Message processData(Message message) {
  def body = message.getBody(java.lang.String) as String
  def jsonSlurper = new JsonSlurper()
  def invoicesInput = jsonSlurper.parseText(body)
  def finalInvoices = []

  invoicesInput.groupedInvoice.each { group ->
    def currentInvoice
    def lineItemForGroup = []
    def additionalChargesForGroup = []
    def complianceFieldsForGroup = []
    def customFieldsForGroup = []
    def documentLinksForGroup = []
    def externalIdLinkObjectsForGroup = []
    def disableFinanceTypeForGroup = []
    def taxItemForGroup = []

    group.invoice.each { it ->
      it.additionalCharges.each { additionalCharges ->
        if (additionalChargeIsComplete(additionalCharges)) {
          additionalCharges.fieldValue = additionalCharges.fieldValue ? new BigDecimal(additionalCharges?.fieldValue) : null
          additionalCharges.taxAmount = additionalCharges.taxAmount ? new BigDecimal(additionalCharges?.taxAmount) : null
          additionalCharges.taxRate = additionalCharges.taxRate ? new BigDecimal(additionalCharges?.taxRate) : null
          additionalCharges.creditIndicator = additionalCharges?.creditIndicator?.equalsIgnoreCase("true")
          additionalChargesForGroup.add(additionalCharges)
        }  else {
        it.additionalCharges = null
      }
      }

      it.compliance?.complianceFields?.each { complianceFields ->
        if (complianceFieldIsComplete(complianceFields)) {
          complianceFields.uiPosition = complianceFields.uiPosition ? new BigDecimal(complianceFields.uiPosition) : null
          complianceFieldsForGroup.add(complianceFields)
        }
      }

      it.compliance?.isSigned = it.compliance?.isSigned?.equalsIgnoreCase("true")

      it.creditedInvoiceErpFields?.grossAmount = it.creditedInvoiceErpFields?.grossAmount ? new BigDecimal(it.creditedInvoiceErpFields?.grossAmount) : null
      it.creditedInvoiceErpFields?.netAmount = it.creditedInvoiceErpFields?.netAmount ? new BigDecimal(it.creditedInvoiceErpFields?.netAmount) : null

      if (it.customFields) {
        it.customFields.each { cf ->
          if (customFieldIsComplete(cf)) {
            customFieldsForGroup.add(cf)
          }
        }
      }

      if (it.disableFinanceType) {
        it.disableFinanceType.each { dft ->
          if (disableFinanceTypeIsComplete(dft)) {
            disableFinanceTypeForGroup.add(dft)
          }
        }
      }
      it.comment = it.comment ? new String(it?.comment) : null
      it.deliverDate = it.deliverDate ? new Date(it?.deliverDate) : null
      it.deliveryNote = it.deliveryNote ? new String(it?.deliveryNote) : null
      it.discountAmount = it.discountAmount ? new BigDecimal(it.discountAmount) : null
      it.discountBaseAmount = it.discountBaseAmount ? new BigDecimal(it.discountBaseAmount) : null

      if (it.documentLinks) {
        it.documentLinks.each { dl ->
          if (documentLinkIsComplete(dl)) {
            documentLinksForGroup.add(dl)
          }
        }
      }
      it.erpStatusExplanation = it.erpStatusExplanation ? new String(it?.erpStatusExplanation) : null

      it.grossAmount = it.grossAmount ? new BigDecimal(it.grossAmount) : null

      //it.epBaseAmount = it.epBaseAmount ? new BigDecimal(it.epBaseAmount) : null

      if (it.earlyPayment) {
        it.earlyPayment?.financeRate = it.earlyPayment?.financeRate ? new BigDecimal(it.earlyPayment?.financeRate) : null
        it.earlyPayment?.invoiceAutoFinanceRate?.autoFinanceRate = it.earlyPayment?.invoiceAutoFinanceRate?.autoFinanceRate ? new BigDecimal(it.earlyPayment?.invoiceAutoFinanceRate?.autoFinanceRate) : null
        it.earlyPayment?.tedEligible = it.earlyPayment?.tedEligible?.equalsIgnoreCase("true")
      } else {
        it.earlyPayment = null
      }

      if (it.externalIdLinkObjects) {
        it.externalIdLinkObjects.each { e ->
          if (externalIdLinkObjectIsComplete(e)) {
            externalIdLinkObjectsForGroup.add(e)
          }
        }
      }

      it.invoiceIndicator = it.invoiceIndicator?.equalsIgnoreCase("true")

      it.metadata?.transferAttemptsToErp = it.metadata?.transferAttemptsToErp ? new BigDecimal(it.metadata?.transferAttemptsToErp) : null

      it.payment?.paymentTerms?.additionalMonth1 = it.payment?.paymentTerms?.additionalMonth1 ? new BigDecimal(it.payment?.paymentTerms?.additionalMonth1) : null

      it.payment?.paymentTerms?.additionalMonth2 = it.payment?.paymentTerms?.additionalMonth2 ? new BigDecimal(it.payment?.paymentTerms?.additionalMonth2) : null

      it.payment?.paymentTerms?.baselineDate?.additionalMonth = it.payment?.paymentTerms?.baselineDate?.additionalMonth ? new BigDecimal(it.payment?.paymentTerms?.baselineDate?.additionalMonth) : null

      it.payment?.paymentTerms?.baselineDate?.fixedDay = it.payment?.paymentTerms?.baselineDate?.fixedDay ? new BigDecimal(it.payment?.paymentTerms?.baselineDate?.fixedDay) : null

      it.payment?.paymentTerms?.days1 = it.payment?.paymentTerms?.days1 ? new BigDecimal(it.payment?.paymentTerms?.days1) : null

      it.payment?.paymentTerms?.days2 = it.payment?.paymentTerms?.days2 ? new BigDecimal(it.payment?.paymentTerms?.days2) : null

      it.payment?.paymentTerms?.days3 = it.payment?.paymentTerms?.days3 ? new BigDecimal(it.payment?.paymentTerms?.days3) : null

      it.payment?.paymentTerms?.daysNet = it.payment?.paymentTerms?.daysNet ? new BigDecimal(it.payment?.paymentTerms?.daysNet) : null

      it.payment?.paymentTerms?.fixedDay1 = it.payment?.paymentTerms?.fixedDay1 ? new BigDecimal(it.payment?.paymentTerms?.fixedDay1) : null

      it.payment?.paymentTerms?.fixedDay2 = it.payment?.paymentTerms?.fixedDay2 ? new BigDecimal(it.payment?.paymentTerms?.fixedDay2) : null

      it.payment?.paymentTerms?.percent1 = it.payment?.paymentTerms?.percent1 ? new BigDecimal(it.payment?.paymentTerms?.percent1) : null

      it.payment?.paymentTerms?.percent2 = it.payment?.paymentTerms?.percent2 ? new BigDecimal(it.payment?.paymentTerms?.percent2) : null

      it.payment?.paymentTerms?.asap = it.payment?.paymentTerms?.asap?.equalsIgnoreCase("true")

      it.partialPayment = it.partialPayment?.equalsIgnoreCase("true")

      it.sapField?.ersIndicator = it.sapField?.ersIndicator?.equalsIgnoreCase("true")

      it.tax?.headerTotalTaxAmount = it.tax?.headerTotalTaxAmount ? new BigDecimal(it.tax?.headerTotalTaxAmount) : null

      it.tax?.lineItemTotalTaxAmount = it.tax?.lineItemTotalTaxAmount ? new BigDecimal(it.tax?.lineItemTotalTaxAmount) : null

      it.tax?.totalTaxAmount = it.tax?.totalTaxAmount ? new BigDecimal(it.tax?.totalTaxAmount) : 0

      if (it.tax?.taxItems) {
        it.tax.taxItems.each { taxItem ->
          taxItem?.amount = taxItem?.amount ? new BigDecimal(taxItem?.amount) : null
          taxItem?.rate = taxItem?.rate ? new BigDecimal(taxItem?.rate) : null
          taxItem?.taxType = taxItem?.taxType ? new String(taxItem?.taxType) : null
          taxItemForGroup.add(taxItem)
        }
      } else {it.tax.taxItems = null}

      if (it.lineItems) {
        it.lineItems.each { lineItem ->
          lineItemForGroup.add(lineItem)
        }
      }

      it.lineItemTotalDiscountAmount = it.lineItemTotalDiscountAmount ? new BigDecimal(it.lineItemTotalDiscountAmount) : null

      it.netAmount = it.netAmount ? new BigDecimal(it.netAmount) : null
      it.otherCurrencyExchangeRate = it.otherCurrencyExchangeRate ? new BigDecimal(it.otherCurrencyExchangeRate) : null
      it.paymentAmount = it.paymentAmount ? new BigDecimal(it.paymentAmount) : null
      it.postingDate = it.postingDate ? new Date(it?.postingDate) : null
      it.receivingDate = it.receivingDate ? new Date(it?.receivingDate) : null
      it.referenceNumber = it.referenceNumber ? new String(it?.referenceNumber) : null

      if (it?.sapField) {
        it.sapField.ersIndicator = it.sapField?.ersIndicator ? new String(it?.sapField?.ersIndicator) : false
        it.sapField.headerText = it.sapField?.headerText ? new String(it?.sapField?.headerText) : null
        it.sapField.paymentSapId = it.sapField.paymentSapId ? new String(it?.sapField?.paymentSapId) : null
        it.sapField.sapCreateDate = it.sapField.sapCreateDate ? new Date(it?.sapField?.sapCreateDate) : null
        it.sapField.sapDoc = it.sapField.sapDoc ? new String(it?.sapField?.sapDoc) : null
        it.sapField.sapIdFi = it.sapField.sapIdFi ? new String(it?.sapField?.sapIdFi) : null
        it.sapField.sapIdWf = it.sapField.sapIdWf ? new String(it?.sapField?.sapIdWf) : null
        it.sapField.sapIdMm = it.sapField.sapIdMm ? new String(it?.sapField?.sapIdMm) : null
     } else {
        it.sapField = null
      }

      if (it?.signature) {
        it.signature.localScheme = it.signature.localScheme ? new String(it?.signature?.localScheme) : null
        it.signature.signatureVersion = it.signature.signatureVersion ? new String(it?.signature?.signatureVersion) : null
        it.signature.entityFingerprint = it.signature.entityFingerprint ? new String(it?.signature?.entityFingerprint) : null
        it.signature.signature = it.signature.signature ? new String(it?.signature?.signature) : null
        it.signature.timestamp = it.signature.timestamp ? new String(it?.signature?.timestamp) : null
     } else {
        it.signature = null
      }
      it.status = it.status ? new String(it?.status) : "IN_PROCESS"
      it.statusLastUpdated = it.statusLastUpdated ? new Date(it?.statusLastUpdated) : null

      it.totalAddlChargesAmount = it.totalAddlChargesAmount ? new BigDecimal(it.totalAddlChargesAmount) : null
      it.totalDiscountAmount = it.totalDiscountAmount ? new BigDecimal(it.totalDiscountAmount) : 0
      it.lineItems = lineItemForGroup as Set
      //finalInvoices.add(it)
    }
    if (group.invoice.first()) {
      currentInvoice = group.invoice.first()
      currentInvoice.lineItems = lineItemForGroup as Set
      currentInvoice.additionalCharges = additionalChargesForGroup as Set
      currentInvoice.compliance?.complianceFields = complianceFieldsForGroup as Set
      currentInvoice.customFields = customFieldsForGroup as Set
      currentInvoice.documentLinks = documentLinksForGroup as Set
      currentInvoice.externalIdLinkObjects = externalIdLinkObjectsForGroup as Set
      currentInvoice.disableFinanceType = disableFinanceTypeForGroup as Set
      currentInvoice.tax?.taxItems = taxItemForGroup as Set
      finalInvoices.add(currentInvoice)
    }
  }

  message.setBody(JsonOutput.prettyPrint(JsonOutput.toJson(finalInvoices)))
  return message
}

def additionalChargeIsComplete(def a) {
  if (a) {
    return (a.creditIndicator || a.description || a.fieldType || a.fieldValue || a.specialChargeName || a.taxAmount || a.taxExemptReason || a.taxRate)
  } else {
    return false
  }
}

def complianceFieldIsComplete(def comp) {
  (comp && comp.name)
}

def customFieldIsComplete(def cf) {
  (cf && cf.name && cf.value)
}

def documentLinkIsComplete(def dl) {
  (dl && dl.financeId)
}

def externalIdLinkObjectIsComplete(def e) {
  (e && e.apiKeyId)
}

def lineItemIsComplete(def li) {
  (li && li.unit && li.netAmount && li.itemNumber && li.itemDescription && li.totalAmount)
}

def disableFinanceTypeIsComplete(def dft) {
  (dft && dft.reason && dft.type)
}