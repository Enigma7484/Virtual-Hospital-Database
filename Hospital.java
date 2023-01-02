package pe2;

import java.util.*;
/* PLEASE DO NOT MODIFY A SINGLE STATEMENT IN THE TEXT BELOW.
READ THE FOLLOWING CAREFULLY AND FILL IN THE GAPS

I hereby declare that all the work that was required to 
solve the following problem including designing the algorithms
and writing the code below, is solely my own and that I received
no help in creating this solution and I have not discussed my solution 
with anybody. I affirm that I have read and understood
the Senate Policy on Academic honesty at 
https://secretariat-policies.info.yorku.ca/policies/academic-honesty-senate-policy-on/
and I am well aware of the seriousness of the matter and the penalties that I will face as a 
result of committing plagiarism in this assignment.

BY FILLING THE GAPS,YOU ARE SIGNING THE ABOVE STATEMENTS.

Full Name: Omar Shamsul Haque	
Student Number: 218498642	
Course Section: M
*/

/**
 * This class implements a virtual Hospital.
 * @author Omar
 *
 */


public class Hospital {
	//creating ArrayLists for Patients, Physicians, Volunteers and Physician Administrators.
	Person person = new Person();
	Patient patient = new Patient();
	List<Patient> patientInformation = new ArrayList<Patient>();
	Physician physician = new Physician();
	List<Physician> physicianInformation = new ArrayList<Physician>();
	private Director director;
	Volunteer volunteer = new Volunteer();
	List<Volunteer> volunteerInformation = new ArrayList<Volunteer>();
	List<PhysicianAdministrator> physicianAdmin = new ArrayList<PhysicianAdministrator>();
	
	//Hospital assigns unique Employee ID and Patient ID.
	public static int employeeID = 100;
    public static int patientID = 1000;
	
    /**
     * The Director handles all the Physician Administrators.
     * @param director
     */
	public Hospital(Director director) {
		this.director = director;
	}
	
	/**
	 * This method admits a new patient to the hospital and adds the patient information to the hospital record
	 * and assigns a physician(no duplicates)
	 * @param patient
	 * @return true or false
	 */

	protected boolean admitPatient(Patient patient) {
        for (Patient p : patientInformation){
            if (p.equals(patient)){
                return false;
            }
        }
        for (Physician p : physicianInformation){
            if(p.hasMaximumpatient()) {
                continue;
            }
            p.assignPatient(patient);
            patient.setAssignedPhysician(p);
            patientInformation.add(patient);
            return true;
        }
        throw new NoSpaceException();
    }
	
	/**
	 * This method discharges a patient from the hospital and clears the patient information from the hospital record.
	 * @param patient
	 * @return true or false
	 */
	
	protected boolean dischargePatient(Patient patient) {
        for (Patient p : patientInformation){
            if (patient.equals(p)){
                for (Physician j : physicianInformation){
                    if(patient.getAssignedPhysician() == j){
                        patientInformation.remove(patient);
                        j.removePatient(patient);
                        return true;
                    }
                }
            }
        }
        return false;
        
    }
	
	/**
	 * This method hires a Volunteer, stores the Volunteer information and assigns the Volunteer to a Physician.
	 * @param volunteer
	 * @return true or false
	 */
	
	protected boolean hireVolunteer(Volunteer volunteer) {
        for (Volunteer i : volunteerInformation){
            if (i.equals(volunteer)){
                return false;
            }
        }
        for (Physician p : physicianInformation){
            if(p.hasMaxVolunteers()) {
                continue;
            }
            p.assignVolunteer(volunteer);
            volunteer.setAssignedPhysician(p);
            volunteerInformation.add(volunteer);    
            return true;
        }
        throw new NoSpaceException();
            
    }
	
	/**
	 * This method resigns a Volunteer from the hospital and removes the Volunteer information.
	 * @param volunteer
	 */
	
	
	protected void resignVolunteer(Volunteer volunteer) {
        
        for (Volunteer i : volunteerInformation){
            if (i.equals(volunteer)){
                if (i.getAssignedPhysician().extractValunterDetail().size() == 1){
                    throw new NoVolunteersException();
                }
                else{
                    i.getAssignedPhysician().extractValunterDetail().remove(i);
                    volunteerInformation.remove(i);
                    return;
                }
            }
        }
    }

	
	
	public void setHospDirector(Director director) {
		this.director = director;
	}
	
	public Director getHospDirector() {
		return director;
	}
	
	/**
	 * This method adds Physician Administrators but not any more than 3.
	 * @param admin1
	 * @return true or false
	 */

	public boolean addAdministrator(PhysicianAdministrator admin1) {
        if (physicianAdmin.size() >= 3){
            return false;
        }
        physicianAdmin.add(admin1);
        return true;
    }
	
	/**
	 * This method hires a Physician, adds the Physician information to the hospital record and the hired Physician is assigned to the designated Physician Administrator.
	 * @param physician1
	 * @return true or false
	 */

	public boolean hirePhysician(Physician physician1) {
        
        for (Physician i : physicianInformation){
            if (i.equals(physician1)){
                return false;
            }
        }
        if (physicianInformation.size() < 70){
            physicianInformation.add(physician1);
            for (PhysicianAdministrator p : physicianAdmin){
                if (p.getAdminSpecialtyType().equals(physician1.getSpecialty())){
                    physician1.setAdmin(p);
                    p.assignPhysician(physician1);
                }
            }
            return true;
        }else{
            return false;
        }
    }
	
	/**
	 * This method resigns a Physician, removes the Physician information from the hospital record.
	 * The Physician Administrator will assign Patients to the next available Physician.
	 * This method also reassigns Volunteers to the next available Physician.
	 * @param physician1
	 */

	public void resignPhysician(Physician physician1) {
	        
	        for (Physician i : physicianInformation){
	            if (i.equals(physician1)){
	                for (PhysicianAdministrator j : physicianAdmin){
	                    if (j.getAdminSpecialtyType().equals(i.getSpecialty())){
	                        if(j.extractPhysician().size() == 1){
	                            throw new NoSpecialtyException();
	                        }
	                    }
	                }
	                for (PhysicianAdministrator j : physicianAdmin){
	                    if (j.getAdminSpecialtyType().equals(i.getSpecialty())){
	                        List<Patient> curPatients = i.extractPatientDetail(); 
	                        List<Volunteer> curVolunteers = i.extractValunterDetail();
	                        physicianInformation.remove(i);
	                        for (Patient k : curPatients){
	                            for (Physician l : physicianInformation){
	                                if (!l.hasMaximumpatient()){
	                                    l.assignPatient(k);
	                                    k.setAssignedPhysician(l);
	                                    break;
	                                }
	                            }
	                        }
	                        for (Volunteer k : curVolunteers){
	                            for (Physician l : physicianInformation){
	                                if (!l.hasMaxVolunteers()){
	                                    l.assignVolunteer(k);
	                                    k.setAssignedPhysician(l);
	                                    break;
	                                }
	                            }
	                        }
	                        j.resignPhysician(i);
	                        return;
	                    }
	                }
	            }
	        }
	    }

	/**
	 * This method extracts all the Physician details stored in the hospital's Physician record as a sorted list of Physicians according to the Physician's full name.
	 * @return returns all the Physician details in the ArrayList named copy.
	 */
	public List<Physician> extractAllPhysicianDetails() {
	    ArrayList<Physician> copy = new ArrayList<>();
	    copy.addAll(physicianInformation);
	    Collections.sort(copy);
	    return copy;
	}
	/**
	 * This method extracts all the Patient Information stored in the hospital's Patient record as a sorted list of Patients according to the Patient's full name.
	 * @return returns all the Patient details in the ArrayList named copy.
	 */
	public List<Patient> extractAllPatientDetails() {
	    ArrayList<Patient> copy = new ArrayList<>();
	    copy.addAll(patientInformation);
	    Collections.sort(copy);
	    return copy;
	}
	/**
	 * This method extracts all the Volunteer Information stored in the hospital's Volunteer record as a sorted list of Volunteers according to the Volunteer's full name.
	 * @return returns all the Volunteer details in the ArrayList named copy.
	 */

	public List<Volunteer> extractAllVolunteerDetails() {
	    ArrayList<Volunteer> copy = new ArrayList<>();
	    copy.addAll(volunteerInformation);
	    Collections.sort(copy);
	    return copy;
	}
}

class IllegalArgumentException extends RuntimeException {
	public IllegalArgumentException() {
		super("");
  }
}

//This exception is thrown if there is no other Physician available in this particular Specialty.
class NoSpecialtyException extends RuntimeException {
	public NoSpecialtyException() {
		super("");
  }
}

//This exception is thrown when the hospital has reached the maximum capacity for Patients.
class NoSpaceException extends RuntimeException {
	public NoSpaceException() {
		super("");
	}
}

//This exception is thrown when there is no Volunteer available to cover for the Volunteer that resigned.
class NoVolunteersException extends RuntimeException {
	public NoVolunteersException() {
		super("");
	}
}

//This is the Person class which has information required by the following classes.
class Person {
	protected String firstName;
	protected String lastName;
	protected int age;
	protected String gender;
	protected String address;
	
	protected Person() {
		this.firstName = "";
		this.lastName = "";
		this.age = 0;
		this.gender = "";
		this.address = "";
	}
	
	protected Person(String firstName, String lastName, int age, String gender, String address) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		this.gender = gender;
		this.address = address;
	}
	
	//These are the Getter and Setter methods for all the Parameters.
	protected String getFirstName() {
		return firstName;
	}
	
	protected void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	protected String getLastName() {
		return lastName;
	}
	
	protected void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	protected int getAge() {
		return age;
	}
	
	protected void setAge(int age) {
		this.age = age;
	}
	
	protected String getGender() {
		return gender;
	}
	
	protected void setGender(String gender) {
		this.gender = gender;
	}
	
	protected String getAddress() {
		return address;
	}
	
	protected void setAddress(String address) {
		this.address = address;
	}
	
	protected String getName() {
		return this.firstName + ", " + this.lastName;
	}
}

//This is the Employee class extending Person class which collects all the information for the Employees.
class Employee extends Person {
	
	protected int employeeID;
	
	protected Employee() {
		
	}
	
	//assigns employeeID
	protected Employee(String firstName, String lastName, int age, String gender, String address) {
		super(firstName, lastName, age, gender, address);
		this.employeeID = Hospital.employeeID;
		Hospital.employeeID++;
	}
	
	protected Employee(String firstName, String lastName, int age, String gender, String address, int employeeID) {
		super(firstName, lastName, age, gender, address);
		this.employeeID = employeeID;
	}
	
	//Getter method for employeeID
	protected int getEmployeeID() {
		return employeeID;
	}
	
	//Setter method for employeeID
	protected void setEmployeeID(int employeeID) {
		this.employeeID = employeeID;
	}
	
	//Returns the Employee information as a String
	public String toString() {
		return "Employee ["+getEmployeeID()+", ["+getFirstName()+", "+getLastName()+", "+getAge()+", "+getGender()+", "+getAddress()+"]]";
	}
	
}

//This is the Patient class extending Person class which collects all the information for the Patients.
class Patient extends Person implements Comparable<Patient> {
	
	private Physician physician;
	protected int patientID;
	
	//This method is for ensuring no duplicates
	@Override
	public int compareTo(Patient o) {
		
		String fullName1 = this.firstName.toUpperCase() + this.lastName.toUpperCase();
		String fullName2 = o.firstName.toUpperCase() + o.lastName.toUpperCase();
		int value = fullName1.compareTo(fullName2);
		return value;
		
	}
	
	protected Patient() {
		
	}
	
	protected Patient(String firstName, String lastName, int age, String gender, String address) {
		super(firstName, lastName, age, gender, address);
		this.patientID = Hospital.patientID;
		Hospital.patientID++;
	}
	
	protected Patient(String firstName, String lastName, int age, String gender, String address, int patientID) {
		super(firstName, lastName, age, gender, address);
		this.patientID = patientID;
	}
	
	//Getter method for patientID
	protected int getPatientID() {
		return patientID;
	}
	
	//Setter method for patientID
	protected void setPatientID(int patientID) {
		this.patientID = patientID;
	}
	
	//Returns the Patient Information as a String
	public String toString() {
		return "Patient ["+getPatientID()+", ["+getFirstName()+", "+getLastName()+", "+getAge()+", "+getGender()+", "+getAddress()+"]]";
	}
	
	/**
	 * Getter method
	 * @return Returns the Physician that was assigned to the Patient.
	 */
	public Physician getAssignedPhysician() {		
		return physician;
	}
	
	/**
	 * This method clears the patient record upon discharge.
	 * @return true or false
	 */
	public boolean clearPatientRecord() {
		if(getAssignedPhysician() != null) {
			return false;
		}
		return true;
	}
	
	/**
	 * Setter method
	 * @param physician is the Physician assigned to the Patient
	 */
	public void setAssignedPhysician(Physician physician) {
		this.physician = physician;
	}
}

/**
 * This class Volunteer extends the Employee class and contains all information pertaining to Volunteer.
 * @author Omar
 *
 */

class Volunteer extends Employee implements Comparable<Volunteer> {
	
	private Physician physician;
	
	//This method is required to ensure no duplicate Volunteers.
	public int compareTo(Volunteer o) {
		
		String fullName1 = this.firstName.toUpperCase() + this.lastName.toUpperCase();
		String fullName2 = o.firstName.toUpperCase() + o.lastName.toUpperCase();
		int value = fullName1.compareTo(fullName2);
		return value;
		
	}
	
	protected Volunteer() {
		
	}
	
	protected Volunteer(String firstName, String lastName, int age, String gender, String address) {
		super(firstName, lastName, age, gender, address);
	}
	
	protected Volunteer(String firstName, String lastName, int age, String gender, String address, int employeeID) {
		super(firstName, lastName, age, gender, address, employeeID);
	}
	//Returns all the Volunteer informations as a String
	public String toString() {
		return "Volunteer [["+getEmployeeID()+",["+getFirstName()+", "+getLastName()+", "+getAge()+", "+getGender()+", "+getAddress()+"]]]"; 
	}
	
	/**
	 * Getter method 
	 * @return returns the Physician assigned to the Volunteer
	 */
	public Physician getAssignedPhysician() {		
		return physician;
	}
	
	/**
	 * Setter method
	 * @param physician is the Physician assigned to the Volunteer
	 */
	public void setAssignedPhysician(Physician physician) {
		this.physician = physician;
	}
}

class SalariedEmployee extends Employee {
	
	protected double salary;
	
	protected SalariedEmployee() {
		
	}
	
	protected SalariedEmployee(String firstName, String lastName, int age, String gender, String address) {
		super(firstName, lastName, age, gender, address);
	}
	
	protected SalariedEmployee(String firstName, String lastName, int age, String gender, String address, int employeeID, double salary) {
		super(firstName, lastName, age, gender, address, employeeID);
		this.salary = salary;
	}
	
	/**
	 * Getter method for/Gets the salary of a SalariedEmployee
	 * @return salary
	 */
	protected double getSalary() {
		return salary;
	}
	
	/**
	 * Setter method for/Sets the salary of a SalariedEmployee
	 * @param salary
	 */
	protected void setSalary(double salary) {
		this.salary = salary;
	}
	
	//returns the information for SalariedEmployee as a String
	public String toString() {
		return "SalariedEmployee ["+getEmployeeID()+", ["+getFirstName()+", "+getLastName()+", "+getAge()+", "+getGender()+", "+getAddress() + getSalary() + "]]";
	}
	
}

/**
 * This is the Administrator class which extends the SalariedEmployee class and contains all the required Administrator info
 * @author Omar
 *
 */
class Administrator extends SalariedEmployee {
	
	protected Administrator(String firstName, String lastName, int age, String gender, String address) {
		super(firstName, lastName, age, gender, address);
	}
	
	protected Administrator(String firstName, String lastName, int age, String gender, String address, int employeeID, double salary) {
		super(firstName, lastName, age, gender, address, employeeID, salary);
	}
	

	//returns Administrator info as a String
	public String toString() {
		return "Administrator ["+getEmployeeID()+", ["+getFirstName()+", "+getLastName()+", "+getAge()+", "+getGender()+", "+getAddress() + getSalary() + "]]";
	}
	
}

/**
 * This is the Physician class extending SalariedEmployee class, which contains all the Physician information
 * @author Omar
 *
 */
class Physician extends SalariedEmployee implements Comparable<Physician> {
	
	private String specialty;
	private PhysicianAdministrator admin;
	private List<Patient> patients = new ArrayList<Patient>();
	private List<Volunteer> volunteers = new ArrayList<Volunteer>();
	
	/**
	 * Method ensuring no duplicate Physicians
	 */
	@Override
	public int compareTo(Physician o) {
		
		String fullName1 = this.firstName.toUpperCase() + this.lastName.toUpperCase();
		String fullName2 = o.firstName.toUpperCase() + o.lastName.toUpperCase();
		int value = fullName1.compareTo(fullName2);
		return value;
		
	}
	
	/**
	 * This method removes the Patient from the Physician's record of Patients.
	 * @param patient is the Patient that is removed.
	 */
	public void removePatient(Patient patient) {
		patients.remove(patient);
	}

	protected Physician() {
		
	}
	
	protected Physician(String firstName, String lastName, int age, String gender, String address) {
		super(firstName, lastName, age, gender, address);
	}
	
	protected Physician(String firstName, String lastName, int age, String gender, String address, int employeeID, double salary) {
		super(firstName, lastName, age, gender, address, employeeID, salary);
	}
	
	//Method returning all Physician information as String
	public String toString() {
		 return "Physician [[["+getEmployeeID()+",["+getFirstName()+", "+getLastName()+", "+getAge()+", "+getGender()+", "+getAddress()+"]], " + getSalary() + "]]";
    }
	
	
	/**
	 * This method sets a PhysicianAdministrator named admin to the Physician.
	 * @param admin is the name of the PhysicianAdministrator
	 */
	public void setAdmin(PhysicianAdministrator admin){
        this.admin = admin;
    }
	
	/**
	 * Method checks whether the Physician p1 that is input as the Parameter of this method has the same employeeID as the Physician.
	 * @param p1
	 * @return true or false
	 */
	public boolean equals(Physician p1){
        return (this.employeeID == p1.employeeID);
    }

	/**
	 * This method sets the Specialty of the Physician
	 * @param string is the Specialty
	 */
	public void setSpecialty(String string) {
        if ((string != "Immunology") && (string != "Dermatology") && (string != "Neurology")){
            throw new IllegalArgumentException();
        }else{
            specialty = string;
        }
        
    }

	/**
	 * Getter method for the Specialty
	 * @return specialty
	 */
	public String getSpecialty() {
		return specialty;
	}

	/**
	 * This method extracts the Patient's details
	 * @return a List of Patient details named patients
	 */
	public List<Patient> extractPatientDetail() {
		return patients;
	}

	/**
	 * Extracts Volunteer's details
	 * @return a List of Volunteer details names volunteers
	 */
	public List<Volunteer> extractValunterDetail() {
		return volunteers;
	}

	/**
	 * This method assigns a Volunteer to a Physician but no more than 5 to a single Physician
	 * @param employee
	 * @return true or false
	 */
	public boolean assignVolunteer(Employee employee) {
        if (volunteers.size() >= 5){
            return false;
        }
        volunteers.add((Volunteer)employee);
        return true;
    }
	
	/**
	 * Assigns a Patient to the Physician
	 * @param patient is the Patient assigned to the Physician
	 */
	public void assignPatient(Patient patient) {
		patients.add(patient);
	}
	
	/**
	 * This method checks if the Physician has the maximum no. of volunteers
	 * @return true or false
	 */
	public boolean hasMaxVolunteers() {
		if(volunteers.size() == 5){
			return true;
		}
		else return false;
	}

	/**
	 * This method checks if the Physician has the maximum no. of patients
	 * @return true or false
	 */
	public boolean hasMaximumpatient() {
		if(patients.size() == 8){
			return true;
		}
		else return false;
	}
	
}

/**
 * This is the PhysicianAdmininstrator class extending Administrator class containing all PhysicianAdministrator info
 * @author Omar
 *
 */
class PhysicianAdministrator extends Administrator {
	
	private String specialty;
    private ArrayList<Physician> physicians = new ArrayList<Physician>();
	
	protected PhysicianAdministrator(String firstName, String lastName, int age, String gender, String address) {
		super(firstName, lastName, age, gender, address);
	}
	
	protected PhysicianAdministrator(String firstName, String lastName, int age, String gender, String address, int employeeID, double salary) {
		 super(firstName, lastName, age, gender, address, employeeID, salary);
	}

	//Returns the PhysicianAdministrator info as a String
	public String toString() {
		return "PhysicianAdministrator [[["+getEmployeeID()+",["+getFirstName()+", "+getLastName()+", "+getAge()+", "+getGender()+", "+getAddress()+"]], "+getSalary()+"], "+getAdminSpecialtyType()+"]";
	}

	/**
	 * Sets the specialty type of the PhysicianAdministrator
	 * @param string is the specialty type
	 */
	public void setAdminSpecialtyType(String string){
        if ((string != "Immunology") && (string != "Dermatology") && (string != "Neurology")){
            throw new IllegalArgumentException();
        }else{
            specialty = string;
        }
        
    }

	/**
	 * gets the specialty type
	 * @return specialty
	 */
	public String getAdminSpecialtyType() {
		return specialty;
	}
	
	/**
	 * This method assigns a Physician to the PhyisicanAdministrator
	 * @param p is the Physician assigned
	 * @return true or false
	 */
	public boolean assignPhysician(Physician p){
        if (physicians.size() < 25){
            physicians.add(p);
            return true;
        }
        return false;
    }
	
	/**
	 * This method removes a Physician from the record
	 * @param p is the Physician removed
	 */
	public void resignPhysician(Physician p){
        physicians.remove(p);
    }

	/**
	 * This method extracts the Physician details
	 * @return a List of Physicians named physicians 
	 */
	public List<Physician> extractPhysician() {
		return physicians;
	}
	
}

/**
 * This is the Director class extending Administrator class containing all the info on Director
 * @author Omar
 *
 */
class Director extends Administrator {
	
	List<PhysicianAdministrator>physicianAdministrators = new ArrayList<PhysicianAdministrator>();
	
	protected Director(String firstName, String lastName, int age, String gender, String address) {
		super(firstName, lastName, age, gender, address);
	}
	
	protected Director(String firstName, String lastName, int age, String gender, String address, int employeeID, double salary) {
		 super(firstName, lastName, age, gender, address, employeeID, salary);
	}
	
	//Returns all Director info
	public String toString() {
		return "Director ["+getEmployeeID()+", ["+getFirstName()+", "+getLastName()+", "+getAge()+", "+getGender()+", "+getAddress() + getSalary() + "]]";
	}

	/**
	 * This method assigns a PhysicianAdministrator to a Director but no more than 3 PhysicianAdministrators can be assigned to a director
	 * @param admin1 is the PhysicianAdministrator assigned to the Director
	 * @return true or false
	 */
	public boolean assignAdministrator(PhysicianAdministrator admin1) {
        
		if (physicianAdministrators.size() < 3){
            physicianAdministrators.add(admin1);
            return true;
        } else {
            return false;
        }
		
    }

	/**
	 * This method extracts all the Physician Administrator details
	 * @return a List of PhysicianAdministrators named physicianAdministrators 
	 */
	public List<PhysicianAdministrator> extractPhysicianAdmins() {
		return physicianAdministrators;
	}

}




