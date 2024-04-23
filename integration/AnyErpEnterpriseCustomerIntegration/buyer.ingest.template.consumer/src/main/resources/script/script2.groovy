import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import groovy.json.JsonSlurper;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.sap.it.api.keystore.*;
import com.sap.it.api.securestore.SecureStoreService;
import com.sap.it.api.securestore.UserCredential;
import com.sap.it.api.ITApiFactory;

def Message processData(Message message) {

    Map<String, Object> props = message.properties
    
    def objectNumber = props.get("objectNumber").toInteger()
    def objects = props.get("objects")
    def objectList = new JsonSlurper().parseText(objects)
    def folder = objectList.get(objectNumber)

    String userName =  "";
    String password = "";
    String hostName = "sftp.taulia.com";
    String filePath = "/john.hughes.int/" + folder;
    String finalString = "";

    /*
    def secureStorageService =  ITApiFactory.getApi(SecureStoreService.class, null);
    def cred = secureStorageService.getUserCredential(“<Name of SFTP Credential deployed in tenant>”);
    if (cred == null){
    // error handling
    }
    userName = cerd.getUserName();
    password = cred.getPassword();
    */
    
    //Get the Service Instance of the KeystoreService API 
    KeystoreService service = ITApiFactory.getService(KeystoreService.class, null);
    //Read the Private Key of a given alias
    def pKey = service.getKey('sap-ci-int');
    
    userName = 'SAP-CI-int';
    password = pKey;
    
    
    JSch jsch = new JSch();
    Session session = null;
    
    try {
        session = jsch.getSession(userName, hostName, 22);
        session.setConfig("StrictHostKeyChecking", "no");
        session.setPassword(password);
        session.connect();

        Channel channel = session.openChannel("sftp");
        channel.connect();
        ChannelSftp sftpChannel = (ChannelSftp) channel;

        InputStream stream = sftpChannel.get(filePath);
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(stream));
            String line;
            while ((line = br.readLine()) != null) {
            	finalString = finalString + line; // You can have your own logic over here
            }
        } catch (IOException io) {
            finalString = io.getMessage();
        } catch (Exception e) {
           finalString = e.getMessage();
        }

        sftpChannel.exit();
        session.disconnect();
    } catch (JSchException e) {
        //Exception Handling 
    } catch (SftpException ee) {
       //Exception Handling 
    }
    
    objectNumber += 1
    props.put('objectNumber', objectNumber)

	message.setBody(finalString);
	
	return message;
}