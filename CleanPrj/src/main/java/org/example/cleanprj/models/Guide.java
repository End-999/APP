package org.example.cleanprj.models;

public class Guide extends Person {
    private String languages;
    private int experience;
    private String contact;

    public Guide(User user) {
        super(user);
    }

    public Guide(User user, String languages, int experience, String contact) {
        super(user);
        this.languages = languages;
        this.experience = experience;
        this.contact = contact;
    }

    @Override
    public String getRole() {
        return "Guide";
    }

    // Getters and Setters
    public String getLanguages() { return languages; }
    public void setLanguages(String languages) { this.languages = languages; }

    public int getExperience() { return experience; }
    public void setExperience(int experience) { this.experience = experience; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
}
