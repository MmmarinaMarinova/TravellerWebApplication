package model;

import com.sun.org.apache.xerces.internal.dom.events.MutationEventImpl;
import model.db.*;
import model.exceptions.CategoryException;
import model.exceptions.LocationException;
import model.exceptions.PostException;
import model.exceptions.UserException;

import java.sql.SQLException;
import java.util.HashSet;

/**
 * Created by Marina on 18.10.2017 г..
 */
public class TestClass {
    public static void main(String[] args) throws UserException, SQLException, CategoryException, LocationException, PostException {
        //User user=new User("pencho","pencho","pencho@pe.ncho");
        //if(UserDao.getInstance().existsUser(user.getUsername(),user.getPassword())){
        //    System.out.println("User exist");
        //}else{
        //    System.out.println("User does not exist");
        //}
        //UserDao.getInstance().insertUser(user);
        User user1=UserDao.getInstance().getUserById(1);
        //System.out.println(user1.getUsername());
        //System.out.println(user1.getEmail());
        //System.out.println(user1.getPassword());
        //Category category=new Category("trial category");
        Category category1=CategoryDao.getInstance().getCategoryById(3);
        //int aff=CategoryDao.getInstance().deleteCategory(category);
        //System.out.println(aff);
        //System.out.println(category.getId());
        //Location location=new Location("lat", "long","desc","trial location");
        //System.out.println(location.getId());
        //Location updated=LocationDao.getInstance().insertLocation(location);
       // System.out.println(updated.getId());
        //Location loc=LocationDao.getInstance().getLocationById(2);
        //System.out.println(loc.getLocationName());
        //System.out.println(loc.getDescription());
       //Multimedia multimedia=new Multimedia("dfgsdf", false);
       //MultimediaDao.getInstance().insertMultimedia(post, multimedia);
        HashSet<Category> categories=new HashSet<>();
        categories.add(category1);
        HashSet<User> taggedPeople=new HashSet<>();
        taggedPeople.add(user1);
        Post post=new Post(user1,categories,taggedPeople);
        PostDao.getInstance().insertNewPost(post);
    }
}
