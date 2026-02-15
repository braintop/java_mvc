import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PersonDatabase {
    // Database connection parameters
    private static final String DB_URL = "jdbc:mysql://localhost:3306/person_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    
    // Connection object
    private Connection connection;
    
    // Constructor - establishes database connection
    public PersonDatabase() {
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Establish connection
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("✓ התחברות למסד נתונים הצליחה!");
            
        } catch (ClassNotFoundException e) {
            System.err.println("✗ לא נמצא MySQL JDBC Driver!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("✗ שגיאה בהתחברות למסד נתונים!");
            e.printStackTrace();
        }
    }
    
    // Insert a person into the database
    public boolean insertPerson(String name, int age, String email) {
        String sql = "INSERT INTO person (name, age, email) VALUES (?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setString(3, email);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("✓ הוספת " + name + " הצליחה!");
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("✗ שגיאה בהוספת אדם למסד נתונים!");
            e.printStackTrace();
        }
        
        return false;
    }
    
    // Display all persons from the database
    public void displayAllPersons() {
        String sql = "SELECT * FROM person";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.println("\n==========================================");
            System.out.println("        רשימת כל האנשים במסד הנתונים");
            System.out.println("==========================================");
            
            boolean hasResults = false;
            
            while (rs.next()) {
                hasResults = true;
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String email = rs.getString("email");
                
                System.out.println("\nID: " + id);
                System.out.println("שם: " + name);
                System.out.println("גיל: " + age);
                System.out.println("אימייל: " + email);
                System.out.println("------------------------------------------");
            }
            
            if (!hasResults) {
                System.out.println("\nמסד הנתונים ריק - אין אנשים רשומים.");
            }
            
            System.out.println("==========================================\n");
            
        } catch (SQLException e) {
            System.err.println("✗ שגיאה בקריאת נתונים מהמסד!");
            e.printStackTrace();
        }
    }
    
    // Delete a person by ID
    public boolean deletePerson(int id) {
        String sql = "DELETE FROM person WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("✓ מחיקת אדם עם ID " + id + " הצליחה!");
                return true;
            } else {
                System.out.println("✗ לא נמצא אדם עם ID " + id);
            }
            
        } catch (SQLException e) {
            System.err.println("✗ שגיאה במחיקת אדם!");
            e.printStackTrace();
        }
        
        return false;
    }
    
    // Close database connection
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✓ החיבור למסד הנתונים נסגר.");
            }
        } catch (SQLException e) {
            System.err.println("✗ שגיאה בסגירת החיבור!");
            e.printStackTrace();
        }
    }
    
    // Main method - demonstrates usage
    public static void main(String[] args) {
        // Create database connection
        PersonDatabase db = new PersonDatabase();
        
        System.out.println("\n========================================");
        System.out.println("  אפליקציית ניהול אנשים - Java + MySQL");
        System.out.println("========================================\n");
        
        // Insert some people
        System.out.println("--- הוספת אנשים למסד נתונים ---");
        db.insertPerson("ישראל ישראלי", 28, "israel@example.com");
        db.insertPerson("שרה כהן", 34, "sarah@example.com");
        db.insertPerson("דוד לוי", 42, "david@example.com");
        db.insertPerson("רחל אברהם", 29, "rachel@example.com");
        
        // Display all persons
        db.displayAllPersons();
        
        // Delete a person (example)
        // System.out.println("\n--- מחיקת אדם ---");
        // db.deletePerson(1);
        
        // Display all persons again
        // db.displayAllPersons();
        
        // Close connection
        db.close();
    }
}
