# Vehicle Rental Management System

A Java-based terminal application for managing vehicle rentals. Users can register, log in, search vehicles, book
vehicles, make payments, and manage their rentals.

## Features

- User registration and login
- Admin, Vehicle Owner, and Customer roles
- Vehicle registration and management
- Search for available vehicles
- Vehicle booking and return
- Payment and wallet management
- Booking cancellation and refunds
- Late return and penalty management
- OTP-based password recovery
- Email notifications
- Invoice generation
- PostgreSQL database

## Technology Stack

- Java 25
- Maven
- PostgreSQL
- JDBC
- HikariCP

## User Roles

### Admin

- Manage vehicles
- View customer and owner records
- View bookings, payments, and transactions
- Manage penalties

### Vehicle Owner

- Register and manage vehicles
- View vehicle bookings
- Track rental and return details
- Manage wallet

### Customer

- Register and log in
- Search and book vehicles
- Return vehicles
- Make payments
- Manage wallet
- View rental history
- Recover password using OTP

## Project Structure

```text
vehicle-rental-system/
├── pom.xml
├── README.md
└── src/
    ├── main/
    │   ├── java/
    │   │   ├── admin/
    │   │   ├── application/
    │   │   ├── authentication/
    │   │   ├── cancellation/
    │   │   ├── config/
    │   │   ├── customer/
    │   │   ├── database/
    │   │   ├── exception/
    │   │   ├── initializer/
    │   │   ├── invoice/
    │   │   ├── notification/
    │   │   ├── otp/
    │   │   ├── payment/
    │   │   ├── penalty/
    │   │   ├── rental/
    │   │   ├── transaction/
    │   │   ├── UI/
    │   │   ├── user/
    │   │   ├── util/
    │   │   ├── vehicle/
    │   │   ├── vehicleowner/
    │   │   ├── wallet/
    │   │   └── Main.java
    │   └── resources/
    │       ├── config.properties
    │       └── schema.sql
    └── test/
```

## Database

The app uses PostgreSQL to store:

- Vehicles
- Admins
- Vehicle owners
- Customers
- Rentals
- Penalties
- Cancellations
- Wallets and wallet login info
- OTP codes
- Transactions

Schema location:

```text
src/main/resources/schema.sql
```

## Prerequisites

- JDK 25 or compatible Java version
- Maven 3.9+
- PostgreSQL database
- Internet connection (for email/PDF features)

### Environment Variables

Create a `.env` file in the project root and configure the following environment variables:

```env
DATABASE_URL=your_database_url_here
BREVO_API_KEY=your_brevo_api_key_here
BREVO_EMAIL=your_brevo_email_here
BREVO_NAME=your_brevo_name_here
```

## Setup

**1. Clone the repository**

```bash
git clone https://github.com/RevanasiddaNimbal/Vehicle-Rental-System.git
cd vehicle-rental-system
```

**2. Build the project**

```bash
mvn clean compile
```

**3. Run the application**

```bash
mvn exec:java -Dexec.mainClass=Main
```

If the exec plugin is unavailable, build the project first and run it manually using your local Maven classpath.

## Runtime Behavior

Main menu options:

- Documentation
- Admin panel
- Vehicle owner panel
- Customer panel
- Exit

The app runs interactively through the console.

## Security Notes

- Passwords are stored as hashed or encoded values.
- OTP and email are used for password recovery and notifications.
- Do not commit database passwords or secrets to source control.

## Use Cases

- Running a vehicle rental business from the terminal
- Testing rental workflows
- Learning layered Java design (factories, services, repositories, strategies)
- Modeling real-world business logic in code

## Notes

This is a command-line application, not a web app. It focuses on clean structure, role-based access, and real business
logic like rentals and payments.

## License

No formal license yet. Treat this as educational or internal use only unless a license is added.

## Developer

**Revanasidda Nimbal** is the developer of AgriMarket.

- GitHub: [**RevanasiddaNimbal**](https://github.com/RevanasiddaNimbal)
- LinkedIn: [**Revanasidda Nimbal**](https://www.linkedin.com/in/revanasidda-nimbal-68a596365/)
- Email: `revanasiddanimbal82@gmail.com`

## Contributing

Ways to contribute:

- Add input validation and edge-case handling
- Improve database migration support
- Add tests for services and controllers
- Enhance reporting and invoice features
- Build a web frontend

