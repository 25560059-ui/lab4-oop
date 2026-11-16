import java.util.ArrayList;
import java.util.Scanner;

abstract class CourseRegistration {
    private String courseCode;
    private String courseName;
    private int credits;
    private int registeredStudents;

    // Overloaded constructor (0 registered students initially)
    public CourseRegistration(String courseCode, String courseName, int credits) {
        this(courseCode, courseName, credits, 0);
    }

    public CourseRegistration(String courseCode, String courseName, int credits, int registeredStudents) {
        setCourseCode(courseCode);
        setCourseName(courseName);
        setCredits(credits);
        setRegisteredStudents(registeredStudents);
    }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) {
        if (courseCode != null && !courseCode.trim().isEmpty()) {
            this.courseCode = courseCode.trim();
        } else if (this.courseCode == null) {
            this.courseCode = "CRS_UNKNOWN";
        }
    }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) {
        if (courseName != null && !courseName.trim().isEmpty()) {
            this.courseName = courseName.trim();
        } else if (this.courseName == null) {
            this.courseName = "Untitled Course";
        }
    }

    public int getCredits() { return credits; }
    public void setCredits(int credits) {
        if (credits >= 1) {
            this.credits = credits;
        } else if (this.credits < 1) {
            this.credits = 1;
        }
    }

    public int getRegisteredStudents() { return registeredStudents; }
    public void setRegisteredStudents(int registeredStudents) {
        if (registeredStudents >= 0) {
            this.registeredStudents = registeredStudents;
        } else if (this.registeredStudents < 0) {
            this.registeredStudents = 0;
        }
    }

    public abstract double tuitionPerStudent();
    public abstract String getCourseType();

    public double expectedTotalCollection() {
        return tuitionPerStudent() * registeredStudents;
    }

    public void displayInfo() {
        System.out.printf("Code: %-8s | Type: %-12s | Name: %-22s | Credits: %-2d | Students: %-3d | Tuition/Student: %,.0f VND | Expected Total: %,.0f VND",
                courseCode, getCourseType(), courseName, credits, registeredStudents, tuitionPerStudent(), expectedTotalCollection());
    }
}

class TheoryCourse extends CourseRegistration {
    public TheoryCourse(String courseCode, String courseName, int credits) {
        super(courseCode, courseName, credits, 0);
    }

    public TheoryCourse(String courseCode, String courseName, int credits, int registeredStudents) {
        super(courseCode, courseName, credits, registeredStudents);
    }

    @Override
    public double tuitionPerStudent() {
        return getCredits() * 450000.0;
    }

    @Override
    public String getCourseType() {
        return "TheoryCourse";
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println();
    }
}

class LabCourse extends CourseRegistration {
    private double equipmentFee;

    public LabCourse(String courseCode, String courseName, int credits, int registeredStudents, double equipmentFee) {
        super(courseCode, courseName, credits, registeredStudents);
        setEquipmentFee(equipmentFee);
    }

    public double getEquipmentFee() { return equipmentFee; }
    public void setEquipmentFee(double equipmentFee) {
        if (equipmentFee >= 0) {
            this.equipmentFee = equipmentFee;
        } else if (this.equipmentFee < 0) {
            this.equipmentFee = 0;
        }
    }

    @Override
    public double tuitionPerStudent() {
        return getCredits() * 600000.0 + equipmentFee;
    }

    @Override
    public String getCourseType() {
        return "LabCourse";
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.printf(" (Equip Fee: %,.0f VND)%n", equipmentFee);
    }
}

public class Exercise5 {
    private static ArrayList<CourseRegistration> courses = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== EXERCISE 5: UNIVERSITY COURSE REGISTRATION & TUITION ===");
            System.out.println("1. Add course registration");
            System.out.println("2. Load suggested sample test data");
            System.out.println("3. Display all courses and expected collections");
            System.out.println("4. Search course by course code");
            System.out.println("5. Statistics (Course counts, Largest collection, Total & Weighted average tuition)");
            System.out.println("0. Exit");
            int choice = readInt("Select an option: ", 0, 5);

            switch (choice) {
                case 1:
                    addCourse();
                    break;
                case 2:
                    loadSampleData();
                    break;
                case 3:
                    displayAll();
                    break;
                case 4:
                    searchByCode();
                    break;
                case 5:
                    showStatistics();
                    break;
                case 0:
                    System.out.println("Exiting Exercise 5.");
                    return;
            }
        }
    }

    private static CourseRegistration findByCode(String code) {
        for (CourseRegistration c : courses) {
            if (c.getCourseCode().equalsIgnoreCase(code.trim())) {
                return c;
            }
        }
        return null;
    }

    private static void addCourse() {
        int type = readInt("Select course type (1 - TheoryCourse, 2 - LabCourse): ", 1, 2);
        String code;
        while (true) {
            code = readNonEmptyString("Enter unique Course Code: ");
            if (findByCode(code) != null) {
                System.out.println("Error: Duplicate Course Code!");
            } else {
                break;
            }
        }
        String name = readNonEmptyString("Enter course name: ");
        int credits = readMinInt("Enter credits (>= 1): ", 1);
        int students = readMinInt("Enter registered students (>= 0): ", 0);

        if (type == 1) {
            courses.add(new TheoryCourse(code, name, credits, students));
        } else {
            double equipFee = readMinDouble("Enter equipment fee per student (>= 0): ", 0);
            courses.add(new LabCourse(code, name, credits, students, equipFee));
        }
        System.out.println("Course registration added successfully!");
    }

    private static void loadSampleData() {
        if (findByCode("IT002.T") == null) {
            courses.add(new TheoryCourse("IT002.T", "OOP Theory", 3, 20));
        }
        if (findByCode("IT002.L") == null) {
            courses.add(new LabCourse("IT002.L", "OOP Lab", 2, 10, 100000));
        }
        System.out.println("Sample data loaded (Theory: 27,000,000 VND; Lab: 13,000,000 VND; Total: 40,000,000 VND; Weighted Avg: 1,333,333.33 VND).");
    }

    private static void displayAll() {
        if (courses.isEmpty()) {
            System.out.println("Course list is empty.");
            return;
        }
        System.out.println("\n--- COURSE REGISTRATIONS ---");
        for (CourseRegistration c : courses) {
            c.displayInfo();
        }
    }

    private static void searchByCode() {
        if (courses.isEmpty()) {
            System.out.println("Course list is empty.");
            return;
        }
        String code = readNonEmptyString("Enter Course Code to search: ");
        CourseRegistration c = findByCode(code);
        if (c != null) {
            c.displayInfo();
        } else {
            System.out.println("No course found with code: " + code);
        }
    }

    private static void showStatistics() {
        if (courses.isEmpty()) {
            System.out.println("Course list is empty.");
            return;
        }
        int theoryCount = 0;
        int labCount = 0;
        double totalCollection = 0;
        int totalStudents = 0;
        CourseRegistration largestCourse = courses.get(0);

        for (CourseRegistration c : courses) {
            if (c instanceof TheoryCourse) theoryCount++;
            else if (c instanceof LabCourse) labCount++;

            double collection = c.expectedTotalCollection();
            totalCollection += collection;
            totalStudents += c.getRegisteredStudents();

            if (collection > largestCourse.expectedTotalCollection()) {
                largestCourse = c;
            }
        }

        System.out.println("\n--- TUITION & REGISTRATION STATISTICS ---");
        System.out.println("Theory Courses count       : " + theoryCount);
        System.out.println("Laboratory Courses count   : " + labCount);
        System.out.printf("Total Expected Collection  : %,.0f VND%n", totalCollection);
        if (totalStudents == 0) {
            System.out.println("Weighted Average Tuition   : N/A (total registered students = 0)");
        } else {
            double weightedAvg = totalCollection / totalStudents;
            System.out.printf("Weighted Average Tuition   : %,.2f VND/student%n", weightedAvg);
        }
        System.out.println("Course with Largest Expected Collection:");
        largestCourse.displayInfo();
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
                System.out.println("Error: Enter an integer between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid integer.");
            }
        }
    }

    private static int readMinInt(String prompt, int min) {
        while (true) {
            System.out.print(prompt);
            try {
                int val = Integer.parseInt(scanner.nextLine().trim());
                if (val >= min) return val;
                System.out.println("Error: Value must be >= " + min + ".");
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
