package domain.contracts;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collectors;

import domain.client.Client;
import domain.property.Property;

public class ContractService {
    private final ContractDAO dao;

    public ContractService() {
        dao = new ContractDAO();
    }

    public void addContract(Client client, Property property, LocalDate contractDate, BigDecimal monthlyFee) throws Exception {
        try {
            if (contractDate == null) {
                throw new Exception("You must indicate a contract date");
            }
            if (client == null) {
                throw new Exception("You must indicate the client");
            }
            if (property == null) {
                throw new Exception("You must indicate the property");
            }
            if(monthlyFee == null){
                throw new Exception("You must indicate the monthly fee");
            }

            Contract contract = new Contract(client, property, contractDate, monthlyFee);
            dao.saveContract(contract);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void modifyContract(Contract contract, int optionChange) throws Exception {
        try {
            if (contract == null) {
                throw new Exception("You must indicate a contract");
            }

            dao.modifyContract(optionChange, contract);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Collection<Contract> searchContracts(int optionChange, Contract contractAux) throws Exception {
        Collection<Contract> contracts = null;
        try {
            if (contractAux == null) {
                throw new Exception("You must indicate a contract");
            }

            contracts = dao.listContracts(optionChange, contractAux);

        } catch (Exception e) {
            System.out.println(e);
        }
        return contracts;
    }

    public Contract returnContract(String sql) throws Exception {
        Contract contract = null;
        try {
            if(sql.isEmpty()){
                System.out.println("Must indicate a contract");
            }else{
                contract = dao.returnContract(sql);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return contract;
    }

    public void deleteContract(int ID) throws Exception {
        try {
            if (ID == 0) {
                throw new Exception("You must provide an ID");
            }
            dao.deleteContract(ID);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void printContracts(int optionChange, Contract contract, Client user) throws Exception {
        if (contract == null) {
            throw new IllegalArgumentException("Contract cannot be null");
        }
        try {
            Collection<Contract> contracts = dao.listContracts(optionChange, contract);

            if(optionChange == 2 || optionChange == 1){
                contracts = contracts.stream()
                    .filter(c -> c.getProperty().getAgent().getId() == user.getId())
                    .collect(Collectors.toList());
            }

            if (contracts.isEmpty()) {
                throw new Exception("There are no contracts to print");
            } else {
                for (Contract c : contracts) {
                    System.out.println(c);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
