package domain.property;

import domain.client.Client;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class PropertyService {

    private PropertyDAO dao;

    public PropertyService() {
        dao = new PropertyDAO();
    }

    public void addProperty(String name, String address, String description,
            String photo, BigDecimal value, String status, Client agentID) throws Exception {
        try {
            if (name == null || name.trim().isEmpty()) {
                throw new Exception("You must indicate the name");
            }
            if (address == null || address.trim().isEmpty()) {
                throw new Exception("You must indicate the address");
            }
            if (description == null || description.trim().isEmpty()) {
                throw new Exception("You must indicate the description");
            }
            if (photo == null || photo.trim().isEmpty()) {
                throw new Exception("You must indicate a photo");
            }
            if (value == null) {
                throw new Exception("You must indicate a value");
            }
            if (status == null || status.trim().isEmpty()) {
                throw new Exception("You must indicate your status");
            }
            if (agentID == null) {
                throw new Exception("You must indicate a agent");
            }

            Property property = new Property(name, address, description, photo, 
                    true, value, status, agentID); 

            dao.saveProperty(property);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void modifyProperty(Property property, int optionChange) throws Exception {
        try {
            if (property == null) {
                throw new Exception("You must indicate a property");
            }

            dao.modifyProperty(property, optionChange);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Collection<Property> searchProperty(int optionChange, Property propertyAux) throws Exception {
        Collection<Property> properties = null; 
        try {
            if (propertyAux == null) {
                throw new Exception("You must indicate a property");
            }

            properties = dao.listProperty(optionChange, propertyAux);

            if(optionChange == 8){
                properties = properties.stream()
                .filter(p -> p.getAgent().getId() == propertyAux.getAgent().getId())
                .collect(Collectors.toList()); 
            }
            
        } catch (Exception e) {
            System.out.println(e);
        }
        return properties;
    }

    public void deleteProperty(int ID) throws Exception {
        try {
            if (ID == 0) {
                throw new Exception("You must provide an ID");
            }
            dao.deleteProperty(ID);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}