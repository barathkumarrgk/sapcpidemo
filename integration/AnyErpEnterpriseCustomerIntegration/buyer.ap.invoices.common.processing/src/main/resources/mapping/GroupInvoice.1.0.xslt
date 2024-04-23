<xsl:stylesheet	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:template match="/*">
        <root>
            <xsl:for-each-group select="invoice" group-by="concat(number, '|', supplier/vendorNumber, '|', sapField/sapIdFi)">
                <groupedInvoice>
                    <xsl:sequence select="current-group()"/>
                </groupedInvoice>
            </xsl:for-each-group>
        </root>
    </xsl:template>
</xsl:stylesheet>