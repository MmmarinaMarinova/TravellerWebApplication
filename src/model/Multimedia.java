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
    private long postId;

    //constructor to be used when fetching from database
    public Multimedia(long id, String url, boolean isVideo, long postId) {
        this(url, isVideo, postId);
        this.id = id;
    }

    //constructor to be used when putting object in database
    public Multimedia(String url, boolean isVideo, long postId) {
        this.setUrl(url);
        this.setVideo(isVideo);
        this.setPostId(postId);
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        //TODO regex for url
        this.url = url;
    }

    public boolean isVideo() {
        return this.isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    public long getPostId() {
        return this.postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }
}
