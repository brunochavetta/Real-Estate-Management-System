# Gauchocode Software Development

- Theme:  Real Estate agency 
 
# Proyect Setup

1. Install prerequisites 
   - MySQL server (https://dev.mysql.com/downloads/mysql/)
   - (Optional) MySQL Workbench (https://dev.mysql.com/downloads/workbench/)
   - Maven (https://maven.apache.org/download.cgi)
2. Run sql script (create database and tables on database folder)
3. Build project ("mvn compile" on terminal)
4. Create on root folder .env file 
   
    ```
    USER= "YOUR_USER"
    PASSWORD="YOUR_PASSWORD"
    DATABASE="YOUR_DATABASE_NAME"
    PORT="jdbc:mysql://localhost:YOUR_PORT"
    ```
5. Run proyect

## Problem Description:

An real estate agency faces operational challenges due to the absence of a centralized system that integrates employee management, properties, contracts, clients, owners, payment tracking, among other functionalities specific to real estate operations. The lack of integration leads to inefficient management, fragmented communication, and manual processes that are prone to errors.

## Goals:

Develop an internal system for a real estate agency that facilitates the management and registration of contracts, clients, guarantors, real estate brokers and other related functionalities.

The aim is to optimize the internal processes of a real estate company through the development of comprehensive software that allows managing all areas related to its operation. The objective is to reduce the costs associated with the use of multiple applications to carry out real estate management and reduce the time necessary to carry out tasks, making their management more agile and efficient.

### Requirements:
- Contract Registration: The creation of new rental contracts must be allowed, registering their details, as well as those of the property (address, type of property, price, conditions, assigned real estate broker, among other things). Finally, clients and requested guarantors must be associated with the contract.
- Client Management: You must have a record of all the real estate agency's active clients and also those inactive clients that have generated conflicts during their career. Store customers with their corresponding information (names, addresses, contact channels, etc.). Allow you to search for a customer based on specific criteria.
- Guarantor Management: Information on guarantors associated with active contracts must be recorded.
- Payment/Collection Tracking: Track rental payments. Record payments received and debts if any. Finally, reminders can be generated for deadlines that are about to expire or have already expired.
- Security: Generate a validation system so that only people who have permissions can access confidential information. Designate roles for each employee who works in the real estate agency, assigning them a corresponding area.
- Properties: Designate the required information about the property (type of property, price, address, number of rooms, etc.). Specify its status (active, with reservation, not available). Finally, it must be associated with the owner/client to whom it belongs.
- Visits: A day, time, property, client and real estate broker must be associated for the visit. If you have reserved, the amount must be indicated. 

## Concepts: 

- Real estate agency: A company that is dedicated to intermediating in the purchase, sale or rental of real estate, such as houses, apartments, land, etc.
- Employees: People who work for the real estate agency, performing roles such as real estate agents, administrators, accountants, etc.
- Properties: The real estate that the agency is responsible for selling, renting or managing on behalf of the owners.
- Owners: The people or entities that own the properties that the agency manages.
- Clients: People interested in buying, renting or selling properties through the real estate agency.
- Payments: Financial transactions related to the purchase, sale or rental of properties, including agency commissions, payments to owners, etc.
- Employee Management: The lack of a centralized platform makes it difficult to assign tasks, track progress, and coordinate the team.
- Property Management: The agency needs an efficient way to keep up-to-date records of all its properties, including details such as location, features and availability status.
- Owner Management: A system is required to maintain a record of the owners of the properties the agency manages, as well as their contact information and any other relevant information.
- Client Management: The agency needs a way to store and organize its clients' information, including contact details, preferences and past transactions.
- Payment Tracking: It is essential to have a tool that allows you to track payments made and pending, ensuring transparent and efficient financial management.

## Daily Schedule

Every day at 19:30 pm UTC -3.

## Current Scrum Master

Agustín De Luca was the first Scrum Master chosen by the GauchoCode team.

## Team Members

- Agustin De Luca
- Brian Salina
- Bruno Chavetta
- Luciano Moisé

## Important Links

[Trello Board](https://trello.com/b/gQVRD5at/gauchocode-software-development-1)
[Figma](https://www.figma.com/design/bLVMuoDoYowmcd4iY3GTvf/GauchoCode?node-id=0-1&t=bEorJvU56rfmqtvD-0)