package HospitalManagementSystem;

import java.sql.*;
import java.util.*;
//import java.util.InputMismatchException;
public class Patient {
    private Connection connection;
    private Scanner scanner;

    public Patient(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }

    public void addPatient() {
        try {
            System.out.print("Enter Patient Name: ");
            String name = scanner.next();

            if (!name.matches("[a-zA-Z]+")) { // Check if the input contains only letters
                System.out.println("Error: Input must contain only letters for name.");
                return; // Exit method if input is invalid
            }

            System.out.print("Enter Patient Age: ");
            int age = scanner.nextInt();

            // Validate if the age is a positive integer
            if (age <= 0) {
                System.out.println("Error: Age must be a positive integer.");
                return; // Exit method if input is invalid
            }

            System.out.print("Enter Patient Gender: ");
            String gender = scanner.next();

            if (!gender.matches("[a-zA-Z]+")) { // Check if the input contains only letters
                System.out.println("Error: Input must contain only letters for gender.");
                return; // Exit method if input is invalid
            }

            String query = "INSERT INTO patients(name, age, gender) VALUES(?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, gender);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Patient Added Successfully!!");
            } else {
                System.out.println("Failed to add Patient!!");
            }

        } catch (InputMismatchException e) {
            System.out.println("Error: Invalid input for age.");
            scanner.nextLine(); // Clear input buffer
        } catch (SQLException e) {
            e.printStackTrace(); // Handle database errors
        }
    }

    public void viewPatients(){
        String query = "select * from patients";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Patients: ");
            System.out.println("+------------+--------------------+----------+------------+");
            System.out.println("| Patient Id | Name               | Age      | Gender     |");
            System.out.println("+------------+--------------------+----------+------------+");
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                System.out.printf("| %-10s | %-18s | %-8s | %-10s |\n", id, name, age, gender);
                System.out.println("+------------+--------------------+----------+------------+");
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean getPatientById(int id){
        String query = "SELECT * FROM patients WHERE id = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }else{
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

}