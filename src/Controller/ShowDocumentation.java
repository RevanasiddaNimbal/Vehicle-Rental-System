package Controller;

public class ShowDocumentation {

    public void display() {

        System.out.println("\n====================================================");
        System.out.println("      VEHICLE RENTAL SYSTEM -DOCUMENTATION    ");
        System.out.println("====================================================");

        System.out.println("OVERVIEW:");
        System.out.println("This system allows you to manage vehicles, customers,\n" +
                "and rental operations easily.");

        System.out.println("\n----------------------------------------------------");
        System.out.println("           VEHICLE MANAGEMENT       ");
        System.out.println("----------------------------------------------------");

        System.out.println("• Add Vehicle        - Register a new vehicle.");
        System.out.println("• View Vehicles      - Display all vehicles.");
        System.out.println("• Available Vehicles - Show vehicles ready for rent.");
        System.out.println("• Update Vehicle     - Modify vehicle details.");
        System.out.println("• Remove Vehicle     - Delete a vehicle from system.");

        System.out.println("\n----------------------------------------------------");
        System.out.println("           CUSTOMER MANAGEMENT      ");
        System.out.println("----------------------------------------------------");

        System.out.println("• Register Customer  - Create a new customer profile.");
        System.out.println("• View Customers     - Display all customers.");
        System.out.println("• Search Customer    - View customer details by ID.");
        System.out.println("• Remove Customer    - Delete customer record.");


        System.out.println("\n----------------------------------------------------");
        System.out.println("           RENTAL MANAGEMENT     ");
        System.out.println("----------------------------------------------------");

        System.out.println("• Rent Vehicle       - Rent an available vehicle.");
        System.out.println("• Return Vehicle     - Return a rented vehicle.");
        System.out.println("• View Rentals       - See all rental records.");

        System.out.println("\n----------------------------------------------------");
        System.out.println("           IMPORTANT RULES     ");
        System.out.println("----------------------------------------------------");

        System.out.println("• Vehicle ID and Customer ID are auto-generated.");
        System.out.println("• Access to rental and management operations requires a valid Vehicle ID and Customer ID.");
        System.out.println("• Only available vehicles can be rented.");
        System.out.println("• Rental cost = Price per day × Number of days.");
        System.out.println("• Vehicle becomes available after return.");

        System.out.println("\n----------------------------------------------------");
        System.out.println("Thank you for using Vehicle Rental System!");
        System.out.println("======================================================");
    }

}
