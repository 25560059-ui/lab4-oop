import java.util.ArrayList;
import java.util.Scanner;

abstract class Ticket {
    private String ticketId;
    private String movieTitle;
    private int quantity;

    // Constructor chaining: default 1 ticket
    public Ticket(String ticketId, String movieTitle) {
        this(ticketId, movieTitle, 1);
    }

    public Ticket(String ticketId, String movieTitle, int quantity) {
        setTicketId(ticketId);
        setMovieTitle(movieTitle);
        setQuantity(quantity);
    }

    public String getTicketId() { return ticketId; }
    public void setTicketId(String ticketId) {
        if (ticketId != null && !ticketId.trim().isEmpty()) {
            this.ticketId = ticketId.trim();
        } else if (this.ticketId == null) {
            this.ticketId = "TCK_UNKNOWN";
        }
    }

    public String getMovieTitle() { return movieTitle; }
    public void setMovieTitle(String movieTitle) {
        if (movieTitle != null && !movieTitle.trim().isEmpty()) {
            this.movieTitle = movieTitle.trim();
        } else if (this.movieTitle == null) {
            this.movieTitle = "Unknown Movie";
        }
    }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) {
        if (quantity >= 1) {
            this.quantity = quantity;
        } else if (this.quantity < 1) {
            this.quantity = 1;
        }
    }

    public abstract double getUnitPrice();
    public abstract double getDiscountRate();
    public abstract double calculateTotalPrice();
    public abstract String getTicketType();

    public double getDiscountAmount() {
        return getUnitPrice() * quantity * getDiscountRate();
    }

    public void displayInfo() {
        System.out.printf("ID: %-7s | Type: %-13s | Movie: %-18s | Qty: %-3d | Unit: %,.0f VND | Discount: %,.0f VND | Final Total: %,.0f VND%n",
                ticketId, getTicketType(), movieTitle, quantity, getUnitPrice(), getDiscountAmount(), calculateTotalPrice());
    }
}

class RegularTicket extends Ticket {
    private static final double UNIT_PRICE = 80000.0;

    // Constructor chaining
    public RegularTicket(String ticketId, String movieTitle) {
        this(ticketId, movieTitle, 1);
    }

    public RegularTicket(String ticketId, String movieTitle, int quantity) {
        super(ticketId, movieTitle, quantity);
    }

    @Override
    public double getUnitPrice() {
        return UNIT_PRICE;
    }

    @Override
    public double getDiscountRate() {
        return (getQuantity() >= 5) ? 0.10 : 0.0;
    }

    @Override
    public double calculateTotalPrice() {
        double subtotal = UNIT_PRICE * getQuantity();
        return subtotal * (1.0 - getDiscountRate());
    }

    @Override
    public String getTicketType() {
        return "RegularTicket";
    }
}

class VIPTicket extends Ticket {
    private static final double UNIT_PRICE = 150000.0;

    // Constructor chaining
    public VIPTicket(String ticketId, String movieTitle) {
        this(ticketId, movieTitle, 1);
    }

    public VIPTicket(String ticketId, String movieTitle, int quantity) {
        super(ticketId, movieTitle, quantity);
    }

    @Override
    public double getUnitPrice() {
        return UNIT_PRICE;
    }

    @Override
    public double getDiscountRate() {
        return (getQuantity() >= 3) ? 0.15 : 0.0;
    }

    @Override
    public double calculateTotalPrice() {
        double subtotal = UNIT_PRICE * getQuantity();
        return subtotal * (1.0 - getDiscountRate());
    }

    @Override
    public String getTicketType() {
        return "VIPTicket";
    }
}

public class Exercise4 {
    private static ArrayList<Ticket> tickets = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== EXERCISE 4: CINEMA TICKET SALES MANAGEMENT ===");
            System.out.println("1. Add ticket purchase");
            System.out.println("2. Load suggested sample test data");
            System.out.println("3. Display all ticket purchases");
            System.out.println("4. Search purchases by movie title");
            System.out.println("5. Display purchases sorted from highest to lowest final total");
            System.out.println("6. Statistics (Total revenue & Highest-grossing movie)");
            System.out.println("0. Exit");
            int choice = readInt("Select an option: ", 0, 6);

            switch (choice) {
                case 1:
                    addTicket();
                    break;
                case 2:
                    loadSampleData();
                    break;
                case 3:
                    displayAll();
                    break;
                case 4:
                    searchByMovieTitle();
                    break;
                case 5:
                    displaySortedByTotalDescending();
                    break;
                case 6:
                    showStatistics();
                    break;
                case 0:
                    System.out.println("Exiting Exercise 4.");
                    return;
            }
        }
    }

    private static Ticket findById(String id) {
        for (Ticket t : tickets) {
            if (t.getTicketId().equalsIgnoreCase(id.trim())) {
                return t;
            }
        }
        return null;
    }

    private static void addTicket() {
        int type = readInt("Select ticket type (1 - RegularTicket, 2 - VIPTicket): ", 1, 2);
        String id;
        while (true) {
            id = readNonEmptyString("Enter unique Ticket ID: ");
            if (findById(id) != null) {
                System.out.println("Error: Duplicate Ticket ID!");
            } else {
                break;
            }
        }
        String movie = readNonEmptyString("Enter movie title: ");
        int qty = readMinInt("Enter quantity (>= 1): ", 1);

        if (type == 1) {
            tickets.add(new RegularTicket(id, movie, qty));
        } else {
            tickets.add(new VIPTicket(id, movie, qty));
        }
        System.out.println("Ticket purchase added successfully!");
    }

    private static void loadSampleData() {
        if (findById("T01") == null) {
            tickets.add(new RegularTicket("T01", "Dune: Part Two", 5));
        }
        if (findById("T02") == null) {
            tickets.add(new VIPTicket("T02", "Oppenheimer", 3));
        }
        System.out.println("Sample data loaded (5 Regular -> 360,000 VND; 3 VIP -> 382,500 VND; Total -> 742,500 VND).");
    }

    private static void displayAll() {
        if (tickets.isEmpty()) {
            System.out.println("Ticket list is empty.");
            return;
        }
        System.out.println("\n--- ALL TICKET PURCHASES ---");
        for (Ticket t : tickets) {
            t.displayInfo();
        }
    }

    private static void searchByMovieTitle() {
        if (tickets.isEmpty()) {
            System.out.println("Ticket list is empty.");
            return;
        }
        String query = readNonEmptyString("Enter movie title to search: ");
        boolean found = false;
        System.out.println("\n--- SEARCH RESULTS ---");
        for (Ticket t : tickets) {
            if (t.getMovieTitle().toLowerCase().contains(query.toLowerCase())) {
                t.displayInfo();
                found = true;
            }
        }
        if (!found) {
            System.out.println("No ticket purchases found for movie: " + query);
        }
    }

    private static void displaySortedByTotalDescending() {
        if (tickets.isEmpty()) {
            System.out.println("Ticket list is empty.");
            return;
        }
        // Manual Selection Sort on a copy of the list without advanced frameworks
        ArrayList<Ticket> sorted = new ArrayList<>(tickets);
        int n = sorted.size();
        for (int i = 0; i < n - 1; i++) {
            int maxIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (sorted.get(j).calculateTotalPrice() > sorted.get(maxIdx).calculateTotalPrice()) {
                    maxIdx = j;
                }
            }
            if (maxIdx != i) {
                Ticket temp = sorted.get(i);
                sorted.set(i, sorted.get(maxIdx));
                sorted.set(maxIdx, temp);
            }
        }

        System.out.println("\n--- TICKET PURCHASES (HIGHEST TO LOWEST FINAL TOTAL) ---");
        for (Ticket t : sorted) {
            t.displayInfo();
        }
    }

    private static void showStatistics() {
        if (tickets.isEmpty()) {
            System.out.println("Ticket list is empty.");
            return;
        }
        double totalRevenue = 0;
        ArrayList<String> distinctMovies = new ArrayList<>();
        ArrayList<Double> movieRevenues = new ArrayList<>();

        for (Ticket t : tickets) {
            double price = t.calculateTotalPrice();
            totalRevenue += price;

            String title = t.getMovieTitle();
            int idx = -1;
            for (int i = 0; i < distinctMovies.size(); i++) {
                if (distinctMovies.get(i).equalsIgnoreCase(title)) {
                    idx = i;
                    break;
                }
            }
            if (idx == -1) {
                distinctMovies.add(title);
                movieRevenues.add(price);
            } else {
                movieRevenues.set(idx, movieRevenues.get(idx) + price);
            }
        }

        int bestMovieIdx = 0;
        for (int i = 1; i < movieRevenues.size(); i++) {
            if (movieRevenues.get(i) > movieRevenues.get(bestMovieIdx)) {
                bestMovieIdx = i;
            }
        }

        System.out.println("\n--- CINEMA REVENUE STATISTICS ---");
        System.out.printf("Combined Total Revenue : %,.0f VND%n", totalRevenue);
        System.out.printf("Highest-Grossing Movie : %s (%,.0f VND)%n",
                distinctMovies.get(bestMovieIdx), movieRevenues.get(bestMovieIdx));
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
}
