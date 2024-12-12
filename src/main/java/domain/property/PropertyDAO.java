package domain.property;

import domain.client.Client;
import domain.client.ClientDAO;
import java.util.ArrayList;
import java.util.Collection;
import persistence.DAO;

public final class PropertyDAO extends DAO {

    public void saveProperty(Property property) throws Exception {
        try {
            if (property == null) {
                throw new Exception("You must indicate a property");
            }

            int activeValue = property.isActive() ? 1 : 0;

            String sql = "INSERT INTO property (name, address, description, photo, active, value, status, agentID) "
                    + "VALUES ('" + property.getName() + "', '" + property.getAddress() + "', '"
                    + property.getDescription() + "', '" + property.getPhoto() + "', "
                    + activeValue + ", '" + property.getValue() + "', '"
                    + property.getStatus() + "', " +  property.getAgent().getId() + " );";

            insertModifyDelete(sql);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void modifyProperty(Property property, int optionChange) throws Exception {
        try {
            if (property == null) {
                throw new Exception("You must indicate a property");
            }
            
            String sqlAux = setUpQueryModify(optionChange, property);
          
            if (sqlAux.contains("ID")){
                throw new Exception("Cannot modify an id"); 
            }

            String sql = "UPDATE property SET " + sqlAux + "WHERE ID = "
                    + property.getId();

            insertModifyDelete(sql);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void deleteProperty(int ID) throws Exception {
        try {
            String sql = "DELETE FROM property WHERE ID = " + ID;
            insertModifyDelete(sql);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String setUpQueryModify(int optionChange, Property property) {
        String sql = "";
        switch (optionChange) {
            case 0:
                sql = "ID = " + property.getId();
                break;
            case 1:
                sql = "name = '" + property.getName() + "'";
                break;
            case 2:
                sql = "address = '" + property.getAddress() + "'";
                break;
            case 3:
                sql = "description = '" + property.getDescription() + "'";
                break;
            case 4:
                sql = "photo = '" + property.getPhoto() + "'";
                break;
            case 5:
                sql = "active = " + property.isActive();
                break;
            case 6:
                sql = "value = " + property.getValue();
                break;
            case 7:
                sql = "status = '" + property.getStatus() + "'";
                break;
            default:
                System.out.print("You must enter a correct option");
        }
        return sql;
    }

    public String setUpQuerySearch(int optionChange, Property property) {
        String sql = "";
        switch (optionChange) {
            case 0:
                sql = "ID = " + property.getId();
                break;
            case 1:
                sql = "name LIKE '%" + property.getName() + "%'";
                break;
            case 2:
                sql = "address LIKE '%" + property.getAddress() + "%'";
                break;
            case 3:
                sql = "description LIKE '%" + property.getDescription() + "%'";
                break;
            case 4:
                sql = "photo LIKE '%" + property.getPhoto() + "%'";
                break;
            case 5:
                sql = "active = " + property.isActive();
                break;
            case 6:
                sql = "value = " + property.getValue();
                break;
            case 7:
                sql = "status LIKE '%" + property.getStatus() + "%'";
                break;
            case 8: 
                sql = "agentID = " + property.getAgent().getId();  
            default:
                System.out.print("You must enter a correct option");
        }
        return sql;
    }
    
    public Property returnProperty(String sql) throws Exception {
        Property property = new Property();
        try {
            consultBase(sql);
            while (result.next()) {
                property.setId(result.getInt(1));
                property.setName(result.getString(2));
                property.setAddress(result.getString(3));
                property.setDescription(result.getString(4));
                property.setPhoto(result.getString(5));
                property.setActive(result.getBoolean(6));
                property.setValue(result.getBigDecimal(7));
                property.setStatus(result.getString(8)); 
                
                Client agentID = new Client();
                agentID.setId(result.getInt(9));

                ClientDAO clientDao = new ClientDAO();
                String sqlAgent = "SELECT * FROM client WHERE ID = " + agentID.getId(); 
                agentID = clientDao.returnClient(sqlAgent); 

                property.setAgent(agentID);
            }
            disconnectBase();
        } catch (Exception e) {
            disconnectBase();
            System.out.println(e);
        }
        return property;
    }

    public Collection<Property> listProperty(int optionChange, Property property) throws Exception {
        Collection<Property> properties = null;
        try {
            String sql = "";
            if (optionChange == 9) {
                sql = "SELECT * FROM property";
            } else {
                String sqlAux = setUpQuerySearch(optionChange, property);
                sql = "SELECT * FROM property WHERE " + sqlAux;
            }

            consultBase(sql);

            properties = new ArrayList<>();

            while (result.next()) {
                Property propertyAux = new Property();
                propertyAux.setId(result.getInt(1));
                propertyAux.setName(result.getString(2));
                propertyAux.setAddress(result.getString(3));
                propertyAux.setDescription(result.getString(4));
                propertyAux.setPhoto(result.getString(5));
                propertyAux.setActive(result.getBoolean(6));
                propertyAux.setValue(result.getBigDecimal(7));
                propertyAux.setStatus(result.getString(8)); 
                
                Client agentID = new Client();
                agentID.setId(result.getInt(9));

                ClientDAO clientDao = new ClientDAO();
                Collection<Client> clients = clientDao.listClient(0, agentID);

                if (clients.size() == 1) {
                    Client[] clientsArray = clients.toArray(new Client[0]);
                    agentID = clientsArray[0];
                }

                propertyAux.setAgent(agentID);


                properties.add(propertyAux);
            }

            disconnectBase();

        } catch (Exception e) {
            disconnectBase();
            System.out.println(e);
        }
        return properties;
    }

}