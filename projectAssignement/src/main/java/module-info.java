module org.example.projectassignement {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;

    opens org.example.projectassignement to javafx.fxml;
    exports org.example.projectassignement;
}