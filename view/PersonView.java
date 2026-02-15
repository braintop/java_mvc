package view;

import controller.PersonController;
import model.Person;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PersonView extends JFrame {
    private JTextField nameField;
    private JTextField ageField;
    private JTextField emailField;
    private JButton addButton;
    private JButton deleteButton;
    private JButton refreshButton;
    private JTable personsTable;
    private DefaultTableModel tableModel;
    
    private PersonController controller;
    
    public PersonView() {
        initComponents();
    }
    
    public void setController(PersonController controller) {
        this.controller = controller;
        controller.loadAllPersons();
    }
    
    private void initComponents() {
        setTitle("מערכת ניהול אנשים - Java MVC + MySQL");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 550);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        mainPanel.add(createInputPanel(), BorderLayout.NORTH);
        mainPanel.add(createTablePanel(), BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            "הוספת אדם חדש",
            0,
            0,
            new Font("Arial", Font.BOLD, 14)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        Font labelFont = new Font("Arial", Font.PLAIN, 13);
        Font fieldFont = new Font("Arial", Font.PLAIN, 13);
        
        // Name
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        JLabel nameLabel = new JLabel("שם:");
        nameLabel.setFont(labelFont);
        panel.add(nameLabel, gbc);
        
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        nameField = new JTextField(25);
        nameField.setFont(fieldFont);
        panel.add(nameField, gbc);
        
        // Age
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        JLabel ageLabel = new JLabel("גיל:");
        ageLabel.setFont(labelFont);
        panel.add(ageLabel, gbc);
        
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        ageField = new JTextField(25);
        ageField.setFont(fieldFont);
        panel.add(ageField, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        JLabel emailLabel = new JLabel("אימייל:");
        emailLabel.setFont(labelFont);
        panel.add(emailLabel, gbc);
        
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0;
        emailField = new JTextField(25);
        emailField.setFont(fieldFont);
        panel.add(emailField, gbc);
        
        // Add button
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        addButton = new JButton("➕ הוסף אדם");
        addButton.setFont(new Font("Arial", Font.BOLD, 15));
        addButton.setBackground(new Color(76, 175, 80));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setPreferredSize(new Dimension(200, 35));
        addButton.addActionListener(e -> onAddButtonClicked());
        panel.add(addButton, gbc);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            "רשימת אנשים",
            0,
            0,
            new Font("Arial", Font.BOLD, 14)
        ));
        
        String[] columnNames = {"ID", "שם", "גיל", "אימייל"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        personsTable = new JTable(tableModel);
        personsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        personsTable.setRowHeight(28);
        personsTable.setFont(new Font("Arial", Font.PLAIN, 13));
        personsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        personsTable.getTableHeader().setReorderingAllowed(false);
        
        // Column widths
        personsTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        personsTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        personsTable.getColumnModel().getColumn(2).setPreferredWidth(50);
        personsTable.getColumnModel().getColumn(3).setPreferredWidth(200);
        
        JScrollPane scrollPane = new JScrollPane(personsTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        
        refreshButton = new JButton("🔄 רענן");
        refreshButton.setFont(new Font("Arial", Font.PLAIN, 13));
        refreshButton.setBackground(new Color(33, 150, 243));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.addActionListener(e -> controller.loadAllPersons());
        buttonPanel.add(refreshButton);
        
        deleteButton = new JButton("🗑️ מחק נבחר");
        deleteButton.setFont(new Font("Arial", Font.PLAIN, 13));
        deleteButton.setBackground(new Color(244, 67, 54));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.addActionListener(e -> onDeleteButtonClicked());
        buttonPanel.add(deleteButton);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void onAddButtonClicked() {
        controller.addPerson(
            nameField.getText(),
            ageField.getText(),
            emailField.getText()
        );
    }
    
    private void onDeleteButtonClicked() {
        int selectedRow = personsTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                this,
                "אנא בחר אדם למחיקה מהטבלה!",
                "אזהרה",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        controller.deletePerson(id);
    }
    
    public void updateTable(List<Person> persons) {
        tableModel.setRowCount(0);
        
        for (Person person : persons) {
            Object[] row = {
                person.getId(),
                person.getName(),
                person.getAge(),
                person.getEmail()
            };
            tableModel.addRow(row);
        }
    }
    
    public void clearInputs() {
        nameField.setText("");
        ageField.setText("");
        emailField.setText("");
        nameField.requestFocus();
    }
}
