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

    public Category getCategoryById(long categoryId) throws SQLException {
        Connection con = DBManager.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement(
                "select category_id from categories where category_id= ?  ;");
        ps.setLong(1, categoryId);
        ResultSet rs = ps.executeQuery();
        rs.next();
        Category category=new Category(categoryId, rs.getString("category_name"));
        return category;
    }

    public void deleteCategoryById(long categoryId) throws SQLException {
        Connection con = DBManager.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement(
                "delete from categories where category_id= ? ;");
        ps.setLong(1, categoryId);
        ps.executeUpdate();
    }
}
