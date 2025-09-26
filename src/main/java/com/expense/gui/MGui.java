package com.expense.gui;
import com.expense.dao.expensedao;
import com.expense.model.Catemodel;
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

        JPanel panel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;

        JPanel buttonPanel = new JPanel(new FlowLayout());

        buttonPanel.add(expenseButton);
        buttonPanel.add(categoryButton);

        panel.add(buttonPanel,gbc);
        add(panel,BorderLayout.CENTER);
    }
    private void setupEventListeners(){
        expenseButton.addActionListener((e)->{
            new ExpenseGui().setVisible(true);
        });
        categoryButton.addActionListener((e)->{
            new CategoryGui().setVisible(true);
        });
    }
}

class CategoryGui extends JFrame {

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
                        //loadSelectedCategory();
                    }
                });
    }

    public void setupLayout(){
        setTitle("Category UI");

        setSize(1000,1000);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JPanel northPanel = new JPanel(new BorderLayout());

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

        northPanel.add(inputPanel,BorderLayout.NORTH);
        northPanel.add(buttonsPanel,BorderLayout.CENTER);

        add(northPanel,BorderLayout.NORTH);
        add(new JScrollPane(categoryTable),BorderLayout.CENTER);
    }

    public void setupEventListeners(){
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

}
