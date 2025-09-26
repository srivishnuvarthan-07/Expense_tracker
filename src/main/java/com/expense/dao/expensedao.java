package com.expense.dao;

import com.expense.util.*;
import com.expense.model.Catemodel;
import com.expense.model.Expensemodel;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.sql.*;

public class expensedao {
    private static final String SELECTCATEGORIES = "SELECT * FROM categories";
    private static final String INSERT_CATEGORY = "INSERT INTO categories(name) VALUES(?)";
    private static final String UPDATE_CATEGORY = "UPDATE categories SET name = ? WHERE id = ?";
    private static final String DELETE_CATEGORY = "DELETE FROM categories WHERE id = ?";
    private static final String SELECT_CATEGORY_NAME = "SELECT name FROM categories WHERE id = ?";
    private static final String SELECT_CATEGORY_ID = "SELECT id FROM categories WHERE name = ?";
    private static final String SELECT_ALL_EXPENSE = "SELECT * FROM expenses ORDER BY created_at DESC";
    private static final String INSERT_EXPENSE = "INSERT INTO expenses(amount, description, category_id) VALUES(?,?,?)";
    private static final String UPDATE_EXPENSE = "UPDATE expenses SET amount = ? , description = ?, category_id = ? WHERE id = ?";
    private static final String DELETE_EXPENSE = "DELETE FROM expenses WHERE id = ?";

    public List<Catemodel> getAllCategories() throws SQLException{
        List<Catemodel> categories = new ArrayList<>();

        try(
                Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(SELECTCATEGORIES);
                ResultSet res = stmt.executeQuery();
        )
        {
            while(res.next()){
                categories.add(new Catemodel(res.getInt("id"), res.getString("name")));
            }
        }
        return categories;
    }

    public int createCategory(String name) throws SQLException{
        try(
                Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(INSERT_CATEGORY);
        )
        {
            stmt.setString(1,name);
            return stmt.executeUpdate();
        }
    }
    public int updateCategory(int id,String categoryName) throws SQLException{
        try(
                Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(UPDATE_CATEGORY);
        )
        {
            stmt.setString(1,categoryName);
            stmt.setInt(2,id);
            return stmt.executeUpdate();
        }
    }

    public int deleteCategory(int id) throws SQLException{
        try(
                Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(DELETE_CATEGORY);
        )
        {
            stmt.setInt(1,id);
            return stmt.executeUpdate();
        }
    }
    public int getCategoryId(String category) throws SQLException{
        try(
                Connection conn = Database.getConnection();
                PreparedStatement stmt1 = conn.prepareStatement(SELECT_CATEGORY_ID);
        )
        {
            stmt1.setString(1, category);
            try(ResultSet res = stmt1.executeQuery()){
                if(res.next()){
                    return res.getInt("id");
                }
                else{
                    throw new SQLException("No Such Category");
                }
            }
        }
    }

    public String getCategoryName(int cate_id) throws SQLException{
        String category = "";
        try(
                Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(SELECT_CATEGORY_NAME);
        )
        {
            stmt.setInt(1,cate_id);
            try(ResultSet res = stmt.executeQuery()){
                if(res.next()){
                    category = res.getString("name");
                }

            }
        }
        return category;
    }

    public Expensemodel getExpenseRow(ResultSet res) throws SQLException{
        int id = res.getInt("id");
        int Amount = res.getInt("amount");
        String describtion = res.getString("description");
        int cate_id = res.getInt("category_id");
        Timestamp createdAt = res.getTimestamp("created_at");
        String category = getCategoryName(cate_id);

        return new Expensemodel(id, Amount,describtion, createdAt, cate_id,category);
    }

    public List<Expensemodel> getAllExpenses() throws Exception {
        List<Expensemodel> expense = new ArrayList<>();

        try(
                Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_EXPENSE);
                ResultSet res = stmt.executeQuery();
        )
        {
            while(res.next()){
                expense.add(getExpenseRow(res));
            }
        }
        catch (SQLException e){
            System.err.println("SQL Error: " + e.getMessage());
            throw new Exception("There is a error");
        }
        return expense;
    }

    public int createExpense(int amt,String category,String description) throws SQLException{
        try(
                Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(INSERT_EXPENSE);
        )
        {
            stmt.setInt(1,amt);
            int cateId = getCategoryId(category);
            stmt.setString(2,description);
//            stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(3,cateId);

            return stmt.executeUpdate();
        }
    }

    public int updateExpense(int id, int amount, String description, String category) throws SQLException{
        try(
                Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(UPDATE_EXPENSE);
        )
        {
            stmt.setInt(1,amount);
            stmt.setString(2,description);

            int cateId = getCategoryId(category);
            stmt.setInt(3,cateId);
            stmt.setInt(4,id);

            return stmt.executeUpdate();
        }
    }

    public int deleteExpense(int id) throws SQLException{
        try(
                Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(DELETE_EXPENSE);
        )
        {
            stmt.setInt(1,id);
            return stmt.executeUpdate();
        }
    }
}
