package domain.client;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import persistence.DAO;

public class ClientDAO extends DAO {

    public void saveClient(Client client) throws Exception {
        try {
            if (client == null) {
                throw new Exception("You must indicate a client");
            }
    
            int activeValue = client.isActive() ? 1 : 0;
            int agentValue = client.isAgent() ? 1 : 0;
            int ownerValue = client.isOwner() ? 1 : 0;
    
            String sql = "INSERT INTO client (name, lastName, photo, gender, active, birthDate, email, phone, dni, password, agent, owner) "
                       + "VALUES ('" + client.getName() + "', '" + client.getLastName() + "', '"
                       + client.getPhoto() + "', '" + client.getGender() + "', "
                       + activeValue + ", '" + client.getBirthDate() + "', '"
                       + client.getEmail() + "', '" + client.getPhone() + "', '"
                       + client.getDni() + "', '" + client.getPassword() + "', "
                       + agentValue + ", " + ownerValue + " )";
    
            insertModifyDelete(sql);
    
        } catch (Exception e) {
            System.out.println(e);
            throw new Exception("Error saving client: " + e.getMessage());
        }
    }    

    public void modifyClient(Client client, int optionChange) throws Exception {
        try {
            if (client == null) {
                throw new Exception("You must indicate a client");
            }

            String sqlAux = setUpQueryModify(optionChange, client);

            if (sqlAux.contains("ID = ")) {
                throw new Exception("Cannot modify an ID");
            }

            String sql = "UPDATE client SET " + sqlAux + " WHERE ID = "
                    + client.getId();

            insertModifyDelete(sql);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void deleteClient(int ID) throws Exception {
        try {
            String sql = "DELETE FROM client WHERE ID = " + ID;
            insertModifyDelete(sql);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String setUpQuerySearch(int optionChange, Client client) {
        String sql = "";
        switch (optionChange) {
            case 0:
                sql = "ID = " + client.getId();
                break;
            case 1:
                sql = "name LIKE '%" + client.getName() + "%'";
                break;
            case 2:
                sql = "lastName LIKE '%" + client.getLastName() + "%'";
                break;
            case 3:
                sql = "photo LIKE '%" + client.getPhoto() + "%'";
                break;
            case 4:
                sql = "gender = '" + client.getGender() + "'";
                break;
            case 5:
                sql = "active = " + client.isActive();
                break;
            case 6:
                sql = "birthDate LIKE '%" + client.getBirthDate() + "%'";
                break;
            case 7:
                sql = "email LIKE '%" + client.getEmail() + "%'";
                break;
            case 8:
                sql = "phone LIKE '%" + client.getPhone() + "%'";
                break;
            case 9:
                sql = "password LIKE '%" + client.getPassword() + "%'";
                break;
            case 10:
                sql = "agent = " + client.isAgent();
                break;
            case 11:
                sql = "owner = " + client.isOwner();
                break;
            case 12:
                sql = "dni = '" + client.getDni() + "'";
                break;
            default:
                System.out.print("You must enter a correct option");
        }
        return sql;
    }

    public String setUpQueryModify(int optionChange, Client client) {
        String sql = "";
        switch (optionChange) {
            case 0:
                sql = "ID = " + client.getId();
                break;
            case 1:
                sql = "name = '" + client.getName() + "'";
                break;
            case 2:
                sql = "lastName = '" + client.getLastName() + "'";
                break;
            case 3:
                sql = "photo = '" + client.getPhoto() + "'";
                break;
            case 4:
                sql = "gender = '" + client.getGender() + "'";
                break;
            case 5:
                int activeValue = client.isActive() ? 1 : 0;
                sql = "active = " + activeValue;
                break;
            case 6:
                sql = "birthDate = '" + client.getBirthDate() + "'";
                break;
            case 7:
                sql = "email = '" + client.getEmail() + "'";
                break;
            case 8:
                sql = "phone = '" + client.getPhone() + "'";
                break;
            case 9:
                sql = "password = '" + client.getPassword() + "'";
                break;
            case 10:
                int agentValue = client.isAgent() ? 1 : 0;
                sql = "agent = " + agentValue;
                break;
            case 11:
                int ownerValue = client.isOwner() ? 1 : 0;
                sql = "owner = " + ownerValue;
                break;
            case 12:
                sql = "dni = '" + client.getDni() + "'";
                break;
            default:
                System.out.print("You must enter a correct option");
        }
        return sql;
    }

    public Client returnClient(String sql) throws Exception {
        Client client = null; 
        try {
            consultBase(sql);
            if (result.next()) {
                client = new Client();
                client.setId(result.getInt(1));
                client.setName(result.getString(2));
                client.setLastName(result.getString(3));
                client.setPhoto(result.getString(4));
                client.setGender(result.getString(5));
                client.setActive(result.getBoolean(6));
                String birthDateString = result.getString(7);
                LocalDate birthDate = LocalDate.parse(birthDateString);
                client.setBirthDate(birthDate);
                client.setEmail(result.getString(8));
                client.setPhone(result.getString(9));
                client.setDni(result.getString(10));
                client.setPassword(result.getString(11));
                client.setAgent(result.getBoolean(12));
                client.setOwner(result.getBoolean(13));
            }
            disconnectBase();

        } catch (Exception e) {
            disconnectBase();
            throw e;
        }
        return client;
    }    

    public Collection<Client> listClient(int optionChange, Client client) throws Exception {
        Collection<Client> clients = null;
        try {
            String sql = "";
            if (optionChange == 13) {
                sql = "SELECT * FROM client";
            } else {
                String sqlAux = setUpQuerySearch(optionChange, client);
                sql = "SELECT * FROM client WHERE " + sqlAux;
            }

            consultBase(sql);

            clients = new ArrayList<>();

            while (result.next()) {
                Client clientAux = new Client();
                clientAux.setId(result.getInt(1));
                clientAux.setName(result.getString(2));
                clientAux.setLastName(result.getString(3));
                clientAux.setPhoto(result.getString(4));
                clientAux.setGender(result.getString(5));
                clientAux.setActive(result.getBoolean(6));
                String birthDateString = result.getString(7);
                LocalDate birthDate = LocalDate.parse(birthDateString);
                clientAux.setBirthDate(birthDate);
                clientAux.setEmail(result.getString(8));
                clientAux.setPhone(result.getString(9));
                clientAux.setDni(result.getString(10));
                clientAux.setPassword(result.getString(11));
                clientAux.setAgent(result.getBoolean(12));
                clientAux.setOwner(result.getBoolean(13));
                clients.add(clientAux);
            }

            disconnectBase();

        } catch (Exception e) {
            disconnectBase();
            System.out.println(e);
        }
        return clients;
    }
}