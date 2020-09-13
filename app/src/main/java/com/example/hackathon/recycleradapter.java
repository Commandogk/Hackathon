package com.example.hackathon;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class recycleradapter extends RecyclerView.Adapter<recycleradapter.MyViewHolder>{
    ArrayList<file> files = new ArrayList<>();
    Context ct;
    itemclick itemclick;
    public recycleradapter(Context context, ArrayList<file> files1,itemclick itemclick) {
        ct=  context;
        files=files1;
        this.itemclick =itemclick;
    }
    @NonNull
    @Override
    public recycleradapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ct);
        View view = inflater.inflate(R.layout.listitem,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull recycleradapter.MyViewHolder holder, final int position) {
        holder.Us.setVisibility(View.VISIBLE);
        final file b = files.get(position);
        String a = b.getFilename();
        holder.Us.setText(a);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(ct,Firstpage.class);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                a.putExtra("path",b.getFilepath());
                ct.startActivity(a);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                itemclick.onitemClick(b,position);
                return true;
            }
        });
    }



    @Override
    public int getItemCount() {
        return files.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Us;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Us=((View)itemView).findViewById(R.id.listitemtext);
        }
    }
}
