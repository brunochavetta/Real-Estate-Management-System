package domain.payment;

import java.util.Collection;
import java.util.Scanner;

import domain.client.Client;
import domain.client.ClientDAO;
import domain.contracts.Contract;
import domain.contracts.ContractDAO;

public class AppPayment {

    private Scanner sc = new Scanner(System.in);
    PaymentDAO dao = new PaymentDAO();

    public void app(Client client) throws Exception {

        PaymentService as = new PaymentService();
        int option;

        dao.updateDebtor(); 
        do {
            System.out.println("\n\n\"Welcome to payment management!:");
            System.out.println("1. Pay monthly fee \n"
                    + "2. Payment history \n"
                    + "0. Exit");

            System.out.print("Enter an option: ");
            option = sc.nextInt();

            switch (option) {
                case 1:
                    requestPayment(as, client);
                    break;
                case 2:
                    paymentHistory(as, client);
                    break;
                case 0:
                    System.out.println("Return main menu...");
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        } while (option != 0);
    }

    public void requestPayment(PaymentService ps, Client user) throws Exception {
        System.out.println("  PAY MONTHLY FEE");
        System.out.println("====================");

        try {
            String dni = user.getDni(); 

            ContractDAO cDAO = new ContractDAO();
            ClientDAO clDAO = new ClientDAO();
            String sqlDNI = "SELECT * FROM client WHERE dni = " + dni; 
            Client client = clDAO.returnClient(sqlDNI); 
            Contract contract = new Contract(); 
            contract.setClient(client);
            Collection<Contract> contracts = cDAO.listContracts(1, contract); 
            for (Contract contract2 : contracts) {
                System.out.println(contract2);
            }
            System.out.println("Enter the contract id: ");
            int id = sc.nextInt(); 
            String sqlClient = "SELECT * FROM contract WHERE ID = " + id; 
            contract = cDAO.returnContract(sqlClient); 

            System.out.println("Monthly fee value: $" + contract.getMonthlyFee());

            System.out.println("Pay?(y/n):");
            String paid = sc.next();

            if (paid.contains("y")) {
                try {
                    ps.addPayment(contract);
                    System.out.println("Your payment has been registered");
                } catch (Exception e) {
                    System.out.println("An error occurred while processing your payment. Please try again.");
                    System.out.println(e.getMessage());
                }
            } else if (paid.contains("n")) {
                System.out.println("Return menu...");
            } else {
                throw new Exception("Invalid input. Please enter 'yes' or 'no'.");
            }

        } catch (Exception e) {
            System.out.println("An error occurred while retrieving contract information. Please try again.");
            System.out.println(e.getMessage());
        }
    }

    public void paymentHistory(PaymentService ps, Client user) throws Exception {
        System.out.println(" PAYMENT HISTORY");
        System.out.println("==================");

        try {
            String dni = user.getDni();

            ContractDAO cDAO = new ContractDAO();
            ClientDAO clDAO = new ClientDAO();
            String sqlDNI = "SELECT * FROM client WHERE dni = " + dni; 
            Client client = clDAO.returnClient(sqlDNI); 
            Contract contract = new Contract(); 
            contract.setClient(client);
            Collection<Contract> contracts = cDAO.listContracts(1, contract); 
            for (Contract contract2 : contracts) {
                System.out.println(contract2);
            }
            System.out.println("Enter the contract id: ");
            int id = sc.nextInt(); 
            String sqlClient = "SELECT * FROM contract WHERE ID = " + id; 
            contract = cDAO.returnContract(sqlClient);

            ps.printPayment(contract);

        } catch (Exception e) {
            System.out.println("An error occurred while retrieving payment history. Please try again.");
            System.out.println(e.getMessage());
        }
    }
}
