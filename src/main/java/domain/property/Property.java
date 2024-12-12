package domain.property;

import domain.client.Client;
import java.math.BigDecimal;

public class Property {
    
    private int id; 
    private String name; 
    private String address; 
    private String description; 
    private String photo; 
    private boolean active; 
    private BigDecimal value; 
    private String status; 
    private Client agent; 

    public Property() {
        agent = new Client(); 
    }

    public Property(String name, String address, String description, String photo, boolean active, BigDecimal value, String status, Client agent) {
        this.name = name;
        this.address = address;
        this.description = description;
        this.photo = photo;
        this.active = active;
        this.value = value;
        this.status = status;
        this.agent = agent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Client getAgent() {
        return agent;
    }

    public void setAgent(Client agent) {
        this.agent = agent;
    }

    
    public String printProperty() {
        return "Property: " + "\n - Id = " + id + "\n - Name = " + 
                name + "\n - Address = " + address; 
    }

    @Override
    public String toString() {
        return "Property: " + "\n - Id = " + id + "\n - Name = " + 
                name + "\n - Address = " + address + "\n - Description = " + 
                description + "\n - Photo = " + photo + "\n - Active = " + active + 
                "\n - Value = " + value + "\n - Status = " + status + "\n - Agent = " + agent.printClient();
    }
}