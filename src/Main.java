import Controller.showDocumentation;

import java.util.Scanner;

public class Main{

    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        showDocumentation documentation = new showDocumentation();
          int Option;

        System.out.println("\n========================================================================");
        System.out.println("           WELCOME TO VEHICLE RENTAL MANAGEMENT SYSTEM                 ");
        System.out.println("=========================================================================");

        do {
            // Displaying the options of the Rental system.
            System.out.println("\n⭐ AVAILABLE OPTIONS ⭐");
            System.out.println("-------------------------");

            System.out.println("\nDOCUMENTATION");
            System.out.println("  1. View Documentation");


            System.out.print("\nChoose a command option: ");
             Option = input.nextInt();
             input.nextLine();
         switch (Option){
             case 1:  documentation.display();
             break;
             default : break;
         }
        }while(Option!=0);
    }

}