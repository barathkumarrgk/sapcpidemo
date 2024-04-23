<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:template match="/*">
    <root>
      <xsl:for-each-group select="customerMaster" group-by="customerNumber">
          <groupedCustomerMaster>
			<xsl:sequence select="current-group()"/>
		  </groupedCustomerMaster>
      </xsl:for-each-group>
    </root>
  </xsl:template>
</xsl:stylesheet>