package domain.client;

import java.time.LocalDate;
import java.util.Collection;

public class ClientService {

    private ClientDAO dao;

    public ClientService() {
        dao = new ClientDAO();
    }

    public void addClient(String name, String lastName, String photo,
            int genderOption, LocalDate birthDate, String email, String phone,
            String password, String dni) throws Exception {
        System.out.println("Principio del servicee");
        try {
            if (name == null || name.trim().isEmpty()) {
                throw new Exception("You must indicate the name");
            }
            if (lastName == null || lastName.trim().isEmpty()) {
                throw new Exception("You must indicate the lastName");
            }
            if (photo == null || photo.trim().isEmpty()) {
                throw new Exception("You must indicate the photo");
            }
            if (genderOption == 0) {
                throw new Exception("You must indicate a gender");
            }
            if (birthDate == null) {
                throw new Exception("You must indicate a birthDate");
            }
            if (dni == null || dni.trim().isEmpty()) {
                throw new Exception("You must indicate your dni");
            }
            if (email == null || email.trim().isEmpty()) {
                throw new Exception("You must indicate a email");
            }
            if (phone == null || phone.trim().isEmpty()) {
                throw new Exception("You must indicate a phone");
            }
            if (password == null || password.trim().isEmpty()) {
                throw new Exception("You must indicate a password");
            }
            if (!isValidEmail(email)) {
                throw new Exception("Invalid email format.");
            }

            if (!isValidPhone(phone)) {
                throw new Exception("Invalid phone format. Must be 10 digits");
            }

            if (!isValidPassword(password)) {
                throw new Exception(
                        "Invalid password format. Minimum of 8 characters, one uppercase letter, one lowercase letter and one number");
            }

            String gender = "";

            switch (genderOption) {
                case 1:
                    gender = "F";
                    break;
                case 2:
                    gender = "M";
                    break;
                default:
                    throw new Exception("You must indicate a valid option for gender");
            }

            Client client = new Client(name, lastName, photo,
                    gender, true, birthDate, email, phone, password, dni);
            System.out.println(client);
            dao.saveClient(client);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    public static boolean isValidPhone(String phone) {
        String phoneRegex = "\\d{10}";
        return phone.matches(phoneRegex);
    }

    public static boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
        return password.matches(passwordRegex);
    }

    public void modifyClient(Client client, int optionChange) throws Exception {

        try {
            if (client == null) {
                throw new Exception("You must indicate a client");
            }

            switch (optionChange) {
                case 7:
                    if (!isValidEmail(client.getEmail())) {
                        throw new Exception("Invalid email format.");
                    }
                    break;
                case 8:
                    if (!isValidPhone(client.getPhone())) {
                        throw new Exception("Invalid phone format. Must be 10 digits");
                    }
                    break;
                case 9:
                    if (!isValidPassword(client.getPassword())) {
                        throw new Exception("Invalid password format. Minimum of 8 characters, one uppercase letter, one lowercase letter and one number");
                    }
                    break;
            }
            dao.modifyClient(client, optionChange);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Collection<Client> searchClient(int optionChange, Client clientAux) throws Exception {
        Collection<Client> clients = null; 
        try {
            if (clientAux == null) {
                throw new Exception("You must indicate a client");
            }

            clients = dao.listClient(optionChange, clientAux);

        } catch (Exception e) {
            System.out.println(e);
        }
        return clients;
    }

    public void deleteClient(int ID) throws Exception {
        try {
            if (ID == 0) {
                throw new Exception("You must provide an ID");
            }
            dao.deleteClient(ID);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}