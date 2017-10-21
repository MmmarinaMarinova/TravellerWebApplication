package model;

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
    Multimedia(long id, String url, boolean isVideo, Post post) {
        this(url, isVideo, post);
        this.id = id;
        this.setPost(post);
    }

    //constructor to be used when putting object in database
    Multimedia(String url, boolean isVideo, Post post) {
        this.setUrl(url);
        this.setVideo(isVideo);
        this.setPost(post);

    }

    Multimedia(String url, boolean isVideo) {
        this.setUrl(url);
        this.setVideo(isVideo);
    }

    long getId() {
        return this.id;
    }

    void setId(long id) {
        this.id = id;
    }

    String getUrl() {
        return this.url;
    }

    void setUrl(String url) {
        //TODO regex for url
        this.url = url;
    }

    boolean isVideo() {
        return this.isVideo;
    }

    void setVideo(boolean video) {
        isVideo = video;
    }

    Post getPost() {
        return this.post;
    }

    void setPost(Post post) {
        this.post = post;
    }
}
