import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EMSRunner {
	static Scanner input;

	public static void main(String[] args) {
		
		File file = new File("resources/employees.csv");
		input = new Scanner(System.in);
		
		try {
			if (!file.exists()) {
				file.createNewFile();
				System.out.println("Reading initial data from initialData.txt");
				
				// Write initial data
				File init = new File("resources/initialData.txt");
				List<Employee> empList = readFromFile(init);
				
				// Write file with updated list
				writeObjectsToFile(file, empList);
				
				// Display the first 10 records
				System.out.println("\nDisplaying the first 10 records:");
				listFromFile(file, 10);
				
			} else {
				System.out.println("Welcome back to Employee Management System 2000\n");
				
				// Display the first 10 records
				System.out.println("\nDisplaying the first 10 records:");
				listFromFile(file, 10);
			}
			
			while(true) {
			// Display initial prompt
			System.out.println("\nWhich action would you like to take (enter the respective key)?");
			System.out.println("(V)iew employee\t(M)odify employee\t(A)dd a new employee\t(D)elete existing employee\t(L)ist employees\t(S)ream options");
			
			// Get user input
			char c = Character.toLowerCase(input.next().charAt(0));
			
				// Invoke the appropriate method to handle user's request
				switch(c) {
					case 'v':
						// Display employee info
						displayEmployee(file);
						break;
					case 'm':
						callModifier(file);					
						break;
					case 'a':
						try {
							addNewEmployee(file);
						} catch (InvalidNameException e) {
							System.out.println("Invalid name, please try again!");
						}
						break;
					case 'd':
						deleteEmployee(file);
						break;
					case 'l':
						// Display the first 10 records
						System.out.println("\nDisplaying the first 10 records:");
						listFromFile(file, 10);
						break;
					case 's':
						streamOptions(file);
						break;
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void displayEmployee(File file) {

		try (ObjectInputStream reader = new ObjectInputStream(new FileInputStream(file))) {
			
			// Print the first 10 objects unless there are less than 10 elements
			@SuppressWarnings("unchecked")
			List<Employee> empList = (List<Employee>) reader.readObject();
			// Retrieve employee id from user
			System.out.println("Please enter an employee id");
			int empId = input.nextInt();

			reader.close();
			
			Employee employee = empList.get(empId - 1);
			System.out.println(employee);
			
			
		} catch(IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	public static void callModifier(File file) {
		System.out.println("Please enter an employee id to modify");
		int empId = input.nextInt();
		System.out.println("Which employee field would you like to modify?");
		System.out.println("(N)Name\t(D)Department\t(S)alary");
		char c = Character.toLowerCase(input.next().charAt(0));
		
		// Modify the record
		modifyObjectFromFile(file, empId, c);		
	}
	public static void addNewEmployee(File file) throws InvalidNameException{
		System.out.println("Please enter a first and last name:");
		String firstName = input.next();
		String lastName = input.next();
		String name = firstName + lastName;
		
		if (firstName.length() < 3) {
			throw new InvalidNameException();
		}
		
		System.out.println("Please enter a department");
		System.out.println("(I)T\t(H)R\t(S)ales\t(F)inance");
		
		char c = Character.toLowerCase(input.next().charAt(0));
		String department;
		switch(c) {
			case 'i':
				department = "IT";
				break;
			case 'h':
				department = "HR";
				break;
			case 's':
				department = "Sales";
				break;
			case 'f':
				department = "Finance";
				break;
			default:
				department = "";
				break;
		}
		
		System.out.println("Please enter employee's salary:");
		int salary = input.nextInt();
		
		// Create new employee
		Employee employee = new Employee(name, department, salary);

		try (ObjectInputStream reader = new ObjectInputStream(new FileInputStream(file))) {
			
			@SuppressWarnings("unchecked")
			List<Employee> empList = (List<Employee>) reader.readObject();
			empList.add(employee);
			
			reader.close();
			
			// Write new employee
			writeObjectsToFile(file, empList);
			
			
		} catch(IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	public static void deleteEmployee(File file) {
		try (ObjectInputStream reader = new ObjectInputStream(new FileInputStream(file))) {
			System.out.println("Please enter an employee id to delete:");
			int id = input.nextInt();
			
			@SuppressWarnings("unchecked")
			List<Employee> empList = (List<Employee>) reader.readObject();
			
			// Retrieve the info from the employee we're modifying
			empList.remove(id - 1);
						
			// If file exists, delete existing file and write a new one with an updated list
			if (file.exists()) {
				reader.close();
				System.out.println("Deleting employee with ID " + id + "...");
				
				// Write file with updated list
				writeObjectsToFile(file, empList);
			}	
			
		} catch(IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	private static void streamOptions(File file) {
		System.out.println("Please choose one of the following options:");
		System.out.println("(1) Show departments\t(2) Display N users");//\t(3) Department with the highest salaray average\t(4) Highest earner from a department");
		int choice = input.nextInt();
		List<Employee> empList = readFromFile(file);
		
		switch(choice) {
			case 1:
				String allDepts = empList.stream()
				.map(Employee::getDept)
				.distinct()
				.reduce((dept1, dept2) -> dept1 + ", " + dept2)
				.get();
				System.out.println(allDepts);
				break;
			case 2:
				System.out.println("How many users would you like to display?");
				int n = input.nextInt();
				empList.stream()
						.filter(e -> e.getId() <= n)
						.forEach(System.out::println);
				break;
			case 3:
				
				break;
			case 4:
				
				break;
			default:
				break;
			
		}
	}
	private static void writeObjectsToFile(File file, List<Employee> empList) {
		try (ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream(file))) {			
			writer.writeObject(empList);
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unchecked")
	public static List<Employee> readFromFile(File file) {	
	List<Employee> empList = new ArrayList<Employee>();
		
		try (ObjectInputStream reader = new ObjectInputStream(new FileInputStream(file))) {
			empList = (List<Employee>) reader.readObject();			
						
			reader.close();			
		} catch(IOException e) {
			e.printStackTrace();
		}  catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	
		return empList;
	}
	public static void listFromFile(File file, int n) {	
		
		try (ObjectInputStream reader = new ObjectInputStream(new FileInputStream(file))) {
		
			// Print the first 10 objects unless there are less than 10 elements
			@SuppressWarnings("unchecked")
			List<Employee> list = (List<Employee>) reader.readObject();
			int ceiling = list.size() <= n ? list.size() : n;
			
			// Print out the employee database
			for (int i = 0; i < ceiling; i++) {
				System.out.println(list.get(i));
			}
			reader.close();
			
		} catch(IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	
	}
	
	public static void modifyObjectFromFile(File file, int Id, char f) {
		try (ObjectInputStream reader = new ObjectInputStream(new FileInputStream(file))) {
			
			@SuppressWarnings("unchecked")
			List<Employee> empList = (List<Employee>) reader.readObject();
			
			// Retrieve the info from the employee we're modifying
			Employee newEmp = empList.remove(Id - 1);
			
			// Retrieve the field that should be modified
			switch(f) {
			case 'n':
				System.out.println("Please enter a name");
				String name = input.next();
				newEmp.setName(name);
				break;
			case 'd':
				System.out.println("Choose from one of the following departments:");
				System.out.println("(I)T\t(H)R\t(S)ales(F)inance");
				char c = Character.toLowerCase(input.next().charAt(0));
				String department;
				switch(c) {
					case 'i':
						department = "IT";
						break;
					case 'h':
						department = "HR";
						break;
					case 's':
						department = "Sales";
						break;
					case 'f':
						department = "Finance";
						break;
					default:
						department = "";
						break;
				}
				newEmp.setDept(department);
				break;
			case 's':
				System.out.println("Please enter a new salary");
				int salary = input.nextInt();
				newEmp.setSalary(salary);
				break;
			}
			
			
			empList.add(Id - 1, newEmp);
			
			// If file exists, delete existing file and write a new one with an updated list
			if (file.exists()) {
				reader.close();
				System.out.println("Modifying employee with ID " + Id);
				
				// Write file with updated list
				writeObjectsToFile(file, empList);
			}	
			
		} catch(IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}