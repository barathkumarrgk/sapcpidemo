<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
   <xsl:output omit-xml-declaration="no" indent="yes"/>
   <xsl:template match="/*">
      <root>
         <xsl:copy-of select="/root/row[1]"/>
         <xsl:for-each select="/root/row">
            <xsl:if test="type eq 'attachment'">
               <xsl:variable name="attachmentIds" select="tokenize(contents, ',')"/>
               <xsl:for-each select="$attachmentIds">
                  <row>
                  	<type>attachment</type>
                     <contents>
                        <xsl:copy-of select="."/>
                     </contents>
                  </row>
               </xsl:for-each>
            </xsl:if>
         </xsl:for-each>
      </root>
   </xsl:template>
</xsl:stylesheet>