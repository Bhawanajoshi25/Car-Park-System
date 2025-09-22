/**
 * This class represents a car. It keeps track of the car’s registration number,
 * owner’s name, whether it belongs to a staff member or visitor, and when it was parked.
 * It also helps calculate how long the car has been parked.
 * 
 * @author Bhawana Joshi
 * @version 1
 * @date 20/10/2024
 */

import java.time.LocalDateTime;

public class Car {

    private String regNumber; // The car's registration number (e.g., T2345)
    private String owner; // The owner's name
    private boolean isStaff; // true if the car belongs to a staff member, false if it's a visitor's car
    private LocalDateTime parkingTime; // The time when the car was parked (null if it's not parked yet)

    /**
     * Constructor to set up a new Car object with its registration number, owner, 
     * and whether it's a staff car.
     * 
     * @param regNumber The registration number of the car.
     * @param owner The name of the car owner.
     * @param isStaff true if the car belongs to a staff member, false otherwise.
     */
    public Car(String regNumber, String owner, boolean isStaff) {
        this.regNumber = regNumber;
        this.owner = owner;
        this.isStaff = isStaff;
        this.parkingTime = null; // Initially, the car hasn't been parked
    }

    /**
     * Gets the car's registration number.
     * 
     * @return The registration number of the car.
     */
    public String getRegNumber() {
        return regNumber;
    }

    /**
     * Gets the owner's name.
     * 
     * @return The name of the owner of the car.
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Checks if this car belongs to a staff member.
     * 
     * @return true if this is a staff car, false if it's a visitor car.
     */
    public boolean isStaff() {
        return isStaff;
    }

    /**
     * Gets the time when the car was parked.
     * 
     * @return The time the car was parked, or null if it has not been parked yet.
     */
    public LocalDateTime getParkingTime() {
        return parkingTime;
    }

    /**
     * Sets the time when the car was parked.
     * 
     * @param parkingTime The time when the car was parked.
     */
    public void setParkingTime(LocalDateTime parkingTime) {
        this.parkingTime = parkingTime;
    }

    /**
     * Calculates how long the car has been parked, in seconds.
     * 
     * @return The number of seconds since the car was parked, or 0 if it hasn't been parked.
     */
    public long getParkingDurationInSeconds() {
        if (parkingTime == null) {
            return 0; // Not parked yet
        }
        return java.time.Duration.between(parkingTime, LocalDateTime.now()).getSeconds();
    }

    /**
     * Calculates how long the car has been parked, in hours, rounding up to the nearest hour.
     * 
     * @return The number of hours the car has been parked (rounded up).
     */
    public long getParkingDurationInHours() {
        long durationInSeconds = getParkingDurationInSeconds();
        return (durationInSeconds + 3599) / 3600; // Round up any fraction of an hour
    }

    /**
     * Returns the parking fee based on the number of hours parked. The fee is $5 per hour.
     * 
     * @return The parking fee in dollars.
     */
    public double getParkingFee() {
        long hoursParked = getParkingDurationInHours();
        return hoursParked * 5; // Parking fee is $5 per hour
    }

    /**
     * Provides a string representation of the car.
     * 
     * @return A string with details about the car.
     */
    @Override
    public String toString() {
        return "Car[RegNumber=" + regNumber + ", Owner=" + owner + 
               ", Staff=" + (isStaff ? "Yes" : "No") + "]";
    }
}
