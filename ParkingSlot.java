/**
 * The ParkingSlot class represents one parking spot. 
 * It tracks the slot's ID, its type (staff or visitor), and the car parked there (if any).
 * It also calculates how long a car has been parked and figures out the parking fee.
 *
 * @author Bhawana Joshi
 * @version 1
 * @date 20/10/2024
 */
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ParkingSlot {
    
    private String id; // Unique ID for this parking slot
    private String type; // Type of slot (either "staff" or "visitor")
    private Car car; // The car parked in this slot (null if empty)

    // Constructor to set up a parking slot with its ID and type
    public ParkingSlot(String id, String type) {
        this.id = id;
        this.type = type;
        this.car = null; // Starts empty
    }

    // Get the slot's ID
    public String getId() {
        return id;
    }

    // Get the type of the slot (staff or visitor)
    public String getType() {
        return type;
    }

    // Check if there's a car parked in this slot. Returns true if occupied, false otherwise.
    public boolean isOccupied() {
        return car != null;
    }

    // Get the car parked in this slot (or null if it's empty)
    public Car getCar() {
        return car;
    }

    // Park a car in this slot and record the time it was parked
    public void parkCar(Car car) {
        this.car = car;
        this.car.setParkingTime(LocalDateTime.now()); // Save the current time when the car is parked
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println("Car parked at: " + car.getParkingTime().format(formatter)); // Print the parking time
    }

    // Remove the car from this slot, making it available again
    public void removeCar() {
        this.car = null;
    }
    
    // Calculate how long the car has been parked (in seconds)
    public long getParkingDurationInSeconds() {
        if (car == null || car.getParkingTime() == null) {
            return 0; // No car parked, so no time
        }
        // Calculate the time difference in seconds
        return Duration.between(car.getParkingTime(), LocalDateTime.now()).getSeconds();
    }

    /**
     * Calculate the parking fee based on how long the car has been parked.
     * Charges $5 per hour, rounding up for any part of an hour.
     * Returns the total fee.
     */
    public double calculateParkingFee() {
        long durationInSeconds = getParkingDurationInSeconds();
        long hours = (durationInSeconds + 3599) / 3600; // Round up to the next hour
        return hours * 5.0; // $5 per hour
    }
    
    // Return a summary of this slot’s status, including any parked car, how long it’s been parked, and the fee
    @Override
    public String toString() {
        if (!isOccupied()) {
            return "Slot ID: " + id + ", Type: " + type + ", Status: Unoccupied";
        } else {
            long totalDurationInSeconds = getParkingDurationInSeconds();
            long hours = totalDurationInSeconds / 3600;
            long minutes = (totalDurationInSeconds % 3600) / 60;
            long seconds = totalDurationInSeconds % 60;
            double fee = calculateParkingFee();
            return "Slot ID: " + id + ", Type: " + type + ", Status: Occupied by " + car.getRegNumber() +
                   " (Owner: " + car.getOwner() + "), Parked for: " + hours + " hours " + minutes + " minutes " + seconds + " seconds, Fee: $" + fee;
        }
    }
}
