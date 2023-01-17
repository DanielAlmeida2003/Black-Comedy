package com.example.blackcomedyfinal.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blackcomedyfinal.R;
import com.example.blackcomedyfinal.models.Jokes;
import com.example.blackcomedyfinal.singleton.singleJoke;

import java.util.ArrayList;

public class jokesAdapter extends RecyclerView.Adapter<jokesAdapter.ViewHolder> {


    TextView nomeUser, jokeText;
    ImageView profileImage;
    private LayoutInflater layoutInflater;
    private ArrayList<Jokes> data;

    public jokesAdapter(Context context, ArrayList<Jokes> data){

        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;

    }

    @NonNull
    @Override
    public jokesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.joke_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull jokesAdapter.ViewHolder holder, int position) {

        nomeUser.setText(data.get(position).getComedyText());
        jokeText.setText(data.get(position).getComedyText());

    }

    public void delete(){

        int size = data.size();
        data.clear();
        notifyItemRangeRemoved(0, size);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            nomeUser = itemView.findViewById(R.id.nomeUser);
            jokeText = itemView.findViewById(R.id.jokeText);


        }
    }

}
