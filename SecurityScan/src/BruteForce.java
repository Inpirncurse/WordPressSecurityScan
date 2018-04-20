import java.net.URL;
import java.lang.String;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Scanner;


public class BruteForce implements Runnable{

    public static int FLAG;
    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String POST_PARAMS = "log=flowers@hotmail.com&pwd=hola123";
    protected String URL;

    public BruteForce (String POST_URL){
        this.URL = POST_URL;
    }

    public void run(){
        try{
            URL obj = new URL(this.URL);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);

            // For POST only - START
            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();
            os.write(POST_PARAMS.getBytes());
            os.flush();
            os.close();
            // For POST only - END

            int responseCode = con.getResponseCode();
            System.out.println("POST Response Code :: " + responseCode);

            if(responseCode == 503 || responseCode == 400 || responseCode == 401 || responseCode == 403 || responseCode == 520 || responseCode == 301){
                System.out.println("YOUR SITE IS PROTECTED AGAINST BRUTE FORCE ATTACKS...");
                FLAG = 0;
            }

            if (responseCode == HttpURLConnection.HTTP_OK) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
                FLAG = 1;
                try (Scanner scanner = new Scanner(response.toString())) {
                    String responseBody = scanner.useDelimiter("\\A").next();
                    System.out.println(responseBody);
                }
            } else {
                System.out.println("POST request not worked");
            }


        }catch(IOException i) {
            System.out.println("Thread BruteForce terminated...");
        }
    }
    public int getFLAG() {
        return FLAG;
    }

}


