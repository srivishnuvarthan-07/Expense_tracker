package com.expense.gui;
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
    public CategoryGui(){
        initializeComponents();
        setupLayout();
        setupEventListeners();
//        expenseDao = new ExpenseDAO();
        //loadCategory();
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
            //addCategory();
        });
        updateButton.addActionListener((e)->{
            //updateCategory();
        });
        deleteButton.addActionListener((e)->{
            //deleteCategory();
        });
        refreshButton.addActionListener((e)->{
            //refreshCategory();
        });
    }
}
class ExpenseGui extends JFrame {

}
