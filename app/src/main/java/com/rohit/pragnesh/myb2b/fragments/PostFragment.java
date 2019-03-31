package com.rohit.pragnesh.myb2b.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rohit.pragnesh.myb2b.MainActivity;
import com.rohit.pragnesh.myb2b.R;
import com.rohit.pragnesh.myb2b.adapter.PostAdapter;
import com.rohit.pragnesh.myb2b.models.Image;
import com.rohit.pragnesh.myb2b.models.Post;
import com.rohit.pragnesh.myb2b.utility.AuthUtility;
import com.rohit.pragnesh.myb2b.utility.ModelUtils;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PostFragment extends Fragment {

    private final String TAG = "NEOTag";
    ModelUtils modelUtils = ModelUtils.getInstance();

    RecyclerView recycler_posts;
    Context context;

    List<Post> posts;
    List<Image> images;

    ProgressDialog progressDialog;

    public PostFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        if (savedInstanceState != null)
            return view;

        context = view.getContext();
        //View
        recycler_posts = view.findViewById(R.id.recycler_posts);
        recycler_posts.setHasFixedSize(true);
        recycler_posts.setLayoutManager(new GridLayoutManager(view.getContext(), 2));

        fetchPosts();

        return view;
    }

    //set posts
    public void fetchPosts() {

        Call<List<Post>> call = MainActivity.mAPIService.getPosts();
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(@NonNull Call<List<Post>> call,
                                   @NonNull Response<List<Post>> response) {
                if (response.isSuccessful()) {
                    posts = response.body();
                    modelUtils.setPosts(posts);
                    fetchImage();
                } else
                    AuthUtility.getInstance().getErrorMessage(response.errorBody());
            }

            @Override
            public void onFailure(@NonNull Call<List<Post>> call,
                                  @NonNull Throwable t) {
                Log.e(TAG, "PostFragment: Unable to submit post req to API." + t);
            }
        });
    }

    //set images
    public void fetchImage() {

        Call<List<Image>> call = MainActivity.mAPIService.getImages();

        call.enqueue(new Callback<List<Image>>() {
            @Override
            public void onResponse(@NonNull Call<List<Image>> call,
                                   @NonNull Response<List<Image>> response) {
                if (response.isSuccessful()) {
                    images = response.body();
                    modelUtils.setImages(images);
                    displayData(modelUtils.getPosts(), modelUtils.getImages());
                } else
                    AuthUtility.getInstance().getErrorMessage(response.errorBody());
            }

            @Override
            public void onFailure(@NonNull Call<List<Image>> call,
                                  @NonNull Throwable t) {
                Log.e(TAG, "PostFragment: fetchImages: Unable to submit post req to API. " + t);
            }
        });
    }

    private void displayData(List<Post> postList, List<Image> imageList) {
        Collections.reverse(postList);
        PostAdapter adapter = new PostAdapter(context, postList, imageList);
        recycler_posts.setAdapter(adapter);
        progressDialog.dismiss();
    }
}
