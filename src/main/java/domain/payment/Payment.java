package domain.payment;

import java.time.LocalDate;
import java.time.Period;

import domain.contracts.Contract;

public class Payment {

    private int ID;
    private Contract contract;
    private LocalDate dueDate;
    private int months;
    private boolean paid;
    private boolean debtor;
    
    public Payment(){
        
    }
    
    public Payment(Contract contract){
        this.contract = contract;
    }
    
    public int getId(){
        return ID;
    }
    
    public void setId(int ID){
        this.ID = ID;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }
    
    public LocalDate getDueDate(){
        return dueDate;
    }
    
    public void setDueDate(Contract contract) throws Exception {
        PaymentDAO pDAO = new PaymentDAO();
        LocalDate sourceDate = pDAO.getLatestDueDate(contract);

        this.dueDate = sourceDate.plusMonths(1);
    }

    public void setDueDate(LocalDate dueDate){
        this.dueDate = dueDate;
    }

    public boolean isPaid() {
        return paid ;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public void setPaid() {
        this.paid = false;
    }


    public int getMonths() {
        return months;
    }

    public void setMonths(Contract contract) {
        LocalDate contractDate = contract.getContractDate();
        Period period = Period.between(contractDate, dueDate);
        
        this.months = period.getMonths();   
    }

    public void setMonths(int months) {
        this.months = months;
    }


    public boolean isDebtor() {
        return debtor;
    }

    public void setDebtor() {
        this.debtor = LocalDate.now().isAfter(dueDate);

    }

    public void setDebtor(boolean debtor) {
        this.debtor = debtor;
    }
    
}