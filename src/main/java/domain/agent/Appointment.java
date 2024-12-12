package domain.agent;

import domain.client.Client;
import domain.property.Property;

import java.time.LocalDate;

public class Appointment {
    
    private int ID;
    private LocalDate visitDate;
    private Client client;
    private Property property;
    private boolean approved; 
    
    public Appointment(){
        client = new Client(); 
        property = new Property(); 
        approved = false; 
    }
    
    public Appointment(LocalDate visitDate, Client client, Property property){
        this.visitDate = visitDate;
        this.client = client;
        this.property = property;
        this.approved = false; 
    }
    
    public int getId(){
        return ID;
    }
    
    public void setId(int ID){
        this.ID = ID;
    }
    
    public LocalDate getVisitDate(){
        return visitDate;
    }
    
    public void setVisitDate(LocalDate visitDate){
        this.visitDate = visitDate;
    }
    
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    @Override
    public String toString() {
        return "Appointment: " + "\n - ID = " + ID + "\n - " + client.printClient() + "\n - " + property + "\n - Approved = " + approved; 
    }
}