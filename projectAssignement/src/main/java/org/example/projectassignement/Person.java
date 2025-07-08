package org.example.projectassignement;

public abstract class Person {
    protected User user;

    public Person(User user) { this.user = user; }

    public User getUser() { return user; }

    public abstract String getRole();
}
