package org.example.cleanprj.dao;

import org.example.cleanprj.models.User;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private static final String FILE_PATH = "data/loginDetails";

    public UserDAO() {
        createDataDirectoryIfNotExists();
    }

// It prevent, If your app will write to "data" (e.g., save a CSV, log file, DB), this step prevents errors like:
// java.io.FileNotFoundException: data/myfile.txt (No such file or directory)
    private void createDataDirectoryIfNotExists() {
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
    }

    public void save(User user) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            bw.write(user.toCSV());
            bw.newLine();
        }
    }

    public List<User> findAll() throws IOException {
        List<User> users = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return users; // Return empty list if file doesn't exist
        }

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

    public User findByEmail(String email) throws IOException {
        for (User user : findAll()) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }
        return null;
    }

    public boolean existsByEmail(String email) throws IOException {
        return findByEmail(email) != null;
    }
}

