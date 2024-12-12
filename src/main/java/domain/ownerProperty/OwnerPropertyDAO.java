package domain.ownerProperty;

import domain.client.Client;
import domain.client.ClientDAO;
import domain.property.Property;
import domain.property.PropertyDAO;
import java.util.ArrayList;
import java.util.Collection;
import persistence.DAO;


public class OwnerPropertyDAO extends DAO{
    public void saveOwnerProperty(OwnerProperty ownerProperty) throws Exception {
        try {
            if (ownerProperty == null) {
                throw new Exception("You must indicate a ownerProperty owner");
            }

            String sql = "INSERT INTO ownerProperty (ownerID, propertyID) "
                    + "VALUES ( " + ownerProperty.getOwner().getId() + ", " + ownerProperty.getProperty().getId() + " );";

            insertModifyDelete(sql);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void modifyOwnerProperty(OwnerProperty ownerProperty, int optionChange) throws Exception {
        try {
            if (ownerProperty == null) {
                throw new Exception("You must indicate a property owner");
            }
            
            String sqlAux = setUpQuery(optionChange, ownerProperty);

            if (sqlAux.contains("ID")){
                throw new Exception("Cannot modify an id"); 
            }

            String sql = "UPDATE ownerProperty SET " + sqlAux + "WHERE ID = "
                    + ownerProperty.getId();

            insertModifyDelete(sql);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void deleteOwnerProperty(int ID) throws Exception {
        try {
            String sql = "DELETE FROM ownerProperty WHERE ID = " + ID;
            insertModifyDelete(sql);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String setUpQuery(int ownerPropertytionChange, OwnerProperty ownerProperty) {
        String sql = "";
        switch (ownerPropertytionChange) {
            case 0:
                sql = "ID = " + ownerProperty.getId();
                break;
            case 1:
                sql = "ownerID = " + ownerProperty.getOwner().getId();
                break;
            case 2:
                sql = "propertyID = " + ownerProperty.getProperty().getId();
                break;
            default:
                System.out.print("You must enter a correct ownerPropertytion");
        }
        return sql;
    }

    public Collection<OwnerProperty> listOwnerProperty(int ownerPropertytionChange, OwnerProperty ownerProperty) throws Exception {
        Collection<OwnerProperty> owners = new ArrayList<>();
        try {
            String sql = "";
            if (ownerPropertytionChange == 3) {
                sql = "SELECT * FROM ownerProperty";
            } else {
                String sqlAux = setUpQuery(ownerPropertytionChange, ownerProperty);
                sql = "SELECT * FROM ownerProperty WHERE " + sqlAux;
            }

            consultBase(sql);

            while (result.next()) {
                OwnerProperty ownerPropertyAux = new OwnerProperty();
                ownerPropertyAux.setId(result.getInt(1));
                
                Client owner = new Client(); 
                owner.setId(result.getInt(2));
                Property property = new Property(); 
                property.setId(result.getInt(3));
                
                ClientDAO cd = new ClientDAO(); 
                PropertyDAO pd = new PropertyDAO(); 

                Collection<Client> clients = cd.listClient(0, owner); 
                
                Collection<Property> properties = pd.listProperty(0, property); 
                
                if (clients.size() == 1) {
                    Client[] clientsArray = clients.toArray(new Client[0]);
                    owner = clientsArray[0];
                }
                
                if (properties.size() == 1) {
                    Property[] propertiesArray = properties.toArray(new Property[0]);
                    property = propertiesArray[0];
                }
                
                ownerPropertyAux.setOwner(owner);
                ownerPropertyAux.setProperty(property);

                owners.add(ownerPropertyAux);
            }

            disconnectBase();
            return owners;

        } catch (Exception e) {
            disconnectBase();
            System.out.println(e);
        }
        return owners;
    }
}
