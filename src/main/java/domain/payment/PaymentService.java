package domain.payment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;

import domain.contracts.Contract;

public class PaymentService {

    private PaymentDAO dao;

    public PaymentService() {
        dao = new PaymentDAO();
    }

    public void addPayment(Contract contract) throws Exception {
        try {
            if ( contract == null) {
                throw new Exception("You must indicate a contract");
            }

            Payment payment = new Payment(contract); 

            payment.setContract(contract);
            payment.setDueDate(contract);
            payment.setMonths(contract);
            payment.setPaid();
            payment.setDebtor();

            dao.savePayment(payment);

        } catch (Exception e) {
            System.out.println(e); 
        }
    }
    
    public void deletePayment(int ID) throws Exception {
        try {
            if (ID == 0) {
                throw new Exception("You must provide an ID");
            }
            dao.deletePayment(ID);
        } catch (Exception e) {
            System.out.println(e); 
        }
    }

    public void printPayment(Contract contract) throws Exception {
        try {
            Collection<Payment> payments = dao.listPayment(contract);

            if (payments.isEmpty()) {
                throw new Exception("There are no appointments to print");
            } else {
                for (Payment p : payments) {
                    LocalDate dueDate = p.getDueDate();
                    BigDecimal monthlyfee = p.getContract().getMonthlyFee() ;
                    int month = p.getMonths();
                    boolean paid = p.isPaid();

                    String yesNo = paid ? "yes" : "no";
                  
                    System.out.println("---------------------------------");
                    System.out.println("Due date: " + dueDate);
                    System.out.println("---------------------------------");
                    System.out.println("Value: $" + monthlyfee);
                    System.out.println("Month: " + month);
                    System.out.println("Paid: " +  yesNo);
              
                }
            }
        } catch (Exception e) {
            System.out.println(e); 
        }
    }
    
}