package com.example.soulscribe;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.browser.trusted.sharing.ShareData;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.util.DataUtils;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.myViewHolder> {
    private Context context;
    private List<Journal> journalList;
    private int currpos;

    public MyAdapter(Context context, List<Journal> journalList) {
        this.context = context;
        this.journalList = journalList;
    }

    public void sharedata(){
        if (currpos!=RecyclerView.NO_POSITION){
            Journal currjournal = journalList.get(currpos);
            String title = currjournal.getTitle();
            String thoughts = currjournal.getThoughts();
            Uri imguri = Uri.parse(currjournal.getImageUrl());
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/*");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT,title);
            shareIntent.putExtra(Intent.EXTRA_TEXT, thoughts);
            shareIntent.putExtra(Intent.EXTRA_STREAM, imguri);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(Intent.createChooser(shareIntent, "Journals"));
        }else {
            Toast.makeText(context, "No Journal is shareable!", Toast.LENGTH_SHORT).show();
        }

    }



    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.journal_row,parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
       Journal currentJournal = journalList.get(position);
       holder.title.setText(currentJournal.getTitle());
       holder.thoughts.setText(currentJournal.getThoughts());
       holder.name.setText(currentJournal.getUserName());
       String imageurl = currentJournal.getImageUrl();
       String time = (String) DateUtils.getRelativeTimeSpanString(currentJournal.getTimeadded().getSeconds()*1000);
       holder.dateAdded.setText(time);

        Glide.with(context).load(imageurl).fitCenter().into(holder.image);

        holder.shareBtn.setOnClickListener(v -> {
            currpos=position;
            sharedata();
        });
    }

    @Override
    public int getItemCount() {
        return journalList.size();
    }


    public class myViewHolder extends  RecyclerView.ViewHolder{
        public TextView title,thoughts,dateAdded,name;
        public ImageView image , shareBtn,deletebtn;
        public String userId,usename;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.journal_title_list);
            thoughts=itemView.findViewById(R.id.journal_thought_list);
            dateAdded=itemView.findViewById(R.id.journal_timestamp_list);
            image=itemView.findViewById(R.id.journal_img_list);
            name= itemView.findViewById(R.id.journal_row_username);
            shareBtn=itemView.findViewById(R.id.journal_roe_share_btn);
            deletebtn=itemView.findViewById(R.id.journal_roe_delete_btn);

        }



    }
}
