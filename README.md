## Project Goal
Building a portal for EarnIt where students could create an account, file their earnings, calculate taxes to pay and streamline their process of receiving a BTW number - Work permit number to receive work permits as well as apply for internshios on this website. The portal also support employees logging in to their dashboard to view  students to approve their files before filing for an official work permit and automate PDF generation of official documents to be sent to the KVK.


## Usage

The Project
Earn It facilitates employment for students through self-employment permits (BTW in Dutch), offering flexibility and simplifying the usual complexities associated with work visas. We currently assist students manually with BTW application forms, aiming to automate this process. Our vision includes:

Automating the BTW form filling process based on student inputs.
Providing instructions on mailing the filled forms to the KvK (Chamber of Commerce).
Additionally, our platform supports students in managing their taxes on a quarterly basis, aiming to simplify tax filing and avoid fines. Key features include:

Centralized income reporting and quarterly tax calculation.
Email reminders for tax deadlines.
Integration with a Mail Server for automated reminders.




## Roadmap


Phase 1: Initial Setup and Core Functionality
Project Initialization

Assignments: Set up project structure and dependencies using Maven (pom.xml), including necessary libraries and frameworks.
Why: Ensures a standardized development environment and facilitates easy management of dependencies.
Database Configuration

Assignments: Configure database connection using PostgreSQL (DatabaseConnection.java).
Why: Establishes a reliable connection to the database, enabling data persistence and management.
Data Access Layer Development

Assignments: Develop DAO classes (CommentDao, EmployeeDao, StudentDao, TaxDao, VATformDao) to handle CRUD operations.
Why: Facilitates structured data access, separation of concerns, and maintainability.
Model Definition

Assignments: Define data models (Comment, Employee, Student, Tax, VATform) representing core entities within the application.
Why: Ensures consistency in data representation and supports integrity in database operations.
RESTful API Development

Assignments: Implement API endpoints using JAX-RS (CommentRoutes, EmployeeRoutes, StudentRoutes, TaxRoutes, VATformRoutes) to manage client-server interactions.
Why: Enables seamless communication between front-end and back-end, supporting modular development and scalability.
Session Management

Assignments: Implement SessionFilter to manage user sessions and enhance application security.
Why: Ensures secure and efficient session handling across the application.
Utility Development

Assignments: Develop utilities (VATformPDFgenerator) to automate PDF document generation for VAT forms.
Why: Improves efficiency and accuracy in handling administrative tasks related to document generation.
Phase 2: Feature Development and Integration
User Authentication

Assignments: Implement authentication mechanisms to secure user access (EarnitApp.java).
Why: Ensures only authorized users can access the platform, enhancing overall application security.
Automated VAT Form Generation

Assignments: Develop functionality for automated filling of VAT forms based on user inputs (VATformDao, VATformRoutes).
Why: Simplifies and accelerates the process of VAT form submission for students.
Tax Management System

Assignments: Implement features for income reporting, quarterly tax calculation, and deadline reminders (TaxDao, TaxRoutes).
Why: Assists students in complying with tax regulations, minimizing errors and penalties.
Email Notification Integration

Assignments: Integrate Mail Server for automated email reminders regarding tax filing deadlines (MailService).
Why: Improves user engagement and compliance by notifying students of upcoming tasks and deadlines.
Phase 3: Optimization and Deployment
UI/UX Enhancement

Assignments: Refine user interfaces (html, css, js files) for better usability and aesthetics.
Why: Enhances user experience, making navigation and interaction intuitive and pleasant.
Continuous Integration and Deployment (CI/CD)

Assignments: Set up CI/CD pipelines (earnit1.iml, target, testing scripts) for automated testing and deployment.
Why: Ensures code quality, reliability, and rapid deployment of new features and updates.
Documentation and Support





## Authors and acknowledgment

Begaiym Sarbagysheva
Martina Roci
Rian Sood
Mara Teodorescu
Tania-Maria Mincu
Ioana-Natasa Tudorache



## Project status

We did not have time to implement the security on the email server and therefore used a dummy email to send emails and some extra functionalities regarding the profile (edit the profile picture, deleting the profile).
