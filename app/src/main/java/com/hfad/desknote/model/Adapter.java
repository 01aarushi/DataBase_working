package com.hfad.desknote.model;

import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.hfad.desknote.NoteDetails;
import com.hfad.desknote.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    List<String> contents;
    List<String> titles;
    public Adapter(List<String> contents, List<String> titles)
    {
        this.contents=contents;
        this.titles=titles;


    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.note_view_layout,parent,false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.Notetitle.setText(titles.get(position));
        holder.Notecontent.setText(contents.get(position));
        final int code=getBackgroudColor();
        holder.mCardview.setCardBackgroundColor(holder.view.getResources().getColor(code,null));

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(), NoteDetails.class);
                intent.putExtra("content",contents.get(position));
                intent.putExtra("title",titles.get(position));
                intent.putExtra("code",code);
                v.getContext().startActivity(intent);
            }
        });
    }

    private int getBackgroudColor() {
        List<Integer> colorcode=new ArrayList<>();
        colorcode.add(R.color.blue);
        colorcode.add(R.color.darkblue);
        colorcode.add(R.color.dark);
        colorcode.add(R.color.orange);
        colorcode.add(R.color.purple);
        colorcode.add(R.color.red);
        colorcode.add(R.color.yellow);
        colorcode.add(R.color.green);
        colorcode.add(R.color.grey);
        colorcode.add(R.color.pink);

        Random random=new Random();
        int number=random.nextInt(colorcode.size());
        return colorcode.get(number);
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView Notecontent,Notetitle;
        View view;
        CardView mCardview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Notetitle=itemView.findViewById(R.id.titles);
            Notecontent=itemView.findViewById(R.id.content);
            mCardview=itemView.findViewById(R.id.noteCard);
            view=itemView;

        }
    }
}
