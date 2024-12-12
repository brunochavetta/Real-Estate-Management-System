use FinalWorkDS;
-- Database and Table Creation (already provided)

-- Insert data into Client table (5 records)
INSERT INTO client (name, lastName, photo, gender, active, birthDate, email, phone, dni, password, agent, owner) VALUES
  ('Agustin', 'De Luca', 'image.jpg', 'M', TRUE, '1990-01-01', 'agustin.deluca@gmail.com', '1234567890', '12345678', 'Agustin12', TRUE, FALSE),
  ('Bruno', 'Chavetta', 'image2.jpg', 'M', TRUE, '1995-02-15', 'bruno.chavetta@gmail.com', '9876543210', '87654321', 'Bruno123', TRUE, FALSE),
  ('Brian', 'Salina', 'image3.jpg', 'M', TRUE, '1980-12-24', 'brian.salinas@gmail.com', '0987654321', '21345678', 'Brian123', TRUE, TRUE),
  ('Luciano', 'Moise', 'image4.jpg', 'M', TRUE, '2000-05-03', 'luciano.moise@gmail.com', '1122334455', '54321098', 'Luciano1', TRUE, TRUE),
  ('William', 'Brown', 'image5.jpg', 'M', TRUE, '1975-08-10', 'william.brown@email.com', '2233445566', '67890123', 'Password123', FALSE, FALSE),
  ('Jennifer', 'Garcia', 'image6.jpg', 'F', TRUE, '1988-03-21', 'jennifer.garcia@email.com', '3344556677', '12345098', 'Password123', false, FALSE),
  ('David', 'Miller', 'image7.jpg', 'M', TRUE, '1992-09-09', 'david.miller@email.com', '4455667788', '78901234', 'Password123', FALSE, TRUE),
  ('Maria', 'Davis', 'image8.jpg', 'F', FALSE, '2002-04-18', 'maria.davis@email.com', '5566778899', '34567890', 'Password123', false, TRUE),
  ('Andrew', 'Rodriguez', 'image9.jpg', 'M', TRUE, '1985-10-27', 'andrew.rodriguez@email.com', '6677889900', '90123456', 'Password123', FALSE, TRUE),
  ('Elizabeth', 'Clark', 'image10.jpg', 'F', TRUE, '1998-06-16', 'elizabeth.clark@email.com', '7788990011', '56789012', 'Password123', false, FALSE);


INSERT INTO property (name, address, description, photo, active, value, status, agentID)
VALUES ('Cozy Beach House', '123 Ocean View Ave, Miami FL', 'Beautiful oceanfront property with private beach access', 'cozy.png', TRUE, 1200000.00, 'Rent', 1),
       ('Modern City Apartment', '456 Main St, New York NY', 'Spacious loft apartment with stunning city views', 'Modern.png', TRUE, 850000.00, 'Rent', 2),
       ('Charming Country Cottage', '789 Maple Lane, Vermont VT', 'Rustic cottage nestled in the heart of Vermont countryside', 'Charming.png', TRUE, 425000.00, 'Rent', 4),
       ('Luxury Mountain Cabin', '1011 Peak Dr, Colorado CO', 'Breathtaking mountain views and ski-in/ski-out access', 'Luxury.png', TRUE, 1500000.00, 'Rent', 4),
       ('Historical Townhouse', '1213 Elm St, Boston MA', 'Restored townhouse with unique architectural details', 'Historical.png', TRUE, 780000.00, 'Rent', 1),
       ('Renovated Beach Bungalow', '1415 Sandpiper Way, California CA', 'Beachfront bungalow with modern updates', 'Renovated.png', TRUE, 925000.00, 'Sale', 2),
       ('Tranquil Garden Retreat', '1617 Evergreen St, Seattle WA', 'Peaceful home surrounded by lush gardens', 'Tranquil.png', TRUE, 675000.00, 'Sale', 3),
       ('Spacious Family Home', '1819 Oak Blvd, Chicago IL', 'Large home with plenty of space for a growing family', 'Spacious.png', TRUE, 1100000.00, 'Sale', 4),
       ('Elegant Condo', '2021 Park Ave, Philadelphia PA', 'Sophisticated condo in a prestigious location', 'Elegant.png', TRUE, 575000.00, 'Sale', 1),
       ('Studio Apartment', '2223 Pine St, Austin TX', 'Compact and stylish studio apartment in a vibrant city', 'Studio.png', TRUE, 325000.00, 'Sale', 2);


INSERT INTO ownerProperty (ownerID, propertyID)
VALUES (7, 2),  -- John Doe owns Spacious House
       (8, 4),  -- Michael Johnson owns Charming Bungalow
       (9, 5);   -- Jane Smith owns Luxury Townhouse

INSERT INTO contract ( propertyID, clientID, contractDate, monthlyFee)
VALUES (9, 7, '2024-06-01', 2000.50),
       (5, 9, '2024-05-15', 2000.50),
       (2, 8, '2024-04-20', 2020.50),
       (3, 8, '2023-12-12', 200.50),
       (1, 6, '2024-02-05', 2000.50),
       (6, 5, '2023-12-15', 1500.20),
       (7, 5, '2009-10-25', 1500.00);       

INSERT INTO visitsProperty (VisitDate, clientID, approved, propertyID)
VALUES ('2024-06-10', 5, TRUE, 1),  -- Michael Johnson visits Cozy Apartment
       ('2024-06-12', 6, FALSE, 6);   -- Jane Smith visits Charming Bungalow

INSERT INTO payment (contractID, dueDate, months, paid, debtor)
VALUES (1, '2024-06-01', 3, TRUE,  false),
       (2, '2024-05-15', 1, TRUE, false),
       (3, '2024-04-20', 2, TRUE,  false),
       (4, '2024-03-12', 4, TRUE,  false),
       (1, '2024-06-15', 2, false,  TRUE), 
       (5, '2024-02-05', 1, TRUE, false),
       (2, '2024-01-10', 2, TRUE,  false),
       (6, '2023-12-15', 3, false, true),
       (3, '2023-11-20', 3, TRUE,  false),
       (7, '2009-10-25', 180, FALSE, true);
