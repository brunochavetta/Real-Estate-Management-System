package domain.agent;

import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collectors;

import domain.client.Client;
import domain.property.Property;

public class AppointmentService {

    private AppointmentDAO dao;

    public AppointmentService() {
        dao = new AppointmentDAO();
    }

    public void addAppointment(LocalDate visitDate, Client client, Property property) throws Exception {
        try {
            if (visitDate == null) {
                throw new Exception("You must indicate date of  the visit");
            }
            if (client == null) {
                throw new Exception("You must indicate the client");
            }
            if (property == null) {
                throw new Exception("You must indicate the property");
            }

            Appointment appointment = new Appointment(visitDate, client, property);

            dao.saveAppointment(appointment);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void modifyAppointment(Appointment appointment, int optionChange) throws Exception {
        try {
            if (appointment == null) {
                throw new Exception("You must indicate a appointment");
            }

            dao.modifyAppointment(optionChange, appointment);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Collection<Appointment> searchAppointment(int optionChange, Appointment appointmentAux) throws Exception {
        Collection<Appointment> owners = null;
        try {
            if (appointmentAux == null) {
                throw new Exception("You must indicate a appointment");
            }

            owners = dao.listAppointment(optionChange, appointmentAux);

        } catch (Exception e) {
            System.out.println(e);
        }
        return owners;
    }

    public Appointment returnAppointment(String sql) throws Exception {
        Appointment appointment = null;
        try {
            if(sql.isEmpty()){
                System.out.println("Must indicate a appointment");
            }else{
                appointment = dao.returnAppointment(sql);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return appointment;
    }

    public void deleteAppointment(int ID) throws Exception {
        try {
            if (ID == 0) {
                throw new Exception("You must provide an ID");
            }
            dao.deleteAppointment(ID);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void printAppointment(int optionChange, Appointment appointment, Client user) throws Exception {
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment cannot be null");
        }
        try {
            Collection<Appointment> appointments = dao.listAppointment(optionChange, appointment);
            if(optionChange == 1 || optionChange == 2){
                appointments = appointments.stream()
                    .filter(c -> c.getProperty().getAgent().getId() == appointment.getProperty().getAgent().getId())
                    .collect(Collectors.toList());
            }
            if (appointments.isEmpty()) {
                throw new Exception("There are no appointments to print");
            } else {
                for (Appointment a : appointments) {
                    System.out.println(a);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}