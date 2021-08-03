import java.io.Serializable;

public class Employee implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5473795322495926629L;

	private static int idCounter = 1;
	
	private int id;
	private String name;
	private String dept;
	private int salary;
	
	public Employee(String name, String dept, int salary) {
		this.id = idCounter++;
		this.name = name;
		this.dept = dept;
		this.salary = salary;
	}
	public boolean hasId(int i) {
		return (i == this.id) ? true : false;
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public int getSalary() {
		return salary;
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", name=" + name + ", dept=" + dept + ", salary=" + salary + "]";
	}
	
}
