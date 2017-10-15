package Model;

/**
 * Created by Marina on 15.10.2017 г..
 */
public class Category {
    private long id;
    private String name;


    //     * constructor to be used when putting object in database
    public Category(String name) {
        this.name = name;
    }


    //     * constructor to be used for fetching from database
    public Category(long id, String name) {
        this(name);
        this.id = id;
    }


}
