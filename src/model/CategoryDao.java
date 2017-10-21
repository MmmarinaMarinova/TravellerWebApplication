﻿package model;

import model.Category;
import model.Post;
import model.exceptions.CategoryException;

import java.sql.*;
import java.util.HashSet;

/**
 * Created by Marina on 15.10.2017 г..
 */
public class CategoryDao extends AbstractDao{
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
        this.getCon().setAutoCommit(false);
        PreparedStatement ps = null;
        try {
            ps = this.getCon().prepareStatement(
                    "insert into categories(category_name) value (?);",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, category.getName());
            ps.executeUpdate();
            ResultSet rs=ps.getGeneratedKeys();
            rs.next();
            category.setId(rs.getLong(1));
            this.getCon().commit();
        } catch (SQLException e) {
            this.getCon().rollback();
            this.getCon().setAutoCommit(true);
            this.getCon().close();
            throw e;
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
    public void deleteCategory(Category category) throws SQLException {
        this.getCon().setAutoCommit(false);
        PreparedStatement ps = null;
        try {
            ps = this.getCon().prepareStatement(
                    "delete from categories where category_id=?;");
            ps.setLong(1, category.getId());
            ps.executeUpdate();
            this.getCon().commit();
        } catch (SQLException e) {
            this.getCon().rollback();
            this.getCon().setAutoCommit(true);
            this.getCon().close();
            throw e;
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
    public void addAllCategoriesToPost(Post post,HashSet<Category> categories) throws SQLException {
        //TODO IF ENTRY EXISTS- THROWS EXCEPTION!!!
        PreparedStatement ps = this.getCon().prepareStatement("INSERT into posts_categories(post_id, category_id) values (?,?);");
        for (Category category : categories) {
            ps.setLong(1,post.getId());
            ps.setLong(2,category.getId());
            ps.addBatch();
        }
        ps.executeBatch();
    }
}