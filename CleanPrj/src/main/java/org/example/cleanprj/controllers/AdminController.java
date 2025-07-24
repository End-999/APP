package org.example.cleanprj.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;

public class AdminController {

    @FXML private ToggleButton analyticsTab;
    @FXML private ToggleButton touristTab;
    @FXML private ToggleButton guideTab;
    @FXML private ToggleButton attractionTab;
    @FXML private ToggleButton bookingTab;
    @FXML private ToggleButton emergencyTab;

    @FXML private VBox analyticsPane;
    @FXML private VBox touristPane;
    @FXML private VBox guidePane;
    @FXML private VBox attractionPane;
    @FXML private VBox bookingPane;
    @FXML private VBox emergencyPane;

    public void initialize() {
        // Set default visible pane
        showPane(analyticsPane);
        setActiveTab(analyticsTab);

        analyticsTab.setOnAction(e -> {
            showPane(analyticsPane);
            setActiveTab(analyticsTab);
        });

        touristTab.setOnAction(e -> {
            showPane(touristPane);
            setActiveTab(touristTab);
        });

        guideTab.setOnAction(e -> {
            showPane(guidePane);
            setActiveTab(guideTab);
        });

        attractionTab.setOnAction(e -> {
            showPane(attractionPane);
            setActiveTab(attractionTab);
        });

        bookingTab.setOnAction(e -> {
            showPane(bookingPane);
            setActiveTab(bookingTab);
        });

        emergencyTab.setOnAction(e -> {
            showPane(emergencyPane);
            setActiveTab(emergencyTab);
        });
    }

    private void showPane(VBox paneToShow) {
        analyticsPane.setVisible(false);
        touristPane.setVisible(false);
        guidePane.setVisible(false);
        attractionPane.setVisible(false);
        bookingPane.setVisible(false);
        emergencyPane.setVisible(false);

        paneToShow.setVisible(true);
    }

    private void setActiveTab(ToggleButton activeTab) {
        ToggleButton[] allTabs = {analyticsTab, touristTab, guideTab, attractionTab, bookingTab, emergencyTab};

        for (ToggleButton tab : allTabs) {
            if (tab == activeTab) {
                tab.getStyleClass().remove("inactive-tab");
                if (!tab.getStyleClass().contains("active-tab")) {
                    tab.getStyleClass().add("active-tab");
                }
            } else {
                tab.getStyleClass().remove("active-tab");
                if (!tab.getStyleClass().contains("inactive-tab")) {
                    tab.getStyleClass().add("inactive-tab");
                }
            }
        }
    }
}
