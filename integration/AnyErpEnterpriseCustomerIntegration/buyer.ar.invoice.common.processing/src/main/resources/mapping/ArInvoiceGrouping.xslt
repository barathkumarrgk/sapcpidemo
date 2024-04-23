<xsl:stylesheet	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"> 
	<xsl:template match="/*">
		<root>
			<xsl:for-each-group select="arInvoice" group-by="erpUniqueId">
				<groupedArInvoice>
					<xsl:sequence select="current-group()"/>
				</groupedArInvoice>
			</xsl:for-each-group>
		</root>
	</xsl:template>
</xsl:stylesheet>