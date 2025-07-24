package org.example.cleanprj.models;

public class User {
    private String fullName;
    private String email;
    private String password;
    private String nationality;

    public User(String name, String email, String password, String nationality) {
        this.fullName = name;
        this.email = email;
        this.password = password;
        this.nationality = nationality;
    }

    // Getters
    public String getName() { return fullName; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getNationality() { return nationality; }

    // Setters
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setNationality(String nationality) { this.nationality = nationality; }

    @Override
    public String toString() {
        return fullName + "," + email + "," + password + "," + nationality;
    }

    public String toCSV() {
        return fullName + "," + email + "," + password + "," + nationality;
    }
}