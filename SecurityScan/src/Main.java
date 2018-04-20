import java.lang.String;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        String WordPressUrl;
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter an URL of a WordPress page to test SQLInjection and BruteForce vulnerabilities. \n");
        WordPressUrl = sc.nextLine();

        final String GET_URL = WordPressUrl + "/admin";
        final String POST_URL = WordPressUrl + "/wp-login.php";

        //objects
        HttpRequest get = new HttpRequest(GET_URL);
        SQLInjection sqlInject = new SQLInjection(POST_URL);

        //threads
        Thread get_thread = new Thread(get);
        Thread sqlInject_thread = new Thread(sqlInject);

        //start threads
        get_thread.start();
        while (get_thread.isAlive()) {
            if (get.getHoli() == 1) {
                //System.out.println("nel");
                get_thread.interrupt();
                get_thread.join();
                return;
            }
        }

        //SQL Injection
        sqlInject_thread.start();
        while (sqlInject_thread.isAlive()) {
            if (sqlInject.getFLAG() == 1) {
                System.out.println("YOUR SITE IS VULNERABLE AGAINST BRUTE FORCE ATTACKS...");
                break;
            }
        }

        //Brute Force
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

        get_thread.interrupt();
        get_thread.join();
        sqlInject_thread.interrupt();
        sqlInject_thread.join();
        executorService.shutdown();

    }
}
