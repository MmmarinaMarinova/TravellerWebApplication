package model;

import model.exceptions.CategoryException;

/**
 * Created by Marina on 15.10.2017 г..
 */
public class Category {
    private final static int MAX_LENGTH=255;
    private final static int MIN_LENGTH=5;
    private long id;
    private String name;


    //     * constructor to be used when putting object in database
    public Category(String name) throws CategoryException {
        this.setName(name);
    }


    //     * constructor to be used for fetching from database
    public Category(long id, String name) throws CategoryException {
        this(name);
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public long getId() {
        return this.id;
    }

    public void setName(String name) throws CategoryException {
        if(!name.isEmpty()){
            if(name.length()<MIN_LENGTH){
                throw new CategoryException("Name of the category is too short. It should be more than "+MIN_LENGTH+" symbols.");
            }else if(name.length()>MAX_LENGTH){
                throw new CategoryException("Name of the category is too long. It should be less than"+MAX_LENGTH+" symbols");
            }
        }else{
            throw new CategoryException("Name of the category should not be empty!");
        }
        this.name = name;
    }

    public void setId(long id) throws CategoryException {
        this.id = id;
    }
}
