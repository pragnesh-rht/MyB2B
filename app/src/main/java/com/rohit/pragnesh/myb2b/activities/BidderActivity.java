package com.rohit.pragnesh.myb2b.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rohit.pragnesh.myb2b.MainActivity;
import com.rohit.pragnesh.myb2b.R;
import com.rohit.pragnesh.myb2b.adapter.HistoryAdapter;
import com.rohit.pragnesh.myb2b.models.Bidder;
import com.rohit.pragnesh.myb2b.models.Post;
import com.rohit.pragnesh.myb2b.utility.AuthUtility;
import com.rohit.pragnesh.myb2b.utility.ModelUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BidderActivity extends AppCompatActivity {
    private final String TAG = "NEOTag";

    ModelUtils modelUtils = ModelUtils.getInstance();
    AuthUtility authUtility = AuthUtility.getInstance();
    Post post;
    int id = 0;
    List<Bidder> bidders = new ArrayList<>();

    ProgressBar progressBar;
    TextView tv_id_val, tv_min_bid_val, tv_end_date_val, tv_current_bid;
    EditText et_bid;
    Button btn_bid;
    RecyclerView history_recycler_view;
    HistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bidder);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getIntExtra("ID", 0);
        }
        post = modelUtils.getPost(id);
        toolbar.setTitle(post.title);

        String s_id = "" + post.getId();
        String initPrice = "₹ " + post.getInitialPrice();
        String currentPrice = "₹ " + post.getCurrentPrice();

        tv_id_val = findViewById(R.id.tv_id_value);
        tv_min_bid_val = findViewById(R.id.tv_minbid_value);
        tv_end_date_val = findViewById(R.id.tv_enddate_value);
        tv_current_bid = findViewById(R.id.tv_currentbid_value);

        progressBar = findViewById(R.id.bid_progress);
        btn_bid = findViewById(R.id.btn_bid);
        et_bid = findViewById(R.id.et_currentbid);

        tv_id_val.setText(s_id);
        tv_min_bid_val.setText(initPrice);
        tv_end_date_val.setText(post.getDateEnd());
        tv_current_bid.setText(currentPrice);

        history_recycler_view = findViewById(R.id.history_recycler_view);
        history_recycler_view.setHasFixedSize(true);
        history_recycler_view.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, true));

        progressBar.setVisibility(View.VISIBLE);
        fetchHistory();

        btn_bid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int bid_amount;
                if (!TextUtils.isEmpty(et_bid.getText())) {
                    bid_amount = Integer.parseInt(et_bid.getText().toString());
                    authUtility.bidNow(bid_amount, post, adapter, tv_current_bid);
                }
            }
        });

        // Lookup the swipe container view
        SwipeRefreshLayout swipeContainer = findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                adapter.notifyDataSetChanged();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void fetchHistory() {
        Call<List<Bidder>> call = MainActivity.mAPIService.getBidder();

        call.enqueue(new Callback<List<Bidder>>() {
            @Override
            public void onResponse(@NonNull Call<List<Bidder>> call,
                                   @NonNull Response<List<Bidder>> response) {
                if (response.isSuccessful()) {
                    bidders = response.body();
                    modelUtils.setBidders(bidders);
                    displayData(bidders);
                } else
                    AuthUtility.getInstance().getErrorMessage(response.errorBody());
            }

            @Override
            public void onFailure(@NonNull Call<List<Bidder>> call,
                                  @NonNull Throwable t) {
                Log.e(TAG, "BidderActivity: please connect to internet " + t);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void displayData(List<Bidder> bidders) {
        this.bidders = bidders;
        adapter = new HistoryAdapter(bidders, post);
        history_recycler_view.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null)
            adapter.notifyDataSetChanged();
        String currentPrice = "₹ " + post.getCurrentPrice();
        tv_current_bid.setText(currentPrice);
    }
}
