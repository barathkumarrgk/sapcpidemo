<xsl:stylesheet	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"> 
	<xsl:template match="/*">
		<root>
			<xsl:for-each-group select="paymentRecord" group-by="concat(documentId, '|', vendorNumber, '|', number)">
				<groupedPayment>
					<xsl:sequence select="current-group()"/>
				</groupedPayment>
			</xsl:for-each-group>
		</root>
	</xsl:template>
</xsl:stylesheet>