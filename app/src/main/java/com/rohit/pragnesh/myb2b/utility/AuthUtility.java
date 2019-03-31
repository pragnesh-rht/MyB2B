package com.rohit.pragnesh.myb2b.utility;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;

import com.rohit.pragnesh.myb2b.MainActivity;
import com.rohit.pragnesh.myb2b.activities.PostActivity;
import com.rohit.pragnesh.myb2b.adapter.HistoryAdapter;
import com.rohit.pragnesh.myb2b.models.Bidder;
import com.rohit.pragnesh.myb2b.models.Image;
import com.rohit.pragnesh.myb2b.models.JWT;
import com.rohit.pragnesh.myb2b.models.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthUtility {
    private static final AuthUtility ourInstance = new AuthUtility();
    private final String TAG = "NEOTag";
    private String username, password;
    private HistoryAdapter adapter;
    private ModelUtils modelUtils = ModelUtils.getInstance();
    private ArrayList<String> paths;
    private int count = 0;
    private Context context;
    private TextView tv_current_bid;

    private AuthUtility() {
    }

    public static AuthUtility getInstance() {
        return ourInstance;
    }

    public void login(final String username, final String password, final Context context) {
        this.context = context;

        Call<JWT> call = MainActivity.mAPIService.generateToken(username, password);

        call.enqueue(new Callback<JWT>() {
            @Override
            public void onResponse(@NonNull Call<JWT> call, @NonNull Response<JWT> response) {
                JWT jwt = response.body();
                if (response.isSuccessful()) {
                    if (jwt != null) {
                        modelUtils.setJwt(jwt);
                        MainActivity.prefConfig.writeTokens(jwt.getRefresh(), jwt.getAccess());
                        MainActivity.prefConfig.writeLoginStatus(true);
                        MainActivity.prefConfig.writeUserName(username);
                        MainActivity.prefConfig.writePassword(password);

                        Intent intent = new Intent(context, PostActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        //MainActivity.prefConfig.displayToast("Welcome!");
                    } else {
                        MainActivity.prefConfig.displayToast("No response from the server!");
                        Log.e(TAG, " Error code: " + response.code());
                    }
                } else if (response.errorBody() != null) {
                    getErrorMessage(response.errorBody());
                }

            }

            @Override
            public void onFailure(@NonNull Call<JWT> call, @NonNull Throwable t) {
                Log.e(TAG, "AuthUtility: login: Unable to submit post req to API." + t);
            }
        });
    }

    public void getErrorMessage(ResponseBody responseErrorBody) {
        String errorBody;
        JSONObject jsonObject;
        StringBuilder errors = new StringBuilder();
        try {
            if (responseErrorBody != null) {
                errorBody = responseErrorBody.string();
                jsonObject = new JSONObject(errorBody.trim());

                Iterator<String> keys = jsonObject.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    JSONArray arr = jsonObject.getJSONArray(key);
                    for (int i = 0; i < arr.length(); i++) {
                        errors.append(arr.getString(i)).append("\n");
                    }
                }
                MainActivity.prefConfig.displayToast(errors.toString());
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private void getAuth() {
        username = MainActivity.prefConfig.readUserName();
        password = MainActivity.prefConfig.readPassword();
    }

    public void bidNow(final int bid_amount, final Post post, HistoryAdapter adapter, TextView tv_current_bid) {
        this.adapter = adapter;
        this.tv_current_bid = tv_current_bid;
        getAuth();

        Call<JWT> call = MainActivity.mAPIService.generateToken(username, password);

        call.enqueue(new Callback<JWT>() {
            @Override
            public void onResponse(@NonNull Call<JWT> call, @NonNull Response<JWT> response) {
                JWT jwt = response.body();
                if (response.isSuccessful()) {
                    if (jwt != null) {
                        modelUtils.setJwt(jwt);
                        MainActivity.prefConfig.writeTokens(jwt.getRefresh(), jwt.getAccess());
                        MainActivity.prefConfig.writeLoginStatus(true);
                        setBidder(bid_amount, post);

                    } else {
                        MainActivity.prefConfig.displayToast("No response from the server!");
                        Log.e(TAG, " Error code: " + response.code());
                    }
                } else if (response.errorBody() != null) {
                    getErrorMessage(response.errorBody());
                }
            }

            @Override
            public void onFailure(@NonNull Call<JWT> call, @NonNull Throwable t) {
                Log.e(TAG, "Unable to submit post req to API." + t);
            }
        });
    }

    private void setBidder(final int bid_amount, final Post post) {

        Call<Bidder> call = MainActivity.mAPIService.setBidder(modelUtils.getJwt()
                .getAuthorization(), post.getUrl(), bid_amount);
        call.enqueue(new Callback<Bidder>() {
            @Override
            public void onResponse(@NonNull Call<Bidder> call,
                                   @NonNull Response<Bidder> response) {
                if (response.isSuccessful()) {
                    Bidder bidder = response.body();
                    if (bidder != null) {
                        modelUtils.setBidder(bidder);
                        MainActivity.prefConfig
                                .displayToast("Your bid has been submitted successfully!!");
                        modelUtils.getPost(post.getId()).setCurrentPrice(bid_amount);
                        String newPrice = "₹ " + post.getCurrentPrice();
                        tv_current_bid.setText(newPrice);
                        adapter.notifyDataSetChanged();
                        adapter.notifyItemInserted(adapter.getItemCount());
                    } else
                        Log.e(TAG, "Could not place bid... code: " + response.code());
                } else if (response.errorBody() != null) {
                    getErrorMessage(response.errorBody());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Bidder> call,
                                  @NonNull Throwable t) {
                MainActivity.prefConfig
                        .displayToast("Server isn't responding please try again later");
                Log.e(TAG, "ERROR : " + t);
            }
        });
    }

    private void logout() {
        MainActivity.prefConfig.writeLoginStatus(false);
    }

    public void sendPost(final String title, final String desc, final String content,
                         final int price, final ArrayList<String> paths, Context context) {
        this.paths = paths;
        getAuth();

        this.context = context;
        Call<JWT> call = MainActivity.mAPIService.generateToken(username, password);

        call.enqueue(new Callback<JWT>() {
            @Override
            public void onResponse(@NonNull Call<JWT> call, @NonNull Response<JWT> response) {
                JWT jwt = response.body();
                if (response.isSuccessful()) {
                    if (jwt != null) {
                        modelUtils.setJwt(jwt);
                        MainActivity.prefConfig.writeTokens(jwt.getRefresh(), jwt.getAccess());
                        MainActivity.prefConfig.writeLoginStatus(true);
                        setPost(title, desc, content, price);
                    } else {
                        MainActivity.prefConfig.displayToast("No response from the server!");
                        Log.e(TAG, " Error code: " + response.code());
                    }
                } else if (response.errorBody() != null) {
                    getErrorMessage(response.errorBody());
                }
            }

            @Override
            public void onFailure(@NonNull Call<JWT> call, @NonNull Throwable t) {
                Log.e(TAG, "Unable to submit post req to API." + t);
            }
        });
    }

    private void setPost(String title, String desc, String content, int price) {
        Call<Post> call = MainActivity.mAPIService.setPost(modelUtils.getJwt().getAuthorization(),
                title, desc, content, price);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(@NonNull Call<Post> call,
                                   @NonNull Response<Post> response) {
                if (response.isSuccessful()) {
                    Post post = response.body();
                    if (post != null) {
                        modelUtils.getPosts().add(post);
                        MainActivity.prefConfig.displayToast("Your Post has been submitted successfully!!");
                        uploadImage(post);
                    } else
                        Log.e(TAG, "Could not send post.. code: " + response.code() + " body:" + response.body());
                } else if (response.errorBody() != null) {
                    getErrorMessage(response.errorBody());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Post> call,
                                  @NonNull Throwable t) {
                MainActivity.prefConfig
                        .displayToast("Server isn't responding please try again later");
                Log.e(TAG, "ERROR : " + t);
            }
        });
    }

    private void uploadImage(Post post) {
        if (paths.size() == 0 || paths.size() == count) return;

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMax(100);
        progressDialog.setMessage("Uploading....");
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Your Post is on it's way..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        while (count != paths.size()) {

            String imagePath = paths.get(count);
            count++;
            File file = new File(imagePath);

            //creating request body for file
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("post_image", file.getName(), requestFile);
            RequestBody post_url = RequestBody.create(MediaType.parse("text/plain"), post.getUrl());
            Log.e(TAG, "requestFile: " + body.toString());

            Call<Image> call = MainActivity.mAPIService.setImage(modelUtils.getJwt().getAuthorization(), body, post_url);

            call.enqueue(new Callback<Image>() {
                @Override
                public void onResponse(@NonNull Call<Image> call,
                                       @NonNull Response<Image> response) {

                    if (response.isSuccessful()) {
                        Image image = response.body();
                        Log.e(TAG, "Image upload complete.. code: " + response.code() + "");
                        if (image != null) {
                            modelUtils.getImages().add(image);
                            progressDialog.dismiss();
                        } else
                            Log.e(TAG, "Could not upload image.. code: " + response.code() + " body:" + response.body());
                    } else if (response.errorBody() != null) {
                        getErrorMessage(response.errorBody());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Image> call,
                                      @NonNull Throwable t) {
                    MainActivity.prefConfig
                            .displayToast("Server isn't responding please try again later");
                    Log.e(TAG, "ERROR : " + t);
                }
            });
        }
        count = 0;
        if (progressDialog.isShowing())
            progressDialog.dismiss();
        Intent intent = new Intent(context, PostActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
