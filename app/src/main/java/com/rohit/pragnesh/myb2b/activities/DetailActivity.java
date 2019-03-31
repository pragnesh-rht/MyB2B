package com.rohit.pragnesh.myb2b.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohit.pragnesh.myb2b.R;
import com.rohit.pragnesh.myb2b.adapter.ViewPagerAdapter;
import com.rohit.pragnesh.myb2b.models.Post;
import com.rohit.pragnesh.myb2b.models.Profile;
import com.rohit.pragnesh.myb2b.utility.ModelUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static String[] imageUrls;
    public static Post post;
    ModelUtils modelUtils = ModelUtils.getInstance();
    ViewPager viewPager;
    Toolbar toolbar;
    List<Profile> profiles;
    TextView title, desc, content, init_price, current_price, end_date, author;
    ImageView author_pic;
    Button btn_bid_now;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Bid 2 Buy");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.inflateMenu(R.menu.menu_main);

        //yeah
        title = findViewById(R.id.tv_title);
        desc = findViewById(R.id.tv_description);
        content = findViewById(R.id.tv_content);
        init_price = findViewById(R.id.tv_init_price);
        current_price = findViewById(R.id.tv_current_price);
        end_date = findViewById(R.id.tv_end_date);
        author = findViewById(R.id.tv_author);
        author_pic = findViewById(R.id.detail_author_pic);
        viewPager = findViewById(R.id.detail_view_pager);
        btn_bid_now = findViewById(R.id.btn_bid_now);


        //getIntent();

        ViewPagerAdapter adapter = new ViewPagerAdapter(this, imageUrls);
        viewPager.setAdapter(adapter);
        toolbar.setTitle(post.title);


        profiles = modelUtils.getProfiles();
        setData();

        btn_bid_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), BidderActivity.class);
                intent.putExtra("ID", post.getId());
                startActivity(intent);
            }
        });
    }

    private void setData() {
        String initPrice = "" + post.getInitialPrice();
        String currentPrice = "" + post.getCurrentPrice();

        title.setText(post.getTitle());
        desc.setText(post.getDescription());
        content.setText(post.getContent());
        init_price.setText(initPrice);
        current_price.setText(currentPrice);
        end_date.setText(post.getDateEnd());
        author.setText(post.getAuthor());

        for (Profile profile : profiles) {
            if (profile.getUser().equals(post.getAuthor())) {

                Picasso.get()
                        .load(profile.getImage())
                        .resize(300, 300)
                        .into(author_pic);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        post = modelUtils.getPost(post.getId());
        String currentPrice = post.getCurrentPrice() + "";
        current_price.setText(currentPrice);
    }
}
