package Tests;

import client.RTS2Client;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 *
 * @author Radek
 */
public class HttpClientTest {
    public static void main(String[] args) throws MalformedURLException, IOException {
        //UUID ui = UUID.fromString("");
        
        String pass = "test";
        String name = "test";
        String adresa = "192.168.56.101:8889";
        
        UsernamePasswordCredentials creds = new UsernamePasswordCredentials("test", "test");
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
            new AuthScope("192.168.56.101", AuthScope.ANY_PORT), creds);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpClientContext context = HttpClientContext.create();
        context.setCredentialsProvider(credsProvider);
        HttpGet httpget = new HttpGet("http://192.168.56.101:8889/api/devices");
        CloseableHttpResponse response = httpclient.execute(httpget, context);
        try {
            
            //RTS2Client.GetCurrentImage("192.168.56.101:8889", "test", "test");
            
            System.out.println(response.getProtocolVersion());
            System.out.println(response.getStatusLine().getStatusCode());
            System.out.println(response.getStatusLine().getReasonPhrase());
            System.out.println(response.getStatusLine().toString());

            InputStream is = response.getEntity().getContent();
            
            Scanner s = new Scanner(is).useDelimiter("\\A");
            String inputJSON = s.hasNext() ? s.next() : "";
            
            System.out.println("json "+inputJSON);
            
        } finally {
            response.close();
        }
        
    }
}
