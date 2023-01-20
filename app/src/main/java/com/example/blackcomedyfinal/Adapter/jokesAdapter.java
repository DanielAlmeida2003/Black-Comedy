package com.example.blackcomedyfinal.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifBitmapProvider;
import com.example.blackcomedyfinal.R;
import com.example.blackcomedyfinal.models.Jokes;
import com.example.blackcomedyfinal.singleton.singleJoke;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class jokesAdapter extends RecyclerView.Adapter<jokesAdapter.ViewHolder> {
    TextView nomeUser, jokeText, likes;
    ImageView imageView;
    LinearLayout audioPlayer;

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

        String nome = (String) ((Map<String, Object>) data.get(position).getUser()).get("nome");
        String imagem = (String) ((Map<String, Object>) data.get(position).getUser()).get("image");
        Object nrLikes =  data.get(position).getLikes();

        //Audio
        if(data.get(position).getAudioFormat() == null){
            audioPlayer.setVisibility(View.INVISIBLE);
        }else{
            //jokes.setAudioFormat(null);
        }

        //Likes
        if(data.get(position).getLikes() != null){
            likes.setText(String.valueOf(((JSONArray)nrLikes).length()));
        }
        //Dados Simples
        nomeUser.setText(nome);
        jokeText.setText(data.get(position).getComedyText());

        //Imagem
        Glide.with(holder.itemView.getContext()).load(imagem).into(imageView);

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
            likes = itemView.findViewById(R.id.nrLikes);
            imageView = itemView.findViewById(R.id.userImage);
            audioPlayer = itemView.findViewById(R.id.audioPlayer);


        }
    }

}
