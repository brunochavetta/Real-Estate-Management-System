package domain.property;

import domain.agent.AppAgent;
import domain.client.AppClient;
import domain.client.Client;
import domain.client.ClientService;
import domain.ownerProperty.AppOwnerProperty;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Collection;

public class AppProperty {

    private Scanner sc = new Scanner(System.in);

    public void app() throws Exception {
        PropertyService ps = new PropertyService();

        int option = 0;

        do {
            System.out.println("\n\n\"Welcome to property management!:");
            System.out.println("1. Add property");
            System.out.println("2. Modify property");
            System.out.println("3. Search property");
            System.out.println("4. Delete property");
            System.out.println("5. Manage owner");
            System.out.println("6. View registered agents");
            System.out.println("0. Exit");

            try {
                System.out.print("Enter an option: ");
                option = sc.nextInt();

                switch (option) {
                    case 1:
                        requestPropertyDetails(ps);
                        break;
                    case 2:
                        modifyProperty(ps);
                        break;
                    case 3:
                        showFoundProperties(ps);
                        break;
                    case 4:
                        deleteProperty(ps);
                        break;
                    case 5:
                        AppOwnerProperty mOwner = new AppOwnerProperty();
                        mOwner.app();
                    case 6:
                        AppAgent mAgent = new AppAgent();
                        mAgent.searchAgent(4);
                    case 0:
                        System.out.println("Return main menu...");
                        break;
                    default:
                        System.out.println("invalid option. Try again.");
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
        System.out.println("1. Name \n" + "2. Adress \n" + "3. Description \n"
                + "4. Photo \n" + "5. Active \n" + "6. Value \n"
                + "7. Status \n" + "8. Agent");
        int option = sc.nextInt();

        return option;
    }

    public void requestPropertyDetails(PropertyService ps) throws Exception {
        System.out.println("ADD PROPERTY");
        System.out.println("============");

        sc.nextLine();
        System.out.println("Enter name:");
        String name = sc.nextLine();

        System.out.println("Enter address:");
        String address = sc.nextLine();

        System.out.println("Enter description:");
        String description = sc.nextLine();

        System.out.println("Enter the link of the photo:");
        String photo = sc.nextLine();

        System.out.println("Enter your value:");
        BigDecimal value = sc.nextBigDecimal();
        sc.nextLine();

        System.out.println("Enter your status");
        String status = sc.nextLine();

        AppAgent mAgent = new AppAgent();
        Client agent = mAgent.addAgentToProperty();

        ps.addProperty(name, address, description, photo, value, status, agent);
    }

    public void developmentModifyProperty(PropertyService ps, Property property) throws Exception {
        System.out.println("Enter the option you want to modify");
        int option = returnOption();

        String[] attributes = { "name", "address", "description", "photo", "active",
                "value", "status", "agent" };

        System.out.println("Enter the new data: ");
        try {
            String methodName = "set" + attributes[option - 1].substring(0, 1).toUpperCase()
                    + attributes[option - 1].substring(1);

            Method method = null;
            Object newValue = null;

            if (attributes[option - 1].equals("active")) {
                boolean active = sc.nextBoolean();
                method = Property.class.getMethod(methodName, boolean.class);
                newValue = active;
            } else if (attributes[option - 1].equals("agent")) {
                AppAgent mAgent = new AppAgent();
                Client agent = new Client();
                agent = property.getAgent();
                mAgent.modifyAgent(agent, property);
            } else if (attributes[option - 1].equals("value")) {
                BigDecimal value = sc.nextBigDecimal();
                method = Property.class.getMethod(methodName, BigDecimal.class);
                newValue = value;
            } else {
                sc.nextLine(); // Clear buffer
                String newData = sc.nextLine();
                method = Property.class.getMethod(methodName, String.class);
                newValue = newData;
            }

            method.invoke(property, newValue);

            ps.modifyProperty(property, option);

            System.out.println("Property " + attributes[option - 1] + " set to: " + newValue);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void modifyAgentAttribute(PropertyService ps, Client agent, Property property) throws Exception {
        if (property.getAgent().equals(agent)) {
            ClientService cs = new ClientService();
            AppClient mClient = new AppClient();
            mClient.modifyAgentAttribute(cs, agent, false);
        }
    }

    public void modifyStatusAttribute(PropertyService ps, Property property) throws Exception {
        property.setStatus(property.getStatus() + " not available");
        ps.modifyProperty(property, 7);
    }

    public void modifyProperty(PropertyService ps) throws Exception {
        System.out.println("MODIFY PROPERTY");
        System.out.println("===============");

        Property property = new Property();

        showFoundProperties(ps);
        System.out.println("Enter the id of the property you want to modify:");
        int id = sc.nextInt();
        property.setId(id);

        developmentModifyProperty(ps, property);
    }

    public Collection<Property> searchProperty(PropertyService ps) throws Exception {
        System.out.println("SEARCH PROPERTY");
        System.out.println("===============");

        System.out.println("Enter the option you want to search");
        System.out.println("0. Id");
        int option = returnOption();

        String[] attributes = { "id", "name", "address", "description", "photo", "active",
                "value", "status", "agent" };

        Property property = new Property();
        Collection<Property> properties = null;

        System.out.println("Enter the phrase, word or number you want to search by: ");
        try {
            String methodName = "set" + attributes[option].substring(0, 1).toUpperCase()
                    + attributes[option].substring(1);

            Method method;
            Object newValue;

            if (attributes[option].equals("active")) {
                boolean active = sc.nextBoolean();
                method = Property.class.getMethod(methodName, boolean.class);
                newValue = active;
            } else if (attributes[option].equals("id")) {
                int id = sc.nextInt();
                method = Property.class.getMethod(methodName, int.class);
                newValue = id;
            } else if (attributes[option].equals("agent")) {
                AppAgent mAgent = new AppAgent();
                method = Property.class.getMethod(methodName, Client.class);
                newValue = mAgent.searchAgent(3);
            } else if (attributes[option].equals("value")) {
                BigDecimal value = sc.nextBigDecimal();
                method = Property.class.getMethod(methodName, BigDecimal.class);
                newValue = value;
            } else {
                sc.nextLine(); // Clear buffer
                String newData = sc.nextLine();
                method = Property.class.getMethod(methodName, String.class);
                newValue = newData;
            }

            method.invoke(property, newValue);

            properties = ps.searchProperty(option, property);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return properties;
    }

    public Collection<Property> searchPropertyAppointment(PropertyService ps) throws Exception {
        System.out.println("SEARCH PROPERTY");
        System.out.println("===============");

        System.out.println("Enter the option you want to search");
        System.out.println("0. Id");
        System.out.println("1. Name \n" + "2. Adress \n" + "3. Description \n"
                + "4. Photo \n" + "5. Active \n" + "6. Value \n"
                + "7. Status");
        int option = sc.nextInt();

        String[] attributes = { "id", "name", "address", "description", "photo", "active",
                "value", "status" };

        Property property = new Property();
        Collection<Property> properties = null;

        System.out.println("Enter the phrase, word or number you want to search by: ");
        try {
            String methodName = "set" + attributes[option].substring(0, 1).toUpperCase()
                    + attributes[option].substring(1);

            Method method;
            Object newValue;

            if (attributes[option].equals("active")) {
                boolean active = sc.nextBoolean();
                method = Property.class.getMethod(methodName, boolean.class);
                newValue = active;
            } else if (attributes[option].equals("id")) {
                int id = sc.nextInt();
                method = Property.class.getMethod(methodName, int.class);
                newValue = id;
            } else if (attributes[option].equals("value")) {
                BigDecimal value = sc.nextBigDecimal();
                method = Property.class.getMethod(methodName, BigDecimal.class);
                newValue = value;
            } else {
                sc.nextLine(); // Clear buffer
                String newData = sc.nextLine();
                method = Property.class.getMethod(methodName, String.class);
                newValue = newData;
            }

            method.invoke(property, newValue);

            properties = ps.searchProperty(option, property);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return properties;
    }

    public void showFoundProperties(PropertyService ps) throws Exception {
        Collection<Property> properties = searchProperty(ps);
        if (properties.isEmpty()) {
            throw new Exception("There are no properties to print");
        } else {
            for (Property p : properties) {
                System.out.println(p);

            }
        }
    }

    public void deleteProperty(PropertyService ps) throws Exception {
        System.out.println("DELETE PROPERTY");
        System.out.println("===============");

        showFoundProperties(ps);

        Property property = new Property();
        System.out.println("Enter the id of the property you want to delete:");
        int id = sc.nextInt();
        property.setId(id);

        ps.deleteProperty(id);
    }
}