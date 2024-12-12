package domain.agent;

import domain.client.Client;
import domain.client.ClientDAO;
import domain.property.Property;
import domain.property.PropertyDAO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import persistence.DAO;

public class AppointmentDAO extends DAO {
    
    public void saveAppointment(Appointment appointment) throws Exception {
        try {
            if (appointment == null) {
                throw new Exception("You must indicate a...");
            }

            String sql = "INSERT INTO visitsProperty (VisitDate, clientID, propertyID, approved)"
                    + "VALUES ('" + appointment.getVisitDate() + "', " + appointment.getClient().getId() + ", "
                    + appointment.getProperty().getId() + ", " + appointment.isApproved() + " )";

            insertModifyDelete(sql);

        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public void modifyAppointment(int optionChange, Appointment appointment) throws Exception{
        try {
            if (appointment == null) {
                throw new Exception("You must indicate a appointment");
            }
            
            String sqlAux = setUpQuery(optionChange, appointment);

            if (sqlAux.contains("ID")){
                throw new Exception("Cannot modify an id"); 
            }

            String sql = "UPDATE appointment SET " + sqlAux + "WHERE ID = "
                    + appointment.getId();

            insertModifyDelete(sql);

        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public String setUpQuery(int optionChange, Appointment appointment) {
        String sql = "";
        switch (optionChange) {
            case 0:
                sql = "ID = " + appointment.getId();
                break;
            case 1:
                sql = "VisitDate = '" + appointment.getVisitDate() + "'";
                break;
            case 2:
                sql = "clientID = " + appointment.getClient().getId();
                break;
            case 3:
                sql = "propertyID = " + appointment.getProperty().getId();
                break;
            default:
                System.out.print("You must enter a correct option");
        }
        return sql;
    }

    public void deleteAppointment(int ID) throws Exception {
        try {
            String sql = "DELETE FROM visitsProperty WHERE ID = " + ID;
            insertModifyDelete(sql);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Appointment returnAppointment(String sql) throws Exception {
        Appointment appointment = null; 
        try {
            consultBase(sql);

            while (result.next()) {
                appointment.setId(result.getInt(1));

                String visitDateString = result.getString(2);
                LocalDate visitDate = LocalDate.parse(visitDateString);
                appointment.setVisitDate(visitDate);
                
                Client client = new Client();
                client.setId(result.getInt(3));
                ClientDAO cDao = new ClientDAO(); 
                Collection<Client> clients = cDao.listClient(0, client);
                if (clients.size() == 1) {
                    Client[] clientsArray = clients.toArray(new Client[0]);
                    client = clientsArray[0];
                }
                appointment.setClient(client);

                Property property = new Property();
                property.setId(result.getInt(4));
                PropertyDAO pd = new PropertyDAO();
                Collection<Property> properties = pd.listProperty(0, property); 
                if (properties.size() == 1) {
                    Property[] propertiesArray = properties.toArray(new Property[0]);
                    property = propertiesArray[0];
                }
                appointment.setProperty(property);
            }

            disconnectBase();

        } catch (Exception e) {
            disconnectBase();
            System.out.println(e);
        }
        return appointment;
    }
    
    public Collection<Appointment> listAppointment(int optionChange, Appointment appointment) throws Exception {
        Collection<Appointment> appointments = null; 
        try {
            String sql = "";
            if (optionChange == 4) {
                sql = "SELECT * FROM visitsProperty";
            } else {
                String sqlAux = setUpQuery(optionChange, appointment);
                sql = "SELECT * FROM visitsProperty WHERE " + sqlAux;
            }

            consultBase(sql);

            appointments = new ArrayList<>();

            while (result.next()) {
                Appointment appointmentAux = new Appointment();
                appointmentAux.setId(result.getInt(1));

                String visitDateString = result.getString(2);
                LocalDate visitDate = LocalDate.parse(visitDateString);
                appointmentAux.setVisitDate(visitDate);
                
                Client client = new Client();
                client.setId(result.getInt(3));
                ClientDAO cDao = new ClientDAO(); 
                Collection<Client> clients = cDao.listClient(0, client);
                if (clients.size() == 1) {
                    Client[] clientsArray = clients.toArray(new Client[0]);
                    client = clientsArray[0];
                }
                appointmentAux.setClient(client);

                Property property = new Property();
                property.setId(result.getInt(4));
                PropertyDAO pd = new PropertyDAO();
                Collection<Property> properties = pd.listProperty(0, property); 
                if (properties.size() == 1) {
                    Property[] propertiesArray = properties.toArray(new Property[0]);
                    property = propertiesArray[0];
                }
                appointmentAux.setProperty(property);

                appointments.add(appointmentAux);
            }

            disconnectBase();

        } catch (Exception e) {
            disconnectBase();
            System.out.println(e);
        }
        return appointments;
    }
}
