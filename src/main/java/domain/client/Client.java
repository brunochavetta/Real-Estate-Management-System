package domain.client;

import java.time.LocalDate;

public class Client {
    private int ID; 
    private String name; 
    private String lastName; 
    private String photo; 
    private String gender; 
    private boolean active; 
    private LocalDate birthDate; 
    private String email; 
    private String phone; 
    private String password;
    private boolean agent; 
    private boolean owner; 
    private String dni;
   

    public Client() {
        this.agent = false;
        this.owner = false;
    }

    public Client(String name, String lastName, String photo, 
            String gender, boolean active, LocalDate birthDate, 
            String email, String phone, String password, String dni) {
        this.name = name;
        this.lastName = lastName;
        this.photo = photo;
        this.gender = gender;
        this.active = active;
        this.birthDate = birthDate;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.agent = false;
        this.owner = false;
        this.dni = dni; 
    }

    public int getId() {
        return ID;
    }

    public void setId(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAgent() {
        return agent;
    }

    public void setAgent(boolean agent) {
        this.agent = agent;
    }

    public boolean isOwner() {
        return owner;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }
    
    public String printClient() {
        return "Client: " + "\n  - ID = " + ID + "\n  - Name = " + 
                name + "\n  - LastName = " + lastName + "\n  - Email = " + email + "\n  - Phone = " + phone; 
    }

    @Override
    public String toString() {
        return "Client: " + "\n - ID = " + ID + "\n - Name = " + name + "\n - Last name = " 
                + lastName + "\n - Photo = " + photo + "\n - Gender = " + gender + 
                "\n - Active = " + active + "\n - BirthDate = " + birthDate + "\n - DNI = " + dni + "\n - Email = "
                + email + "\n - Phone = " + phone + "\n - Agent = " + agent + "\n - Owner = " + owner;
    }
    
}