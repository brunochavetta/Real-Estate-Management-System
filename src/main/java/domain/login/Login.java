package domain.login;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import domain.agent.AppAppointment;
import domain.agent.AppointmentService;
import domain.client.AppClient;
import domain.client.Client;
import domain.client.ClientDAO;
import domain.client.ClientService;
import domain.contracts.AppContract;
import domain.contracts.ContractService;
import domain.payment.AppPayment;
import domain.property.AppProperty;
import domain.property.PropertyService;

public class Login extends ClientDAO{
    private Client client; 
    private Scanner sc = new Scanner(System.in);

    public Login(){
    }

    public void app() throws Exception{
        int option = 0; 
        do {
            System.out.println("\n\nWelcome to the real estate management system!");
            System.out.println("1. Login");
            System.out.println("2. Signin");
            System.out.println("0. Exit");

            try {
                System.out.print("Enter an option: ");
                option = sc.nextInt();
                switch (option) {
                    case 1:
                        enterAccount();
                        break;
                    case 2:
                        signIn();
                        break;
                    case 0:
                        System.out.println("Thank you for choosing us!");
                        break;
                    default:
                        System.out.println("Invalid option. Try again.");
                }
                checkPermissions();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine(); 
            } catch (NoSuchElementException e) {
                System.out.println("Input was expected but not provided. Please try again.");
                sc.nextLine(); 
            }
        } while (option != 0);
        sc.close();
    }

    public void enterAccount() throws Exception{
        System.out.println("LOGIN");
        System.out.println("======");
        sc.nextLine(); 
        System.out.println("Enter your email"); 
        String email = sc.nextLine(); 

        String sql = "SELECT * FROM client WHERE email = '" + email + "'";
        client = super.returnClient(sql); 

        System.out.println("Enter your password"); 
        String password = sc.nextLine(); 

        if(client == null){
            throw new Exception("The email entered is not registered");
        }else if (!client.getPassword().equals(password)){
            throw new Exception("Invalid password");
        }
    }

    public void signIn() throws Exception{
        System.out.println("SIGNIN");
        System.out.println("======");

        AppClient mClient = new AppClient(); 
        ClientService cs = new ClientService(); 
        mClient.requestClientDetails(cs);

        System.out.println("Registered user successfully"); 
        enterAccount();
    }

    public void showAgentMenu() throws Exception{
        AppProperty mProperty = new AppProperty();
        AppClient mClient = new AppClient();
        AppContract mContract = new AppContract();
        AppAppointment mAppointment = new AppAppointment(); 
    
        int option = 0;

        do {
            System.out.println("\n\nMain menu:");
            System.out.println("1. Property management");
            System.out.println("2. Client management");
            System.out.println("3. Contract managment");
            System.out.println("4. Manage appointments");
            System.out.println("0. Exit");
            try {
                System.out.print("Enter an option: ");
                option = sc.nextInt();

                switch (option) {
                    case 1:
                        mProperty.app();
                        break;
                    case 2:
                        mClient.app();
                        break;
                    case 3:
                        mContract.app(client);
                        break;
                    case 4: 
                        mAppointment.app(client);
                        break; 
                    case 0:
                        System.out.println("Exit program...");
                        client = null; 
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

    public void showClientMenu() throws Exception{
        AppProperty mProperty = new AppProperty();
        PropertyService ps = new PropertyService();
        AppClient mClient = new AppClient();
        ClientService cs = new ClientService(); 
        AppAppointment mAppointment = new AppAppointment(); 
        AppointmentService as = new AppointmentService(); 
        AppContract mContract = new AppContract(); 
        ContractService cService = new ContractService(); 
        AppPayment mPayment = new AppPayment(); 
    
        int option = 0;

        do {
            System.out.println("\n\nMain menu:");
            System.out.println("1. Search property");
            System.out.println("2. Modify my account");
            System.out.println("3. Schedule Appointment");
            System.out.println("4. See my Appointment");
            System.out.println("5. See my contracts");
            System.out.println("6. Manage my payments");
            System.out.println("0. Exit");
            try {
                System.out.print("Enter an option: ");
                option = sc.nextInt();

                switch (option) {
                    case 1: 
                        mProperty.showFoundProperties(ps);
                        break;
                    case 2:
                        mClient.developmentModifyClient(cs, client);
                        break;
                    case 3: 
                        mAppointment.requestAppointmentClient(as, client); 
                        break; 
                    case 4:
                        mAppointment.viewAppointmentsClient(as, client);
                        break;
                    case 5: 
                        mContract.viewContractsClient(cService, client); 
                        break; 
                    case 6: 
                        mPayment.app(client);
                        break; 
                    case 0:
                        System.out.println("Exit program...");
                        client = null; 
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

    public void checkPermissions() throws Exception{
        if(client != null){
            if(client.isAgent()){
                showAgentMenu();
            }else{
                showClientMenu();
            }
        } else {
            System.out.println("You must log in with your account before"); 
        }
    }
    
}