<xsl:stylesheet	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:template match="/*">
    <root>
      <xsl:for-each-group select="purchaseOrder" group-by="number">
        <xsl:for-each-group select="current-group()" group-by="vendorNumber">
          <groupedPurchaseOrder>
            <xsl:sequence select="current-group()"/>
          </groupedPurchaseOrder>
        </xsl:for-each-group>
      </xsl:for-each-group>
    </root>
  </xsl:template>
</xsl:stylesheet>