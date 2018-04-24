/*
<Call other classes, create organize and manage all threads with ExecutorService
    and Thead start interrupts and joins, execute the attacks to the given URL
    and tell the user if the page is vulnerable to SQL Inject, and Brute Force Attacks.>
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
import java.lang.String;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        //request input to the user
        String WordPressUrl;
        Scanner sc = new Scanner(System.in);
        System.out.println("<WordPressSecurityScan>  Copyright (C) <2018>  <Alan Kuri>\n" +
                "    This program comes with ABSOLUTELY NO WARRANTY; for details type `show w'.\n" +
                "    This is free software, and you are welcome to redistribute it\n" +
                "    under certain conditions; type `show c' for details. \n\n");
        System.out.println("Enter an URL of a WordPress page to test SQLInjection and BruteForce vulnerabilities. \n");
        WordPressUrl = sc.nextLine();

        //URL
        final String GET_URL = WordPressUrl + "/admin";
        final String POST_URL = WordPressUrl + "/wp-login.php";

        //objects
        HttpRequest get = new HttpRequest(GET_URL);
        SQLInjection sqlInject = new SQLInjection(POST_URL);

        //threads
        Thread get_thread = new Thread(get);
        Thread sqlInject_thread = new Thread(sqlInject);

        //start threads
        //while get_thread is alive check if an error occur
        get_thread.start();
        while (get_thread.isAlive()) {
            if (get.getHoli() == 1) {
                get_thread.interrupt();
                get_thread.join();
                return;
            }
        }

        //SQL Injection
        //Start the thread sqlInject and while its alive check if an error occur.
        sqlInject_thread.start();
        while (sqlInject_thread.isAlive()) {
            if (sqlInject.getFLAG() == 1) {
                System.out.println("YOUR SITE IS VULNERABLE AGAINST SQL INJECTIONS ATTACKS...");
                break;
            }
        }

        //Brute Force
        //Create an Executor Service, assigned a task and submit it, wait 5 seconds beetween each task for http respond.
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++){
            BruteForce tsk = new BruteForce(POST_URL);
            executorService.submit(tsk);
            TimeUnit.MILLISECONDS.sleep(5000);
            if (tsk.getFLAG() == 0) {
                executorService.shutdownNow();
                break;
            } else if(i == 9){
                System.out.println("YOUR SITE IS VULNERABLE AGAINST BRUTE FORCE ATTACKS");
            }
        }

        //Interrupts and joins to clear threads.
        get_thread.interrupt();
        get_thread.join();
        sqlInject_thread.interrupt();
        sqlInject_thread.join();
        executorService.shutdown();
    }
}
