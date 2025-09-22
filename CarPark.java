/**
 * The CarPark class is all about managing parking slots. It handles adding and removing slots, 
 * finding and parking cars, and even checking if slots are occupied or not. 
 * Basically, it’s the brain behind the parking system.
 *
 * @author Bhawana Joshi
 * @version 1
 * @date 20/10/2024
 */
import java.util.ArrayList;
import java.util.List;

public class CarPark {
    private List<ParkingSlot> slots; // List that stores all the parking slots

    // Constructor that sets up the car park with the given number of staff and visitor slots
    public CarPark(int staffSlots, int visitorSlots) {
        slots = new ArrayList<>();
        // Create staff slots with IDs like "S01", "S02", etc.
        for (int i = 1; i <= staffSlots; i++) {
            slots.add(new ParkingSlot("S" + String.format("%02d", i), "staff"));
        }
        // Create visitor slots with IDs like "V01", "V02", etc.
        for (int i = 1; i <= visitorSlots; i++) {
            slots.add(new ParkingSlot("V" + String.format("%02d", i), "visitor"));
        }
    }
    
    // Method to get the list of parking slots
    public List<ParkingSlot> getSlots() {
        return slots; // Return the list of parking slots
    }

    
    // Method to add a slot, but only if a slot with the same ID doesn't already exist
    public void addSlot(ParkingSlot slot) {
        if (findSlotById(slot.getId()) == null) {
            slots.add(slot);
            System.out.println("Slot added successfully.");
        } else {
            System.out.println("Slot ID already exists.");
        }
    }
    
    // Method to delete a slot, but only if it's not occupied
    public void deleteSlot(String slotId) {
        ParkingSlot slot = findSlotById(slotId);
        if (slot != null && !slot.isOccupied()) {
            slots.remove(slot);
            System.out.println("Slot deleted successfully.");
        } else if (slot != null && slot.isOccupied()) {
            System.out.println("Cannot delete an occupied slot.");
        } else {
            System.out.println("Slot not found.");
        }
    }
    
    // Method to list all the slots and their current status (occupied/unoccupied)
    public void listAllSlots() {
        System.out.println("Here’s a list of all parking slots and their status:\n");
        for (ParkingSlot slot : slots) {
            System.out.println(slot);
        }
    }
    
    // Method to delete all slots that aren't occupied
    public void deleteAllUnoccupiedSlots() {
        slots.removeIf(slot -> !slot.isOccupied());
        System.out.println("All unoccupied slots have been deleted.");
    }

    // Method to park a car in a specific slot, if the slot is available and the car type matches the slot type
    public void parkCar(String slotId, Car car) {
        ParkingSlot slot = findSlotById(slotId);
        if (slot != null && !slot.isOccupied()) {
            if ((slot.getType().equals("staff") && car.isStaff()) ||
                (slot.getType().equals("visitor") && !car.isStaff())) {
                slot.parkCar(car);
                System.out.println("Car parked successfully.");
            } else {
                System.out.println("Car cannot be parked in this slot type.");
            }
        } else if (slot != null) {
            System.out.println("Slot is already occupied.");
        } else {
            System.out.println("Slot not found.");
        }
    }
    
    // Method to find a car by its registration number and show how long it's been parked and the fee
    public void findCar(String regNumber) {
        for (ParkingSlot slot : slots) {
            if (slot.isOccupied() && slot.getCar().getRegNumber().equals(regNumber)) {
                long totalDurationInSeconds = slot.getParkingDurationInSeconds();
                long hours = totalDurationInSeconds / 3600;
                long minutes = (totalDurationInSeconds % 3600) / 60;
                long seconds = totalDurationInSeconds % 60;
                double fee = slot.calculateParkingFee();
                System.out.println("Car found in slot: " + slot.getId() + " (Owner: " + slot.getCar().getOwner() + ")");
                System.out.println("Parked for: " + hours + " hours " + minutes + " minutes " + seconds + " seconds, Fee: $" + fee);
                return;
            }
        }
        System.out.println("Car not found.");
    }

    // Method to remove a car from its slot, based on its registration number
    public void removeCar(String regNumber) {
        for (ParkingSlot slot : slots) {
            if (slot.isOccupied() && slot.getCar().getRegNumber().equals(regNumber)) {
                slot.removeCar();
                System.out.println("Car removed successfully.");
                return;
            }
        }
        System.out.println("Car not found.");
    }

    //method to find a slot by its ID
    public ParkingSlot findSlotById(String slotId) {
        for (ParkingSlot slot : slots) {
            if (slot.getId().equals(slotId)) {
                return slot;
            }
        }
        return null;
    }
}
