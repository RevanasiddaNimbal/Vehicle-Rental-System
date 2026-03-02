import application.Application;
import config.AppConfig;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            Application app = AppConfig.createApplication(input);
            app.start();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Application failed to start: "
                    + e.getMessage());
            System.exit(1);
        }
    }
}
