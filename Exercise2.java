import java.util.ArrayList;
import java.util.Scanner;

abstract class Employee {
    private String id;
    private String fullName;
    private String department;

    public Employee(String id, String fullName, String department) {
        setId(id);
        setFullName(fullName);
        setDepartment(department);
    }

    public String getId() { return id; }
    public void setId(String id) {
        if (id != null && !id.trim().isEmpty()) {
            this.id = id.trim();
        } else if (this.id == null) {
            this.id = "EMP_UNKNOWN";
        }
    }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) {
        if (fullName != null && !fullName.trim().isEmpty()) {
            this.fullName = fullName.trim();
        } else if (this.fullName == null) {
            this.fullName = "Unknown";
        }
    }

    public String getDepartment() { return department; }
    public void setDepartment(String department) {
        if (department != null && !department.trim().isEmpty()) {
            this.department = department.trim();
        } else if (this.department == null) {
            this.department = "General";
        }
    }

    public abstract double calculateSalary();
    public abstract String getEmploymentType();

    public void displayInfo() {
        System.out.printf("ID: %-8s | Name: %-18s | Type: %-16s | Dept: %-12s | Salary: %,.0f VND%n",
                id, fullName, getEmploymentType(), department, calculateSalary());
    }
}

class FullTimeEmployee extends Employee {
    private double baseSalary;
    private double allowance;
    private int workdays;

    // Overloaded constructor (default 26 workdays)
    public FullTimeEmployee(String id, String fullName, String department, double baseSalary, double allowance) {
        this(id, fullName, department, baseSalary, allowance, 26);
    }

    public FullTimeEmployee(String id, String fullName, String department, double baseSalary, double allowance, int workdays) {
        super(id, fullName, department);
        setBaseSalary(baseSalary);
        setAllowance(allowance);
        setWorkdays(workdays);
    }

    public double getBaseSalary() { return baseSalary; }
    public void setBaseSalary(double baseSalary) {
        if (baseSalary >= 0) this.baseSalary = baseSalary;
    }

    public double getAllowance() { return allowance; }
    public void setAllowance(double allowance) {
        if (allowance >= 0) this.allowance = allowance;
    }

    public int getWorkdays() { return workdays; }
    public void setWorkdays(int workdays) {
        if (workdays >= 0 && workdays <= 31) {
            this.workdays = workdays;
        }
    }

    @Override
    public double calculateSalary() {
        return baseSalary * Math.min(workdays, 26) / 26.0 + allowance;
    }

    @Override
    public String getEmploymentType() {
        return "FullTimeEmployee";
    }
}

class PartTimeEmployee extends Employee {
    private double hoursWorked;
    private double hourlyRate;

    public PartTimeEmployee(String id, String fullName, String department, double hoursWorked, double hourlyRate) {
        super(id, fullName, department);
        setHoursWorked(hoursWorked);
        setHourlyRate(hourlyRate);
    }

    public double getHoursWorked() { return hoursWorked; }
    public void setHoursWorked(double hoursWorked) {
        if (hoursWorked >= 0) this.hoursWorked = hoursWorked;
    }

    public double getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(double hourlyRate) {
        if (hourlyRate >= 0) this.hourlyRate = hourlyRate;
    }

    @Override
    public double calculateSalary() {
        return hoursWorked * hourlyRate;
    }

    @Override
    public String getEmploymentType() {
        return "PartTimeEmployee";
    }
}

public class Exercise2 {
    private static ArrayList<Employee> employees = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== EXERCISE 2: EMPLOYEE PAYROLL MANAGEMENT ===");
            System.out.println("1. Add employee");
            System.out.println("2. Load suggested sample test data");
            System.out.println("3. Display payroll");
            System.out.println("4. Find employees by department (case-insensitive)");
            System.out.println("5. Search employee (Overloaded: by ID or by Name + Department)");
            System.out.println("6. Payroll statistics (Total payroll & Highest salary)");
            System.out.println("0. Exit");
            int choice = readInt("Select an option: ", 0, 6);

            switch (choice) {
                case 1:
                    addEmployee();
                    break;
                case 2:
                    loadSampleData();
                    break;
                case 3:
                    displayPayroll();
                    break;
                case 4:
                    findByDepartment();
                    break;
                case 5:
                    searchMenu();
                    break;
                case 6:
                    showPayrollStatistics();
                    break;
                case 0:
                    System.out.println("Exiting Exercise 2.");
                    return;
            }
        }
    }

    // Method Overloading #1: Find by ID
    public static Employee findEmployee(String id) {
        for (Employee emp : employees) {
            if (emp.getId().equalsIgnoreCase(id.trim())) {
                return emp;
            }
        }
        return null;
    }

    // Method Overloading #2: Find by Name and Department
    public static ArrayList<Employee> findEmployee(String name, String department) {
        ArrayList<Employee> result = new ArrayList<>();
        for (Employee emp : employees) {
            if (emp.getFullName().toLowerCase().contains(name.trim().toLowerCase()) &&
                emp.getDepartment().equalsIgnoreCase(department.trim())) {
                result.add(emp);
            }
        }
        return result;
    }

    private static void addEmployee() {
        int type = readInt("Select employee type (1 - FullTime, 2 - PartTime): ", 1, 2);
        String id;
        while (true) {
            id = readNonEmptyString("Enter unique Employee ID: ");
            if (findEmployee(id) != null) {
                System.out.println("Error: Duplicate Employee ID!");
            } else {
                break;
            }
        }
        String name = readNonEmptyString("Enter full name: ");
        String dept = readNonEmptyString("Enter department: ");

        if (type == 1) {
            double base = readMinDouble("Enter base salary (>= 0): ", 0);
            double allowance = readMinDouble("Enter allowance (>= 0): ", 0);
            int days = readInt("Enter workdays (0 - 31): ", 0, 31);
            employees.add(new FullTimeEmployee(id, name, dept, base, allowance, days));
        } else {
            double hours = readMinDouble("Enter hours worked (>= 0): ", 0);
            double rate = readMinDouble("Enter hourly rate (>= 0): ", 0);
            employees.add(new PartTimeEmployee(id, name, dept, hours, rate));
        }
        System.out.println("Employee added successfully!");
    }

    private static void loadSampleData() {
        if (findEmployee("FT01") == null) {
            employees.add(new FullTimeEmployee("FT01", "Nguyen Van An", "IT", 10400000, 1000000, 26));
        }
        if (findEmployee("PT01") == null) {
            employees.add(new PartTimeEmployee("PT01", "Tran Thi Binh", "HR", 40, 50000));
        }
        System.out.println("Sample data loaded (Full-time: 11,400,000 VND; Part-time: 2,000,000 VND).");
    }

    private static void displayPayroll() {
        if (employees.isEmpty()) {
            System.out.println("Employee list is empty.");
            return;
        }
        System.out.println("\n--- EMPLOYEE PAYROLL ---");
        for (Employee emp : employees) {
            emp.displayInfo();
        }
    }

    private static void findByDepartment() {
        if (employees.isEmpty()) {
            System.out.println("Employee list is empty.");
            return;
        }
        String dept = readNonEmptyString("Enter department to search: ");
        boolean found = false;
        System.out.println("\nEmployees in department '" + dept + "':");
        for (Employee emp : employees) {
            if (emp.getDepartment().equalsIgnoreCase(dept)) {
                emp.displayInfo();
                found = true;
            }
        }
        if (!found) {
            System.out.println("No employees found in department: " + dept);
        }
    }

    private static void searchMenu() {
        if (employees.isEmpty()) {
            System.out.println("Employee list is empty.");
            return;
        }
        int mode = readInt("Search mode (1 - By ID, 2 - By Name & Department): ", 1, 2);
        if (mode == 1) {
            String id = readNonEmptyString("Enter Employee ID: ");
            Employee emp = findEmployee(id);
            if (emp != null) {
                emp.displayInfo();
            } else {
                System.out.println("No employee found with ID: " + id);
            }
        } else {
            String name = readNonEmptyString("Enter Name: ");
            String dept = readNonEmptyString("Enter Department: ");
            ArrayList<Employee> matches = findEmployee(name, dept);
            if (matches.isEmpty()) {
                System.out.println("No matching employees found.");
            } else {
                for (Employee emp : matches) {
                    emp.displayInfo();
                }
            }
        }
    }

    private static void showPayrollStatistics() {
        if (employees.isEmpty()) {
            System.out.println("Employee list is empty.");
            return;
        }
        double totalPayroll = 0;
        Employee highestPaid = employees.get(0);

        for (Employee emp : employees) {
            double salary = emp.calculateSalary();
            totalPayroll += salary;
            if (salary > highestPaid.calculateSalary()) {
                highestPaid = emp;
            }
        }

        System.out.println("\n--- PAYROLL STATISTICS ---");
        System.out.printf("Total Monthly Payroll: %,.0f VND%n", totalPayroll);
        System.out.println("Employee with Highest Salary:");
        highestPaid.displayInfo();
    }

    private static String readNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) return input;
            System.out.println("Error: Input cannot be empty.");
        }
    }

    private static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int val = Integer.parseInt(scanner.nextLine().trim());
                if (val >= min && val <= max) return val;
                System.out.println("Error: Please enter an integer between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid integer.");
            }
        }
    }

    private static double readMinDouble(String prompt, double min) {
        while (true) {
            System.out.print(prompt);
            try {
                double val = Double.parseDouble(scanner.nextLine().trim());
                if (val >= min) return val;
                System.out.println("Error: Value must be >= " + min + ".");
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid number.");
            }
        }
    }
}
