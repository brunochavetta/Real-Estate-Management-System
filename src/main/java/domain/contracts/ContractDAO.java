package domain.contracts;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import domain.client.Client;
import domain.client.ClientDAO;
import domain.payment.Payment;
import domain.payment.PaymentDAO;
import domain.property.Property;
import domain.property.PropertyDAO;
import persistence.DAO;

public class ContractDAO extends DAO {

    public void saveContract(Contract contract) throws Exception {
        try {
            if (contract == null) {
                throw new Exception("You must indicate a contract");
            }

            String sql = "INSERT INTO contract (clientID, propertyID, contractDate) "
                    + "VALUES (" + contract.getClient().getId() + ", " 
                    + contract.getProperty().getId() + ", '" + contract.getContractDate() + "')";

            insertModifyDelete(sql);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void modifyContract(int optionChange, Contract contract) throws Exception {
        try {
            if (contract == null) {
                throw new Exception("You must indicate a contract");
            }

            String sqlAux = setUpQuery(optionChange, contract);

            if (sqlAux.contains("ID")) {
                throw new Exception("Cannot modify an id");
            }

            String sql = "UPDATE contract SET " + sqlAux + " WHERE ID = " + contract.getId();
            insertModifyDelete(sql);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String setUpQuery(int optionChange, Contract contract) {
        String sql = "";
        switch (optionChange) {
            case 0: 
                sql = "ID = " + contract.getId(); 
            case 1:
                sql = "clientID = " + contract.getClient().getId();
                break;
            case 2:
                sql = "propertyID = " + contract.getProperty().getId();
                break;
            case 3:
                sql = "contractDate = '" + contract.getContractDate() + "'";
                break;
            case 4:
                sql = "monthlyFee = " + contract.getMonthlyFee();
                break;
            default:
                throw new IllegalArgumentException("Invalid option");
        }
        return sql;
    }

    public void deleteContract(int ID) throws Exception {
        try {
            Contract contract = new Contract(); 
            contract.setId(ID);
            PaymentDAO dao = new PaymentDAO(); 
            Collection<Payment> payments = dao.listPayment(contract); 
            for (Payment payment : payments) {
                dao.deletePayment(payment.getId());
            }
            String sql = "DELETE FROM contract WHERE ID = " + ID;
            insertModifyDelete(sql);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Collection<Contract> listContracts(int optionChange, Contract contract) throws Exception {
        Collection<Contract> contracts = null;
        try {
            String sql = "";
            if (optionChange == 5) {
                sql = "SELECT * FROM contract";
            } else {
                String sqlAux = setUpQuery(optionChange, contract);
                sql = "SELECT * FROM contract WHERE " + sqlAux;
            }
            consultBase(sql);

            contracts = new ArrayList<>();

            while (result.next()) {
                ClientDAO cDao = new ClientDAO();
                int clientID = result.getInt(3); 
                String sqlClient = "SELECT * FROM client WHERE ID = " + clientID; 
                Client client = cDao.returnClient(sqlClient);
                
                PropertyDAO pDao = new PropertyDAO();
                int propertyID = result.getInt(2); 
                String sqlProperty = "SELECT * FROM property WHERE ID = " + propertyID; 
                Property property = pDao.returnProperty(sqlProperty); 

                BigDecimal monthlyFee = result.getBigDecimal(5); 
                
                contract = new Contract(client, property, result.getDate(4).toLocalDate(), monthlyFee);
                contract.setId(result.getInt(1));
                
                contracts.add(contract);
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            disconnectBase();
        }
        return contracts;
    }

    public Contract returnContract(String sql) throws Exception {
        Contract contract = null;
        try {
            consultBase(sql);

            while (result.next()) {
                ClientDAO cDao = new ClientDAO();
                int clientID = result.getInt("clientID"); 
                String sqlClient = "SELECT * FROM client WHERE ID = " + clientID; 
                Client client = cDao.returnClient(sqlClient);
                
                PropertyDAO pDao = new PropertyDAO();
                int propertyID = result.getInt("propertyID"); 
                String sqlProperty = "SELECT * FROM property WHERE ID = " + propertyID; 
                Property property = pDao.returnProperty(sqlProperty); 

                BigDecimal monthlyFee = result.getBigDecimal("monthlyFee"); 
                
                contract = new Contract(client, property, result.getDate("contractDate").toLocalDate(), monthlyFee);
                contract.setId(result.getInt("ID"));
            }

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            disconnectBase();
        }
        return contract;
    }
}