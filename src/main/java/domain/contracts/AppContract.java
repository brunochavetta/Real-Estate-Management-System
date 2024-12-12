package domain.contracts;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.stream.Collectors;

import domain.agent.AppAppointment;
import domain.client.AppClient;
import domain.client.Client;
import domain.client.ClientDAO;
import domain.client.ClientService;
import domain.property.AppProperty;
import domain.property.Property;
import domain.property.PropertyDAO;
import domain.property.PropertyService;

public class AppContract {

    public Scanner sc = new Scanner(System.in);

    public AppAppointment mAppointment = new AppAppointment();

    public void app(Client client) throws Exception {

        ContractService contracts = new ContractService();

        int option;

        do {
            System.out.println("\n\nWelcome to Contract Management:");
            System.out.println("1. Add Contract");
            System.out.println("2. Modify Contract");
            System.out.println("3. Search Contracts");
            System.out.println("4. Delete Contract");
            System.out.println("5. Contract List");
            System.out.println("0. Exit");

            System.out.print("Enter an option: ");
            option = sc.nextInt();

            switch (option) {
                case 1:
                    addContract(contracts, client);
                    break;
                case 2:
                    modifyContract(contracts, client);
                    break;
                case 3:
                    searchContract(contracts, client);
                    break;
                case 4:
                    deleteContract(contracts, client);
                    break;
                case 5:
                    printContracts(contracts, client);
                    break;
                case 0:
                    System.out.println("Returning to main menu...");
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        } while (option != 0);
    }

    public void addContract(ContractService cs, Client user) {
        System.out.println("\n=== Add Contract ===");
        try {
            Client client = mAppointment.addClient();
            Property property = mAppointment.addProperty(user);

            System.out.print("Enter contract date (yyyy-mm-dd): ");
            String contractDateInput = sc.next();

            LocalDate contractDate = null;

            try {
                contractDate = LocalDate.parse(contractDateInput);
                System.out.println("Your date of contract is: " + contractDate);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please enter the date in the format YYYY-MM-DD.");
                return;
            }

            System.out.println("Enter the monthly fee");
            BigDecimal monthlyFee = sc.nextBigDecimal();

            modificarPropertyStatusNotAvailable(cs, user);
            cs.addContract(client, property, contractDate, monthlyFee);
            System.out.println("Contract added successfully.");
        } catch (Exception e) {
            System.out.println("Error adding contract: " + e.getMessage());
        }
    }

    public void modifyClient(ContractService contractServ, Contract contract) {
        try {
            Client client = contract.getClient();
            AppClient mClient = new AppClient();
            ClientService cs = new ClientService();

            System.out.println("Current client: " + client.printClient());
            System.out.println("Do you want to modify the current client or search for a new one?");
            System.out.println("1. Modify current client");
            System.out.println("2. Search for a new client");

            int choice = sc.nextInt();

            if (choice == 1) {
                System.out.println("Modifying current client...");
                mClient.developmentModifyClient(cs, client);
            } else if (choice == 2) {
                client = mAppointment.addClient();
                contract.setClient(client);
            } else {
                System.out.println("Invalid choice.");
                return;
            }

            contractServ.modifyContract(contract, 1);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void modificarPropertyStatusNotAvailable(ContractService cs, Client user) throws Exception {
        AppProperty mProperty = new AppProperty();
        PropertyService ps = new PropertyService();
        searchContract(cs, user);
        System.out.println("Enter the id");
        int id = sc.nextInt();
        String sql = "SELECT * FROM contract WHERE ID = " + id;
        Contract contract = cs.returnContract(sql);
        Property property = contract.getProperty();
        mProperty.modifyStatusAttribute(ps, property);
    }

    public void modificarPropertyStatusAvailable(Property property) throws Exception {
        PropertyService ps = new PropertyService();

        String currentStatus = property.getStatus();

        String cleanedStatus = currentStatus.replaceAll("not available", "");

        property.setStatus(cleanedStatus);
        ps.modifyProperty(property, 7);
    }

    public void modifyProperty(ContractService contractServ, Contract contract, Client client) {
        try {
            Property property = contract.getProperty();
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
                property = mAppointment.addProperty(client);
                contract.setProperty(property);
            } else {
                System.out.println("Invalid choice.");
                return;
            }

            contractServ.modifyContract(contract, 2);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void modifyContract(ContractService contracts, Client user) {
        System.out.println("\n=== Modify Contract ===");

        String[] attributes = { "client", "property", "contractDate", "monthlyFee" };

        System.out.println("Enter the option you want to modify:");
        for (int i = 0; i < attributes.length; i++) {
            System.out.println((i + 1) + ". " + attributes[i]);
        }

        int option = sc.nextInt();

        if (option < 1 || option > attributes.length) {
            System.out.println("Invalid option.");
            return;
        }

        try {
            searchContract(contracts, user);
            System.out.print("Enter the ID of the contract you want to modify: ");
            int id = sc.nextInt();

            String sql = "SELECT * FROM contract WHERE ID = " + id;
            Contract existingContract = contracts.returnContract(sql);

            if (existingContract == null) {
                System.out.println("No contract found with ID: " + id);
                return;
            }

            existingContract.setId(id);

            Method method;
            Object newValue;

            System.out.println("Enter the new data for " + attributes[option - 1] + ": ");
            String methodName = "set" + attributes[option - 1].substring(0, 1).toUpperCase()
                    + attributes[option - 1].substring(1);

            if (attributes[option - 1].equals("contractDate")) {
                String contractDateInput = sc.next();
                LocalDate contractDate = null;
                try {
                    contractDate = LocalDate.parse(contractDateInput);
                    System.out.println("New contract date: " + contractDate);
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format. Please enter the date in the format YYYY-MM-DD.");
                    return;
                }
                method = Contract.class.getMethod(methodName, LocalDate.class);
                newValue = contractDate;
                method.invoke(existingContract, newValue);
                contracts.modifyContract(existingContract, option);
                System.out.println("Contract " + attributes[option - 1] + " set to: " + newValue);
            } else if (attributes[option - 1].equals("property")) {
                modifyProperty(contracts, existingContract, user);
            } else if (attributes[option - 1].equals("client")) {
                modifyClient(contracts, existingContract);
            } else if (attributes[option - 1].equals("monthlyFee")) {
                Double newData = sc.nextDouble();
                method = Contract.class.getMethod(methodName, double.class);
                newValue = newData;
                method.invoke(existingContract, newValue);
                contracts.modifyContract(existingContract, option);
                System.out.println("Contract " + attributes[option - 1] + " set to: " + newValue);
            }

        } catch (Exception e) {
            System.out.println("Error modifying contract: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Collection<Contract> searchContractsClient(ContractService cs, Client client) throws Exception {
        Contract contract = new Contract();
        Collection<Contract> contracts = null;
        System.out.println("\n=== Search Contracts ===");

        try {
            contracts = cs.searchContracts(5, contract).stream()
                    .filter(c -> c.getClient().getId() == client.getId())
                    .collect(Collectors.toList());

            if (contracts.isEmpty()) {
                System.out.println("You don't have any contracts assigned to you.");
                return null;
            }

        } catch (NoSuchElementException e) {
            System.out.println("Input was expected but not provided. Please try again.");
            sc.nextLine();
        }
        return contracts;
    }

    public void viewContractsClient(ContractService cs, Client client) throws Exception {
        System.out.println("VIEW YOUR CONTRACTS");
        System.out.println("====================");

        try {
            Collection<Contract> contracts = searchContractsClient(cs, client);

            if (contracts.isEmpty()) {
                System.out.println("You have no contracts scheduled.");
            } else {
                System.out.println("Your contracts:");
                for (Contract c : contracts) {
                    System.out.println(c);
                }
            }
        } catch (Exception e) {
            System.out.println("Error retrieving contracts: " + e.getMessage());
        }
    }

    public Client searchClient(ContractService as, Contract contract, Client user) {
        Client client = new Client();
        try {
            ClientDAO DaoClient = new ClientDAO();
            ClientService cs = new ClientService();

            Collection<Contract> contracts = null;
            Collection<Client> clients = cs.searchClient(13, client);

            contracts = as.searchContracts(5, contract).stream()
                    .filter(c -> clients.stream().anyMatch(cl -> cl.getId() == c.getClient().getId()))
                    .collect(Collectors.toList());

            contracts = contracts.stream()
                    .filter(c -> c.getProperty().getAgent().getId() == user.getId())
                    .collect(Collectors.toList());

            if (contracts.isEmpty()) {
                System.out.println("You don't have any contracts assigned to you.");
                return null;
            }

            for (Contract a : contracts) {
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

    public Property searchProperty(ContractService cs, Contract contract, Client client) {
        Property property = new Property();
        try {
            PropertyDAO dao = new PropertyDAO();

            Collection<Contract> contracts = null;

            contracts = cs.searchContracts(5, contract).stream()
                    .filter(c -> c.getProperty().getAgent().getId() == client.getId())
                    .collect(Collectors.toList());

            if (contracts.isEmpty()) {
                System.out.println("You don't have any contracts assigned to you.");
                return null;
            }

            for (Contract a : contracts) {
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

    public void searchContract(ContractService cs, Client user) throws Exception {
        System.out.println("SEARCH CONTRACT");
        System.out.println("================");

        String[] attributes = { "id", "client", "property", "contractDate" };

        System.out.println("Enter the option you want to modify:");
        for (int i = 0; i < attributes.length; i++) {
            System.out.println((i) + ". " + attributes[i]);
        }

        int option = sc.nextInt();

        String methodName = "set" + attributes[option].substring(0, 1).toUpperCase() + attributes[option].substring(1);
        Method method;
        Object newValue;

        Contract contract = new Contract();
        Client client = null;
        try {
            if (attributes[option].equals("client")) {
                client = searchClient(cs, contract, user);
                contract.setClient(client);
            } else if (attributes[option].equals("property")) {
                Property property = searchProperty(cs, contract, user);
                contract.setProperty(property);
            } else if (attributes[option].equals("visitDate")) {
                String contractDateInput = sc.next();
                LocalDate contractDate = null;
                try {
                    contractDate = LocalDate.parse(contractDateInput);
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format. Please enter the date in the format YYYY-MM-DD.");
                    return;
                }
                method = Contract.class.getMethod(methodName, LocalDate.class);
                newValue = contractDate;
                method.invoke(contract, newValue);
            } else if (attributes[option].equals("monthlyFee")) {
                BigDecimal monthlyFee = sc.nextBigDecimal();
                newValue = monthlyFee;
                method = Contract.class.getMethod(methodName, BigDecimal.class);
                method.invoke(contract, newValue);
                cs.modifyContract(contract, option);
                System.out.println("Contract " + attributes[option] + " set to: " + newValue);
            } else {
                System.out.println("Enter the id to search");
                int id = sc.nextInt();
                contract.setId(id);
            }

            cs.printContracts(option, contract, user);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteContract(ContractService contracts, Client client) throws Exception {
        System.out.println("\n=== Delete Contract ===");

        ContractDAO dao = new ContractDAO();
        Contract contract = new Contract();
        searchContract(contracts, client);
        System.out.println("Enter the id of the contract you want to delete:");
        int id = sc.nextInt();

        String sql = "SELECT * FROM contract WHERE ID = " + id;
        contract = dao.returnContract(sql);

        modificarPropertyStatusAvailable(contract.getProperty());

        if (contract == null || !contract.getClient().equals(client)) {
            System.out.println("You can only delete your own contracts.");
        } else {
            contracts.deleteContract(id);
            System.out.println("Contracts deleted successfully.");
        }
        contracts.deleteContract(id);
    }

    public void printContracts(ContractService cs, Client client) throws Exception {
        Contract contract = new Contract();
        Collection<Contract> contracts = null;

        try {
            contracts = cs.searchContracts(5, contract).stream()
                    .filter(c -> c.getProperty().getAgent().getId() == client.getId())
                    .collect(Collectors.toList());

            if (contracts.isEmpty()) {
                System.out.println("You don't have any contracts assigned to you.");
            }

            for (Contract c : contracts) {
                System.out.println(c);
            }

        } catch (NoSuchElementException e) {
            System.out.println("Input was expected but not provided. Please try again.");
            sc.nextLine();
        }
    }
}