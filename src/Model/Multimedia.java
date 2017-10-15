package Model;

/**
 * Created by Marina on 15.10.2017 г..
 */
public class Multimedia {

    /*multimedia_id INT(11)
file_dir VARCHAR(255)
is_video TINYINT(4)
post_id INT(11)*/

    private long id;
    private String url;
    private boolean isVideo;
    private Post post;

    //constructor to be used when fetching from database
    public Multimedia(long id, String url, boolean isVideo, Post post) {
        this(url, isVideo, post);
        this.id = id;
    }

    //constructor to be used when putting object in database
    public Multimedia(String url, boolean isVideo, Post post) {
        this.url = url;
        this.isVideo = isVideo;
        this.post = post;
    }
}
