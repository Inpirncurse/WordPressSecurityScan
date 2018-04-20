import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequest implements Runnable {

    private static final String USER_AGENT = "Mozilla/5.0";
    protected String URL;
    protected int holi;


    public HttpRequest (String GET_URL){
        this.URL = GET_URL;
    }

    public void run(){
        try{
            URL obj = new URL(this.URL);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);
            int responseCode = con.getResponseCode();


            if(responseCode == 404){
                System.out.println("There is nothing on that url");
                holi = 1;
            }

            System.out.println("GET Response Code :: " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
                holi = 0;
                System.out.println(response.toString());

            } else {
                System.out.println("GET request not worked");
            }

        }catch(IOException e){
            System.out.println("Thread HTTP_GET terminated...");
        }
    }
    public int getHoli(){
        return holi;
    }

}
