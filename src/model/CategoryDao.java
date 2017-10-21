package model;

import model.exceptions.CategoryException;

import java.sql.*;
import java.util.HashSet;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created by Marina on 15.10.2017 ?..
 */
public class CategoryDao extends AbstractDao{
    private static CategoryDao instance;
    public static ConcurrentSkipListSet<Category> cachedCategories;


    private CategoryDao(){}

    public static ConcurrentSkipListSet<Category> getCachedCategories() {
        return cachedCategories;
    }

    public static synchronized CategoryDao getInstance() {
        if(instance==null){
            instance=new CategoryDao();
        }
        return instance;
    }

    //tested
    public Category insertNewCategory(Category category) throws CategoryException, SQLException {
        try (PreparedStatement ps = this.getCon().prepareStatement(
                "insert into categories(category_name) value (?);",
                Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1, category.getName());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            category.setId(rs.getLong(1));
        }catch (SQLException e){
            throw new CategoryException("New Category couldn't be inserted in database.Reason: "+e.getMessage());
        }
        return category;
    }

    //tested
    public Category getCategoryById(long categoryId) throws SQLException, CategoryException {
        PreparedStatement ps = this.getCon().prepareStatement(
                "select category_name from categories where category_id= ?  ;");
        ps.setLong(1, categoryId);
        ResultSet rs = ps.executeQuery();
        rs.next();
        Category category=new Category(categoryId, rs.getString("category_name"));
        return category;
    }

    //tested
    public void deleteCategory(Category category) throws SQLException, CategoryException {
        Connection connection=this.getCon();
        PreparedStatement deleteCategory=null;
        PreparedStatement deleteFromPostsCategories=null;
        try {
            connection.setAutoCommit(false);
            deleteCategory= connection.prepareStatement(
                    "delete from categories where category_id=?;");
            deleteCategory.setLong(1, category.getId());
            deleteCategory.executeUpdate();
            deleteFromPostsCategories=connection.prepareStatement("delete from posts_categories where category_id=?;");
            deleteFromPostsCategories.setLong(1,category.getId());
            deleteFromPostsCategories.executeUpdate();
            connection.commit();
            //TODO REMOVE CATEGORY FROM ALL POSTS' COLLECTIONS
            cachedCategories.remove(category);
        } catch (SQLException e) {
            throw new CategoryException("Category could not be deleted. Reason: "+e.getMessage());
        }finally {
            connection.rollback();
            connection.setAutoCommit(true);
        }
    }

    //tested
    public HashSet<Category> getCategoriesForPost(Post post) throws SQLException, CategoryException {
        PreparedStatement ps = this.getCon().prepareStatement("select category_id from posts_categories where post_id= ?;");
        ps.setLong(1, post.getId());
        ResultSet rs=ps.executeQuery();
        HashSet<Category> categories=new HashSet<>();
        while (rs.next()){
            categories.add(CategoryDao.getInstance().getCategoryById(rs.getLong("category_id")));
        }
        return categories;
    }

    //tested
    public void addAllCategoriesToPost(Post post,HashSet<Category> categories) throws CategoryException {
        //TODO IF ENTRY EXISTS- THROWS EXCEPTION!!!
        try {
            PreparedStatement ps = this.getCon().prepareStatement("INSERT into posts_categories(post_id, category_id) values (?,?);");
            for (Category category : categories) {
                ps.setLong(1,post.getId());
                ps.setLong(2,category.getId());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new CategoryException("Could not add all categories to this post. Reason: "+e.getMessage());
        }

    }
}
