package org.example.projectassignement;

import org.example.projectassignement.User;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserService {
    private static final String FILE_PATH = "loginDetails";

    public void saveUser(User user) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            bw.write(user.toCSV());
            bw.newLine();
        }
    }

    public List<User> getAllUsers() throws IOException {
        List<User> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    User user = new User(parts[0], parts[1], parts[2], parts[3]);
                    users.add(user);
                }
            }
        }
        return users;
    }

    public boolean isEmailRegistered(String email) throws IOException {
        for (User user : getAllUsers()) {
//            System.out.println(user);
            if (user.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    public boolean authenticate(String email, String password) throws IOException {
        for (User user : getAllUsers()) {
            System.out.println(user);
            if (user.getEmail().equalsIgnoreCase(email) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }
}
