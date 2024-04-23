   import com.sap.gateway.ip.core.customdev.util.Message;
    import java.util.HashMap;
    import com.sap.it.api.pd.PartnerDirectoryService;
    import com.sap.it.api.ITApiFactory;

def Message processData(Message message) {

    def service = ITApiFactory.getApi(PartnerDirectoryService.class, null); 
    if (service == null){
        throw new IllegalStateException("Partner Directory Service not found");
    }
	
    // Partner Authorization
    def headers = message.getHeaders();
    def partnerID = headers.get("SAP_AS2PartnerID");
    if (partnerID == null){
        throw new IllegalStateException("SAP_AS2PartnerID is not set as a header")      
    }
    
     def Outbound_ProcessDirect_URL = service.getParameter("Outbound_ProcessDirect_URL", partnerID , String.class);
        if (Outbound_ProcessDirect_URL == null){
        throw new IllegalStateException("Outbound_ProcessDirect_URL not found for partnerID: " + partnerID)      
    }

    message.setProperty("Outbound_ProcessDirect_URL", Outbound_ProcessDirect_URL);    

    return message;
}