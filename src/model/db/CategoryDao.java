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

    //tested
    public Category insertNewCategory(Category category) throws CategoryException, SQLException {
        Connection con = DBManager.getInstance().getConnection();
        con.setAutoCommit(false);
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(
                    "insert into categories(category_name) value (?);",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, category.getName());
            ps.executeUpdate();
            ResultSet rs=ps.getGeneratedKeys();
            rs.next();
            category.setId(rs.getLong(1));
            con.commit();
        } catch (SQLException e) {
            con.rollback();
            con.setAutoCommit(true);
            con.close();
        }
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
    public void deleteCategory(Category category) throws SQLException {
        Connection con = DBManager.getInstance().getConnection();
        con.setAutoCommit(false);
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(
                    "delete from categories where category_id=?;");
            ps.setLong(1, category.getId());
            ps.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            con.rollback();
            con.setAutoCommit(true);
            con.close();

        }
    }


    //tested
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

    //tested
    public void addAllCategoriesToPost(Post post,HashSet<Category> categories) throws SQLException {
        //TODO IF ENTRY EXISTS- THROWS EXCEPTION!!!
        Connection con = DBManager.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement("INSERT into posts_categories(post_id, category_id) values (?,?);");
        for (Category category : categories) {
            ps.setLong(1,post.getId());
            ps.setLong(2,category.getId());
            ps.addBatch();
        }
        ps.executeBatch();
    }
}
