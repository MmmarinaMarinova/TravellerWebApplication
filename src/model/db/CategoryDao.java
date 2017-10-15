package model.db;

import model.Category;

import java.sql.*;

/**
 * Created by Marina on 15.10.2017 г..
 */
public class CategoryDao {
    private static CategoryDao instance;

    private CategoryDao(){}

    public static synchronized CategoryDao getInstance() {
        if(instance==null){
            instance=new CategoryDao();
        }
        return instance;
    }

    public void insertNewCategory(Category category) throws SQLException {
        Connection con = DBManager.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement(
                "insert into categories(category_name) value (?);",
                Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, category.getName());
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        category.setId(rs.getLong(1));
    }

    public Category getCategory(String categoryName) throws SQLException {
        Connection con = DBManager.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement(
                "select category_id from categories where category_name= ? ;");
        ps.setString(1, categoryName);
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        Category category=new Category(rs.getLong("category_id"), categoryName);
        return category;
    }
}
