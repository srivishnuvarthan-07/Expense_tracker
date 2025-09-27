package com.expense.gui;
import com.expense.dao.expensedao;
import com.expense.model.Catemodel;
import com.expense.model.Expensemodel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

import java.awt.*;
public class MGui extends JFrame {

    private JButton expenseButton;
    private JButton categoryButton;
    public MGui(){
        initializeComponents();
        setupLayout();
        setupEventListeners();
    }

    private void initializeComponents() {
        setTitle("Main UI");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);

        expenseButton = new JButton("Expense");
        categoryButton = new JButton("Category");
    }

    private void setupLayout(){
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();

//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.gridx = 2;
//        gbc.gridy = 2;

        JPanel buttonPanel = new JPanel(new FlowLayout());

        buttonPanel.add(expenseButton);
        buttonPanel.add(categoryButton);

        panel.add(buttonPanel);
        add(panel,BorderLayout.CENTER);
    }
    private void setupEventListeners(){
        expenseButton.addActionListener((e)->{
            setVisible(false);
            new ExpenseGui();
        });
        categoryButton.addActionListener((e)->{
            setVisible(false);
            new CategoryGui();
        });
    }
}

class CategoryGui extends JFrame {
    private JButton backButton;
    private JTextField titleField;
    private JButton addButton;
    private JButton refreshButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JTable categoryTable;
    private DefaultTableModel tableModel;
    private expensedao expenseDao;
    public CategoryGui(){
        initializeComponents();
        setupLayout();
        setupEventListeners();
        expenseDao = new expensedao();
        loadCategory();
    }
    public void initializeComponents(){

        titleField = new JTextField(20);
        backButton= new JButton("BACK");
        addButton = new JButton("Add");
        refreshButton = new JButton("Refresh");
        deleteButton = new JButton("Delete");
        updateButton = new JButton("Update");
        String[] columnNames = {"Id","Title"};

        tableModel = new DefaultTableModel(columnNames,0){
            @Override
            public boolean isCellEditable(int row,int column){
                return false;
            }
        };
        categoryTable = new JTable(tableModel);

        categoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        categoryTable.getSelectionModel().addListSelectionListener(
                e->{
                    if(!e.getValueIsAdjusting()){
                        loadSelectedCategory();
                    }
                });
    }

    private void loadSelectedCategory() {
        int row = categoryTable.getSelectedRow();
        if(row != -1){
            String categoryName =  categoryTable.getValueAt(row, 1).toString();
            titleField.setText(categoryName);
        }
    }

    public void setupLayout(){
        setTitle("Category UI");

        setSize(600,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setVisible(true);

        JPanel northPanel = new JPanel(new BorderLayout());
        JPanel firstpanel = new JPanel();
        firstpanel.add(backButton,BorderLayout.NORTH);
        JPanel inputPanel = new JPanel();
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.insets = new Insets(0,0,0,0);
//        gbc.anchor = GridBagConstraints.CENTER;
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//
//        gbc.gridx = 0;
//        gbc.gridy = 0;

        inputPanel.add(new JLabel("Name"));

//        gbc.gridx = 0;
//        gbc.gridy = 0;
        inputPanel.add(titleField);

        JPanel buttonsPanel = new JPanel(new FlowLayout());

        buttonsPanel.add(addButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(updateButton);
        buttonsPanel.add(refreshButton);

        northPanel.add(firstpanel,BorderLayout.WEST);
        northPanel.add(inputPanel,BorderLayout.NORTH);
        northPanel.add(buttonsPanel,BorderLayout.CENTER);

        add(northPanel,BorderLayout.NORTH);
        add(new JScrollPane(categoryTable),BorderLayout.CENTER);
    }

    public void setupEventListeners(){
        backButton.addActionListener((e)->{
            backfunction();
        });
        addButton.addActionListener((e)->{
            addCategory();
        });
        updateButton.addActionListener((e)->{
            updateCategory();
        });
        deleteButton.addActionListener((e)->{
            deleteCategory();
        });
        refreshButton.addActionListener((e)->{
            refreshCategory();
        });
    }
    private void backfunction(){
        setVisible(false);
        new MGui().setVisible(true);
    }
    private void loadCategory(){
        try{
            List<Catemodel> categories = expenseDao.getAllCategories();
            updateTable(categories);
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(this,"Database Error: "+e.getMessage(),"DataBase Error",JOptionPane.ERROR_MESSAGE);
        }
    }
    private void updateTable(List<Catemodel> category){
        tableModel.setRowCount(0);
        for(Catemodel cate: category){
            Object row[] = {
                    cate.getId(),
                    cate.getName()
            };
            tableModel.addRow(row);
        }
    }
    private  void clearTable(){
        titleField.setText("");
    }
    private void addCategory(){
        String name = titleField.getText().trim();
        try{
            int rowsAffected = expenseDao.createCategory(name);
            if(rowsAffected > 0){
                JOptionPane.showMessageDialog(this,"Category Added Successfully", "Success",JOptionPane.INFORMATION_MESSAGE);
            }
            else{
                JOptionPane.showMessageDialog(this,"Failed to add category", "Failed",JOptionPane.ERROR_MESSAGE);
            }
            loadCategory();
            clearTable();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(this, "Database Failed","Database Error",JOptionPane.ERROR_MESSAGE);
        }
    }
    private void updateCategory(){
        int row = categoryTable.getSelectedRow();
        if(row == -1){
            JOptionPane.showMessageDialog(this,"Select a Category to update..","Invalid Update",JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int)categoryTable.getValueAt(row,0);
        String categoryName = titleField.getText();

        if(categoryName == ""){
            JOptionPane.showMessageDialog(this,"Category name is emty!","Invaild Category Name",JOptionPane.WARNING_MESSAGE);
            return;
        }
        try{
            if(expenseDao.updateCategory(id,categoryName) > 0){
                JOptionPane.showMessageDialog(this,"Category updated Successfully","Update Success",JOptionPane.INFORMATION_MESSAGE);
                loadCategory();
                clearTable();
            }
            else{
                JOptionPane.showMessageDialog(this, "Category Update Failed","Update Failed",JOptionPane.ERROR_MESSAGE);
            }
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(this,"Databse Failed while Updating - "+e.getMessage(),"Databse failed",JOptionPane.ERROR_MESSAGE);
        }

    }

    private void deleteCategory(){
        int row = categoryTable.getSelectedRow();
        if(row == -1){
            JOptionPane.showMessageDialog(this,"Select a Category to update..","Invalid Update",JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int)categoryTable.getValueAt(row,0);
        try{
            if(expenseDao.deleteCategory(id) > 0){
                JOptionPane.showMessageDialog(this,"Category deleted Successfully","Delete Success",JOptionPane.INFORMATION_MESSAGE);
                loadCategory();
                clearTable();
            }
            else{
                JOptionPane.showMessageDialog(this, "Category Delete Failed","Delete Failed",JOptionPane.ERROR_MESSAGE);
            }
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(this,"Databse Failed while deleting - "+e.getMessage(),"Databse failed",JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshCategory(){
        loadCategory();
        clearTable();
    }
}
class ExpenseGui extends JFrame {
    private JButton backbutton;
    private JTextField amountField;
    private JTextArea descriptoinArea;
    private JButton addButton;
    private JButton refreshButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JComboBox<String> categoryComboBox;

    private JTable expenseTable;
    private DefaultTableModel tableModel;
    private expensedao expenseDao;

    public ExpenseGui(){
        expenseDao = new expensedao();
        initializeComponents();
        setupLayout();
        setupEventListeners();
        loadExpense();
    }

    public void initializeComponents(){

        amountField = new JTextField(10);
        descriptoinArea = new JTextArea(1,10);
        backbutton = new JButton("Back");
        addButton = new JButton("Add");
        refreshButton = new JButton("Refresh");
        deleteButton = new JButton("Delete");
        updateButton = new JButton("Update");
        List<String> categories = new ArrayList<>();
        try{
            List<Catemodel> cate = expenseDao.getAllCategories();
            for(Catemodel c: cate){
                categories.add(c.getName());
            }
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(this,"Databse Failed : "+e.getMessage(),"Database Error",JOptionPane.ERROR_MESSAGE);
        }

        String[] categoriesArray = categories.toArray(new String[0]);


        categoryComboBox = new JComboBox<>(categoriesArray);

        String[] columnNames = {"Id","Amount","Description","Category","Created At"};

        tableModel = new DefaultTableModel(columnNames,0){
            @Override
            public boolean isCellEditable(int row,int column){
                return false;
            }
        };
        expenseTable = new JTable(tableModel);

        expenseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        expenseTable.getSelectionModel().addListSelectionListener(
                e->{
                    if(!e.getValueIsAdjusting()){
                        loadSelectedExpense();
                    }
                });
    }

    private void loadSelectedExpense() {
        int row = expenseTable.getSelectedRow();
        if(row != -1){
            String amount = expenseTable.getValueAt(row,1).toString();
            String description = expenseTable.getValueAt(row, 2).toString();
            String category = expenseTable.getValueAt(row, 3).toString();

            amountField.setText(amount);
            descriptoinArea.setText(description);
            categoryComboBox.setSelectedItem(category);
        }
    }

    public void setupLayout(){
        setTitle("Expenses");
        setSize(600,600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setVisible(true);

        JPanel northPanel = new JPanel(new BorderLayout());
        JPanel firstPanel = new JPanel();

        JPanel inputPanel = new JPanel();
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.insets = new Insets(5,5,5,5);
//        gbc.anchor = GridBagConstraints.CENTER;

//        gbc.gridx = 0;
//        gbc.gridy = 0;

        inputPanel.add(new JLabel("Amount"));

//        gbc.gridx = 1;
//        gbc.gridy = 0;
        inputPanel.add(amountField);

//        gbc.gridx = 0;
//        gbc.gridy = 1;
        inputPanel.add(new JLabel("Description"));

//        gbc.gridx = 1;
//        gbc.gridy = 1;
        inputPanel.add(descriptoinArea);


//        gbc.gridx = 0;
//        gbc.gridy = 2;
        inputPanel.add(new JLabel("Category"));

//        gbc.gridx = 1;
//        gbc.gridy = 2;
        inputPanel.add(categoryComboBox);

        firstPanel.add(backbutton);
        JPanel buttonsPanel = new JPanel();

        buttonsPanel.add(addButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(updateButton);
        buttonsPanel.add(refreshButton);

        northPanel.add(inputPanel,BorderLayout.NORTH);
        northPanel.add(firstPanel,BorderLayout.CENTER);
        northPanel.add(buttonsPanel,BorderLayout.SOUTH);


        add(northPanel,BorderLayout.NORTH);
        add(new JScrollPane(expenseTable),BorderLayout.CENTER);
    }
    public void setupEventListeners(){
        addButton.addActionListener((e)->{
            addExpense();
        });
        updateButton.addActionListener((e)->{
            updateExpense();
        });
        deleteButton.addActionListener((e)->{
            deleteExpense();
        });
        refreshButton.addActionListener((e)->{
            refreshExpense();
        });
        backbutton.addActionListener((e)->{
            backfunction();
        });
    }

    private void backfunction() {
        setVisible(false);
        new MGui().setVisible(true);
    }

    private void loadExpense(){
        try{
            List<Expensemodel> expense = expenseDao.getAllExpenses();
            updateTable(expense);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this,"Database Error viv: "+e.getMessage(),"DataBase Error",JOptionPane.ERROR_MESSAGE);
        }
    }
    private void addExpense(){
        String amount = amountField.getText();
        String category = (categoryComboBox.getSelectedItem().toString()).trim();
        String description = descriptoinArea.getText().trim();
        int amt = 0;

        if(amount.equals("")){
            JOptionPane.showMessageDialog(this, "Enter a Amount","Invaild field",JOptionPane.WARNING_MESSAGE);
            return;
        }

        try{
            amt = Integer.parseInt(amount);
        }
        catch(NumberFormatException e){
            JOptionPane.showMessageDialog(this,"Enter a Number in Amount","Invaild Input",JOptionPane.WARNING_MESSAGE);
            return;
        }

        try{
            if(expenseDao.createExpense(amt,category,description)>0){
                JOptionPane.showMessageDialog(this,"Expense Added Successfully", "Success",JOptionPane.INFORMATION_MESSAGE);
            }
            else{
                JOptionPane.showMessageDialog(this,"Failed to add expense", "Failed",JOptionPane.ERROR_MESSAGE);
            }
            loadExpense();
            clearTable();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(this, "Database Failed "+e.getMessage(),"Database Error",JOptionPane.ERROR_MESSAGE);
        }


    }

    private void updateExpense(){
        int row = expenseTable.getSelectedRow();
        if(row == -1){
            JOptionPane.showMessageDialog(this,"Select a Category to update..","Invalid Update",JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int)expenseTable.getValueAt(row,0);
        String amount = amountField.getText();
        String description = descriptoinArea.getText();
        String category = categoryComboBox.getSelectedItem().toString();
        int amt = 0;

        if(amount == ""){
            JOptionPane.showMessageDialog(this,"Expense Amount is emty!","Invaild Category Name",JOptionPane.WARNING_MESSAGE);
            return;
        }
        try{
            amt = Integer.parseInt(amount);
        }
        catch(NumberFormatException e){
            JOptionPane.showMessageDialog(this,"Enter a Number in Amount","Invaild Input",JOptionPane.WARNING_MESSAGE);
            return;
        }
        try{
            if(expenseDao.updateExpense(id,amt, description, category) > 0){
                JOptionPane.showMessageDialog(this,"Expense updated Successfully","Update Success",JOptionPane.INFORMATION_MESSAGE);
                loadExpense();
                clearTable();
            }
            else{
                JOptionPane.showMessageDialog(this, "Expense Update Failed","Update Failed",JOptionPane.ERROR_MESSAGE);
            }
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(this,"Databse Failed while Updating - "+e.getMessage(),"Databse failed",JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteExpense(){
        int row = expenseTable.getSelectedRow();
        if(row == -1){
            JOptionPane.showMessageDialog(this,"Select a Expense to update..","Invalid Update",JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int)expenseTable.getValueAt(row,0);
        try{
            if(expenseDao.deleteExpense(id) > 0){
                JOptionPane.showMessageDialog(this,"Expense deleted Successfully","Delete Success",JOptionPane.INFORMATION_MESSAGE);
                loadExpense();
                clearTable();
            }
            else{
                JOptionPane.showMessageDialog(this, "Expense Delete Failed","Delete Failed",JOptionPane.ERROR_MESSAGE);
            }
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(this,"Databse Failed while deleting - "+e.getMessage(),"Databse failed",JOptionPane.ERROR_MESSAGE);
        }

    }
    private void refreshExpense(){
        loadExpense();
        clearTable();
    }
    public void clearTable(){
        amountField.setText("");
        descriptoinArea.setText("");
    }
    private void updateTable(List<Expensemodel> expense){
        tableModel.setRowCount(0);
        for(Expensemodel exp: expense){
            Object[] row = {
                    exp.getExpId(),
                    exp.getAmount(),
                    exp.getDescription(),
                    exp.getCategory(),
                    exp.getCreatedAt()
            };
            tableModel.addRow(row);
        }
    }


}
