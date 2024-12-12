package domain.agent;

import domain.client.*;
import domain.property.AppProperty;
import domain.property.Property;
import domain.property.PropertyService;

import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.Collection;

public class AppAgent extends Client {

    private Scanner sc = new Scanner(System.in);

    public Client searchAgent(int option) throws Exception {
        Client agent = new Client();
        ClientService cs = new ClientService();
        AppClient mClient = new AppClient();
        String sql = "";

        ClientDAO clientDao = new ClientDAO();

        switch (option) {
            case 1:
                mClient.requestClientDetails(cs);
                sql = "SELECT * FROM client ORDER BY ID DESC LIMIT 1;";
                agent = clientDao.returnClient(sql);
                break;
            case 2:
                mClient.showFoundClients(cs);
                System.out.println("Enter the id of the agent assigned to the property");
                int idClient = sc.nextInt();
                sql = "SELECT * FROM client WHERE ID = " + idClient;
                agent = clientDao.returnClient(sql);
                break;
            case 3:
                Collection<Client> agents1 = cs.searchClient(13, agent).stream()
                        .filter(a -> a.isAgent() == true)
                        .collect(Collectors.toList());
                if (agents1.isEmpty()) {
                    System.out.println("There are no agents that match the search");
                } else {
                    for (Client a : agents1) {
                        System.out.println(a.printClient());
                    }
                    System.out.println("Enter the id of the agent assigned to the property");
                    int id = sc.nextInt();
                    sql = "SELECT * FROM client WHERE ID = " + id;
                    agent = clientDao.returnClient(sql);
                }
                break;
            case 4:
                Collection<Client> agents = mClient.searchClient(cs);
                if (agents.isEmpty()) {
                    throw new Exception("There are no agents to print");
                } else {
                    for (Client a : agents) {
                        if (a.isAgent()) {
                            System.out.println(a);
                        }
                    }
                }
                break;
            default:
                System.out.println("The option entered is not correct");

        }

        return agent;
    }

    public Client addAgentToProperty() throws Exception {
        ClientService cs = new ClientService();
        AppClient mClient = new AppClient();
        System.out.println("Enter: \n"
                + "1. If you want to create an agent\n"
                + "2. If you want to search for one in the customer list");
        int option = sc.nextInt();
        Client agent = searchAgent(option);
        mClient.modifyAgentAttribute(cs, agent, true);
        return agent;
    }

    public void modifyAgent(Client agent, Property property) throws Exception {
        ClientService cs = new ClientService();
        AppClient mClient = new AppClient();

        System.out.println("Enter: \n"
                + "1. If you want to modify data of the current agent\n"
                + "2. If you want to change agents");
        int optionAgent = sc.nextInt();

        switch (optionAgent) {
            case 1:
                mClient.developmentModifyClient(cs, agent);
                break;
            case 2:
                AppProperty mProperty = new AppProperty();
                PropertyService ps = new PropertyService();
                mProperty.modifyAgentAttribute(ps, agent, property);
                agent = searchAgent(2);
                break;
            default:
                System.out.println("Invalid option");
        }
    }

}