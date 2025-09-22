/**
 * The CarParkSystem class is the main graphical user interface (GUI) for managing the car park.
 * It allows the user to add/remove parking slots, park/remove cars, find a car, and refresh the display.
 * The GUI uses JFrame to provide an interactive view of the car park.
 *
 * @author Bhawana Joshi
 * @version 1
 * @date 20/10/2024
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;

public class CarParkSystem extends JFrame {
    private JLabel[] parkingSlots; // Array to hold labels representing parking slots
    private String[] slotTypes; // Array to hold the type of each slot (staff or visitor)
    private boolean[] isOccupied; // Tracks if a slot is occupied
    private int visitorSlotsCount; // Number of visitor slots
    private int staffSlotsCount; // Number of staff slots
    private CarPark carPark; // Instance of CarPark to manage parking slots
    private JPanel parkingPanel; // Panel to display parking slots

    /**
     * Constructor to initialize the car park system GUI.
     * Prompts the user for the number of visitor and staff slots, sets up the GUI components.
     */
    public CarParkSystem() {
        // Get number of visitor and staff slots from user input
        visitorSlotsCount = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter number of visitor slots:"));
        staffSlotsCount = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter number of staff slots:"));

        int totalSlots = visitorSlotsCount + staffSlotsCount;
        slotTypes = new String[totalSlots];
        isOccupied = new boolean[totalSlots];
        parkingSlots = new JLabel[totalSlots];

        // Initialize CarPark instance
        carPark = new CarPark(staffSlotsCount, visitorSlotsCount);

        // Fill slotTypes array based on user input
        for (int i = 0; i < visitorSlotsCount; i++) {
            slotTypes[i] = "Visitor";
        }
        for (int i = visitorSlotsCount; i < totalSlots; i++) {
            slotTypes[i] = "Staff";
        }

        // Set frame properties
        setTitle("Swinburne Car Park System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // header panel with title and image
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        JLabel titleLabel = new JLabel("SWINBURNE CAR PARK SYSTEM", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        headerPanel.add(titleLabel, BorderLayout.NORTH);

        JLabel carIcon = new JLabel(new ImageIcon("car_icon.png"), JLabel.CENTER);
        headerPanel.add(carIcon, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);

        // Parking slots panel creation (based on visitor and staff slots count)
        parkingPanel = new JPanel(); // Changed to an instance variable
        parkingPanel.setLayout(new GridLayout((int) Math.ceil(totalSlots / 5.0), 5, 10, 10));

        initializeParkingSlots(); 
        add(parkingPanel, BorderLayout.CENTER);

        // Action buttons panel
        JPanel actionsPanel = new JPanel();
        actionsPanel.setLayout(new GridLayout(2, 4, 10, 10));

        JButton refreshButton = new JButton("Refresh Slots");
        JButton parkCarButton = new JButton("Park Car");
        JButton removeCarButton = new JButton("Remove Car");
        JButton exitButton = new JButton("Exit Application");
        JButton findCarButton = new JButton("Find Car");
        JButton deleteslotButton = new JButton("Delete slot");
        JButton addslotButton = new JButton("Add Parking slot");

        // Buttons to the action panel
        actionsPanel.add(refreshButton);
        actionsPanel.add(findCarButton);
        actionsPanel.add(deleteslotButton);
        actionsPanel.add(addslotButton);
        actionsPanel.add(parkCarButton);
        actionsPanel.add(removeCarButton);
        actionsPanel.add(exitButton);

        add(actionsPanel, BorderLayout.SOUTH);

        // Action listeners for buttons
        addslotButton.addActionListener(e -> {
            String result = addParkingSlot();
            JOptionPane.showMessageDialog(this, result);
            refreshParkingSlots(); // Refresh display after adding
        });
        deleteslotButton.addActionListener(e -> {
            String result = deleteParkingSlot();
            JOptionPane.showMessageDialog(this, result);
            refreshParkingSlots(); // Refresh display after deleting
        });
        parkCarButton.addActionListener(e -> parkCar());
        removeCarButton.addActionListener(e -> removeCar());
        findCarButton.addActionListener(e -> findCar());
        exitButton.addActionListener(e -> System.exit(0));
        refreshButton.addActionListener(e -> refreshParkingSlots());
    }

    /**
     * Initializes the parking slots panel by adding JLabels for each parking slot.
     * Updates the display based on the current status of each slot.
     */
    private void initializeParkingSlots() {
        parkingPanel.removeAll(); // Clear existing slots
        java.util.List<ParkingSlot> currentSlots = carPark.getSlots(); // Get the updated list of slots
        int totalSlots = currentSlots.size(); // Use updated number of slots
        parkingSlots = new JLabel[totalSlots]; // Update the parkingSlots array size

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (int i = 0; i < totalSlots; i++) {
            ParkingSlot slot = currentSlots.get(i);
            String slotId = slot.getId();
            String status = "<html><div style='text-align: center;'>" + slotId + "<br>(" + slot.getType() + ")<br>";

            if (slot.isOccupied()) {
                Car car = slot.getCar();
                LocalDateTime parkedTime = car.getParkingTime();
                Duration duration = Duration.between(parkedTime, LocalDateTime.now());

                long hours = duration.toHours();
                long minutes = duration.toMinutes() % 60;
                double fee = car.getParkingFee();

                String formattedTime = parkedTime.format(formatter);
                status += "Occupied by " + car.getOwner() + "<br>Car Reg. Number: " + car.getRegNumber() + "<br>Parked since: " + formattedTime + "<br>Duration: " + hours + "h " + minutes + "m" + "<br>Fee: $" + fee;
            } else {
                status += "Vacant";
            }
            status += "</div></html>";

            // Create JLabel with updated status
            JLabel parkingSlotLabel = new JLabel(status, JLabel.CENTER);
            parkingSlotLabel.setOpaque(true); // Making JLabel opaque to see background color
            parkingSlotLabel.setBackground(slot.isOccupied() ? Color.RED : Color.PINK); // Setting color based on occupancy
            parkingSlotLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Border for better visualization

            // Mouse listener to each slot to enable interaction
            parkingSlotLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    handleSlotInteraction(slot);
                }
            });

            parkingPanel.add(parkingSlotLabel);
        }

        parkingPanel.revalidate();
        parkingPanel.repaint();
    }
    
        private void parkCar() {
        // Prompt for car details
        String regNumber = JOptionPane.showInputDialog(this, "Enter Car Registration Number:");
        if (regNumber == null || regNumber.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Registration number cannot be empty.");
            return;
        }
    
        String owner = JOptionPane.showInputDialog(this, "Enter Owner Name:");
        if (owner == null || owner.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Owner name cannot be empty.");
            return;
        }
    
        int isStaffOption = JOptionPane.showConfirmDialog(this, "Is this a staff car?", "Car Type", JOptionPane.YES_NO_OPTION);
        boolean isStaff = (isStaffOption == JOptionPane.YES_OPTION);
    
        Car car = new Car(regNumber, owner, isStaff);
        car.setParkingTime(LocalDateTime.now());
    
        // Dialog to choose which slot to park in
        String[] slotOptions = carPark.getSlots().stream()
                .filter(slot -> !slot.isOccupied() && ((isStaff && slot.getType().equals("staff")) || (!isStaff && slot.getType().equals("visitor"))))
                .map(ParkingSlot::getId)
                .toArray(String[]::new);
    
        if (slotOptions.length == 0) {
            JOptionPane.showMessageDialog(this, "No available slots for the selected car type.");
            return;
        }
    
        String selectedSlotId = (String) JOptionPane.showInputDialog(this, "Choose a slot:",
                "Park Car", JOptionPane.QUESTION_MESSAGE, null, slotOptions, slotOptions[0]);
    
        if (selectedSlotId != null) {
            // Park the car in the selected slot
            ParkingSlot selectedSlot = carPark.findSlotById(selectedSlotId);
            if (selectedSlot != null) {
                selectedSlot.parkCar(car);
                JOptionPane.showMessageDialog(this, "Car parked successfully in slot " + selectedSlotId + ".");
                refreshParkingSlots();
            } else {
                JOptionPane.showMessageDialog(this, "Slot not found.");
            }
        }
    }


    /**
     * Handles interaction when a parking slot is clicked.
     * Allows the user to park a car, remove a car, or delete the slot depending on its status.
     *
     * @param slot The parking slot that was clicked.
     */
    private void handleSlotInteraction(ParkingSlot slot) {
        if (slot.isOccupied()) {
            // If the slot is occupied, provides options to remove the car or remove the slot
            String[] options = {"Remove Car", "Remove Slot"};
            int choice = JOptionPane.showOptionDialog(this,
                    "Slot " + slot.getId() + " is occupied by " + slot.getCar().getOwner() + ". What would you like to do?",
                    "Occupied Slot Interaction",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (choice == 0) {
                // Remove the car
                slot.removeCar();
                JOptionPane.showMessageDialog(this, "Car removed successfully from slot " + slot.getId() + ".");
                refreshParkingSlots(); // Refresh display after removing the car
            } else if (choice == 1) {
                // Remove the slot
                carPark.deleteSlot(slot.getId());
                JOptionPane.showMessageDialog(this, "Slot " + slot.getId() + " deleted successfully.");
                refreshParkingSlots(); // Refresh display after deleting the slot
            }
        } else {
            // If the slot is vacant, provides options to park a car or remove the slot
            String[] options = {"Park Car", "Remove Slot"};
            int choice = JOptionPane.showOptionDialog(this,
                    "Slot " + slot.getId() + " is vacant. What would you like to do?",
                    "Vacant Slot Interaction",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (choice == 0) {
                // Park a car in the selected slot
                parkCarInSlot(slot);
            } else if (choice == 1) {
                // Remove the slot
                carPark.deleteSlot(slot.getId());
                JOptionPane.showMessageDialog(this, "Slot " + slot.getId() + " deleted successfully.");
                refreshParkingSlots(); // Refresh display after deleting the slot
            }
        }
    }

    /**
     * Parks a car in the specified parking slot.
     * Prompts the user for car details and parks the car if the slot type matches the car type.
     *
     * @param slot The parking slot where the car will be parked.
     */
    private void parkCarInSlot(ParkingSlot slot) {
        // Prompt for car details
        String regNumber = JOptionPane.showInputDialog(this, "Enter Car Registration Number:");
        if (regNumber == null || regNumber.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Registration number cannot be empty.");
            return;
        }

        String owner = JOptionPane.showInputDialog(this, "Enter Owner Name:");
        if (owner == null || owner.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Owner name cannot be empty.");
            return;
        }

        int isStaffOption = JOptionPane.showConfirmDialog(this, "Is this a staff car?", "Car Type", JOptionPane.YES_NO_OPTION);
        boolean isStaff = (isStaffOption == JOptionPane.YES_OPTION);

        if ((isStaff && slot.getType().equalsIgnoreCase("visitor")) || (!isStaff && slot.getType().equalsIgnoreCase("staff"))) {
            JOptionPane.showMessageDialog(this, "Car type does not match the slot type.");
            return;
        }

        Car car = new Car(regNumber, owner, isStaff);
        car.setParkingTime(LocalDateTime.now());
        slot.parkCar(car);

        JOptionPane.showMessageDialog(this, "Car parked successfully in slot " + slot.getId() + ".");
        refreshParkingSlots(); // Refresh display after parking the car
    }

    /**
     * Refreshes the display of parking slots.
     */
    private void refreshParkingSlots() {
        initializeParkingSlots();
    }

    /**
     * Adds a new parking slot based on user input for slot type and slot ID.
     *
     * @return A message indicating success or failure of the slot addition.
     */
    private String addParkingSlot() {
        // Prompt for slot type first, as it determines the format for slot ID
        String[] slotTypes = {"staff", "visitor"};
        String slotType = (String) JOptionPane.showInputDialog(this, "Select Slot Type:",
                "Add Parking Slot", JOptionPane.QUESTION_MESSAGE, null, slotTypes, slotTypes[0]);

        if (slotType == null) {
            return "Slot type must be selected.";
        }

        // Prompt for slot ID
        String slotId = JOptionPane.showInputDialog(this, "Enter Slot ID (e.g., S01 or V01):");
        if (slotId == null || slotId.trim().isEmpty()) {
            return "Slot ID cannot be empty.";
        }

        // Validate the slot ID format based on slot type
        if (slotType.equalsIgnoreCase("staff") && !slotId.matches("S\\d{2}")) {
            return "Staff slot ID must start with 'S' followed by two digits (e.g., S01).";
        } else if (slotType.equalsIgnoreCase("visitor") && !slotId.matches("V\\d{2}")) {
            return "Visitor slot ID must start with 'V' followed by two digits (e.g., V01).";
        }

        // Create the new ParkingSlot
        ParkingSlot newSlot = new ParkingSlot(slotId, slotType);

        // Add the new slot to the CarPark
        if (carPark.findSlotById(slotId) == null) {
            carPark.addSlot(newSlot);
            refreshParkingSlots(); // Refresh display after adding
            return "Parking slot added successfully.";
        } else {
            return "Slot ID already exists.";
        }
    }

    /**
     * Deletes an existing parking slot based on user input for slot ID.
     *
     * @return A message indicating success or failure of the slot deletion.
     */
    private String deleteParkingSlot() {
        // Prompt for the slot ID to delete
        String slotId = JOptionPane.showInputDialog(this, "Enter Slot ID to Delete (e.g., S01 or V01):");
        if (slotId == null || slotId.trim().isEmpty()) {
            return "Slot ID cannot be empty.";
        }

        // Attempt to delete the slot
        ParkingSlot slot = carPark.findSlotById(slotId);
        if (slot == null) {
            return "Slot not found.";
        } else if (slot.isOccupied()) {
            return "Cannot delete an occupied slot.";
        } else {
            carPark.deleteSlot(slotId);
            refreshParkingSlots(); // Refresh display after deleting
            return "Slot deleted successfully.";
        }
    }

    /**
     * Handles removing a car based on user input for the car registration number.
     */
    private void removeCar() {
        // Prompt the user for the car registration number to remove
        String regNumber = JOptionPane.showInputDialog(this, "Enter Car Registration Number to Remove:");
        if (regNumber == null || regNumber.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Registration number cannot be empty.");
            return;
        }

        // Find the slot containing the car with the given registration number
        ParkingSlot slotToRemove = carPark.getSlots().stream()
                .filter(slot -> slot.isOccupied() && slot.getCar().getRegNumber().equals(regNumber))
                .findFirst()
                .orElse(null);

        if (slotToRemove == null) {
            JOptionPane.showMessageDialog(this, "Car with registration number " + regNumber + " not found.");
        } else {
            slotToRemove.removeCar();
            JOptionPane.showMessageDialog(this, "Car removed successfully from slot " + slotToRemove.getId() + ".");
            refreshParkingSlots();
        }
    }

    /**
     * Handles finding a car based on user input for the car registration number.
     */
    private void findCar() {
        // Prompt the user for the car registration number to find
        String regNumber = JOptionPane.showInputDialog(this, "Enter Car Registration Number to Find:");
        if (regNumber == null || regNumber.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Registration number cannot be empty.");
            return;
        }

        // Use the findCar method from CarPark class
        ParkingSlot slotToFind = carPark.getSlots().stream()
                .filter(slot -> slot.isOccupied() && slot.getCar().getRegNumber().equals(regNumber))
                .findFirst()
                .orElse(null);

        if (slotToFind == null) {
            JOptionPane.showMessageDialog(this, "Car with registration number " + regNumber + " not found.");
        } else {
            Car car = slotToFind.getCar();
            LocalDateTime parkedTime = car.getParkingTime();
            double fee = car.getParkingFee();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedTime = parkedTime.format(formatter);

            String message = "Car found in slot: " + slotToFind.getId() + "\n" +
                    "Parked since: " + formattedTime + "\n" +
                    "Parking fee: $" + fee;
            JOptionPane.showMessageDialog(this, message);
        }
    }

    /**
     * Main method to launch the car park system GUI.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        // Creates and show the application window
        SwingUtilities.invokeLater(() -> {
            CarParkSystem carParkSystem = new CarParkSystem();
            carParkSystem.setVisible(true);
        });
    }
}
