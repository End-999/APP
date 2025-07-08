package org.example.projectassignement;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.util.List;


public class TourismController {

    // Tab buttons
    @FXML
    private ToggleButton exploreTab;

    @FXML
    private ToggleButton myBookingsTab;

    @FXML
    private ToggleButton profileTab;

    @FXML
    private ToggleButton emergencyTab;

    // Content screens
    @FXML
    private VBox exploreScreen;

    @FXML
    private VBox myBookingsScreen;

    @FXML
    private VBox profileScreen;

    @FXML
    private VBox emergencyScreen;

    @FXML
    private VBox bookingList;

    @FXML
    private TilePane destinationCards;

    @FXML
    public void initialize() {
        showScreen(exploreScreen);
        setActiveTab(exploreTab);

        exploreTab.setOnAction(e -> {
            showScreen(exploreScreen);
            setActiveTab(exploreTab);
        });

        myBookingsTab.setOnAction(e -> {
            showScreen(myBookingsScreen);
            setActiveTab(myBookingsTab);
        });

        profileTab.setOnAction(e -> {
            showScreen(profileScreen);
            setActiveTab(profileTab);
        });

        emergencyTab.setOnAction(e -> {
            showScreen(emergencyScreen);
            setActiveTab(emergencyTab);
        });

        destinationCards.getChildren().clear();
        for (Destination d : destinations) {
            VBox card = createDestinationCard(d);
            destinationCards.getChildren().add(card);
        }
    }

    private List<Destination> destinations = List.of(
            new Destination("Kathmandu Durbar Square", "Kathmandu", "2–3 hours", "1400m altitude", "Historic palace complex showcasing traditional Nepalese architecture.", "$1010", "kathmandu.jpg"),
            new Destination("Chitwan National Park", "Chitwan", "2–3 hours", "150m altitude", "UNESCO World Heritage site famous for rhinos, tigers, and elephants.", "$1600", "img.png"),
            new Destination("Pokhara Paragliding", "Pokhara", "2–3 hours", "1600m altitude", "Tandem paragliding with stunning views of the Himalayas.", "$1260", "new.jpg")
            // add as many as you want dynamically here
    );
    /**
     * Shows only the selected screen and hides all others
     */
    private void showScreen(VBox selectedScreen) {
        exploreScreen.setVisible(false);
        exploreScreen.setManaged(false);

        myBookingsScreen.setVisible(false);
        myBookingsScreen.setManaged(false);

        profileScreen.setVisible(false);
        profileScreen.setManaged(false);

        emergencyScreen.setVisible(false);
        emergencyScreen.setManaged(false);

        selectedScreen.setVisible(true);
        selectedScreen.setManaged(true);
    }

    private VBox createDestinationCard(Destination d) {
        System.out.println("Loading image: " + d.imagePath);
        System.out.println("Resource exists? " +
                (getClass().getResource(d.imagePath) != null));

        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 15;");
        card.setPrefWidth(380);

        ImageView imageView = new ImageView(getClass().getResource(d.imagePath).toExternalForm());
        imageView.setFitWidth(460);
        imageView.setFitHeight(235);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        Label title = new Label(d.name);
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        Label location = new Label("📍 " + d.location);
        Label duration = new Label("⏱️ " + d.duration);
        Label altitude = new Label("🏔️ " + d.altitude);
        Label description = new Label(d.description);
        description.setWrapText(true);
        description.setStyle("-fx-text-fill: #6b6b6b; -fx-font-size: 18px;");

        Label price = new Label(d.price);
        price.setStyle("-fx-font-weight: bold; -fx-font-size: 20px;");

        Button bookBtn = new Button("Book Now");
        bookBtn.setStyle("-fx-background-color: #392424; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-style: italic; -fx-font-size: 18px;");

        // This lambda captures the destination 'd' for this button:
        bookBtn.setOnAction(e -> {
            addBookingAndSwitchTab(
                    d.name,
                    "Available Guide",
                    "2025-07-13 to 2025-07-20",
                    d.price,
                    "pending",
                    "Awaiting Confirmation"
            );
        });

        card.getChildren().addAll(imageView, title, location, duration, altitude, description, price, bookBtn);
        return card;
    }

    private VBox createBookingCard(String title, String guide, String dates, String price, String status, String extraNote) {

        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: #fefefe; -fx-border-color: #dddddd; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 20;");
        card.setPrefWidth(1104);

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label guideLabel = new Label("Guide: " + guide);
        guideLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #4b4b4b;");

        Label dateLabel = new Label("Dates: " + dates);
        dateLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #4b4b4b;");

        Label priceLabel = new Label(price);
        priceLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label statusLabel = new Label(status);
        statusLabel.setStyle("-fx-background-color: #ffeeaa; -fx-padding: 4 10 4 10; -fx-border-radius: 5; -fx-background-radius: 5; -fx-font-size: 12px;");

        Label noteLabel = new Label(extraNote);
        noteLabel.setStyle("-fx-font-size: 13px; -fx-border-color: #ccc; -fx-border-radius: 15; -fx-padding: 4 12 4 12; -fx-background-radius: 15;");

        HBox hBox = new HBox(10, statusLabel, noteLabel);

        card.getChildren().addAll(titleLabel, guideLabel, dateLabel, priceLabel, hBox);

        return card;
    }


    private void addBookingAndSwitchTab(String title, String guide, String dates, String price, String status, String note) {
        VBox newBooking = createBookingCard(title, guide, dates, price, status, note);
        bookingList.getChildren().add(newBooking);

//        showScreen(myBookingsScreen);
//        setActiveTab(myBookingsTab);
    }


    /**
     * Sets only the clicked tab as selected
     */
    private void setActiveTab(ToggleButton selectedTab) {
        // Remove styles from all tabs
        exploreTab.getStyleClass().removeAll("active-tab", "inactive-tab");
        myBookingsTab.getStyleClass().removeAll("active-tab", "inactive-tab");
        profileTab.getStyleClass().removeAll("active-tab", "inactive-tab");
        emergencyTab.getStyleClass().removeAll("active-tab", "inactive-tab");

        // Apply styles
        if (selectedTab == exploreTab) exploreTab.getStyleClass().add("active-tab"); else exploreTab.getStyleClass().add("inactive-tab");
        if (selectedTab == myBookingsTab) myBookingsTab.getStyleClass().add("active-tab"); else myBookingsTab.getStyleClass().add("inactive-tab");
        if (selectedTab == profileTab) profileTab.getStyleClass().add("active-tab"); else profileTab.getStyleClass().add("inactive-tab");
        if (selectedTab == emergencyTab) emergencyTab.getStyleClass().add("active-tab"); else emergencyTab.getStyleClass().add("inactive-tab");
    }


}
