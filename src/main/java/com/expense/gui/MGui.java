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
        //setupEventListeners();
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
}
