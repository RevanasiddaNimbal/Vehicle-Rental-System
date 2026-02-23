import application.Application;
import config.AppConfig;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Application app = AppConfig.createApplication(input);
        app.start();
    }
}
