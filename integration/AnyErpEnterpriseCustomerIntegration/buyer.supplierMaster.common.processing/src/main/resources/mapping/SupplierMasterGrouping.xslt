<xsl:stylesheet	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"> 
	<xsl:template match="/*">
		<root>
			<xsl:for-each-group select="supplierMaster" group-by="supplierNumber">
				<groupedSupplierMaster>
					<xsl:sequence select="current-group()"/>
				</groupedSupplierMaster>
			</xsl:for-each-group>
		</root>
	</xsl:template>
</xsl:stylesheet>