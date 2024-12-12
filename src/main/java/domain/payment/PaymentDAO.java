package domain.payment;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import domain.contracts.Contract;
import domain.contracts.ContractDAO;

import persistence.DAO;

public class PaymentDAO extends DAO {

    public void savePayment(Payment payment) throws Exception {
        try {
            if (payment == null) {
                throw new Exception("Invalid input: Payment cannot be null");
            }

            String updateSql = "UPDATE payment SET paid = true" 
                    + ", debtor = false" + " WHERE contractID = " + payment.getContract().getId()
                    + " AND dueDate = '" + getLatestDueDate(payment.getContract()) + "';";

            String insertSql = "INSERT INTO payment (contractID, dueDate, months, paid, debtor)"
                    + "VALUES (" + payment.getContract().getId() + ", '" + payment.getDueDate()
                    + "'" + ", " + payment.getMonths() + "," + payment.isPaid() + "," + payment.isDebtor() + " );";

            insertModifyDelete(updateSql);
            insertModifyDelete(insertSql);

        } catch (SQLException e) {
            throw new Exception("An error occurred while saving the payment. Please try again later.", e);
        } catch (Exception e) {
            throw e;
        }
    }

    public String setUpQuery(int optionChange, Payment payment) {
        String sql = "";
        switch (optionChange) {
            case 0:
                sql = "ID = " + payment.getId();
                break;
            case 1:
                sql = "contractID = " + payment.getContract().getId() + "";
                break;
            case 2:
                sql = "dueDate = '" + payment.getDueDate() + "'";
                break;
            case 3:
                sql = "months = " + payment.getMonths();
                break;
            case 4:
                sql = "paid = " + payment.isPaid();
                break;

            case 5:
                sql = "debtor = " + payment.isDebtor();
                break;
            default:
                System.out.print("You must enter a correct option");
        }
        return sql;
    }

    public void deletePayment(int ID) throws Exception {
        try {
            String sql = "DELETE FROM payment WHERE ID = " + ID;
            insertModifyDelete(sql);
        } catch (SQLException e) {
            throw new Exception("An error occurred while deleting the payment. Please try again.", e);
        } catch (Exception e) {
            throw e;
        }
    }

    public Collection<Payment> listPayment(Contract contract) throws Exception {
        Collection<Payment> payments = null;
        try {

            String sql = "SELECT * FROM payment WHERE contractID = " + contract.getId() + ";";

            consultBase(sql);

            payments = new ArrayList<>();

            while (result.next()) {
                Payment paymentAux = new Payment();
                paymentAux.setId(result.getInt(1));
                contract.setId(result.getInt(2));
                ContractDAO cDao = new ContractDAO();
                String sqlContract = "SELECT * FROM contract WHERE ID = " + contract.getId();
                contract = cDao.returnContract(sqlContract); 
                paymentAux.setContract(contract);

                String dueDateString = result.getString(3);
                LocalDate dueDate = LocalDate.parse(dueDateString);
                paymentAux.setDueDate(dueDate);

                paymentAux.setMonths(result.getInt(4));
                paymentAux.setPaid(result.getBoolean(5));
                paymentAux.setDebtor(result.getBoolean(6));

                payments.add(paymentAux);
            }

            disconnectBase();

        } catch (SQLException e) {
            disconnectBase();
            throw new Exception("An error occurred while retrieving payment history. Please try again.", e);
        } catch (Exception e) {
            disconnectBase();
            throw e;
        }
        return payments;
    }

    public LocalDate getLatestDueDate(Contract contract) throws Exception {

        String sql = "SELECT MAX(dueDate) AS latestDueDate FROM payment WHERE contractID = " + contract.getId();

        consultBase(sql);
        try {
            if (result.next()) {
                Date latestDueDate = result.getDate("latestDueDate");
                if (latestDueDate != null) {
                    return latestDueDate.toLocalDate();
                }
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while retrieving the latest due date.");
            System.out.println(e);
        } catch (Exception e) {
     
            throw e;
        }
        return null;
    }

    public void updateDebtor() throws Exception {
        String sql = "UPDATE payment SET debtor = true WHERE dueDate < CURRENT_DATE;";
        try {
            insertModifyDelete(sql);
        } catch (SQLException e) {
            throw new Exception("An error occurred while updating debtor status. Please try again.", e);
        } catch (Exception e) {
            throw e;
        }
    }
}