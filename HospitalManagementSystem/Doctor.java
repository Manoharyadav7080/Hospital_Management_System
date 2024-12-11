package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Doctor {
	private Connection connection;
//	private Scanner scanner;

	public Doctor(Connection connction ) {
		this.connection = connction;
//		this.scanner = scanner;

	}
 

	public void viweDoctors() {
		String query = "SELECT * FROM doctors";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery(); // Actually resultset are hold a table data which
																	// are retrive to the database ;
			System.out.println("Doctors: ");
			System.out.println("+------------+------------------------------+-----------------------+");
			System.out.println("| Doctor Id  | Name                         | Specialization        |");
			System.out.println("+------------+------------------------------+-----------------------+");
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				String specialization = resultSet.getString("specialization");
				System.out.printf("| %-10s | %-28s | %-21s |\n" , id ,name ,specialization); // printf are use for formating
				System.out.println("+------------|------------------------------|-----------------------+");

			}

		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	public boolean getDoctorById(int id) {
		String query = "SELECT * FROM doctors WHERE id=?";

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
