package com.rohit.pragnesh.myb2b.utility;

import com.rohit.pragnesh.myb2b.models.Bidder;
import com.rohit.pragnesh.myb2b.models.Image;
import com.rohit.pragnesh.myb2b.models.JWT;
import com.rohit.pragnesh.myb2b.models.Post;
import com.rohit.pragnesh.myb2b.models.Profile;
import com.rohit.pragnesh.myb2b.models.Users;

import java.util.List;

public class ModelUtils {
    private static final ModelUtils ourInstance = new ModelUtils();
    private final String TAG = "NEOTag";
    private List<Post> posts;
    private List<Image> images;
    private List<Profile> profiles;
    private List<Bidder> bidders;
    private Users user;
    private JWT jwt;
    private Profile current_profile;
    private Bidder bidder;

    private ModelUtils() {
    }

    public static ModelUtils getInstance() {
        return ourInstance;
    }

    public Profile getCurrent_profile() {
        return current_profile;
    }

    public void setCurrent_profile(Profile current_profile) {
        this.current_profile = current_profile;
    }

    public Bidder getBidder() {
        return bidder;
    }

    public void setBidder(Bidder bidder) {
        this.bidder = bidder;
    }

    public JWT getJwt() {
        return jwt;
    }

    public void setJwt(JWT jwt) {
        this.jwt = jwt;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public Post getPost(int id) {
        Post post = posts.get(0);
        for (Post p : posts) {
            if (p.getId() == id)
                post = p;
        }
        return post;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public List<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<Profile> profiles) {
        this.profiles = profiles;
    }

    public List<Bidder> getBidders() {
        return bidders;
    }

    public void setBidders(List<Bidder> bidders) {
        this.bidders = bidders;
    }
}
