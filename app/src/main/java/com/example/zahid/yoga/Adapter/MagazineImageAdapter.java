package com.example.zahid.yoga.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zahid.yoga.CustomItemClickListener;
import com.example.zahid.yoga.GetterSetter.Magazine;
import com.example.zahid.yoga.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by Zahid on 10/20/2018.
 */

public class MagazineImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //private int listItemLayout;

    private Context context;
    private LayoutInflater inflater;
    List<Magazine> magazines = Collections.emptyList();
    Magazine currentMagazine;

    CustomItemClickListener listener;




    public MagazineImageAdapter(Context context, List<Magazine> magazines,CustomItemClickListener listener){
        this.context = context;
        inflater= LayoutInflater.from(context);
        this.magazines = magazines;
        this.listener = listener;
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        ImageView magazine_imageView;
        TextView magazine_name;

        public MyHolder(View view) {
            super(view);
            magazine_imageView = (ImageView) view.findViewById(R.id.magazine_ImageView);
            magazine_name = (TextView) view.findViewById(R.id.magazine_Name);
        }


    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.magazine_list, parent, false);
        final MyHolder holder = new MyHolder(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(view,holder.getPosition());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder)holder;
        Magazine current = magazines.get(position);
        myHolder.magazine_name.setText(current.name);

        //load image
        Glide.with(context).load(current.bitmap).into(myHolder.magazine_imageView);

    }

//    @Override
//    public void onBindViewHolder(MyViewHolder holder, int position) {
//
//        Magazine magazine = magazines.get(position);
//
//        holder.magazine_imageView.setImageBitmap(magazine.getImageView());
//        holder.magazine_name.setText(magazine.getName());
//
//    }

    @Override
    public int getItemCount() {
        return magazines.size();
    }




}
