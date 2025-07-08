package org.example.projectassignement;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneChanger {
    public void changeScene(String sceneName, Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(sceneName));
        Scene scene = new Scene(fxmlLoader.load(), 1440, 750);
        stage.setTitle(sceneName);
        stage.setScene(scene);
        stage.show();
    }
}
