package domain.contracts;

import java.math.BigDecimal;
import java.time.LocalDate;

import domain.client.Client;
import domain.property.Property;

public class Contract {
    private int ID; 
    private Client client;
    private Property property;
    private LocalDate contractDate;
    private BigDecimal monthlyFee;

    public Contract(){
        client = new Client(); 
        property = new Property(); 
    }
    
    public Contract(Client client, Property property, LocalDate contractDate, BigDecimal monthlyFee) {
        this.client = client;
        this.property = property;
        this.contractDate = contractDate;
        this.monthlyFee = monthlyFee; 
    }

    public int getId() {
        return ID;
    }

    public void setId(int ID) {
        this.ID = ID;
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

    public LocalDate getContractDate() {
        return contractDate;
    }

    public void setContractDate(LocalDate contractDate) {
        this.contractDate = contractDate;
    }

    public BigDecimal getMonthlyFee() {
        return monthlyFee;
    }

    public void setMonthlyFee(BigDecimal monthlyFee) {
        this.monthlyFee = monthlyFee;
    }

    @Override
    public String toString() {
        return "Contract: " +
                "\n - ID = " + ID +
                "\n - " + client.printClient() +
                "\n - " + property +
                "\n - Contract date = " + contractDate + 
                "\n - Monthly Fee = " + monthlyFee;
    }
}