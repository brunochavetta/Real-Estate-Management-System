package domain.client;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Collection;

public class AppClient {

    private Scanner sc = new Scanner(System.in);

    public void app() throws Exception {
        ClientService cs = new ClientService();

        int option = 0;

        do {
            System.out.println("\n\nWelcome to client management!");
            System.out.println("1. Add Client");
            System.out.println("2. Modify Client");
            System.out.println("3. Search Client");
            System.out.println("4. Delete Client");
            System.out.println("0. Exit");

            try {
                System.out.print("Enter an option: ");
                option = sc.nextInt();
                switch (option) {
                    case 1:
                        requestClientDetails(cs);
                        break;
                    case 2:
                        modifyClient(cs);
                        break;
                    case 3:
                        showFoundClients(cs);
                        break;
                    case 4:
                        deleteClient(cs);
                        break;
                    case 0:
                        System.out.println("Exit program...");
                        break;
                    default:
                        System.out.println("Invalid option. Try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine(); 
            } catch (NoSuchElementException e) {
                System.out.println("Input was expected but not provided. Please try again.");
                sc.nextLine(); 
            }
        } while (option != 0);
    }

    public int returnOption() {
        System.out.println("1. Name \n" + "2. Last name \n" + "3. Photo \n"
                + "4. Gender \n" + "5. Active \n" + "6. Birth date \n"
                + "7. Email \n" + "8. Phone \n" + "9. Password \n" + "10. DNI");
        int option = sc.nextInt();

        return option;
    }

    public void requestClientDetails(ClientService cs) throws Exception {
        System.out.println("ADD CLIENT");
        System.out.println("==========");
        
        sc.nextLine(); 
        System.out.println("Enter name:");
        String name = sc.nextLine();

        System.out.println("Enter last name:");
        String lastName = sc.nextLine();

        System.out.println("Enter the link of the photo:");
        String photo = sc.nextLine();

        System.out.println("Enter your gender: \n"
                + "1. Female \n" + "2. Male");
        int gender = sc.nextInt();
        sc.nextLine();

        System.out.println("Enter your date of birth (YYYY-MM-DD):");
        String birthDateInput = sc.next();

        LocalDate birthDate = null;

        try {
            birthDate = LocalDate.parse(birthDateInput);
            System.out.println("Your date of birth is: " + birthDate);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please enter the date in the format YYYY-MM-DD.");
        }

        System.out.println("Enter your email:");
        String email = sc.next();

        System.out.println("Enter your phone:");
        String phone = sc.next();

        System.out.println("Enter your password:");
        String password = sc.next();

        System.out.println("Enter your dni:");
        String dni = sc.next();

        sc.nextLine();

        cs.addClient(name, lastName, photo, gender, birthDate, email, phone, password, dni);
    }

    public void developmentModifyClient(ClientService cs, Client client) throws Exception {
        System.out.println("Enter the option you want to modify");
        int option = returnOption();

        String[] attributes = {"name", "lastName", "photo", "gender", "active",
            "birthDate", "email", "phone", "password", "dni"};

        System.out.println("Enter the new data: ");

        try {
            String methodName = "set" + attributes[option - 1].substring(0, 1).toUpperCase()
                    + attributes[option - 1].substring(1);

            Method method;
            Object newValue;

            if (attributes[option - 1].equals("active")) {
                boolean active = sc.nextBoolean();
                method = Client.class.getMethod(methodName, boolean.class);
                newValue = active;
            } else if (attributes[option - 1].equals("birthDate")) {
                String birthDateInput = sc.next();
                sc.nextLine();
                LocalDate birthDate = null;
                try {
                    birthDate = LocalDate.parse(birthDateInput);
                    System.out.println("Your date of birth is: " + birthDate);
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format. Please enter the date in the format YYYY-MM-DD.");
                }
                method = Client.class.getMethod(methodName, LocalDate.class);
                newValue = birthDate;
            } else {
                sc.nextLine(); // Clear buffer
                String newData = sc.nextLine();
                method = Client.class.getMethod(methodName, String.class);
                newValue = newData;
            }

            method.invoke(client, newValue);

            cs.modifyClient(client, option);

            System.out.println("Client " + attributes[option - 1] + " set to: " + newValue);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void modifyAgentAttribute(ClientService cs, Client client, boolean status) throws Exception{
        client.setAgent(status);
        cs.modifyClient(client,10); 
    }

    public void modifyOwnerAttribute(ClientService cs, Client client, boolean status) throws Exception{
        try {
            client.setOwner(status);
            cs.modifyClient(client,11);
        }catch(Exception e){
            throw new Exception("Error modifying client attribute: " + e.getMessage());
        }
    }

    public void modifyClient(ClientService cs) throws Exception {
        System.out.println("MODIFY CLIENT");
        System.out.println("==============");

        Client client = new Client();

        showFoundClients(cs);
        System.out.println("Enter the id of the client you want to modify:");
        int id = sc.nextInt();
        client.setId(id);

        developmentModifyClient(cs, client);
    }

    public Collection<Client> searchClient(ClientService cs) throws Exception {
        System.out.println("SEARCH CLIENT");
        System.out.println("=============");
    
        System.out.println("Enter the option you want to search");
        System.out.println("0. Id");
        int option = returnOption();
    
        String[] attributes = {"id", "name", "lastName", "photo", "gender", "active",
            "birthDate", "email", "phone", "password", "dni"};
    
        Client client = new Client();
        Collection<Client> clients = null; 
    
        System.out.println("Enter the new data: ");
        try {
            String methodName = "set" + attributes[option].substring(0, 1).toUpperCase()
                    + attributes[option].substring(1);
    
            Method method;
            Object newValue;
    
            switch (attributes[option]) {
                case "active":
                    boolean active = sc.nextBoolean();
                    method = Client.class.getMethod(methodName, boolean.class);
                    newValue = active;
                    break;
                case "id":
                    int id = sc.nextInt();
                    method = Client.class.getMethod(methodName, int.class);
                    newValue = id;
                    break;
                case "birthDate":
                    String birthDateInput = sc.next();
                    LocalDate birthDate = null;
                    try {
                        birthDate = LocalDate.parse(birthDateInput);
                        System.out.println("Your date of birth is: " + birthDate);
                    } catch (DateTimeParseException e) {
                        System.out.println("Invalid date format. Please enter the date in the format YYYY-MM-DD.");
                    }
                    method = Client.class.getMethod(methodName, LocalDate.class);
                    newValue = birthDate;
                    break;
                default:
                    sc.nextLine(); // Limpiar el buffer
                    String newData = sc.nextLine();
                    method = Client.class.getMethod(methodName, String.class);
                    newValue = newData;
                    break;
            }
    
            method.invoke(client, newValue);
    
            clients = cs.searchClient(option, client);
        
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        return clients; 
    }
    
    public void showFoundClients(ClientService cs) throws Exception {
        Collection<Client> clients = searchClient(cs);
        if (clients == null || clients.isEmpty()) {
            throw new Exception("There are no clients to print");
        } else {
            for (Client c : clients) {
                System.out.println(c);
            }
        }
    }

    public void deleteClient(ClientService cs) throws Exception {
        System.out.println("DELETE CLIENT");
        System.out.println("=============");

        showFoundClients(cs);

        showFoundClients(cs);

        Client client = new Client();
        System.out.println("Enter the id of the client you want to delete:");
        int id = sc.nextInt();
        sc.nextLine();
        client.setId(id);

        cs.deleteClient(id);
    }
}