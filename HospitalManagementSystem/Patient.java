package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {
	private Connection connection;
	private Scanner scanner;

	public Patient(Connection connction, Scanner scanner) {
		this.connection = connction;
		this.scanner = scanner;

	}

	public void addPatient() {
		System.out.print("Enter patient Name: ");
		String name = scanner.next();
		System.out.print("Enter patient age: ");
		int age = scanner.nextInt();
		System.out.print("Enter patient Gender: ");
		String gender = scanner.next();

		try {
			String query = "INSERT INTO patients(name , age , gender) VALUES(?,?,?)";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, name);
			preparedStatement.setInt(2, age);
			preparedStatement.setString(3, gender);
			int affectedrow = preparedStatement.executeUpdate();
			if (affectedrow > 0) {
				System.out.println("patient added successfuly !!");
			} else {
				System.out.println("Failed to add patients !!");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void viwePatients() {
		String query = "SELECT * FROM patients";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery(); // Actually resultset are hold a table data which
																	// are retrive to the database ;
			System.out.println("Patients: ");
			System.out.println("+------------+------------------------------+------+----------------+");
			System.out.println("|Patients Id |Name                          | Age  |Gender          |");
			System.out.println("+------------+------------------------------+------+----------------+");
				while (resultSet.next()) {
					int id = resultSet.getInt("id");
					String name = resultSet.getString("name");
					int age = resultSet.getInt("age");
					String gender = resultSet.getString("gender");
					System.out.printf("|%-12s|%-30s|%-6s|%-16s|\n",id , name , age,gender); // printf are use for formating , \n for adding a new line 
					System.out.println("+------------+------------------------------+------+----------------+");	
			}
				
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	public boolean getPatientById(int id) {
		String query = "SELECT * FROM patients WHERE id=?";

		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return true;
			} else {
				return false;

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return false;

	}

}
