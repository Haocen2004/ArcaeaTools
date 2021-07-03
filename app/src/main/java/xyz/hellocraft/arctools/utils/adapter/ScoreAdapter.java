package xyz.hellocraft.arctools.utils.adapter;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import xyz.hellocraft.arctools.R;
import xyz.hellocraft.arctools.utils.data.ScoreData;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.MyViewHolder> {
    private List<ScoreData> scoreDataList;
    private final Activity activity;

    public ScoreAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.ptt_card, parent, false);
        return new MyViewHolder(itemView);
    }

    public void setScoreDataList(List<ScoreData> scoreDataList) {
        this.scoreDataList = scoreDataList;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        ScoreData scoreData = scoreDataList.get(position);
        if (scoreData.getSongData().getName().contains("Best")) {
            holder.textViewNum.setText(" ");
            holder.textViewSongName.setText(scoreData.getSongData().getName() + " - " + scoreData.getDiffType());
            holder.textViewScore.setText("AvgScore: " + scoreData.getScore());
            holder.textViewPtt.setText("AvgPtt: "+scoreData.getPtt());

        } else {
            holder.textViewNum.setText(position + "");
            holder.textViewSongName.setText(scoreData.getSongData().getName() + " - " + scoreData.getDiffType());
            holder.textViewScore.setText("Score: " + scoreData.getScore());
            holder.textViewPtt.setText("ptt: " + scoreData.getSongData().getRating(scoreData.getDifficulty()) / 10 + "->" + scoreData.getPtt());
        }
//        holder.imageViewAvatar.setImageURI(Uri.parse(sponsorData.getAvatarImgUrl()));
//        Glide.with(activity).load(sponsorData.getAvatarImgUrl()).circleCrop().into(holder.imageViewAvatar);
    }

    @Override
    public int getItemCount() {
        return scoreDataList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewSongName, textViewScore, textViewPtt, textViewNum;
//        ImageView imageViewSongImg;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSongName = itemView.findViewById(R.id.textViewSongName);
            textViewScore = itemView.findViewById(R.id.textViewScore);
            textViewPtt = itemView.findViewById(R.id.textViewPTT);
            textViewNum = itemView.findViewById(R.id.textViewNum);
//            imageViewSongImg = itemView.findViewById(R.id.imageViewSongImg);

        }
    }
}