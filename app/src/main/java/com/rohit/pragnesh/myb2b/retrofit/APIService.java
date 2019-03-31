package com.rohit.pragnesh.myb2b.retrofit;

import com.rohit.pragnesh.myb2b.models.Bidder;
import com.rohit.pragnesh.myb2b.models.Image;
import com.rohit.pragnesh.myb2b.models.JWT;
import com.rohit.pragnesh.myb2b.models.Post;
import com.rohit.pragnesh.myb2b.models.Profile;
import com.rohit.pragnesh.myb2b.models.Users;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIService {

    @POST("auth/jwt/create/")
    @FormUrlEncoded
    Call<JWT> generateToken(@Field("username") String username,
                            @Field("password") String password);

    @POST("auth/jwt/verify/")
    @FormUrlEncoded
    Call<JWT> verifyToken(@Field("token") String token);


    @POST("auth/jwt/refresh/")
    @FormUrlEncoded
    Call<JWT> refreshToken(@Field("refresh") String refresh);


    @POST("auth/users/")
    @FormUrlEncoded
    Call<Users> registerUser(@Field("email") String email,
                             @Field("username") String username,
                             @Field("password") String password);


    @GET("auth/users/me/")
    Call<Users> whoAmI(@Header("Authorization") String authHeader);

    @GET("api/post/")
    Call<List<Post>> getPosts();

    @POST("api/post/")
    @FormUrlEncoded
    Call<Post> setPost(@Header("Authorization") String authHeader,
                       @Field("title") String post_title,
                       @Field("description") String post_desc,
                       @Field("content") String post_content,
                       @Field("initial_price") int price);

    @GET("api/images/")
    Call<List<Image>> getImages();

    @Multipart
    @POST("api/images/")
    Call<Image> setImage(@Header("Authorization") String authHeader,
                         @Part MultipartBody.Part image,
                         @Part("post") RequestBody post_url);

    @GET("api/profile/")
    Call<List<Profile>> getProfiles();

    @POST("api/bidder/")
    @FormUrlEncoded
    Call<Bidder> setBidder(@Header("Authorization") String authHeader,
                           @Field("post") String post_url,
                           @Field("prices") int bid_val);

    @GET("api/bidder/")
    Call<List<Bidder>> getBidder();


    @GET("auth/users/")
    Call<List<Users>> getUsers();
}
