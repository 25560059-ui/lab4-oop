import java.util.ArrayList;
import java.util.Scanner;

abstract class LibraryItem {
    private String id;
    private String title;
    private String author;
    private int daysBorrowed;

    // Constructor overloading: 3 parameters (default 1 day)
    public LibraryItem(String id, String title, String author) {
        this(id, title, author, 1);
    }

    // Parameterized constructor
    public LibraryItem(String id, String title, String author, int daysBorrowed) {
        setId(id);
        setTitle(title);
        setAuthor(author);
        setDaysBorrowed(daysBorrowed);
    }

    public String getId() { return id; }
    public void setId(String id) {
        if (id != null && !id.trim().isEmpty()) {
            this.id = id.trim();
        } else if (this.id == null) {
            this.id = "UNKNOWN_ID";
        }
    }

    public String getTitle() { return title; }
    public void setTitle(String title) {
        if (title != null && !title.trim().isEmpty()) {
            this.title = title.trim();
        } else if (this.title == null) {
            this.title = "Untitled";
        }
    }

    public String getAuthor() { return author; }
    public void setAuthor(String author) {
        if (author != null && !author.trim().isEmpty()) {
            this.author = author.trim();
        } else if (this.author == null) {
            this.author = "Unknown Author";
        }
    }

    public int getDaysBorrowed() { return daysBorrowed; }
    public void setDaysBorrowed(int daysBorrowed) {
        if (daysBorrowed >= 1) {
            this.daysBorrowed = daysBorrowed;
        } else if (this.daysBorrowed < 1) {
            this.daysBorrowed = 1;
        }
    }

    public abstract double calculateFee();
    public abstract String getType();

    public void displayInfo() {
        System.out.printf("ID: %-8s | Type: %-12s | Title: %-20s | Author: %-15s | Days: %-3d",
                id, getType(), title, author, daysBorrowed);
    }
}

class PrintedBook extends LibraryItem {
    private int pages;

    // Overloaded constructor
    public PrintedBook(String id, String title, String author, int pages) {
        super(id, title, author, 1);
        setPages(pages);
    }

    public PrintedBook(String id, String title, String author, int daysBorrowed, int pages) {
        super(id, title, author, daysBorrowed);
        setPages(pages);
    }

    public int getPages() { return pages; }
    public void setPages(int pages) {
        if (pages >= 1) {
            this.pages = pages;
        } else if (this.pages < 1) {
            this.pages = 1;
        }
    }

    @Override
    public double calculateFee() {
        return 5000.0 * getDaysBorrowed() + 10.0 * pages;
    }

    @Override
    public String getType() {
        return "PrintedBook";
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.printf(" | Pages: %-6d | Fee: %,.0f VND%n", pages, calculateFee());
    }
}

class Magazine extends LibraryItem {
    private int issueNumber;

    // Overloaded constructor
    public Magazine(String id, String title, String author, int issueNumber) {
        super(id, title, author, 1);
        setIssueNumber(issueNumber);
    }

    public Magazine(String id, String title, String author, int daysBorrowed, int issueNumber) {
        super(id, title, author, daysBorrowed);
        setIssueNumber(issueNumber);
    }

    public int getIssueNumber() { return issueNumber; }
    public void setIssueNumber(int issueNumber) {
        if (issueNumber >= 1) {
            this.issueNumber = issueNumber;
        } else if (this.issueNumber < 1) {
            this.issueNumber = 1;
        }
    }

    @Override
    public double calculateFee() {
        return 3000.0 * getDaysBorrowed() + 2000.0;
    }

    @Override
    public String getType() {
        return "Magazine";
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.printf(" | Issue: %-6d | Fee: %,.0f VND%n", issueNumber, calculateFee());
    }
}

public class Exercise1 {
    private static ArrayList<LibraryItem> items = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== EXERCISE 1: LIBRARY BORROWING FEE MANAGEMENT ===");
            System.out.println("1. Add N library items");
            System.out.println("2. Load suggested sample test data");
            System.out.println("3. Display all items and fees");
            System.out.println("4. Search item by ID");
            System.out.println("5. Statistics (Count by type, Total revenue, Highest fee)");
            System.out.println("0. Exit");
            int choice = readInt("Select an option: ", 0, 5);

            switch (choice) {
                case 1:
                    addItems();
                    break;
                case 2:
                    loadSampleData();
                    break;
                case 3:
                    displayAll();
                    break;
                case 4:
                    searchById();
                    break;
                case 5:
                    showStatistics();
                    break;
                case 0:
                    System.out.println("Exiting Exercise 1.");
                    return;
            }
        }
    }

    private static void addItems() {
        int n = readMinInt("Enter number of items to add (N >= 1): ", 1);
        for (int i = 0; i < n; i++) {
            System.out.println("\n--- Entering item " + (i + 1) + " of " + n + " ---");
            int type = readInt("Select type (1 - PrintedBook, 2 - Magazine): ", 1, 2);
            String id;
            while (true) {
                id = readNonEmptyString("Enter unique ID: ");
                if (findItemById(id) != null) {
                    System.out.println("Error: Duplicate ID! Please enter a different ID.");
                } else {
                    break;
                }
            }
            String title = readNonEmptyString("Enter title: ");
            String author = readNonEmptyString("Enter author: ");
            int days = readMinInt("Enter days borrowed (>= 1): ", 1);

            if (type == 1) {
                int pages = readMinInt("Enter number of pages (>= 1): ", 1);
                items.add(new PrintedBook(id, title, author, days, pages));
            } else {
                int issue = readMinInt("Enter issue number (>= 1): ", 1);
                items.add(new Magazine(id, title, author, days, issue));
            }
            System.out.println("Item added successfully!");
        }
    }

    private static void loadSampleData() {
        if (findItemById("B01") == null) {
            items.add(new PrintedBook("B01", "Java OOP Guide", "John Doe", 3, 200));
        }
        if (findItemById("M01") == null) {
            items.add(new Magazine("M01", "Tech Monthly", "IEEE", 3, 12));
        }
        System.out.println("Sample data loaded (PrintedBook: 200 pages, 3 days -> 17,000 VND; Magazine: issue 12, 3 days -> 11,000 VND).");
    }

    private static void displayAll() {
        if (items.isEmpty()) {
            System.out.println("The library item list is currently empty.");
            return;
        }
        System.out.println("\n--- LIBRARY ITEM LIST ---");
        for (LibraryItem item : items) {
            item.displayInfo(); // Polymorphic call
        }
    }

    private static LibraryItem findItemById(String id) {
        for (LibraryItem item : items) {
            if (item.getId().equalsIgnoreCase(id.trim())) {
                return item;
            }
        }
        return null;
    }

    private static void searchById() {
        if (items.isEmpty()) {
            System.out.println("The library item list is currently empty.");
            return;
        }
        String id = readNonEmptyString("Enter item ID to search: ");
        LibraryItem found = findItemById(id);
        if (found != null) {
            System.out.println("Item found:");
            found.displayInfo();
        } else {
            System.out.println("No item found with ID: " + id);
        }
    }

    private static void showStatistics() {
        if (items.isEmpty()) {
            System.out.println("The library item list is currently empty. Cannot compute statistics.");
            return;
        }
        int bookCount = 0;
        int magazineCount = 0;
        double totalRevenue = 0;
        LibraryItem highestFeeItem = items.get(0);

        for (LibraryItem item : items) {
            if (item instanceof PrintedBook) {
                bookCount++;
            } else if (item instanceof Magazine) {
                magazineCount++;
            }
            double fee = item.calculateFee(); // Dynamic polymorphism on LibraryItem reference
            totalRevenue += fee;
            if (fee > highestFeeItem.calculateFee()) {
                highestFeeItem = item;
            }
        }

        System.out.println("\n--- BORROWING STATISTICS ---");
        System.out.println("Printed Books count : " + bookCount);
        System.out.println("Magazines count     : " + magazineCount);
        System.out.printf("Total Revenue       : %,.0f VND%n", totalRevenue);
        System.out.println("Item with Highest Fee:");
        highestFeeItem.displayInfo();
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
                System.out.println("Error: Please enter a value between " + min + " and " + max + ".");
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
}
