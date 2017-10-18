package model.db;

import model.Category;
import model.Post;
import model.exceptions.CategoryException;

import java.sql.*;
import java.util.HashSet;

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

    //tested, setting id does not work!?!?!?!?!?!?!?!?
    public Category insertNewCategory(Category category) throws SQLException, CategoryException {
        Connection con = DBManager.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement(
                "insert into categories(category_name) value (?);",
                Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, category.getName());
        ps.executeUpdate();
        ResultSet rs=ps.getGeneratedKeys();
        rs.next();
        category.setId(rs.getLong(1));
        return category;
    }

    //tested
    public Category getCategoryById(long categoryId) throws SQLException, CategoryException {
        Connection con = DBManager.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement(
                "select category_name from categories where category_id= ?  ;");
        ps.setLong(1, categoryId);
        ResultSet rs = ps.executeQuery();
        rs.next();
        Category category=new Category(categoryId, rs.getString("category_name"));
        return category;
    }

    //tested
    public int deleteCategory(Category category) throws SQLException {
        Connection con = DBManager.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement(
                "delete from categories where category_id=?;");
        ps.setLong(1, category.getId());
        int affectedRows=ps.executeUpdate();
        return affectedRows;
    }

    public HashSet<Category> getCategoriesForPost(Post post) throws SQLException, CategoryException {
        Connection con = DBManager.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement("select category_id from posts_categories where post_id= ?;");
        ps.setLong(1, post.getId());
        ResultSet rs=ps.executeQuery();
        HashSet<Category> categories=new HashSet<>();
        while (rs.next()){
            categories.add(CategoryDao.getInstance().getCategoryById(rs.getLong("category_id")));
        }
        return categories;
    }

    public void addAllCategoriesToPost(HashSet<Category> categories) throws SQLException {
        Connection con = DBManager.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement("insert into categories(category_name) values (?);");
        for (Category category : categories) {
            ps.setString(1,category.getName());
            ps.addBatch();
        }
        ps.executeBatch();
    }
}
