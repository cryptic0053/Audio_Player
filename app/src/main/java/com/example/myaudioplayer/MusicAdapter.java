package com.example.myaudioplayer;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<MusicFiles>mFiles;

    MusicAdapter(Context mContext,ArrayList<MusicFiles>mFiles)
    {
        this.mFiles=mFiles;
        this.mContext=mContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.music_items,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.file_name.setText(mFiles.get(position).getTitle());
        byte[] image;
        try {
            image = getAlbumArt(mFiles.get(position).getPath());
        } catch (IOException e) {
            e.printStackTrace();
            image = null; // Set image to null if there's an exception
        }

        if (image != null) {
            // If album art is available, load it using Glide
            Glide.with(mContext).asBitmap()
                    .load(image)
                    .into(holder.album_art);
            holder.album_art.setVisibility(View.VISIBLE);
        } else {
            // If album art is not available, load the default image directly
            holder.album_art.setImageResource(R.drawable.baseline_music_note_24);
            holder.album_art.setVisibility(View.VISIBLE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(mContext,PlayerActivity.class);
                intent.putExtra("position",position);
                mContext.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mFiles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView file_name;
        ImageView album_art;
        public MyViewHolder(@NonNull View itemview){
            super(itemview);
            file_name=itemview.findViewById(R.id.music_file_name);
            album_art=itemview.findViewById(R.id.music_img);
        }
    }
    private byte[] getAlbumArt(String uri) throws IOException {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(uri);
            byte[] art = retriever.getEmbeddedPicture();
            return art;
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception gracefully, e.g., log the error or return null
            return null;
        } finally {
            retriever.release();
        }
    }

}
