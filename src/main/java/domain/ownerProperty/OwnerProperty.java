package domain.ownerProperty;

import domain.client.Client;
import domain.property.Property;


public class OwnerProperty {
    private int ID; 
    private Client owner; 
    private Property property; 

    public OwnerProperty() {
        owner = new Client(); 
        property = new Property(); 
    }

    public OwnerProperty(Client owner, Property property) {
        this.owner = owner;
        this.property = property;
    }

    public int getId() {
        return ID;
    }

    public void setId(int ID) {
        this.ID = ID;
    }

    public Client getOwner() {
        return owner;
    }

    public void setOwner(Client owner) {
        this.owner = owner;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    @Override
    public String toString() {
        return "Owner-Property:  " + "\n - ID = " + ID + "\n - Owner = " + owner.printClient() + "\n - " + property.printProperty();
    } 
}
