package org.example.projectassignement;

public class Guide extends Person {
    public Guide(User user) {
        super(user);
    }

    @Override
    public String getRole() {
        return "Guide";
    }
}
