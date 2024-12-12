package domain.agent;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.stream.Collectors;

import domain.client.AppClient;
import domain.client.Client;
import domain.client.ClientDAO;
import domain.client.ClientService;
import domain.property.AppProperty;
import domain.property.Property;
import domain.property.PropertyDAO;
import domain.property.PropertyService;

public class AppAppointment {

    private Scanner sc = new Scanner(System.in);

    public void app(Client client) throws Exception {

        AppointmentService as = new AppointmentService();

        int option;

        do {
            System.out.println("\n\n\"Welcome to appointment management!:");
            System.out.println("1. Add appointment \n"
                    + "2. Modify appointment \n"
                    + "3. Search appointment \n"
                    + "4. Delete appointment \n"
                    + "0. Exit");

            System.out.print("Enter an option: ");
            option = sc.nextInt();

            switch (option) {
                case 1:
                    requestAppointment(as, client);
                    break;
                case 2:
                    modifyAppointment(as, client);
                    break;
                case 3:
                    searchAppointment(as, client);
                    break;
                case 4:
                    deleteAppointment(as, client);
                    break;
                case 0:
                    System.out.println("Exit program...");
                    break;
                default:
                    System.out.println("invalid option. Try again.");
            }
        } while (option != 0);
    }

    public int returnOption() {
        System.out.println("1. Client \n" + "2. Property \n" + "3. Visit Date \n" + "4. Approval Status");
        int option = sc.nextInt();

        return option;
    }

    public Client addClient() throws Exception {
        System.out.println("\n==ADD CLIENT==");
        System.out.println("Enter: \n"
                + "1. If you want to create a new client\n"
                + "2. If you want to add an existing one");
        int option = sc.nextInt();

        Client client = null;
        AppClient mClient = new AppClient();
        ClientService cs = new ClientService();
        ClientDAO dao = new ClientDAO();

        String sql = "";

        try {
            switch (option) {
                case 1:
                    mClient.requestClientDetails(cs);
                    sql = "SELECT * FROM client ORDER BY ID DESC LIMIT 1;";
                    break;
                case 2:
                    mClient.showFoundClients(cs);
                    System.out.println("Enter the client ID");
                    int id = sc.nextInt();
                    sql = "SELECT * FROM client WHERE ID = " + id;
                    break;
                default:
                    System.out.println("Invalid option. Please enter 1 or 2.");
                    break;
            }

            if (client == null) {
                client = dao.returnClient(sql);
            }

        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number.");
            sc.nextLine();
        } catch (NoSuchElementException e) {
            System.out.println("Input was expected but not provided. Please try again.");
            sc.nextLine();
        }
        return client;
    }

    public Property addProperty(Client client) throws Exception {
        System.out.println("\nWhat property do you want to add?");

        Property property = null;
        AppProperty mProperty = new AppProperty();
        PropertyService ps = new PropertyService();

        try {
            Collection<Property> properties = mProperty.searchPropertyAppointment(ps).stream()
                    .filter(p -> p.getAgent().getId() == client.getId())
                    .collect(Collectors.toList());

            if (properties.isEmpty()) {
                System.out.println("You don't have any properties assigned to you.");
                return null;
            }

            System.out.println("Properties assigned to you:");
            for (Property p : properties) {
                System.out.println(p);
            }

            System.out.println("Enter the property ID");
            int id = sc.nextInt();

            property = properties.stream()
                    .filter(p -> p.getId() == id)
                    .findFirst()
                    .orElse(null);

            if (property == null) {
                throw new Exception("Invalid property ID or you are not assigned as the agent for this property.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number.");
            sc.nextLine();
        } catch (NoSuchElementException e) {
            System.out.println("Input was expected but not provided. Please try again.");
            sc.nextLine();
        }

        return property;
    }

    public void requestAppointment(AppointmentService as, Client client) throws Exception {
        System.out.println("SCHEDULE APPOINTMENT");
        System.out.println("====================");

        System.out.println("Enter visit date:");
        String visitDateInput = sc.next();

        LocalDate visitDate = null;

        try {
            visitDate = LocalDate.parse(visitDateInput);
            System.out.println("Appointment scheduled for: " + visitDate);

            as.addAppointment(visitDate, client, addProperty(client));
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please enter the date in the format YYYY-MM-DD.");
        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            System.out.println("Client or Property indicated not found");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void modifyClient(AppointmentService as, Appointment appointment) {
        try {
            Client client = appointment.getClient();
            AppClient mClient = new AppClient();
            ClientService cs = new ClientService();

            System.out.println("Current client: " + client);
            System.out.println("Do you want to modify the current client or search for a new one?");
            System.out.println("1. Modify current client");
            System.out.println("2. Search for a new client");

            int choice = sc.nextInt();

            if (choice == 1) {
                System.out.println("Modifying current client...");
                mClient.developmentModifyClient(cs, client);
            } else if (choice == 2) {
                client = addClient();
                appointment.setClient(client);
            } else {
                System.out.println("Invalid choice.");
                return;
            }

            as.modifyAppointment(appointment, 2);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void modifyProperty(AppointmentService as, Appointment appointment, Client client) {
        try {
            Property property = appointment.getProperty();
            AppProperty mProperty = new AppProperty();
            PropertyService ps = new PropertyService();

            System.out.println("Current property: " + property.printProperty());
            System.out.println("Do you want to modify the current Property or search for a new one?");
            System.out.println("1. Modify current Property");
            System.out.println("2. Search for a new Property");

            int choice = sc.nextInt();

            if (choice == 1) {
                System.out.println("Modifying current Property...");
                mProperty.developmentModifyProperty(ps, property);
            } else if (choice == 2) {
                property = addProperty(client);
                appointment.setProperty(property);
            } else {
                System.out.println("Invalid choice.");
                return;
            }

            as.modifyAppointment(appointment, 2);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void modifyAppointment(AppointmentService as, Client client) throws Exception {
        System.out.println("MODIFY APPOINTMENT");
        System.out.println("==================");

        String[] attributes = { "client", "property", "visitDate", "approved" };

        System.out.println("Enter the option you want to modify");
        int option = returnOption();

        if (option < 1 || option > attributes.length) {
            System.out.println("Invalid option.");
            return;
        }

        searchAppointment(as, client);
        System.out.println("Enter the id of the appointment you want to modify:");
        int id = sc.nextInt();
        String sql = "SELECT * FROM appointment WHERE ID = " + id;
        Appointment appointment = as.returnAppointment(sql);

        if (appointment == null) {
            System.out.println("No contract found with ID: " + id);
            return;
        }

        System.out.println("Enter the new data for " + attributes[option - 1] + ": ");
        String methodName = "set" + attributes[option - 1].substring(0, 1).toUpperCase()
                + attributes[option - 1].substring(1);

        try {
            Method method;
            Object newValue;
            if (attributes[option - 1].equals("visitDate")) {
                String appointmentDateInput = sc.next();
                LocalDate appointmentDate = null;
                try {
                    appointmentDate = LocalDate.parse(appointmentDateInput);
                    System.out.println("New appointment date: " + appointmentDate);
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format. Please enter the date in the format YYYY-MM-DD.");
                    return;
                }
                method = Appointment.class.getMethod(methodName, LocalDate.class);
                newValue = appointmentDate;
                method.invoke(appointment, newValue);
                appointment.setId(id);
                as.modifyAppointment(appointment, option);
                System.out.println("appointment " + attributes[option - 1] + " set to: " + newValue);
            } else if (attributes[option - 1].equals("approved")) {
                System.out.println("Enter: " +
                        "\n1. If approved" +
                        "\n2. If it is not approved");
                int optionApproved = sc.nextInt();
                switch (optionApproved) {
                    case 1:
                        newValue = true;
                        break;
                    case 2:
                        newValue = false;
                        break;
                    default:
                        System.out.println("Invalid option");
                        newValue = false;
                        break;
                }
                method = Appointment.class.getMethod(methodName, boolean.class);
                method.invoke(appointment, newValue);
                appointment.setId(id);
                as.modifyAppointment(appointment, option);
                System.out.println("appointment " + attributes[option - 1] + " set to: " + newValue);
            } else if (attributes[option - 1].equals("client")) {
                modifyClient(as, appointment);
            } else if (attributes[option - 1].equals("property")) {
                modifyProperty(as, appointment, client);
            }
            as.modifyAppointment(appointment, option);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Collection<Appointment> searchAppointmentClient(AppointmentService as, Client client) throws Exception {
        Appointment appointment = new Appointment();
        Collection<Appointment> appointments = null;
        try {
            appointments = as.searchAppointment(3, appointment).stream()
                    .filter(a -> a.getClient().getId() == client.getId())
                    .collect(Collectors.toList());

            if (appointments.isEmpty()) {
                System.out.println("You don't have any appointments assigned to you.");
                return null;
            }

        } catch (NoSuchElementException e) {
            System.out.println("Input was expected but not provided. Please try again.");
            sc.nextLine();
        }
        return appointments;
    }

    public void requestAppointmentClient(AppointmentService as, Client client) throws Exception {
        System.out.println("SCHEDULE APPOINTMENT");
        System.out.println("====================");

        try {
            System.out.println("Enter visit date (YYYY-MM-DD):");
            String visitDateInput = sc.next();

            LocalDate visitDate = LocalDate.parse(visitDateInput);
            System.out.println("Appointment scheduled for: " + visitDate);

            as.addAppointment(visitDate, client, addPropertyClient(client));
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please enter the date in the format YYYY-MM-DD.");
        } catch (Exception e) {
            System.out.println("Error scheduling appointment: " + e.getMessage());
        }
    }

    public Property addPropertyClient(Client client) throws Exception {
        System.out.println("What property do you want to visit?");

        Property property = null;
        AppProperty mProperty = new AppProperty();
        PropertyService ps = new PropertyService();

        try {
            Collection<Property> properties = mProperty.searchPropertyAppointment(ps).stream()
                    .filter(Property::isActive)
                    .collect(Collectors.toList());

            if (properties.isEmpty()) {
                System.out.println("There are no active properties available for visit.");
                return null;
            }

            System.out.println("Active properties available for visit:");
            for (Property p : properties) {
                System.out.println(p);
            }

            System.out.println("Enter the property ID:");
            int id = sc.nextInt();

            property = properties.stream()
                    .filter(p -> p.getId() == id)
                    .findFirst()
                    .orElse(null);

            if (property == null) {
                throw new Exception("Invalid property ID.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number.");
            sc.nextLine();
        } catch (NoSuchElementException e) {
            System.out.println("Input was expected but not provided. Please try again.");
            sc.nextLine();
        }

        return property;
    }

    public void viewAppointmentsClient(AppointmentService as, Client client) throws Exception {
        System.out.println("VIEW YOUR APPOINTMENTS");
        System.out.println("======================");

        try {
            Collection<Appointment> appointments = searchAppointmentClient(as, client);

            if (appointments.isEmpty()) {
                System.out.println("You have no appointments scheduled.");
            } else {
                System.out.println("Your appointments:");
                for (Appointment a : appointments) {
                    System.out.println(a);
                }
            }
        } catch (Exception e) {
            System.out.println("Error retrieving appointments: " + e.getMessage());
        }
    }

    public Client searchClient(AppointmentService as, Appointment appointment, Client user) {
        Client client = new Client();
        try {
            ClientDAO DaoClient = new ClientDAO();
            ClientService cs = new ClientService();

            Collection<Appointment> appointments = null;
            Collection<Client> clients = cs.searchClient(13, client);

            appointments = as.searchAppointment(4, appointment).stream()
                    .filter(c -> clients.stream().anyMatch(cl -> cl.getId() == c.getClient().getId())) 
                    .collect(Collectors.toList());

            appointments = appointments.stream()
                    .filter(c -> c.getProperty().getAgent().getId() == user.getId())
                    .collect(Collectors.toList());

            if (appointments.isEmpty()) {
                System.out.println("You don't have any appointments assigned to you.");
                return null;
            }

            for (Appointment a : appointments) {
                Client c = a.getClient();
                System.out.println(c);
            }

            System.out.println("Enter the id");
            int newId = sc.nextInt();
            String sql = "SELECT * FROM client WHERE ID = " + newId;
            client = DaoClient.returnClient(sql);
            if (client == null) {
                throw new Exception("You are not the agent for this property.");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return client;
    }

    public Property searchProperty(AppointmentService as, Appointment appointment, Client client) {
        Property property = new Property();
        try {
            PropertyDAO dao = new PropertyDAO();

            Collection<Appointment> appointments = null;

            appointments = as.searchAppointment(4, appointment).stream()
                    .filter(c -> c.getProperty().getAgent().getId() == client.getId())
                    .collect(Collectors.toList());

            if (appointments.isEmpty()) {
                System.out.println("You don't have any appointments assigned to you.");
                return null;
            }

            for (Appointment a : appointments) {
                Property p = a.getProperty();
                System.out.println(p);
            }
            System.out.println("Enter the id");
            int newId = sc.nextInt();
            String sql = "SELECT * FROM property WHERE ID = " + newId;
            property = dao.returnProperty(sql);
            if (property == null) {
                throw new Exception("You are not the agent for this property.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return property;
    }

    public void searchAppointment(AppointmentService as, Client user) throws Exception {
        System.out.println("SEARCH APPOINTMENT");
        System.out.println("==================");

        String[] attributes = { "id", "client", "property", "visitDate", "approved" };

        System.out.println("Enter the option you want to search");
        System.out.println("0. Id");
        int option = returnOption();

        if (option < 1 || option > attributes.length) {
            System.out.println("Invalid option.");
            return;
        }

        System.out.println("Enter the data for search" + attributes[option - 1] + ": ");
        String methodName = "set" + attributes[option - 1].substring(0, 1).toUpperCase()
                + attributes[option - 1].substring(1);

        Appointment appointment = new Appointment();
        try {
            Method method;
            Object newValue;
            if (attributes[option].equals("client")) {
                Client client = searchClient(as, appointment, user);
                appointment.setClient(client);
            } else if (attributes[option].equals("property")) {
                Property property = searchProperty(as, appointment, user);
                appointment.setProperty(property);
            } else if (attributes[option].equals("visitDate")) {
                String appointmentDateInput = sc.next();
                LocalDate appointmentDate = null;
                try {
                    appointmentDate = LocalDate.parse(appointmentDateInput);
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format. Please enter the date in the format YYYY-MM-DD.");
                    return;
                }
                method = Appointment.class.getMethod(methodName, LocalDate.class);
                newValue = appointmentDate;
                method.invoke(appointment, newValue);
            } else if (attributes[option].equals("approved")) {
                System.out.println("Enter: " +
                        "\n1. Find approved appointments" +
                        "\n2. Find unapproved appointments");
                int optionApproved = sc.nextInt();
                switch (optionApproved) {
                    case 1:
                        newValue = true;
                        break;
                    case 2:
                        newValue = false;
                        break;
                    default:
                        System.out.println("Invalid option");
                        newValue = false;
                        break;
                }
                method = Appointment.class.getMethod(methodName, boolean.class);
                method.invoke(appointment, newValue);
            } else {
                System.out.println("Enter the id to search");
                int id = sc.nextInt();
                appointment.setId(id);
            }
            as.printAppointment(option, appointment, user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteAppointment(AppointmentService as, Client client) throws Exception {
        System.out.println("DELETE APPOINTMENT");
        System.out.println("==================");

        AppointmentDAO dao = new AppointmentDAO();

        Appointment appointment = new Appointment();
        searchAppointment(as, client);
        System.out.println("Enter the id of the appointment you want to delete:");
        int id = sc.nextInt();

        String sql = "SELECT * FROM visitProperty WHERE ID = " + id;
        appointment = dao.returnAppointment(sql);

        if (appointment == null || !appointment.getClient().equals(client)) {
            System.out.println("You can only delete your own appointments.");
        } else {
            as.deleteAppointment(id);
            System.out.println("Appointment deleted successfully.");
        }
    }

}