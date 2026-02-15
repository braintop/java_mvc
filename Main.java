import controller.PersonController;
import model.DatabaseManager;
import model.PersonDAO;
import view.PersonView;

import javax.swing.*;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Initialize database connection
                DatabaseManager.getInstance();
                
                // Create MVC components with Dependency Injection
                PersonDAO dao = new PersonDAO();
                PersonView view = new PersonView();
                PersonController controller = new PersonController(view, dao);
                
                // Wire them together
                view.setController(controller);
                
                // Show the view
                view.setVisible(true);
                
                // Add shutdown hook to close DB connection
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    try {
                        DatabaseManager.getInstance().closeConnection();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }));
                
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(
                    null,
                    "שגיאה בהתחברות למסד נתונים:\n" + e.getMessage(),
                    "שגיאה קריטית",
                    JOptionPane.ERROR_MESSAGE
                );
                System.exit(1);
            }
        });
    }
}
