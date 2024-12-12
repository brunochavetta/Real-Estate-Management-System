package domain.ownerProperty;

import domain.client.*;
import domain.property.*;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Collection;

public class AppOwnerProperty {

    private Scanner sc = new Scanner(System.in);

    public void app() throws Exception {

        int option = 0;
        OwnerPropertyService ops = new OwnerPropertyService();

        do {
            System.out.println("\n\nWelcome to Property Owner Management!");

            try {
                System.out.println("1. Add property owner \n"
                    + "2. Modify property owner \n"
                    + "3. Search property owner \n"
                    + "4. Delete property owner \n"
                    + "0. Exit");
                option = sc.nextInt();

                switch (option) {
                    case 1:
                        requestOwnerPropertyDetails(ops);
                        break;
                    case 2:
                        modifyOwnerProperty(ops);
                        break;
                    case 3:
                        searchOwnerProperty(ops);
                        break;
                    case 4:
                        deleteOwnerProperty(ops);
                        break;
                    case 0:
                        System.out.println("Return main menu...");
                        break;
                    default:
                        System.out.println("You must enter a valid option");
                        break;
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
        System.out.println("1. Owner \n" + "2. Property");
        int option = sc.nextInt();

        return option;
    }

    public Client addOwner() throws Exception {
        System.out.println("Enter: \n"
                + "1. If you want to create a new owner\n"
                + "2. If you want to add an existing one");
        int optionOwner = sc.nextInt();
    
        Client owner = null; 
        AppClient mClient = new AppClient();
        ClientService cs = new ClientService();
        ClientDAO dao = new ClientDAO();
    
        String sql = "";
    
        try {
            switch (optionOwner) {
                case 1:
                    mClient.requestClientDetails(cs);
                    sql = "SELECT * FROM client ORDER BY ID DESC LIMIT 1;";
                    break;
                case 2:
                    mClient.showFoundClients(cs);
                    System.out.println("Enter the property owner ID");
                    int id = sc.nextInt();
                    sql = "SELECT * FROM client WHERE ID = " + id;
                    break;
                default:
                    System.out.println("Invalid option. Please enter 1 or 2.");
                    break;
            }
    
            if (owner == null) {
                owner = dao.returnClient(sql);
                mClient.modifyOwnerAttribute(cs, owner, true);
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number.");
            sc.nextLine(); 
        } catch (NoSuchElementException e) {
            System.out.println("Input was expected but not provided. Please try again.");
            sc.nextLine(); 
        }
    
        return owner;
    }
    

    public Property addProperty() throws Exception {
        System.out.println("Enter: \n"
                + "1. If you want to create a new property\n"
                + "2. If you want to add an existing one");
        int optionProperty = sc.nextInt();
        sc.nextLine();

        Property property = null; 
        AppProperty mProperty = new AppProperty();
        PropertyService ps = new PropertyService();
        PropertyDAO dao = new PropertyDAO();

        String sql = "";

        try {
            switch (optionProperty) {
                case 1:
                    mProperty.requestPropertyDetails(ps);
                    sql = "SELECT * FROM property ORDER BY ID DESC LIMIT 1;";
                    break;
                case 2:
                    mProperty.showFoundProperties(ps);
                    System.out.println("Enter the property ID");
                    int id = sc.nextInt();
                    sql = "SELECT * FROM property WHERE ID = " + id;
                    break;
            }
            property = dao.returnProperty(sql);
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number.");
            sc.nextLine(); 
        } catch (NoSuchElementException e) {
            System.out.println("Input was expected but not provided. Please try again.");
            sc.nextLine(); 
        }
        return property;
    }

    public void requestOwnerPropertyDetails(OwnerPropertyService ps) throws Exception {
        System.out.println("ADD PROPERTY OWNER");
        System.out.println("==================");

        ps.addOwnerProperty(addOwner(), addProperty());
    }

    public void modifyOwner(OwnerPropertyService ops, OwnerProperty ownerProperty){
        try {
            AppClient mClient = new AppClient();
            ClientService cs = new ClientService();
            Client owner = new Client();
            ClientDAO dao = new ClientDAO();
            boolean foundAny = false; 
            Collection<Client> owners = mClient.searchClient(cs);
            if (owners.isEmpty()) {
                throw new Exception("There are no owners to print");
            } else {
                for (Client o : owners) {
                    if(o.isOwner()){
                        System.out.println(o);
                        foundAny = true;
                    }
                }
                if (!foundAny) {
                    System.out.println("No common owners were found.");
                }
            }
            System.out.println("Enter the id");
            int newId = sc.nextInt();
            String sql = "SELECT * FROM client WHERE ID = " + newId;
            owner = dao.returnClient(sql);
            ownerProperty.setOwner(owner);
            ops.modifyOwnerProperty(ownerProperty, 1);
        } catch (Exception e) {
            System.out.println(e); 
        }
    }

    public void modifyProperty(OwnerPropertyService ops, OwnerProperty ownerProperty){
        try {
            Property property = new Property();
            PropertyDAO dao = new PropertyDAO();
            PropertyService ps = new PropertyService();
            AppProperty mProperty = new AppProperty();
            Collection<Property> properties = mProperty.searchProperty(ps);
            if (properties.isEmpty()) {
                throw new Exception("There are no properties to print");
            } else {
                boolean foundAny = false; 
                Collection<OwnerProperty> ownerProperties = ops.searchOwnerProperty(3, ownerProperty);
                for (OwnerProperty op : ownerProperties) {
                    Property p = op.getProperty(); 
                    if (properties.contains(p)) {
                        System.out.println(p);
                        foundAny = true;
                    }
                }
                if (!foundAny) {
                    System.out.println("No common properties were found.");
                }
            }
            System.out.println("Enter the id you want to change the current one to");
            int propertyID = sc.nextInt();
            String sql = "SELECT * FROM property WHERE ID = " + propertyID;
            property = dao.returnProperty(sql);
            ownerProperty.setProperty(property);
            ops.modifyOwnerProperty(ownerProperty, 2);
        } catch (Exception e) {
            System.out.println(e); 
        }
    }

    public void modifyOwnerProperty(OwnerPropertyService ops) throws Exception {
        System.out.println("MODIFY PROPERTY OWNER");
        System.out.println("=====================");

        System.out.println("Enter the option you want to modify");
        int option = returnOption();

        String[] attributes = { "owner", "property" };

        OwnerProperty ownerProperty = new OwnerProperty();

        searchOwnerProperty(ops); 
        System.out.println("Enter the id of the property owner you want to modify:");
        int id = sc.nextInt();
        ownerProperty.setId(id);
        try {
            if (attributes[option - 1].equals("owner")) {
                modifyOwner(ops, ownerProperty);
            } else if (attributes[option - 1].equals("property")) {
                modifyProperty(ops, ownerProperty);
            }
            ops.modifyOwnerProperty(ownerProperty, option);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Client searchOwner(){
        Client owner = new Client();
        try{
            AppClient mClient = new AppClient();
            ClientService cs = new ClientService();
            ClientDAO DaoClient = new ClientDAO();

            Collection<Client> owners = mClient.searchClient(cs);
            if (owners.isEmpty()) {
                throw new Exception("There are no owners to print");
            } else {
                boolean foundAny = false; 
                for (Client o : owners) {
                    if(o.isOwner()){
                        System.out.println(o);
                        foundAny = true;
                    }
                }
                if (!foundAny) {
                    System.out.println("No common owners were found.");
                }
            }

            System.out.println("Enter the id");
            int newId = sc.nextInt();
            String sql = "SELECT * FROM client WHERE ID = " + newId;
            owner = DaoClient.returnClient(sql);
        }catch(Exception e){
            System.out.println(e); 
        }
        return owner;
    } 

    public Property searchProperty(OwnerPropertyService ops, OwnerProperty ownerProperty){
        Property property = new Property(); 
        try{
            AppProperty mProperty = new AppProperty();
            PropertyService cs = new PropertyService();
            PropertyDAO dao = new PropertyDAO();

            Collection<Property> properties = mProperty.searchProperty(cs);
            if (properties.isEmpty()) {
                throw new Exception("There are no properties to print");
            } else {
                Collection<OwnerProperty> ownerProperties = ops.searchOwnerProperty(3, ownerProperty);
                boolean foundAny = false; 
                for (OwnerProperty op : ownerProperties) {
                    Property p = op.getProperty(); 
                    if (properties.contains(p)) {
                        System.out.println(p);
                        foundAny = true;
                    }
                }
                if (!foundAny) {
                    System.out.println("No common properties were found.");
                }
            }
            System.out.println("Enter the id");
            int newId = sc.nextInt();
            String sql = "SELECT * FROM property WHERE ID = " + newId;
            property = dao.returnProperty(sql); 
        } catch(Exception e){
            System.out.println(e); 
        }
        return property;
    }

    public void searchOwnerProperty(OwnerPropertyService ops) throws Exception {
        System.out.println("SEARCH PROPERTY OWNER");
        System.out.println("=====================");

        System.out.println("Enter the option you want to search");
        System.out.println("0. Id");
        int option = returnOption();

        String[] attributes = { "id", "owner", "property" };

        OwnerProperty ownerProperty = new OwnerProperty();

        try {
            if (attributes[option].equals("owner")) {
                Client owner = searchOwner(); 
                ownerProperty.setOwner(owner);
            } else if (attributes[option].equals("property")) {
                Property property = searchProperty(ops, ownerProperty); 
                ownerProperty.setProperty(property);
            } else {
                System.out.println("Enter the id to search");
                int id = sc.nextInt();
                ownerProperty.setId(id);
            }
            ops.printOwnerPropiety(option, ownerProperty);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteOwnerProperty(OwnerPropertyService ops) throws Exception {
        System.out.println("DELETE PROPERTY OWNER");
        System.out.println("=====================");

        OwnerProperty ownerProperty = new OwnerProperty();
        searchOwnerProperty(ops);
        System.out.println("Enter the id of the property owner you want to delete:");
        int id = sc.nextInt();
        ownerProperty.setId(id);
        AppClient mClient = new AppClient(); 
        ClientService cs = new ClientService(); 
        mClient.modifyOwnerAttribute(cs, ownerProperty.getOwner(), false);
        ops.deleteOwnerProperty(id);
    }
}
