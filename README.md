Swinburne Car Park System 🚗🅿️
Overview

The Swinburne Car Park System is a Java-based graphical user interface (GUI) project developed in BlueJ. It allows users to efficiently manage staff and visitor parking slots. With this system, users can:

- Add and delete parking slots
- Park and remove cars
- Locate parked cars
- View real-time slot availability with a color-coded display

This project was built as part of an academic assignment to practice object-oriented programming (OOP) and GUI-based system design in Java.

Features
- Manage staff and visitor parking slots
- Park or remove cars with registration details
- Search and locate cars by registration number
- Real-time color-coded slot status:
  - Pink = Vacant slot
  - Red = Occupied slot
- Interactive GUI with buttons and click-based slot interactions
- Input validation and error handling for slot management

System Requirements
- Java 8 or higher
- BlueJ (recommended) or any Java IDE (IntelliJ, Eclipse, NetBeans)

Installation & Running
1. Clone the repository:
git clone https://github.com/Bhawanajoshi25/Car-Park-System.git
cd Car-Park-System
2. Open the project in BlueJ (or any Java IDE).
3. Compile all .java files.
4. Run the CarParkSystem class.
5. At startup, enter the number of staff slots and visitor slots when prompted.

GUI Overview

- Header Panel: Displays the system title.
- Parking Slots Panel: Grid of all parking slots (with IDs and type labels).
- Color Coding:
  - Pink = vacant
  - Red = occupied
- Action Buttons Panel:
1. Refresh Slots – Update display
2. Park Car – Add new car (registration & owner)
3. Remove Car – Remove car by registration
4. Find Car – Search and locate car by registration
5. Add Slot – Add staff/visitor slot
6. Delete Slot – Remove vacant slot
7. Exit – Close application

Interacting with Parking Slots
- Click a vacant slot: Option to park a car or delete the slot.
- Click an occupied slot: Option to remove the car or delete the slot.

How to Perform Common Actions

- Park a Car
1. Click Park Car or select a vacant slot.
2. Enter car registration number and owner’s name.
3. Specify staff or visitor type.

- Remove a Car
1. Click Remove Car or select an occupied slot.
2. Enter the car registration number.

- Find a Car
1. Click Find Car.
2. Enter the registration number.
System highlights the slot and shows details.

- Add/Delete Slots
1. Add Slot → Choose staff or visitor
2. Delete Slot → Only possible if slot is vacant

- Exit
Click Exit Application to close the system.

Project Structure
CarParkSystem/
 ├── src/
 │    ├── Car.java
 │    ├── CarPark.java
 │    ├── CarParkSystem.java
 │    └── ParkingSlot.java
 ├── README.md
 └── package.bluej   (optional, BlueJ config file)
