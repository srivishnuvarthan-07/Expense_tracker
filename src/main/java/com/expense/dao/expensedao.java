package com.expense.dao;

import com.expense.util.*;
import com.expense.model.Catemodel;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.sql.*;

public class expensedao {
    private static final String SELECTCATEGORIES = "SELECT * FROM categories";
    private static final String INSERT_CATEGORY = "INSERT INTO categories(name) VALUES(?)";
    private static final String UPDATE_CATEGORY = "UPDATE categories SET name = ? WHERE id = ?";
    private static final String DELETE_CATEGORY = "DELETE FROM categories WHERE id = ?";

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
}
