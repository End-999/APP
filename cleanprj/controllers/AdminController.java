package org.example.cleanprj.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.cleanprj.dao.*;
import org.example.cleanprj.models.*;
import org.example.cleanprj.services.DataExportService;
import org.example.cleanprj.utils.SessionManager;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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

    // Analytics Components
    @FXML private Button refreshAnalyticsButton;
    @FXML private Label lastUpdatedLabel;

    // Summary Cards
    @FXML private Label totalTouristsLabel;
    @FXML private Label touristGrowthLabel;
    @FXML private Label activeGuidesLabel;
    @FXML private Label guideStatusLabel;
    @FXML private Label totalBookingsLabel;
    @FXML private Label bookingStatusLabel;
    @FXML private Label totalRevenueLabel;
    @FXML private Label revenueStatusLabel;

    // Charts - Large Size without Individual Scrolling
    @FXML private PieChart mainPieChart;
    @FXML private BarChart<String, Number> popularAttractionsChart;
    @FXML private CategoryAxis barChartXAxis;
    @FXML private NumberAxis barChartYAxis;
    @FXML private ComboBox<String> chartTypeComboBox;
    @FXML private ComboBox<String> barChartTypeComboBox;

    // Tourist Table
    @FXML private TableView<User> touristTable;
    @FXML private TableColumn<User, String> touristNameColumn;
    @FXML private TableColumn<User, String> touristNationalityColumn;
    @FXML private TableColumn<User, String> touristContactColumn;
    @FXML private TableColumn<User, String> touristEmergencyColumn;
    @FXML private TableColumn<User, String> touristRegDateColumn;
    @FXML private TableColumn<User, String> touristStatusColumn;
    @FXML private TableColumn<User, Void> touristActionColumn;
    @FXML private TextField touristSearchField;
    @FXML private ComboBox<String> touristNationalityFilter;

    // Guide Table
    @FXML private TableView<Guide> guideTable;
    @FXML private TableColumn<Guide, String> guideNameColumn;
    @FXML private TableColumn<Guide, String> guideLanguageColumn;
    @FXML private TableColumn<Guide, Integer> guideExperienceColumn;
    @FXML private TableColumn<Guide, String> guideSpecializationColumn;
    @FXML private TableColumn<Guide, Double> guideRatingColumn;
    @FXML private TableColumn<Guide, String> guideContactColumn;
    @FXML private TableColumn<Guide, String> guideStatusColumn;
    @FXML private TableColumn<Guide, Void> guideActionColumn;
    @FXML private TextField guideSearchField;
    @FXML private ComboBox<String> guideSpecializationFilter;

    // Attraction Table
    @FXML private TableView<Attraction> attractionTable;
    @FXML private TableColumn<Attraction, String> attractionNameColumn;
    @FXML private TableColumn<Attraction, String> attractionTypeColumn;
    @FXML private TableColumn<Attraction, String> attractionLocationColumn;
    @FXML private TableColumn<Attraction, String> attractionDifficultyColumn;
    @FXML private TableColumn<Attraction, String> attractionAltitudeColumn;
    @FXML private TableColumn<Attraction, String> attractionDurationColumn;
    @FXML private TableColumn<Attraction, String> attractionStatusColumn;
    @FXML private TableColumn<Attraction, Void> attractionActionColumn;
    @FXML private TextField attractionSearchField;
    @FXML private ComboBox<String> attractionTypeFilter;
    @FXML private ComboBox<String> attractionDifficultyFilter;

    // Booking Table
    @FXML private TableView<Booking> bookingTable;
    @FXML private TableColumn<Booking, String> bookingTouristColumn;
    @FXML private TableColumn<Booking, String> bookingAttractionColumn;
    @FXML private TableColumn<Booking, String> bookingDatesColumn;
    @FXML private TableColumn<Booking, Double> bookingCostColumn;
    @FXML private TableColumn<Booking, String> bookingStatusColumn;
    @FXML private TableColumn<Booking, String> bookingGuideColumn;

    // Emergency Table
    @FXML private TableView<EmergencyContact> emergencyTable;
    @FXML private TableColumn<EmergencyContact, String> emergencyNameColumn;
    @FXML private TableColumn<EmergencyContact, String> emergencyOrgColumn;
    @FXML private TableColumn<EmergencyContact, String> emergencyPhoneColumn;
    @FXML private TableColumn<EmergencyContact, String> emergencyTypeColumn;
    @FXML private TableColumn<EmergencyContact, String> emergencyLocationColumn;
    @FXML private TableColumn<EmergencyContact, String> emergency24_7Column;

    // DAOs
    private UserDAO userDAO;
    private GuideDAO guideDAO;
    private AttractionDAO attractionDAO;
    private BookingDAO bookingDAO;
    private EmergencyContactDAO emergencyDAO;
    private DataExportService exportService;

    // Data lists
    private ObservableList<User> touristList;
    private ObservableList<Guide> guideList;
    private ObservableList<Attraction> attractionList;
    private ObservableList<Booking> bookingList;
    private ObservableList<EmergencyContact> emergencyList;

    // Analytics data cache
    private Map<String, Object> analyticsCache = new HashMap<>();
    private LocalDateTime lastAnalyticsUpdate;

    // Color arrays for charts
    private final String[] PIE_COLORS = {
            "#FF6B6B", "#4ECDC4", "#45B7D1", "#96CEB4", "#FFEAA7",
            "#DDA0DD", "#98D8C8", "#F7DC6F", "#BB8FCE", "#85C1E9"
    };

    private final String[] BAR_COLORS = {
            "#FF9F43", "#10AC84", "#EE5A24", "#0984E3", "#A29BFE",
            "#FD79A8", "#FDCB6E", "#6C5CE7", "#00B894", "#E17055"
    };

    public void initialize() {
        initializeDAOs();
        initializeTables();
        setupTabNavigation();
        setupAnalyticsControls();
        setupChartStyling();
        setupTouristSearchAndFilter();
        setupGuideSearchAndFilter();
        setupAttractionSearchAndFilter();
        loadAllData();
        refreshAnalytics();
        showPane(analyticsPane);
        setActiveTab(analyticsTab);
    }

    private void initializeDAOs() {
        userDAO = new UserDAO();
        guideDAO = new GuideDAO();
        attractionDAO = new AttractionDAO();
        bookingDAO = new BookingDAO();
        emergencyDAO = new EmergencyContactDAO();
        exportService = new DataExportService();
    }

    private void setupAnalyticsControls() {
        // Set default values for combo boxes
        if (chartTypeComboBox != null) {
            chartTypeComboBox.setValue("Tourists by Nationality");
        }
        if (barChartTypeComboBox != null) {
            barChartTypeComboBox.setValue("Top Attractions");
        }
    }

    private void setupChartStyling() {
        // Enhanced styling for larger charts
        if (mainPieChart != null) {
            mainPieChart.setLegendVisible(true);
            mainPieChart.setLabelsVisible(true);
            mainPieChart.setStartAngle(90);
            mainPieChart.setClockwise(true);
            mainPieChart.setStyle("-fx-font-size: 14px;");
        }

        if (popularAttractionsChart != null) {
            popularAttractionsChart.setLegendVisible(true);
            popularAttractionsChart.setAnimated(true);
            popularAttractionsChart.setStyle("-fx-font-size: 14px;");

            // Style the axes for better readability
            if (barChartXAxis != null) {
                barChartXAxis.setTickLabelRotation(-45);
                barChartXAxis.setStyle("-fx-font-size: 12px;");
            }
            if (barChartYAxis != null) {
                barChartYAxis.setStyle("-fx-font-size: 12px;");
            }
        }
    }

    private void setupTouristSearchAndFilter() {
        // Setup search functionality
        if (touristSearchField != null) {
            touristSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
                filterTourists();
            });
        }

        // Setup nationality filter
        if (touristNationalityFilter != null) {
            touristNationalityFilter.setOnAction(e -> filterTourists());
        }
    }

    private void setupGuideSearchAndFilter() {
        // Setup search functionality for guides
        if (guideSearchField != null) {
            guideSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
                filterGuides();
            });
        }

        // Setup specialization filter for guides
        if (guideSpecializationFilter != null) {
            guideSpecializationFilter.setOnAction(e -> filterGuides());
        }
    }

    private void setupAttractionSearchAndFilter() {
        // Setup search functionality for attractions
        if (attractionSearchField != null) {
            attractionSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
                filterAttractions();
            });
        }

        // Setup type and difficulty filters for attractions
        if (attractionTypeFilter != null) {
            attractionTypeFilter.setOnAction(e -> filterAttractions());
        }
        if (attractionDifficultyFilter != null) {
            attractionDifficultyFilter.setOnAction(e -> filterAttractions());
        }
    }

    private void filterTourists() {
        if (touristList == null) return;

        String searchText = touristSearchField.getText().toLowerCase().trim();
        String selectedNationality = touristNationalityFilter.getValue();

        ObservableList<User> filteredList = touristList.stream()
                .filter(tourist -> {
                    // Search filter
                    boolean matchesSearch = searchText.isEmpty() ||
                            tourist.getFullName().toLowerCase().contains(searchText) ||
                            tourist.getEmail().toLowerCase().contains(searchText) ||
                            (tourist.getEmergencyContact() != null &&
                                    tourist.getEmergencyContact().toLowerCase().contains(searchText));

                    // Nationality filter
                    boolean matchesNationality = selectedNationality == null ||
                            selectedNationality.equals("All Nationalities") ||
                            tourist.getNationality().equals(selectedNationality);

                    return matchesSearch && matchesNationality;
                })
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        touristTable.setItems(filteredList);
    }

    private void filterGuides() {
        if (guideList == null) return;

        String searchText = guideSearchField.getText().toLowerCase().trim();
        String selectedSpecialization = guideSpecializationFilter.getValue();

        ObservableList<Guide> filteredList = guideList.stream()
                .filter(guide -> {
                    // Search filter
                    boolean matchesSearch = searchText.isEmpty() ||
                            guide.getName().toLowerCase().contains(searchText) ||
                            guide.getLanguages().toLowerCase().contains(searchText) ||
                            guide.getContact().toLowerCase().contains(searchText) ||
                            guide.getSpecialization().toLowerCase().contains(searchText);

                    // Specialization filter
                    boolean matchesSpecialization = selectedSpecialization == null ||
                            selectedSpecialization.equals("All Specializations") ||
                            guide.getSpecialization().equals(selectedSpecialization);

                    return matchesSearch && matchesSpecialization;
                })
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        guideTable.setItems(filteredList);
    }

    private void filterAttractions() {
        if (attractionList == null) return;

        String searchText = attractionSearchField.getText().toLowerCase().trim();
        String selectedType = attractionTypeFilter.getValue();
        String selectedDifficulty = attractionDifficultyFilter.getValue();

        ObservableList<Attraction> filteredList = attractionList.stream()
                .filter(attraction -> {
                    // Search filter
                    boolean matchesSearch = searchText.isEmpty() ||
                            attraction.getName().toLowerCase().contains(searchText) ||
                            attraction.getLocation().toLowerCase().contains(searchText) ||
                            attraction.getType().toLowerCase().contains(searchText) ||
                            attraction.getDescription().toLowerCase().contains(searchText);

                    // Type filter
                    boolean matchesType = selectedType == null ||
                            selectedType.equals("All Types") ||
                            attraction.getType().equals(selectedType);

                    // Difficulty filter
                    boolean matchesDifficulty = selectedDifficulty == null ||
                            selectedDifficulty.equals("All Difficulties") ||
                            attraction.getDifficulty().equals(selectedDifficulty);

                    return matchesSearch && matchesType && matchesDifficulty;
                })
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        attractionTable.setItems(filteredList);
    }

    @FXML
    private void refreshAnalytics() {
        try {
            System.out.println("Refreshing analytics data with colorful charts...");

            // Load fresh data
            loadAllData();

            // Update summary cards
            updateSummaryCards();

            // Update charts with colorful styling
            updatePieChart();
            updateBarChart();

            // Update last updated timestamp
            lastAnalyticsUpdate = LocalDateTime.now();
            if (lastUpdatedLabel != null) {
                lastUpdatedLabel.setText("Last updated: " +
                        lastAnalyticsUpdate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")));
            }

            System.out.println("Colorful analytics refresh completed successfully");

        } catch (Exception e) {
            System.err.println("Error refreshing analytics: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "Failed to refresh analytics: " + e.getMessage());
        }
    }

    private void updateSummaryCards() {
        try {
            // Calculate real statistics
            List<User> tourists = userDAO.findAll().stream()
                    .filter(user -> "Tourist".equals(user.getRole()))
                    .collect(Collectors.toList());

            List<Guide> guides = guideDAO.findAll();
            List<Booking> bookings = bookingDAO.findAll();
            List<Attraction> attractions = attractionDAO.findAll();

            // Update tourist card
            if (totalTouristsLabel != null) {
                totalTouristsLabel.setText(String.valueOf(tourists.size()));
            }
            if (touristGrowthLabel != null) {
                long recentTourists = tourists.stream()
                        .filter(t -> t.getRegistrationDate().isAfter(LocalDateTime.now().minusDays(30)))
                        .count();
                touristGrowthLabel.setText("+" + recentTourists + " this month");
            }

            // Update guides card
            if (activeGuidesLabel != null) {
                long activeGuides = guides.stream()
                        .filter(g -> "Active".equals(g.getStatus()))
                        .count();
                activeGuidesLabel.setText(String.valueOf(activeGuides));
            }
            if (guideStatusLabel != null) {
                guideStatusLabel.setText(guides.size() + " total guides");
            }

            // Update bookings card
            if (totalBookingsLabel != null) {
                totalBookingsLabel.setText(String.valueOf(bookings.size()));
            }
            if (bookingStatusLabel != null) {
                long pendingBookings = bookings.stream()
                        .filter(b -> "Pending".equals(b.getStatus()))
                        .count();
                bookingStatusLabel.setText(pendingBookings + " pending");
            }

            // Update revenue card
            double totalRevenue = bookings.stream()
                    .filter(b -> !"Cancelled".equals(b.getStatus()))
                    .mapToDouble(Booking::getTotalPrice)
                    .sum();

            if (totalRevenueLabel != null) {
                totalRevenueLabel.setText("$" + String.format("%.2f", totalRevenue));
            }
            if (revenueStatusLabel != null) {
                double avgBookingValue = bookings.isEmpty() ? 0 : totalRevenue / bookings.size();
                revenueStatusLabel.setText("Avg: $" + String.format("%.2f", avgBookingValue));
            }

            // Cache the data
            analyticsCache.put("tourists", tourists);
            analyticsCache.put("guides", guides);
            analyticsCache.put("bookings", bookings);
            analyticsCache.put("attractions", attractions);
            analyticsCache.put("totalRevenue", totalRevenue);

        } catch (IOException e) {
            System.err.println("Error updating summary cards: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void changeChartType() {
        updatePieChart();
    }

    @FXML
    private void changeBarChartType() {
        updateBarChart();
    }

    private void updatePieChart() {
        try {
            if (mainPieChart == null || chartTypeComboBox == null) return;

            String chartType = chartTypeComboBox.getValue();
            if (chartType == null) chartType = "Tourists by Nationality";

            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

            switch (chartType) {
                case "Tourists by Nationality":
                    @SuppressWarnings("unchecked")
                    List<User> tourists = (List<User>) analyticsCache.get("tourists");
                    if (tourists == null) {
                        tourists = userDAO.findAll().stream()
                                .filter(user -> "Tourist".equals(user.getRole()))
                                .collect(Collectors.toList());
                    }

                    Map<String, Long> nationalityCount = tourists.stream()
                            .collect(Collectors.groupingBy(User::getNationality, Collectors.counting()));

                    for (Map.Entry<String, Long> entry : nationalityCount.entrySet()) {
                        pieChartData.add(new PieChart.Data(entry.getKey() + " (" + entry.getValue() + ")", entry.getValue()));
                    }
                    mainPieChart.setTitle("Tourist Distribution by Nationality");
                    break;

                case "Bookings by Status":
                    @SuppressWarnings("unchecked")
                    List<Booking> bookings = (List<Booking>) analyticsCache.get("bookings");
                    if (bookings == null) bookings = bookingDAO.findAll();

                    Map<String, Long> statusCount = bookings.stream()
                            .collect(Collectors.groupingBy(Booking::getStatus, Collectors.counting()));

                    for (Map.Entry<String, Long> entry : statusCount.entrySet()) {
                        pieChartData.add(new PieChart.Data(entry.getKey() + " (" + entry.getValue() + ")", entry.getValue()));
                    }
                    mainPieChart.setTitle("Booking Status Distribution");
                    break;

                case "Attractions by Type":
                    @SuppressWarnings("unchecked")
                    List<Attraction> attractions = (List<Attraction>) analyticsCache.get("attractions");
                    if (attractions == null) attractions = attractionDAO.findAll();

                    Map<String, Long> typeCount = attractions.stream()
                            .collect(Collectors.groupingBy(Attraction::getType, Collectors.counting()));

                    for (Map.Entry<String, Long> entry : typeCount.entrySet()) {
                        pieChartData.add(new PieChart.Data(entry.getKey() + " (" + entry.getValue() + ")", entry.getValue()));
                    }
                    mainPieChart.setTitle("Attraction Type Distribution");
                    break;
            }

            mainPieChart.setData(pieChartData);

            // Apply different colors to pie chart segments
            applyPieChartColors();

        } catch (IOException e) {
            System.err.println("Error updating pie chart: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void applyPieChartColors() {
        // Apply colors after the chart is rendered
        javafx.application.Platform.runLater(() -> {
            for (int i = 0; i < mainPieChart.getData().size(); i++) {
                PieChart.Data data = mainPieChart.getData().get(i);
                if (data.getNode() != null) {
                    String color = PIE_COLORS[i % PIE_COLORS.length];
                    data.getNode().setStyle("-fx-pie-color: " + color + ";");
                }
            }
        });
    }

    private void updateBarChart() {
        try {
            if (popularAttractionsChart == null || barChartTypeComboBox == null) return;

            String chartType = barChartTypeComboBox.getValue();
            if (chartType == null) chartType = "Top Attractions";

            popularAttractionsChart.getData().clear();

            switch (chartType) {
                case "Top Attractions":
                    updateTopAttractionsChart();
                    break;
                case "Monthly Bookings":
                    updateMonthlyBookingsChart();
                    break;
                case "Guide Performance":
                    updateGuidePerformanceChart();
                    break;
            }

            // Enhanced styling for larger bar chart
            popularAttractionsChart.setBarGap(3);
            popularAttractionsChart.setCategoryGap(10);

            // Apply colors to bar chart
            applyBarChartColors();

        } catch (Exception e) {
            System.err.println("Error updating bar chart: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void applyBarChartColors() {
        // Apply colors after the chart is rendered
        javafx.application.Platform.runLater(() -> {
            for (int seriesIndex = 0; seriesIndex < popularAttractionsChart.getData().size(); seriesIndex++) {
                XYChart.Series<String, Number> series = popularAttractionsChart.getData().get(seriesIndex);
                for (int dataIndex = 0; dataIndex < series.getData().size(); dataIndex++) {
                    XYChart.Data<String, Number> data = series.getData().get(dataIndex);
                    if (data.getNode() != null) {
                        String color = BAR_COLORS[dataIndex % BAR_COLORS.length];
                        data.getNode().setStyle("-fx-bar-fill: " + color + ";");
                    }
                }
            }
        });
    }

    private void updateTopAttractionsChart() throws IOException {
        @SuppressWarnings("unchecked")
        List<Booking> bookings = (List<Booking>) analyticsCache.get("bookings");
        if (bookings == null) bookings = bookingDAO.findAll();

        Map<String, Long> attractionBookings = bookings.stream()
                .collect(Collectors.groupingBy(Booking::getAttractionName, Collectors.counting()));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Number of Bookings");

        attractionBookings.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(15)
                .forEach(entry -> {
                    String shortName = entry.getKey().length() > 20 ?
                            entry.getKey().substring(0, 17) + "..." : entry.getKey();
                    series.getData().add(new XYChart.Data<>(shortName, entry.getValue()));
                });

        popularAttractionsChart.getData().add(series);
        barChartXAxis.setLabel("Tourist Attractions");
        barChartYAxis.setLabel("Number of Bookings");
        popularAttractionsChart.setTitle("Top 15 Most Popular Attractions");
    }

    private void updateMonthlyBookingsChart() throws IOException {
        @SuppressWarnings("unchecked")
        List<Booking> bookings = (List<Booking>) analyticsCache.get("bookings");
        if (bookings == null) bookings = bookingDAO.findAll();

        Map<String, Long> monthlyBookings = bookings.stream()
                .collect(Collectors.groupingBy(
                        b -> b.getBookingDate().format(DateTimeFormatter.ofPattern("MMM yyyy")),
                        LinkedHashMap::new,
                        Collectors.counting()
                ));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Monthly Bookings");

        monthlyBookings.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue())));

        popularAttractionsChart.getData().add(series);
        barChartXAxis.setLabel("Month");
        barChartYAxis.setLabel("Number of Bookings");
        popularAttractionsChart.setTitle("Monthly Booking Trends");
    }

    private void updateGuidePerformanceChart() throws IOException {
        @SuppressWarnings("unchecked")
        List<Booking> bookings = (List<Booking>) analyticsCache.get("bookings");
        if (bookings == null) bookings = bookingDAO.findAll();

        Map<String, Long> guideBookings = bookings.stream()
                .filter(b -> b.getGuideName() != null && !b.getGuideName().isEmpty())
                .collect(Collectors.groupingBy(Booking::getGuideName, Collectors.counting()));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Bookings Handled");

        guideBookings.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(12)
                .forEach(entry -> series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue())));

        popularAttractionsChart.getData().add(series);
        barChartXAxis.setLabel("Tour Guides");
        barChartYAxis.setLabel("Number of Bookings");
        popularAttractionsChart.setTitle("Top 12 Guide Performance");
    }

    @FXML
    private void exportCurrentChart() {
        if (mainPieChart != null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Export Pie Chart Data");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv"));

            Stage stage = (Stage) mainPieChart.getScene().getWindow();
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                try {
                    exportService.exportPieChartData(mainPieChart, file.getAbsolutePath());
                    showAlert("Success", "Pie chart data exported successfully to: " + file.getName());
                } catch (IOException e) {
                    showAlert("Error", "Failed to export chart data: " + e.getMessage());
                }
            }
        }
    }

    @FXML
    private void exportBarChart() {
        if (popularAttractionsChart != null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Export Bar Chart Data");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv"));

            Stage stage = (Stage) popularAttractionsChart.getScene().getWindow();
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                try {
                    exportBarChartData(file.getAbsolutePath());
                    showAlert("Success", "Bar chart data exported successfully to: " + file.getName());
                } catch (IOException e) {
                    showAlert("Error", "Failed to export bar chart data: " + e.getMessage());
                }
            }
        }
    }

    private void exportBarChartData(String filePath) throws IOException {
        try (java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(filePath))) {
            writer.write("Category,Value\n");

            for (XYChart.Series<String, Number> series : popularAttractionsChart.getData()) {
                for (XYChart.Data<String, Number> data : series.getData()) {
                    writer.write(String.format("%s,%s\n",
                            escapeCSV(data.getXValue()),
                            data.getYValue().toString()));
                }
            }
        }
    }

    private String escapeCSV(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private void initializeTables() {
        // Tourist Table
        if (touristTable != null) {
            touristNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
            touristNationalityColumn.setCellValueFactory(new PropertyValueFactory<>("nationality"));
            touristContactColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
            touristEmergencyColumn.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getEmergencyContact() != null ?
                            cellData.getValue().getEmergencyContact() : "Not provided"));
            touristRegDateColumn.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getRegistrationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
            touristStatusColumn.setCellValueFactory(cellData -> new SimpleStringProperty("Active"));

            // Setup Action Column with Remove Button
            touristActionColumn.setCellFactory(new Callback<TableColumn<User, Void>, TableCell<User, Void>>() {
                @Override
                public TableCell<User, Void> call(final TableColumn<User, Void> param) {
                    final TableCell<User, Void> cell = new TableCell<User, Void>() {
                        private final Button removeButton = new Button("Remove");

                        {
                            removeButton.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 5 10;");
                            removeButton.setOnAction((ActionEvent event) -> {
                                User tourist = getTableView().getItems().get(getIndex());
                                removeTourist(tourist);
                            });
                        }

                        @Override
                        public void updateItem(Void item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                            } else {
                                setGraphic(removeButton);
                            }
                        }
                    };
                    return cell;
                }
            });

            // Add row selection listener for tourist details
            touristTable.setRowFactory(tv -> {
                TableRow<User> row = new TableRow<>();
                row.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2 && (!row.isEmpty())) {
                        User selectedTourist = row.getItem();
                        showTouristDetails(selectedTourist);
                    }
                });
                return row;
            });
        }

        // Guide Table
        if (guideTable != null) {
            guideNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            guideLanguageColumn.setCellValueFactory(new PropertyValueFactory<>("languages"));
            guideExperienceColumn.setCellValueFactory(new PropertyValueFactory<>("experience"));
            guideSpecializationColumn.setCellValueFactory(new PropertyValueFactory<>("specialization"));
            guideRatingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
            guideContactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));
            guideStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

            // Setup Action Column with Remove and Update Buttons
            guideActionColumn.setCellFactory(new Callback<TableColumn<Guide, Void>, TableCell<Guide, Void>>() {
                @Override
                public TableCell<Guide, Void> call(final TableColumn<Guide, Void> param) {
                    final TableCell<Guide, Void> cell = new TableCell<Guide, Void>() {
                        private final Button updateButton = new Button("Update");
                        private final Button removeButton = new Button("Remove");
                        private final HBox buttonBox = new HBox(5);

                        {
                            // Style the buttons
                            updateButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-size: 11px; -fx-padding: 4 8;");
                            removeButton.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-font-size: 11px; -fx-padding: 4 8;");

                            buttonBox.setAlignment(Pos.CENTER);
                            buttonBox.getChildren().addAll(updateButton, removeButton);

                            // Button actions
                            updateButton.setOnAction((ActionEvent event) -> {
                                Guide guide = getTableView().getItems().get(getIndex());
                                updateGuide(guide);
                            });

                            removeButton.setOnAction((ActionEvent event) -> {
                                Guide guide = getTableView().getItems().get(getIndex());
                                removeGuide(guide);
                            });
                        }

                        @Override
                        public void updateItem(Void item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                            } else {
                                setGraphic(buttonBox);
                            }
                        }
                    };
                    return cell;
                }
            });

            // Add row selection listener for guide details
            guideTable.setRowFactory(tv -> {
                TableRow<Guide> row = new TableRow<>();
                row.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2 && (!row.isEmpty())) {
                        Guide selectedGuide = row.getItem();
                        showGuideDetails(selectedGuide);
                    }
                });
                return row;
            });
        }

        // Attraction Table
        if (attractionTable != null) {
            attractionNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            attractionTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
            attractionLocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
            attractionDifficultyColumn.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
            attractionAltitudeColumn.setCellValueFactory(new PropertyValueFactory<>("altitude"));
            attractionDurationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
            attractionStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

            // Setup Action Column with Update and Remove Buttons for Attractions
            attractionActionColumn.setCellFactory(new Callback<TableColumn<Attraction, Void>, TableCell<Attraction, Void>>() {
                @Override
                public TableCell<Attraction, Void> call(final TableColumn<Attraction, Void> param) {
                    final TableCell<Attraction, Void> cell = new TableCell<Attraction, Void>() {
                        private final Button updateButton = new Button("Update");
                        private final Button removeButton = new Button("Remove");
                        private final HBox buttonBox = new HBox(5);

                        {
                            // Style the buttons
                            updateButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-size: 11px; -fx-padding: 4 8;");
                            removeButton.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-font-size: 11px; -fx-padding: 4 8;");

                            buttonBox.setAlignment(Pos.CENTER);
                            buttonBox.getChildren().addAll(updateButton, removeButton);

                            // Button actions
                            updateButton.setOnAction((ActionEvent event) -> {
                                Attraction attraction = getTableView().getItems().get(getIndex());
                                updateAttraction(attraction);
                            });

                            removeButton.setOnAction((ActionEvent event) -> {
                                Attraction attraction = getTableView().getItems().get(getIndex());
                                removeAttraction(attraction);
                            });
                        }

                        @Override
                        public void updateItem(Void item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                            } else {
                                setGraphic(buttonBox);
                            }
                        }
                    };
                    return cell;
                }
            });

            // Add row selection listener for attraction details
            attractionTable.setRowFactory(tv -> {
                TableRow<Attraction> row = new TableRow<>();
                row.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2 && (!row.isEmpty())) {
                        Attraction selectedAttraction = row.getItem();
                        showAttractionDetails(selectedAttraction);
                    }
                });
                return row;
            });
        }

        // Booking Table
        if (bookingTable != null) {
            bookingTouristColumn.setCellValueFactory(new PropertyValueFactory<>("touristName"));
            bookingAttractionColumn.setCellValueFactory(new PropertyValueFactory<>("attractionName"));
            bookingDatesColumn.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getStartDate() + " to " + cellData.getValue().getEndDate()));
            bookingCostColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
            bookingStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
            bookingGuideColumn.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getGuideName() != null ? cellData.getValue().getGuideName() : "Not Assigned"));
        }

        // Emergency Table
        if (emergencyTable != null) {
            emergencyNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            emergencyOrgColumn.setCellValueFactory(new PropertyValueFactory<>("organization"));
            emergencyPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
            emergencyTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
            emergencyLocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
            emergency24_7Column.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().isAvailable24_7() ? "Yes" : "No"));
        }
    }

    private void removeAttraction(Attraction attraction) {
        // Show confirmation dialog
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Attraction Removal");
        confirmAlert.setHeaderText("Are you sure you want to remove this attraction?");

        StringBuilder message = new StringBuilder();
        message.append("Attraction: ").append(attraction.getName()).append("\n");
        message.append("Type: ").append(attraction.getType()).append("\n");
        message.append("Location: ").append(attraction.getLocation()).append("\n");
        message.append("Difficulty: ").append(attraction.getDifficulty()).append("\n");
        message.append("Duration: ").append(attraction.getDuration()).append("\n");
        message.append("Price: $").append(String.format("%.2f", attraction.getPrice())).append("\n\n");
        message.append("⚠️ WARNING: This action will:\n");
        message.append("• Permanently delete the attraction from the system\n");
        message.append("• Remove all associated data\n");
        message.append("• Affect any existing bookings for this attraction\n");
        message.append("• Cannot be undone\n\n");
        message.append("Are you sure you want to proceed?");

        confirmAlert.setContentText(message.toString());

        // Customize buttons
        ButtonType yesButton = new ButtonType("Yes, Remove", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("Cancel", ButtonBar.ButtonData.NO);
        confirmAlert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == yesButton) {
            try {
                // Remove from database
                attractionDAO.delete(attraction.getId());

                // Update any bookings that were for this attraction
                List<Booking> bookings = bookingDAO.findAll();
                for (Booking booking : bookings) {
                    if (attraction.getName().equals(booking.getAttractionName())) {
                        // Cancel the booking or mark it as affected
                        booking.setStatus("Cancelled - Attraction Removed");
                        bookingDAO.update(booking);
                    }
                }

                // Remove from table
                attractionTable.getItems().remove(attraction);
                attractionList.remove(attraction);

                // Update filtered list if search/filter is active
                filterAttractions();

                // Refresh analytics to reflect the change
                refreshAnalytics();

                // Show success message
                showAlert("Success", "Attraction '" + attraction.getName() + "' has been successfully removed from the system.\n" +
                        "All associated bookings have been updated.");

                System.out.println("Attraction removed successfully: " + attraction.getName());

            } catch (IOException e) {
                System.err.println("Error removing attraction: " + e.getMessage());
                e.printStackTrace();
                showAlert("Error", "An error occurred while removing the attraction: " + e.getMessage());
            }
        }
    }

    private void updateAttraction(Attraction attraction) {
        try {
            // Create a dialog for updating attraction information
            Dialog<Attraction> dialog = new Dialog<>();
            dialog.setTitle("Update Attraction Information");
            dialog.setHeaderText("Update information for: " + attraction.getName());

            // Set the button types
            ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

            // Create the form
            VBox form = new VBox(10);
            form.setPrefWidth(500);

            TextField nameField = new TextField(attraction.getName());
            nameField.setPromptText("Attraction Name");

            ComboBox<String> typeComboBox = new ComboBox<>();
            typeComboBox.getItems().addAll("Trekking", "Cultural", "Wildlife", "Sightseeing", "Adventure", "Religious");
            typeComboBox.setValue(attraction.getType());

            TextField locationField = new TextField(attraction.getLocation());
            locationField.setPromptText("Location");

            ComboBox<String> difficultyComboBox = new ComboBox<>();
            difficultyComboBox.getItems().addAll("Easy", "Moderate", "Hard", "Extreme");
            difficultyComboBox.setValue(attraction.getDifficulty());

            TextField altitudeField = new TextField(attraction.getAltitude());
            altitudeField.setPromptText("Altitude (e.g., 1400m)");

            TextField durationField = new TextField(attraction.getDuration());
            durationField.setPromptText("Duration (e.g., 3 days)");

            TextField bestSeasonField = new TextField(attraction.getBestSeason());
            bestSeasonField.setPromptText("Best Season");

            TextArea descriptionArea = new TextArea(attraction.getDescription());
            descriptionArea.setPromptText("Description");
            descriptionArea.setPrefRowCount(3);

            TextField priceField = new TextField(String.valueOf(attraction.getPrice()));
            priceField.setPromptText("Price (USD)");

            ComboBox<String> statusComboBox = new ComboBox<>();
            statusComboBox.getItems().addAll("Active", "Inactive", "Maintenance", "Seasonal");
            statusComboBox.setValue(attraction.getStatus());

            form.getChildren().addAll(
                    new Label("Name:"), nameField,
                    new Label("Type:"), typeComboBox,
                    new Label("Location:"), locationField,
                    new Label("Difficulty:"), difficultyComboBox,
                    new Label("Altitude:"), altitudeField,
                    new Label("Duration:"), durationField,
                    new Label("Best Season:"), bestSeasonField,
                    new Label("Description:"), descriptionArea,
                    new Label("Price (USD):"), priceField,
                    new Label("Status:"), statusComboBox
            );

            dialog.getDialogPane().setContent(form);

            // Enable/Disable update button depending on whether fields are filled
            Button updateButton = (Button) dialog.getDialogPane().lookupButton(updateButtonType);
            updateButton.addEventFilter(ActionEvent.ACTION, event -> {
                if (nameField.getText().trim().isEmpty() ||
                        typeComboBox.getValue() == null ||
                        locationField.getText().trim().isEmpty() ||
                        difficultyComboBox.getValue() == null ||
                        altitudeField.getText().trim().isEmpty() ||
                        durationField.getText().trim().isEmpty() ||
                        bestSeasonField.getText().trim().isEmpty() ||
                        descriptionArea.getText().trim().isEmpty() ||
                        priceField.getText().trim().isEmpty()) {

                    showAlert("Validation Error", "Please fill in all required fields.");
                    event.consume();
                    return;
                }

                try {
                    double price = Double.parseDouble(priceField.getText().trim());
                    if (price < 0) {
                        showAlert("Validation Error", "Price must be a positive number.");
                        event.consume();
                        return;
                    }
                } catch (NumberFormatException e) {
                    showAlert("Validation Error", "Please enter a valid price.");
                    event.consume();
                    return;
                }
            });

            // Convert the result when the update button is clicked
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == updateButtonType) {
                    try {
                        attraction.setName(nameField.getText().trim());
                        attraction.setType(typeComboBox.getValue());
                        attraction.setLocation(locationField.getText().trim());
                        attraction.setDifficulty(difficultyComboBox.getValue());
                        attraction.setAltitude(altitudeField.getText().trim());
                        attraction.setDuration(durationField.getText().trim());
                        attraction.setBestSeason(bestSeasonField.getText().trim());
                        attraction.setDescription(descriptionArea.getText().trim());
                        attraction.setPrice(Double.parseDouble(priceField.getText().trim()));
                        attraction.setStatus(statusComboBox.getValue());
                        return attraction;
                    } catch (NumberFormatException e) {
                        return null;
                    }
                }
                return null;
            });

            Optional<Attraction> result = dialog.showAndWait();

            result.ifPresent(updatedAttraction -> {
                try {
                    // Update in database
                    attractionDAO.update(updatedAttraction);

                    // Update any bookings that reference this attraction
                    List<Booking> bookings = bookingDAO.findAll();
                    for (Booking booking : bookings) {
                        if (updatedAttraction.getId().equals(booking.getAttractionId())) {
                            booking.setAttractionName(updatedAttraction.getName());
                            bookingDAO.update(booking);
                        }
                    }

                    // Refresh the table
                    loadAttractionData();
                    filterAttractions();

                    // Refresh analytics
                    refreshAnalytics();

                    showAlert("Success", "Attraction information updated successfully!");
                    System.out.println("Attraction updated successfully: " + updatedAttraction.getName());

                } catch (IOException e) {
                    System.err.println("Error updating attraction: " + e.getMessage());
                    e.printStackTrace();
                    showAlert("Error", "Failed to update attraction: " + e.getMessage());
                }
            });

        } catch (Exception e) {
            System.err.println("Error opening update dialog: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "Failed to open update dialog: " + e.getMessage());
        }
    }

    private void showAttractionDetails(Attraction attraction) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Attraction Details");
        alert.setHeaderText("Attraction Information");

        StringBuilder details = new StringBuilder();
        details.append("Name: ").append(attraction.getName()).append("\n");
        details.append("Type: ").append(attraction.getType()).append("\n");
        details.append("Location: ").append(attraction.getLocation()).append("\n");
        details.append("Difficulty: ").append(attraction.getDifficulty()).append("\n");
        details.append("Altitude: ").append(attraction.getAltitude()).append("\n");
        details.append("Duration: ").append(attraction.getDuration()).append("\n");
        details.append("Best Season: ").append(attraction.getBestSeason()).append("\n");
        details.append("Price: $").append(String.format("%.2f", attraction.getPrice())).append("\n");
        details.append("Status: ").append(attraction.getStatus()).append("\n");
        details.append("Description: ").append(attraction.getDescription()).append("\n");
        details.append("Attraction ID: ").append(attraction.getId());

        alert.setContentText(details.toString());
        alert.showAndWait();
    }

    private void removeGuide(Guide guide) {
        // Show confirmation dialog
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Guide Removal");
        confirmAlert.setHeaderText("Are you sure you want to remove this guide?");

        StringBuilder message = new StringBuilder();
        message.append("Guide: ").append(guide.getName()).append("\n");
        message.append("Contact: ").append(guide.getContact()).append("\n");
        message.append("Specialization: ").append(guide.getSpecialization()).append("\n");
        message.append("Experience: ").append(guide.getExperience()).append(" years\n");
        message.append("Rating: ").append(guide.getRating()).append("/5.0\n\n");
        message.append("⚠️ WARNING: This action will:\n");
        message.append("• Permanently delete the guide's profile\n");
        message.append("• Remove all associated data\n");
        message.append("• Affect any existing bookings assigned to this guide\n");
        message.append("• Cannot be undone\n\n");
        message.append("Are you sure you want to proceed?");

        confirmAlert.setContentText(message.toString());

        // Customize buttons
        ButtonType yesButton = new ButtonType("Yes, Remove", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("Cancel", ButtonBar.ButtonData.NO);
        confirmAlert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == yesButton) {
            try {
                // Remove from database
                guideDAO.delete(guide.getId());

                // Update any bookings that were assigned to this guide
                List<Booking> bookings = bookingDAO.findAll();
                for (Booking booking : bookings) {
                    if (guide.getName().equals(booking.getGuideName())) {
                        booking.setGuideName(null);
                        booking.setGuideId(null);
                        bookingDAO.update(booking);
                    }
                }

                // Remove from table
                guideTable.getItems().remove(guide);
                guideList.remove(guide);

                // Update filtered list if search/filter is active
                filterGuides();

                // Refresh analytics to reflect the change
                refreshAnalytics();

                // Show success message
                showAlert("Success", "Guide '" + guide.getName() + "' has been successfully removed from the system.\n" +
                        "All associated bookings have been updated.");

                System.out.println("Guide removed successfully: " + guide.getName());

            } catch (IOException e) {
                System.err.println("Error removing guide: " + e.getMessage());
                e.printStackTrace();
                showAlert("Error", "An error occurred while removing the guide: " + e.getMessage());
            }
        }
    }

    private void updateGuide(Guide guide) {
        try {
            // Create a dialog for updating guide information
            Dialog<Guide> dialog = new Dialog<>();
            dialog.setTitle("Update Guide Information");
            dialog.setHeaderText("Update information for: " + guide.getName());

            // Set the button types
            ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

            // Create the form
            VBox form = new VBox(10);
            form.setPrefWidth(400);

            TextField nameField = new TextField(guide.getName());
            nameField.setPromptText("Guide Name");

            TextField languagesField = new TextField(guide.getLanguages());
            languagesField.setPromptText("Languages (comma separated)");

            TextField experienceField = new TextField(String.valueOf(guide.getExperience()));
            experienceField.setPromptText("Years of Experience");

            TextField contactField = new TextField(guide.getContact());
            contactField.setPromptText("Contact Information");

            TextField specializationField = new TextField(guide.getSpecialization());
            specializationField.setPromptText("Specialization");

            TextField ratingField = new TextField(String.valueOf(guide.getRating()));
            ratingField.setPromptText("Rating (0.0 - 5.0)");

            ComboBox<String> statusComboBox = new ComboBox<>();
            statusComboBox.getItems().addAll("Active", "Inactive", "On Leave");
            statusComboBox.setValue(guide.getStatus());

            form.getChildren().addAll(
                    new Label("Name:"), nameField,
                    new Label("Languages:"), languagesField,
                    new Label("Experience (years):"), experienceField,
                    new Label("Contact:"), contactField,
                    new Label("Specialization:"), specializationField,
                    new Label("Rating:"), ratingField,
                    new Label("Status:"), statusComboBox
            );

            dialog.getDialogPane().setContent(form);

            // Enable/Disable update button depending on whether fields are filled
            Button updateButton = (Button) dialog.getDialogPane().lookupButton(updateButtonType);
            updateButton.addEventFilter(ActionEvent.ACTION, event -> {
                if (nameField.getText().trim().isEmpty() ||
                        languagesField.getText().trim().isEmpty() ||
                        contactField.getText().trim().isEmpty() ||
                        specializationField.getText().trim().isEmpty()) {

                    showAlert("Validation Error", "Please fill in all required fields.");
                    event.consume();
                    return;
                }

                try {
                    int experience = Integer.parseInt(experienceField.getText().trim());
                    double rating = Double.parseDouble(ratingField.getText().trim());

                    if (experience < 0) {
                        showAlert("Validation Error", "Experience must be a positive number.");
                        event.consume();
                        return;
                    }

                    if (rating < 0.0 || rating > 5.0) {
                        showAlert("Validation Error", "Rating must be between 0.0 and 5.0.");
                        event.consume();
                        return;
                    }
                } catch (NumberFormatException e) {
                    showAlert("Validation Error", "Please enter valid numbers for experience and rating.");
                    event.consume();
                    return;
                }
            });

            // Convert the result when the update button is clicked
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == updateButtonType) {
                    try {
                        guide.setName(nameField.getText().trim());
                        guide.setLanguages(languagesField.getText().trim());
                        guide.setExperience(Integer.parseInt(experienceField.getText().trim()));
                        guide.setContact(contactField.getText().trim());
                        guide.setSpecialization(specializationField.getText().trim());
                        guide.setRating(Double.parseDouble(ratingField.getText().trim()));
                        guide.setStatus(statusComboBox.getValue());
                        return guide;
                    } catch (NumberFormatException e) {
                        return null;
                    }
                }
                return null;
            });

            Optional<Guide> result = dialog.showAndWait();

            result.ifPresent(updatedGuide -> {
                try {
                    // Update in database
                    guideDAO.update(updatedGuide);

                    // Update any bookings that reference this guide
                    List<Booking> bookings = bookingDAO.findAll();
                    for (Booking booking : bookings) {
                        if (updatedGuide.getId().equals(booking.getGuideId())) {
                            booking.setGuideName(updatedGuide.getName());
                            bookingDAO.update(booking);
                        }
                    }

                    // Refresh the table
                    loadGuideData();
                    filterGuides();

                    // Refresh analytics
                    refreshAnalytics();

                    showAlert("Success", "Guide information updated successfully!");
                    System.out.println("Guide updated successfully: " + updatedGuide.getName());

                } catch (IOException e) {
                    System.err.println("Error updating guide: " + e.getMessage());
                    e.printStackTrace();
                    showAlert("Error", "Failed to update guide: " + e.getMessage());
                }
            });

        } catch (Exception e) {
            System.err.println("Error opening update dialog: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "Failed to open update dialog: " + e.getMessage());
        }
    }

    private void showGuideDetails(Guide guide) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Guide Details");
        alert.setHeaderText("Guide Information");

        StringBuilder details = new StringBuilder();
        details.append("Name: ").append(guide.getName()).append("\n");
        details.append("Languages: ").append(guide.getLanguages()).append("\n");
        details.append("Experience: ").append(guide.getExperience()).append(" years\n");
        details.append("Contact: ").append(guide.getContact()).append("\n");
        details.append("Specialization: ").append(guide.getSpecialization()).append("\n");
        details.append("Rating: ").append(guide.getRating()).append("/5.0\n");
        details.append("Status: ").append(guide.getStatus()).append("\n");
        details.append("Guide ID: ").append(guide.getId());

        alert.setContentText(details.toString());
        alert.showAndWait();
    }

    private void removeTourist(User tourist) {
        // Show confirmation dialog
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Tourist Removal");
        confirmAlert.setHeaderText("Are you sure you want to remove this tourist?");

        StringBuilder message = new StringBuilder();
        message.append("Tourist: ").append(tourist.getFullName()).append("\n");
        message.append("Email: ").append(tourist.getEmail()).append("\n");
        message.append("Nationality: ").append(tourist.getNationality()).append("\n\n");
        message.append("⚠️ WARNING: This action will:\n");
        message.append("• Permanently delete the tourist's account\n");
        message.append("• Remove all associated data\n");
        message.append("• Affect any existing bookings\n");
        message.append("• Cannot be undone\n\n");
        message.append("Are you sure you want to proceed?");

        confirmAlert.setContentText(message.toString());

        // Customize buttons
        ButtonType yesButton = new ButtonType("Yes, Remove", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("Cancel", ButtonBar.ButtonData.NO);
        confirmAlert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == yesButton) {
            try {
                // Check if tourist is an admin (additional safety check)
                if ("Admin".equals(tourist.getRole())) {
                    showAlert("Error", "Cannot remove admin users!");
                    return;
                }

                // Remove from database
                boolean deleted = userDAO.delete(tourist.getId());

                if (deleted) {
                    // Remove from table
                    touristTable.getItems().remove(tourist);
                    touristList.remove(tourist);

                    // Update filtered list if search/filter is active
                    filterTourists();

                    // Refresh analytics to reflect the change
                    refreshAnalytics();

                    // Show success message
                    showAlert("Success", "Tourist '" + tourist.getFullName() + "' has been successfully removed from the system.");

                    System.out.println("Tourist removed successfully: " + tourist.getEmail());
                } else {
                    showAlert("Error", "Failed to remove tourist. Please try again.");
                }

            } catch (IllegalArgumentException e) {
                showAlert("Error", e.getMessage());
            } catch (IOException e) {
                System.err.println("Error removing tourist: " + e.getMessage());
                e.printStackTrace();
                showAlert("Error", "An error occurred while removing the tourist: " + e.getMessage());
            }
        }
    }

    private void showTouristDetails(User tourist) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Tourist Details");
        alert.setHeaderText("Tourist Information");

        StringBuilder details = new StringBuilder();
        details.append("Name: ").append(tourist.getFullName()).append("\n");
        details.append("Email: ").append(tourist.getEmail()).append("\n");
        details.append("Nationality: ").append(tourist.getNationality()).append("\n");
        details.append("Registration Date: ").append(tourist.getRegistrationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).append("\n");
        details.append("Emergency Contact: ").append(tourist.getEmergencyContact() != null ? tourist.getEmergencyContact() : "Not provided").append("\n");
        details.append("Role: ").append(tourist.getRole()).append("\n");
        details.append("User ID: ").append(tourist.getId());

        alert.setContentText(details.toString());
        alert.showAndWait();
    }

    private void setupTabNavigation() {
        analyticsTab.setOnAction(e -> {
            showPane(analyticsPane);
            setActiveTab(analyticsTab);
            refreshAnalytics(); // Refresh analytics when tab is selected
        });

        touristTab.setOnAction(e -> {
            showPane(touristPane);
            setActiveTab(touristTab);
            loadTouristData(); // Refresh tourist data
        });

        guideTab.setOnAction(e -> {
            showPane(guidePane);
            setActiveTab(guideTab);
            loadGuideData(); // Refresh guide data
        });

        attractionTab.setOnAction(e -> {
            showPane(attractionPane);
            setActiveTab(attractionTab);
            loadAttractionData(); // Refresh attraction data
        });

        bookingTab.setOnAction(e -> {
            showPane(bookingPane);
            setActiveTab(bookingTab);
            loadBookingData(); // Refresh booking data
        });

        emergencyTab.setOnAction(e -> {
            showPane(emergencyPane);
            setActiveTab(emergencyTab);
            loadEmergencyData(); // Refresh emergency data
        });
    }

    private void loadAllData() {
        loadTouristData();
        loadGuideData();
        loadAttractionData();
        loadBookingData();
        loadEmergencyData();
    }

    private void loadTouristData() {
        try {
            List<User> tourists = userDAO.findAll().stream()
                    .filter(user -> "Tourist".equals(user.getRole()))
                    .collect(Collectors.toList());

            touristList = FXCollections.observableArrayList(tourists);
            if (touristTable != null) {
                touristTable.setItems(touristList);
            }

            // Update nationality filter
            if (touristNationalityFilter != null) {
                List<String> nationalities = tourists.stream()
                        .map(User::getNationality)
                        .distinct()
                        .sorted()
                        .collect(Collectors.toList());

                ObservableList<String> nationalityOptions = FXCollections.observableArrayList();
                nationalityOptions.add("All Nationalities");
                nationalityOptions.addAll(nationalities);

                touristNationalityFilter.setItems(nationalityOptions);
                touristNationalityFilter.setValue("All Nationalities");
            }

            System.out.println("Loaded " + tourists.size() + " tourists in admin panel");

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load tourist data: " + e.getMessage());
        }
    }

    private void loadGuideData() {
        try {
            List<Guide> guides = guideDAO.findAll();
            guideList = FXCollections.observableArrayList(guides);
            if (guideTable != null) {
                guideTable.setItems(guideList);
            }

            // Update specialization filter
            if (guideSpecializationFilter != null) {
                List<String> specializations = guides.stream()
                        .map(Guide::getSpecialization)
                        .distinct()
                        .sorted()
                        .collect(Collectors.toList());

                ObservableList<String> specializationOptions = FXCollections.observableArrayList();
                specializationOptions.add("All Specializations");
                specializationOptions.addAll(specializations);

                guideSpecializationFilter.setItems(specializationOptions);
                guideSpecializationFilter.setValue("All Specializations");
            }

            System.out.println("Loaded " + guides.size() + " guides in admin panel");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load guide data: " + e.getMessage());
        }
    }

    private void loadAttractionData() {
        try {
            List<Attraction> attractions = attractionDAO.findAll();
            attractionList = FXCollections.observableArrayList(attractions);
            if (attractionTable != null) {
                attractionTable.setItems(attractionList);
            }

            // Update type and difficulty filters
            if (attractionTypeFilter != null) {
                List<String> types = attractions.stream()
                        .map(Attraction::getType)
                        .distinct()
                        .sorted()
                        .collect(Collectors.toList());

                ObservableList<String> typeOptions = FXCollections.observableArrayList();
                typeOptions.add("All Types");
                typeOptions.addAll(types);

                attractionTypeFilter.setItems(typeOptions);
                attractionTypeFilter.setValue("All Types");
            }

            if (attractionDifficultyFilter != null) {
                List<String> difficulties = attractions.stream()
                        .map(Attraction::getDifficulty)
                        .distinct()
                        .sorted()
                        .collect(Collectors.toList());

                ObservableList<String> difficultyOptions = FXCollections.observableArrayList();
                difficultyOptions.add("All Difficulties");
                difficultyOptions.addAll(difficulties);

                attractionDifficultyFilter.setItems(difficultyOptions);
                attractionDifficultyFilter.setValue("All Difficulties");
            }

            System.out.println("Loaded " + attractions.size() + " attractions in admin panel");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load attraction data: " + e.getMessage());
        }
    }

    @FXML
    public void loadBookingData() {
        try {
            List<Booking> bookings = bookingDAO.findAll();
            bookingList = FXCollections.observableArrayList(bookings);
            if (bookingTable != null) {
                bookingTable.setItems(bookingList);
            }
            System.out.println("Loaded " + bookings.size() + " bookings in admin panel");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load booking data: " + e.getMessage());
        }
    }

    private void loadEmergencyData() {
        try {
            List<EmergencyContact> contacts = emergencyDAO.findAll();
            emergencyList = FXCollections.observableArrayList(contacts);
            if (emergencyTable != null) {
                emergencyTable.setItems(emergencyList);
            }
            System.out.println("Loaded " + contacts.size() + " emergency contacts in admin panel");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load emergency contact data: " + e.getMessage());
        }
    }

    @FXML
    private void exportTouristData() {
        if (touristList != null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Export Tourist Data");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv"));

            Stage stage = (Stage) touristTable.getScene().getWindow();
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                try {
                    exportService.exportTouristData(touristList, file.getAbsolutePath());
                    showAlert("Success", "Tourist data exported successfully!");
                } catch (IOException e) {
                    showAlert("Error", "Failed to export data: " + e.getMessage());
                }
            }
        }
    }

    @FXML
    private void exportGuideData() {
        if (guideList != null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Export Guide Data");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv"));

            Stage stage = (Stage) guideTable.getScene().getWindow();
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                try {
                    exportService.exportGuideData(guideList, file.getAbsolutePath());
                    showAlert("Success", "Guide data exported successfully!");
                } catch (IOException e) {
                    showAlert("Error", "Failed to export guide data: " + e.getMessage());
                }
            }
        }
    }

    @FXML
    private void exportAttractionData() {
        if (attractionList != null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Export Attraction Data");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv"));

            Stage stage = (Stage) attractionTable.getScene().getWindow();
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                try {
                    exportService.exportAttractionData(attractionList, file.getAbsolutePath());
                    showAlert("Success", "Attraction data exported successfully!");
                } catch (IOException e) {
                    showAlert("Error", "Failed to export attraction data: " + e.getMessage());
                }
            }
        }
    }

    @FXML
    private void exportBookingData() {
        if (bookingList != null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Export Booking Data");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv"));

            Stage stage = (Stage) bookingTable.getScene().getWindow();
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                try {
                    exportService.exportBookingData(bookingList, file.getAbsolutePath());
                    showAlert("Success", "Booking data exported successfully!");
                } catch (IOException e) {
                    showAlert("Error", "Failed to export booking data: " + e.getMessage());
                }
            }
        }
    }

    @FXML
    private void exportEmergencyData() {
        if (emergencyList != null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Export Emergency Contact Data");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv"));

            Stage stage = (Stage) emergencyTable.getScene().getWindow();
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                try {
                    exportService.exportEmergencyContactData(emergencyList, file.getAbsolutePath());
                    showAlert("Success", "Emergency contact data exported successfully!");
                } catch (IOException e) {
                    showAlert("Error", "Failed to export emergency contact data: " + e.getMessage());
                }
            }
        }
    }

    public void openAddGuideForm(ActionEvent actionEvent) {
        openDialog("/org/example/cleanprj/views/AddNewGuideDialog.fxml", "Add New Guide", this::loadGuideData);
    }

    public void openAddAttractionDialog(ActionEvent actionEvent) {
        openDialog("/org/example/cleanprj/views/AddNewAttractionDialog.fxml", "Add New Attraction", this::loadAttractionData);
    }

    public void openAddContact(ActionEvent actionEvent) {
        openDialog("/org/example/cleanprj/views/AddEmergencyContactDialog.fxml", "Add Emergency Contact", this::loadEmergencyData);
    }

    private void openDialog(String fxmlPath, String title, Runnable onClose) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setScene(new Scene(root));

            stage.setOnHidden(e -> {
                onClose.run();
                // Refresh analytics when data changes
                if (analyticsPane.isVisible()) {
                    refreshAnalytics();
                }
            });
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open dialog: " + e.getMessage());
        }
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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void logout() {
        SessionManager.logout();
        try {
            Stage stage = (Stage) analyticsPane.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/cleanprj/views/hello-view.fxml"));
            Scene scene = new Scene(loader.load(), 1440, 850);
            String css = getClass().getResource("/org/example/cleanprj/styles/style.css").toExternalForm();
            scene.getStylesheets().add(css);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
