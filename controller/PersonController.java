package controller;

import model.Person;
import model.PersonDAO;
import view.PersonView;

import javax.swing.JOptionPane;
import java.sql.SQLException;
import java.util.List;

public class PersonController {
    private PersonDAO dao;
    private PersonView view;
    
    public PersonController(PersonView view, PersonDAO dao) {
        this.view = view;
        this.dao = dao;
    }
    
    public void addPerson(String name, String ageStr, String email) {
        // Validation
        if (name == null || name.trim().isEmpty()) {
            showError("שדה שם הוא חובה!");
            return;
        }
        
        if (ageStr == null || ageStr.trim().isEmpty()) {
            showError("שדה גיל הוא חובה!");
            return;
        }
        
        if (email == null || email.trim().isEmpty()) {
            showError("שדה אימייל הוא חובה!");
            return;
        }
        
        int age;
        try {
            age = Integer.parseInt(ageStr.trim());
            if (age <= 0 || age > 150) {
                showError("גיל חייב להיות בין 1 ל-150!");
                return;
            }
        } catch (NumberFormatException e) {
            showError("גיל חייב להיות מספר!");
            return;
        }
        
        if (!email.contains("@") || !email.contains(".")) {
            showError("אימייל לא תקין! (צריך @ ו-.)");
            return;
        }
        
        Person person = new Person(name.trim(), age, email.trim());
        
        try {
            if (dao.insert(person)) {
                showSuccess("האדם נוסף בהצלחה!");
                view.clearInputs();
                loadAllPersons();
            } else {
                showError("שגיאה בהוספת אדם!");
            }
        } catch (SQLException e) {
            showError("שגיאת מסד נתונים: " + e.getMessage());
        }
    }
    
    public void loadAllPersons() {
        try {
            List<Person> persons = dao.getAll();
            view.updateTable(persons);
        } catch (SQLException e) {
            showError("שגיאה בטעינת נתונים: " + e.getMessage());
        }
    }
    
    public void deletePerson(int id) {
        int confirm = JOptionPane.showConfirmDialog(
            view,
            "האם אתה בטוח שברצונך למחוק אדם זה?",
            "אישור מחיקה",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (dao.delete(id)) {
                    showSuccess("האדם נמחק בהצלחה!");
                    loadAllPersons();
                } else {
                    showError("שגיאה במחיקת אדם!");
                }
            } catch (SQLException e) {
                showError("שגיאת מסד נתונים: " + e.getMessage());
            }
        }
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(view, message, "שגיאה", JOptionPane.ERROR_MESSAGE);
    }
    
    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(view, message, "הצלחה", JOptionPane.INFORMATION_MESSAGE);
    }
}
