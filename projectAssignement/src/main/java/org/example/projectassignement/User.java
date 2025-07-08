package org.example.projectassignement;

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

    public String getName() { return fullName; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getNationality() { return nationality; }

//    public static User fromCSV(String line) {
//        String[] parts = line.split(",");
//        if (parts.length == 4) {
//            return new User(parts[0], parts[1], parts[2], parts[3]);
//        }
//        return null;
//    }

    @Override
    public String toString() {
        return fullName + "," + email + "," + password + "," + nationality;
    }

    public String toCSV() {
        return fullName + "," + email + "," + password + "," + nationality;
    }
}
