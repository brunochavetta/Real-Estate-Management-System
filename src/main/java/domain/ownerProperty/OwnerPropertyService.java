package domain.ownerProperty;

import domain.client.Client;
import domain.property.Property;
import java.util.Collection;


public class OwnerPropertyService {
    private final OwnerPropertyDAO dao; 

    public OwnerPropertyService() {
        dao = new OwnerPropertyDAO();
    }

    public void addOwnerProperty(Client owner, Property property) throws Exception {
        try {
            if (owner == null) {
                throw new Exception("You must indicate an owner");
            }
            if (property == null) {
                throw new Exception("You must indicate an property");
            }

            OwnerProperty ownerProperty = new OwnerProperty(owner, property); 

            dao.saveOwnerProperty(ownerProperty);

        } catch (Exception e) {
            System.out.println(e); 
        }
    }

    public void modifyOwnerProperty(OwnerProperty ownerProperty, int optionChange) throws Exception {
        try {
            if (ownerProperty == null) {
                throw new Exception("You must indicate a property owner");
            }

            dao.modifyOwnerProperty(ownerProperty, optionChange);
        } catch (Exception e) {
            System.out.println(e); 
        }
    }

    public Collection<OwnerProperty> searchOwnerProperty(int optionChange, OwnerProperty ownerPropertyAux) throws Exception {
        Collection<OwnerProperty> owners = null; 
        try {
            if (ownerPropertyAux == null) {
                throw new Exception("You must indicate a property owner");
            }

            owners = dao.listOwnerProperty(optionChange, ownerPropertyAux);

        } catch (Exception e) {
            System.out.println(e); 
        }
        return owners;
    }

    public void deleteOwnerProperty(int ID) throws Exception {
        try {
            if (ID == 0) {
                throw new Exception("You must provide an ID");
            }
            dao.deleteOwnerProperty(ID);
        } catch (Exception e) {
            System.out.println(e); 
        }
    }

    public void printOwnerPropiety(int optionChange, OwnerProperty ownerProperty) throws Exception {
        if (ownerProperty == null) {
            throw new IllegalArgumentException("Property owner cannot be null");
        }
        try {
            Collection<OwnerProperty> properties = dao.listOwnerProperty(optionChange, ownerProperty);

            if (properties.isEmpty()) {
                throw new Exception("There are no property owners to print");
            } else {
                for (OwnerProperty op : properties) {
                    System.out.println(op);
                }
            }
        } catch (Exception e) {
            System.out.println(e); 
        }
    }
}
