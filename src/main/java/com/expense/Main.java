package com.expense;

import com.expense.gui.MGui;
import com.expense.util.Database;

import java.sql.*;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        Database dbConnection = new Database();
        try{
            Connection conn = dbConnection.getConnection();
        }
        catch(SQLException e){
            System.out.println("DB connection failed "+e.getMessage());
        }

//        try{
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        }
//        catch(ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e){
//            System.out.println("Look and field error "+e.getMessage());
//        }

        SwingUtilities.invokeLater(() -> {
            try{
                new MGui().setVisible(true);
            }
            catch(Exception e){
                System.err.println("Failed to load window "+e.getMessage());
            }

        });
    }
}
