package org.example.projectassignement;

public class Tourist extends Person {
    public Tourist(User user) {
        super(user);
    }

    @Override
    public String getRole() {
        return "Tourist";
    }
}