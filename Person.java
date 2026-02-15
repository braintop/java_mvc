public class Person {
    // Properties
    private String name;
    private int age;
    private String email;
    
    // Constructor
    public Person(String name, int age, String email) {
        this.name = name;
        this.age = age;
        this.email = email;
    }
    
    // Default constructor
    public Person() {
        this.name = "Unknown";
        this.age = 0;
        this.email = "";
    }
    
    // Getters
    public String getName() {
        return name;
    }
    
    public int getAge() {
        return age;
    }
    
    public String getEmail() {
        return email;
    }
    
    // Setters
    public void setName(String name) {
        this.name = name;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    // Method to display person information
    public void displayInfo() {
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("Email: " + email);
        System.out.println("-------------------");
    }
    
    // Main method to test the class
    public static void main(String[] args) {
        // Create person objects
        Person person1 = new Person("John Doe", 30, "john@example.com");
        Person person2 = new Person("Jane Smith", 25, "jane@example.com");
        Person person3 = new Person();
        
        // Display information
        System.out.println("Person 1:");
        person1.displayInfo();
        
        System.out.println("Person 2:");
        person2.displayInfo();
        
        System.out.println("Person 3 (default):");
        person3.displayInfo();
        
        // Update person3
        person3.setName("Bob Johnson");
        person3.setAge(35);
        person3.setEmail("bob@example.com");
        
        System.out.println("Person 3 (updated):");
        person3.displayInfo();
    }
}
