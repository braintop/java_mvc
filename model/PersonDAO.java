package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonDAO {
    
    public boolean insert(Person person) throws SQLException {
        String sql = "INSERT INTO person (name, age, email) VALUES (?, ?, ?)";
        
        try (PreparedStatement pstmt = DatabaseManager.getInstance().getConnection().prepareStatement(sql)) {
            pstmt.setString(1, person.getName());
            pstmt.setInt(2, person.getAge());
            pstmt.setString(3, person.getEmail());
            return pstmt.executeUpdate() > 0;
        }
    }
    
    public List<Person> getAll() throws SQLException {
        List<Person> persons = new ArrayList<>();
        String sql = "SELECT * FROM person ORDER BY id";
        
        try (Statement stmt = DatabaseManager.getInstance().getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Person person = new Person(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("age"),
                    rs.getString("email")
                );
                persons.add(person);
            }
        }
        return persons;
    }
    
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM person WHERE id = ?";
        
        try (PreparedStatement pstmt = DatabaseManager.getInstance().getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }
    
    public Person getById(int id) throws SQLException {
        String sql = "SELECT * FROM person WHERE id = ?";
        
        try (PreparedStatement pstmt = DatabaseManager.getInstance().getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Person(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("email")
                    );
                }
            }
        }
        return null;
    }
}
