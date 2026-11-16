import java.util.ArrayList;
import java.util.Scanner;

abstract class Delivery {
    private static int totalDeliveriesCreated = 0;

    private String orderId;
    private String customerName;
    private double distance; // km
    private double weight;   // kg

    // Overloaded constructor (default 1.0 kg weight)
    public Delivery(String orderId, String customerName, double distance) {
        this(orderId, customerName, distance, 1.0);
    }

    public Delivery(String orderId, String customerName, double distance, double weight) {
        setOrderId(orderId);
        setCustomerName(customerName);
        setDistance(distance);
        setWeight(weight);
        totalDeliveriesCreated++;
    }

    public static int getTotalDeliveriesCreated() {
        return totalDeliveriesCreated;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) {
        if (orderId != null && !orderId.trim().isEmpty()) {
            this.orderId = orderId.trim();
        } else if (this.orderId == null) {
            this.orderId = "ORD_UNKNOWN";
        }
    }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) {
        if (customerName != null && !customerName.trim().isEmpty()) {
            this.customerName = customerName.trim();
        } else if (this.customerName == null) {
            this.customerName = "Unknown Customer";
        }
    }

    public double getDistance() { return distance; }
    public void setDistance(double distance) {
        if (distance > 0) {
            this.distance = distance;
        } else if (this.distance <= 0) {
            this.distance = 1.0;
        }
    }

    public double getWeight() { return weight; }
    public void setWeight(double weight) {
        if (weight > 0) {
            this.weight = weight;
        } else if (this.weight <= 0) {
            this.weight = 1.0;
        }
    }

    public abstract double calculateShippingFee();
    public abstract String getDeliveryType();

    public void displayInfo() {
        System.out.printf("Order ID: %-8s | Type: %-16s | Customer: %-16s | Distance: %6.2f km | Weight: %6.2f kg | Fee: %,.0f VND%n",
                orderId, getDeliveryType(), customerName, distance, weight, calculateShippingFee());
    }
}

class StandardDelivery extends Delivery {
    public StandardDelivery(String orderId, String customerName, double distance) {
        super(orderId, customerName, distance);
    }

    public StandardDelivery(String orderId, String customerName, double distance, double weight) {
        super(orderId, customerName, distance, weight);
    }

    @Override
    public double calculateShippingFee() {
        return 15000.0 + 3000.0 * getDistance() + 2000.0 * getWeight();
    }

    @Override
    public String getDeliveryType() {
        return "StandardDelivery";
    }
}

class ExpressDelivery extends Delivery {
    public ExpressDelivery(String orderId, String customerName, double distance) {
        super(orderId, customerName, distance);
    }

    public ExpressDelivery(String orderId, String customerName, double distance, double weight) {
        super(orderId, customerName, distance, weight);
    }

    @Override
    public double calculateShippingFee() {
        return 30000.0 + 5000.0 * getDistance() + 4000.0 * getWeight();
    }

    @Override
    public String getDeliveryType() {
        return "ExpressDelivery";
    }
}

public class Exercise3 {
    private static ArrayList<Delivery> deliveries = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== EXERCISE 3: ONLINE ORDER DELIVERY MANAGEMENT ===");
            System.out.println("1. Add delivery order");
            System.out.println("2. Load suggested sample test data");
            System.out.println("3. Display all orders and shipping fees");
            System.out.println("4. Search order by Order ID");
            System.out.println("5. Filter orders exceeding a fee threshold");
            System.out.println("6. Statistics (Total revenue, Count by type, Static object counter)");
            System.out.println("0. Exit");
            int choice = readInt("Select an option: ", 0, 6);

            switch (choice) {
                case 1:
                    addDelivery();
                    break;
                case 2:
                    loadSampleData();
                    break;
                case 3:
                    displayAll();
                    break;
                case 4:
                    searchByOrderId();
                    break;
                case 5:
                    filterByThreshold();
                    break;
                case 6:
                    showStatistics();
                    break;
                case 0:
                    System.out.println("Exiting Exercise 3.");
                    return;
            }
        }
    }

    private static Delivery findById(String id) {
        for (Delivery d : deliveries) {
            if (d.getOrderId().equalsIgnoreCase(id.trim())) {
                return d;
            }
        }
        return null;
    }

    private static void addDelivery() {
        int type = readInt("Select delivery type (1 - Standard, 2 - Express): ", 1, 2);
        String id;
        while (true) {
            id = readNonEmptyString("Enter unique Order ID: ");
            if (findById(id) != null) {
                System.out.println("Error: Duplicate Order ID!");
            } else {
                break;
            }
        }
        String customer = readNonEmptyString("Enter customer name: ");
        double distance = readPositiveDouble("Enter distance in km (> 0): ");
        double weight = readPositiveDouble("Enter parcel weight in kg (> 0): ");

        if (type == 1) {
            deliveries.add(new StandardDelivery(id, customer, distance, weight));
        } else {
            deliveries.add(new ExpressDelivery(id, customer, distance, weight));
        }
        System.out.println("Order added! Total Delivery objects created (static counter): " + Delivery.getTotalDeliveriesCreated());
    }

    private static void loadSampleData() {
        if (findById("ORD01") == null) {
            deliveries.add(new StandardDelivery("ORD01", "Le Van C", 10.0, 2.0));
        }
        if (findById("ORD02") == null) {
            deliveries.add(new ExpressDelivery("ORD02", "Pham Thi D", 10.0, 2.0));
        }
        System.out.println("Sample data loaded (Standard 10km, 2kg -> 49,000 VND; Express 10km, 2kg -> 88,000 VND).");
    }

    private static void displayAll() {
        if (deliveries.isEmpty()) {
            System.out.println("Delivery list is empty.");
            return;
        }
        System.out.println("\n--- ALL DELIVERY ORDERS ---");
        for (Delivery d : deliveries) {
            d.displayInfo();
        }
    }

    private static void searchByOrderId() {
        if (deliveries.isEmpty()) {
            System.out.println("Delivery list is empty.");
            return;
        }
        String id = readNonEmptyString("Enter Order ID to search: ");
        Delivery d = findById(id);
        if (d != null) {
            d.displayInfo();
        } else {
            System.out.println("No order found with ID: " + id);
        }
    }

    private static void filterByThreshold() {
        if (deliveries.isEmpty()) {
            System.out.println("Delivery list is empty.");
            return;
        }
        double threshold = readNonNegativeDouble("Enter fee threshold (VND): ");
        boolean found = false;
        System.out.printf("\n--- ORDERS WITH SHIPPING FEE > %,.0f VND ---%n", threshold);
        for (Delivery d : deliveries) {
            if (d.calculateShippingFee() > threshold) {
                d.displayInfo();
                found = true;
            }
        }
        if (!found) {
            System.out.println("No orders exceed the specified threshold.");
        }
    }

    private static void showStatistics() {
        System.out.println("\n--- DELIVERY STATISTICS ---");
        System.out.println("Static counter (Total Delivery objects created in memory): " + Delivery.getTotalDeliveriesCreated());
        System.out.println("(Note: 'static' belongs to the Delivery class itself rather than any single instance, so all created objects share and increment one common counter.)");

        if (deliveries.isEmpty()) {
            System.out.println("Delivery list is currently empty.");
            return;
        }

        int standardCount = 0;
        int expressCount = 0;
        double totalRevenue = 0;

        for (Delivery d : deliveries) {
            if (d instanceof StandardDelivery) standardCount++;
            else if (d instanceof ExpressDelivery) expressCount++;
            totalRevenue += d.calculateShippingFee();
        }

        System.out.println("Standard Delivery orders : " + standardCount);
        System.out.println("Express Delivery orders  : " + expressCount);
        System.out.printf("Total Shipping Revenue   : %,.0f VND%n", totalRevenue);
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

    private static double readPositiveDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double val = Double.parseDouble(scanner.nextLine().trim());
                if (val > 0) return val;
                System.out.println("Error: Value must be strictly positive (> 0).");
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid number.");
            }
        }
    }

    private static double readNonNegativeDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double val = Double.parseDouble(scanner.nextLine().trim());
                if (val >= 0) return val;
                System.out.println("Error: Value must be >= 0.");
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid number.");
            }
        }
    }
}
