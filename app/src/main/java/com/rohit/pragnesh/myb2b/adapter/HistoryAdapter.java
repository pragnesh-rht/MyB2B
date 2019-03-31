package com.rohit.pragnesh.myb2b.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rohit.pragnesh.myb2b.R;
import com.rohit.pragnesh.myb2b.models.Bidder;
import com.rohit.pragnesh.myb2b.models.Post;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private List<Bidder> bidderList;


    public HistoryAdapter(List<Bidder> bidderList, Post post) {
        List<Bidder> bidders = new ArrayList<>();

        for (Bidder bidder : bidderList) {
            if (bidder.getPost().equals(post.getUrl())) {
                bidders.add(bidder);
            }
        }
        this.bidderList = bidders;
    }

    @NonNull
    @Override
    public HistoryAdapter.HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.history_layout, viewGroup, false);
        return new HistoryAdapter.HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.HistoryViewHolder historyViewHolder, int i) {
        Date currentTime = Calendar.getInstance().getTime();
        String bidder_id = bidderList.get(i).getId() + "";
        String bidder = bidderList.get(i).getBidAuthor();
        String bid = bidderList.get(i).getPrices() + "";
        String time = currentTime + "";

        historyViewHolder.tv_id.setText(bidder_id);
        historyViewHolder.tv_bidder.setText(bidder);
        historyViewHolder.tv_bid.setText(bid);
        historyViewHolder.tv_time.setText(time);
    }

    @Override
    public int getItemCount() {
        return bidderList.size();
    }


    class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tv_id, tv_bidder, tv_bid, tv_time;

        HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_bidder = itemView.findViewById(R.id.history_bidder);
            tv_id = itemView.findViewById(R.id.history_id);
            tv_bid = itemView.findViewById(R.id.history_bid);
            tv_time = itemView.findViewById(R.id.history_time);
        }
    }
}
