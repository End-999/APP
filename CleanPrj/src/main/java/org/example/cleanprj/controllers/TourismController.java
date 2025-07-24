package org.example.cleanprj.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import org.example.cleanprj.models.Destination;
import org.example.cleanprj.services.DestinationService;

import java.util.List;

public class TourismController {

    // Tab buttons
    @FXML private ToggleButton exploreTab;
    @FXML private ToggleButton myBookingsTab;
    @FXML private ToggleButton profileTab;
    @FXML private ToggleButton emergencyTab;

    // Content screens
    @FXML private VBox exploreScreen;
    @FXML private VBox myBookingsScreen;
    @FXML private VBox profileScreen;
    @FXML private VBox emergencyScreen;
    @FXML private VBox bookingList;
    @FXML private TilePane destinationCards;

    private final DestinationService destinationService;

    public TourismController() {
        this.destinationService = new DestinationService();
    }

    @FXML
    public void initialize() {
        setupTabNavigation();
        loadDestinations();
        showScreen(exploreScreen);
        setActiveTab(exploreTab);
    }

    private void setupTabNavigation() {
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
    }

    private void loadDestinations() {
        destinationCards.getChildren().clear();
        List<Destination> destinations = destinationService.getAllDestinations();

        for (Destination destination : destinations) {
            VBox card = createDestinationCard(destination);
            destinationCards.getChildren().add(card);
        }
    }

    private VBox createDestinationCard(Destination destination) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 15;");
        card.setPrefWidth(418);

        // Image with proper resource path
        ImageView imageView = createImageView(destination.getImagePath());
        HBox imageContainer = new HBox(imageView);
        imageContainer.setAlignment(Pos.CENTER);

        // Labels
        Label title = createLabel(destination.getName(), "-fx-font-size: 20px; -fx-font-weight: bold;");
        Label location = createLabel("📍 " + destination.getLocation(), "-fx-font-size: 16px;");
        Label duration = createLabel("⏱️ " + destination.getDuration(), "-fx-font-size: 16px;");
        Label altitude = createLabel("🏔️ " + destination.getAltitude(), "-fx-font-size: 16px;");
        Label description = createDescriptionLabel(destination.getDescription());
        Label price = createLabel(destination.getPrice(), "-fx-font-weight: bold; -fx-font-size: 18px;");

        // Book button
        Button bookBtn = createBookButton(destination);

        card.getChildren().addAll(imageContainer, title, location, duration, altitude, description, price, bookBtn);
        return card;
    }

    private ImageView createImageView(String imagePath) {
        try {
            // Use proper resource path for images
            String resourcePath = "/org/example/cleanprj/images/" + imagePath;
            Image image = new Image(getClass().getResourceAsStream(resourcePath));
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(370);
            imageView.setFitHeight(200);
            imageView.setPreserveRatio(false);
            imageView.setSmooth(true);
            return imageView;
        } catch (Exception e) {
            // Fallback to placeholder if image not found
            ImageView imageView = new ImageView();
            imageView.setFitWidth(370);
            imageView.setFitHeight(200);
            return imageView;
        }
    }

    private Label createLabel(String text, String style) {
        Label label = new Label(text);
        label.setStyle(style);
        return label;
    }

    private Label createDescriptionLabel(String description) {
        Label label = new Label(description);
        label.setWrapText(true);
        label.setStyle("-fx-text-fill: #6b6b6b; -fx-font-size: 16px;");
        return label;
    }

    private Button createBookButton(Destination destination) {
        Button bookBtn = new Button("Book Now");
        bookBtn.setStyle("-fx-background-color: #392424; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-style: italic; -fx-font-size: 16px;");

        bookBtn.setOnAction(e -> handleBooking(destination));
        return bookBtn;
    }

    private void handleBooking(Destination destination) {
        addBookingAndSwitchTab(
                destination.getName(),
                "Available Guide",
                "2025-07-13 to 2025-07-20",
                destination.getPrice(),
                "pending",
                "Awaiting Confirmation"
        );
    }

    private VBox createBookingCard(String title, String guide, String dates, String price, String status, String extraNote) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: #fefefe; -fx-border-color: #dddddd; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 20;");
        card.setPrefWidth(1104);

        Label titleLabel = createLabel(title, "-fx-font-size: 18px; -fx-font-weight: bold;");
        Label guideLabel = createLabel("Guide: " + guide, "-fx-font-size: 14px; -fx-text-fill: #4b4b4b;");
        Label dateLabel = createLabel("Dates: " + dates, "-fx-font-size: 14px; -fx-text-fill: #4b4b4b;");
        Label priceLabel = createLabel(price, "-fx-font-size: 16px; -fx-font-weight: bold;");

        Label statusLabel = new Label(status);
        statusLabel.setStyle("-fx-background-color: #ffeeaa; -fx-padding: 4 10 4 10; -fx-border-radius: 5; -fx-background-radius: 5; -fx-font-size: 12px;");

        Label noteLabel = new Label(extraNote);
        noteLabel.setStyle("-fx-font-size: 13px; -fx-border-color: #ccc; -fx-border-radius: 15; -fx-padding: 4 12 4 12; -fx-background-radius: 15;");

        HBox statusBox = new HBox(10, statusLabel, noteLabel);
        card.getChildren().addAll(titleLabel, guideLabel, dateLabel, priceLabel, statusBox);

        return card;
    }

    private void addBookingAndSwitchTab(String title, String guide, String dates, String price, String status, String note) {
        VBox newBooking = createBookingCard(title, guide, dates, price, status, note);
        bookingList.getChildren().add(newBooking);
    }

    private void showScreen(VBox selectedScreen) {
        // Hide all screens
        exploreScreen.setVisible(false);
        exploreScreen.setManaged(false);
        myBookingsScreen.setVisible(false);
        myBookingsScreen.setManaged(false);
        profileScreen.setVisible(false);
        profileScreen.setManaged(false);
        emergencyScreen.setVisible(false);
        emergencyScreen.setManaged(false);

        // Show selected screen
        selectedScreen.setVisible(true);
        selectedScreen.setManaged(true);
    }

    private void setActiveTab(ToggleButton selectedTab) {
        // Remove styles from all tabs
        clearTabStyles();

        // Apply styles based on selection
        applyTabStyle(exploreTab, selectedTab == exploreTab);
        applyTabStyle(myBookingsTab, selectedTab == myBookingsTab);
        applyTabStyle(profileTab, selectedTab == profileTab);
        applyTabStyle(emergencyTab, selectedTab == emergencyTab);
    }

    private void clearTabStyles() {
        exploreTab.getStyleClass().removeAll("active-tab", "inactive-tab");
        myBookingsTab.getStyleClass().removeAll("active-tab", "inactive-tab");
        profileTab.getStyleClass().removeAll("active-tab", "inactive-tab");
        emergencyTab.getStyleClass().removeAll("active-tab", "inactive-tab");
    }

    private void applyTabStyle(ToggleButton tab, boolean isActive) {
        tab.getStyleClass().add(isActive ? "active-tab" : "inactive-tab");
    }
}