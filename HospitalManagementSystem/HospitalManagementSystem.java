package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class HospitalManagementSystem {
	private static final String URL = "jdbc:mysql://localhost:3306/hospital";
	//private = outside of class url is not accessible;
	// static = esako use karne ke liye main method ke andar es class ka Object na banana pade ;
	// final = ek bar koi value dal diya jaye to throught the program vahi chale ;
	
	private static final String username = "root";
	private static final String password = "Hima@2003";
	
	
	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		Scanner scanner = new Scanner(System.in);
		System.out.println(" HOSPITAL MANAGEMENT SYSTEM ");
		
		try {
			Connection connection = DriverManager.getConnection(URL, username, password);
			Patient patient = new Patient(connection, scanner);
			Doctor doctor = new Doctor(connection);
			while(true) {
				System.out.println("1. Add Patients");
				System.out.println("2. Viwe Patients");
				System.out.println("3. Viwe Doctors ");
				System.out.println("4. Book Appointment");
				System.out.println("5. viweAppointment");
				System.out.println("6. Exit");
				System.out.print("Enter Your Choice : ");
				System.out.println();
				int Choice = scanner.nextInt();
				switch (Choice) {
				case 1: 
					// add patients
					patient.addPatient();
					System.out.println();
					break;
					
				case 2: 
					//Viwe Patients
					patient.viwePatients();
					break;
					
				case 3:
					//Viwe Doctors
					doctor.viweDoctors();
					break;
					
				case 4: 
					//Book Appointment
					BookAppointment(patient, doctor,connection ,scanner );
					System.out.println();
					break;
					
				case 5: 
					//Exit
					viweAppointment(connection);
					break;
					
					
				case 6: 
					System.out.println("THANK YOU FOR USING Hospital Managemennt System!!!");
                    System.out.println("Exiting System!");
                    return;
						 
					
				default:
					System.out.println("choose valid number: " + Choice);
//					throw new IllegalArgumentException("Unexpected value: " + Choice);
				}
				
			}
			
		} catch (SQLException e) {
			// TODO: handle exception
			System.out.println("Choose valid number.");
			e.printStackTrace();
		}
		
	
		
	}


	private static void viweAppointment( Connection connection) {
		// TODO Auto-generated method stub
		String query = "SELECT * FROM appointements";

		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
			System.out.println("appointements: ");
			System.out.println("+------------+------------------------------+-----------------------+------------------+");
			System.out.println("| Id         | patient_id                   | doctor_id             | appointment_date |");
			System.out.println("+------------+------------------------------+-----------------------+------------------+");
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String patientId = resultSet.getString("patient_id");
				String doctorId = resultSet.getString("doctor_id");
				String appointmentDate =resultSet.getString("appointment_date");
				System.out.printf("| %-10s | %-28s | %-21s | %-16s |\n" , id ,patientId ,doctorId , appointmentDate); // printf are use for formating
				System.out.println("+------------+------------------------------+-----------------------+------------------+");
				System.out.println();
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		
	
		
	}


	private static void BookAppointment(Patient patient, Doctor doctor, Connection connection , Scanner scanner) {
	    System.out.println("Enter Patient ID: ");
	    int patientId = scanner.nextInt();

	    System.out.println("Enter Doctor ID: ");
	    int doctorId = scanner.nextInt();

	    System.out.println("Enter appointment date (YYYY-MM-DD): ");
	    String appointmentDate = scanner.next();

	    // Validate Patient and Doctor
	    if (patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)) {
	        // Check Doctor Availability
	        if (checkDoctorAvilability(doctorId, appointmentDate, connection)) {
	            String appointmentQuery = "INSERT INTO appointements (patient_id, doctor_id, appointment_date) VALUES (?, ?, ?)";
	           
	            
	            try (PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery)) {
	                preparedStatement.setInt(1, patientId); // Set correct patient ID
	                preparedStatement.setInt(2, doctorId);  // Set correct doctor ID
	                preparedStatement.setString(3, appointmentDate);

	                int rowsAffected = preparedStatement.executeUpdate();
	                if (rowsAffected > 0) {
	                    System.out.println("Appointment Booked Successfully!");
	                } else {
	                    System.out.println("Failed to Book Appointment.");
	                }
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        } else {
	            System.out.println("Doctor is not available on this date: " + appointmentDate);
	        }
	    } else {
	        System.out.println("Patient or Doctor does not exist!");
	    }
	}



	private static boolean checkDoctorAvilability(int doctorId, String appointmentDate , Connection connection) { // why are use Connection instance   => because communicate JDBC to database for  check availability in database ;
		// TODO Auto-generated method stub
		String query = "SELECT COUNT(*) FROM  appointements WHERE doctor_id=? AND appointment_date= ?"; // COUNT(*) => mens that no for rows batayega ,by this Not retrieve a all data from  appointments TABLE ; if return 1 that mens doctor are not available on particular Date ;	
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, doctorId);
			preparedStatement.setString(2, appointmentDate);
			ResultSet resultSet = preparedStatement.executeQuery();
			System.out.println();
			if(resultSet.next()) {
				int count = resultSet.getInt(1); // Generally passing a in this field column of field name , but any time passing a integer value in this field it mens that location of INDEX ki phale wala indexx le rhe hai ki second wala ; 1 if doctor available of 0 if doctor is not available;
				if (count == 0 ) {
					
					return true ;
					
				}else {
					return false ;
					
				}
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			   e.printStackTrace();
		}
		
		return false;
	}
	
	
	
	
	
	
	
	
	
}
