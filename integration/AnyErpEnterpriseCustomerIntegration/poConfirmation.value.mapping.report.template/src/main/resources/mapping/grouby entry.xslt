<xsl:stylesheet	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"> 
	<xsl:template match="/*">
		<root>
		    <xsl:for-each-group select="group/entry" group-by="schema">
				<csv>
					<xsl:sequence select="current-group()"/>
				</csv>
			</xsl:for-each-group>
		</root>
	</xsl:template>
</xsl:stylesheet>