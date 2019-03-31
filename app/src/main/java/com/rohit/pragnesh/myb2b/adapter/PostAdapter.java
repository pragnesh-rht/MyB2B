package com.rohit.pragnesh.myb2b.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohit.pragnesh.myb2b.R;
import com.rohit.pragnesh.myb2b.activities.DetailActivity;
import com.rohit.pragnesh.myb2b.models.Image;
import com.rohit.pragnesh.myb2b.models.Post;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private Context context;
    private List<Post> postList;
    private SparseArray<List<Image>> imageSpa;

    public PostAdapter(Context context, List<Post> postList, List<Image> imageList) {
        this.context = context;
        this.postList = postList;

        SparseArray<List<Image>> imageSpa = new SparseArray<>();

        for (Post post : postList) {
            List<Image> images = new LinkedList<>();
            String postUrl = post.getUrl();

            for (Image image : imageList) {
                String imagePostUrl = image.getPost();
                if (postUrl.equals(imagePostUrl)) {
                    images.add(image);
                }
            }
            imageSpa.put(post.getId(), images);
        }
        this.imageSpa = imageSpa;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.post_layout, viewGroup, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder postViewHolder, int i) {
        int post_id = postList.get(i).getId();

        if (imageSpa.get(post_id).size() != 0) {


            String imageUrl = imageSpa.get(post_id).get(0).getPostImage();

            Picasso.get()
                    .load(imageUrl)
                    .resize(600, 600)
                    .into(postViewHolder.image_main);
        }
        String price = String.format("₹ %s", String.valueOf(postList.get(i).getInitialPrice()));

        postViewHolder.txt_initial_price.setText(price);
        postViewHolder.txt_title.setText(String.valueOf(postList.get(i).getTitle()));
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }


    public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_title, txt_initial_price;
        ImageView image_main;

        PostViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_initial_price = itemView.findViewById(R.id.txt_price);
            txt_title = itemView.findViewById(R.id.txt_title);
            image_main = itemView.findViewById(R.id.image_main);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Post current_post = postList.get(getAdapterPosition());
            List<Image> current_image = imageSpa.get(current_post.getId());

            String[] imageUrls = new String[current_image.size()];
            int i = 0;
            for (Image image : current_image) {
                imageUrls[i] = image.getPostImage();
                i++;
            }
            DetailActivity.imageUrls = imageUrls;
            DetailActivity.post = current_post;
            Intent detailIntent = new Intent(context, DetailActivity.class);
            context.startActivity(detailIntent);
        }
    }
}