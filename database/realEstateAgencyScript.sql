CREATE database FinalWorkDS;

use FinalWorkDS;

create table client (
     ID INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
     name varchar(50),
     lastName varchar(50),
     photo varchar(255),
     gender char(1),
     active boolean,
     birthDate date,
     email varchar(50),
     phone varchar(20),
     dni varchar(18) unique,
     password varchar(50),
     agent boolean,
     owner boolean
);

create table property (
	ID INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    name varchar(50),
    address varchar (50),
    description varchar (200),
    photo varchar(255),
    active boolean,
    value decimal(15,2),
    status varchar(23),
    agentID int,
    foreign key (agentID) references client(ID)
);

create table ownerProperty(
	ID INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    ownerID int,
    propertyID int,
    foreign key (ownerID) references client(ID),
    foreign key (propertyID) references property(ID)
);

create table contract(
	ID INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    propertyID int,
    clientID int,
    contractDate date,
    monthlyFee decimal(10,2),
    foreign key (clientID) references client(ID),
    foreign key (propertyID) references property(ID)
);

create table visitsProperty(
    ID INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    VisitDate date, 
    clientID int,
    approved boolean,
    propertyID int,
    foreign key (clientID) references client(ID),
    foreign key (propertyID) references property(ID)
);

create table payment (
    id int AUTO_INCREMENT PRIMARY KEY,
    contractID int,
    dueDate date,
    months int,
    paid boolean,
    debtor boolean,
    foreign key (contractID) references contract(id)
);