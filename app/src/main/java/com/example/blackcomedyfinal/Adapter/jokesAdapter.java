package com.example.blackcomedyfinal.Adapter;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifBitmapProvider;
import com.example.blackcomedyfinal.R;
import com.example.blackcomedyfinal.Settings;
import com.example.blackcomedyfinal.models.Jokes;
import com.example.blackcomedyfinal.models.Utilizadores;
import com.example.blackcomedyfinal.singleton.singleJoke;
import com.example.blackcomedyfinal.singleton.singleuser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.w3c.dom.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class jokesAdapter extends RecyclerView.Adapter<jokesAdapter.ViewHolder> {
    TextView nomeUser, jokeText, likes, player_duration, idPost ;
    ImageView imageView , btnLike;
    LinearLayout audioPlayer;
    ImageButton play;

    SeekBar seekBar;
    Utilizadores utilizadores = singleuser.utilizador.get(0);
    Boolean Liked = false;

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

        int Pos = position;

        String nome = (String) ((Map<String, Object>) data.get(position).getUser()).get("nome");
        String imagem = (String) ((Map<String, Object>) data.get(position).getUser()).get("image");
        Object nrLikes =  data.get(position).getLikes();


        //Audio
        if(data.get(position).getAudioFormat() == null){
            audioPlayer.setVisibility(View.INVISIBLE);
        }else{

            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(data.get(Pos).getAudioFormat());

            Long duration = Long.parseLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
            player_duration.setText(String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(duration), TimeUnit.MILLISECONDS.toSeconds(duration), TimeUnit.MILLISECONDS.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))));

            play.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {


                    MediaPlayer mediaPlayer = new MediaPlayer();


                    try {

                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mediaPlayer.setDataSource(data.get(Pos).getAudioFormat());
                        mediaPlayer.prepareAsync();
                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mp.start();
                            }
                        });

                    }catch (IOException e){
                        Log.e("TAG", "Erro ao reproduzir áudio: " + e.getMessage());
                    }

                }
            });

            //jokes.setAudioFormat(null);
        }

        //Contar Likes
        if(data.get(position).getLikes() != null){
            likes.setText(String.valueOf(data.get(position).getLikes().size()));
        }
        //Fazer Like
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference docRef = db.collection("comedy").document(data.get(Pos).getIdJoke());

                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            // Documento encontrado
                            // Aqui você pode acessar os dados do documento
                            Map<String, Object> data1 = documentSnapshot.getData();

                            List<String> like = (List<String>) documentSnapshot.get("Likes");

                            if(like == null){

                                likes.setText("1");

                                if(data.get(Pos).getLikes() == null){

                                    List<String> likeds = new ArrayList<>();
                                    likeds.add(utilizadores.getId());
                                    data.get(Pos).setLikes(likeds);

                                }else{
                                    data.get(Pos).getLikes().add(utilizadores.getId());
                                }


                                Map<String, Object> updates = new HashMap<>();
                                updates.put("Likes", data.get(Pos).getLikes());
                                db.collection("comedy").document(data.get(Pos).getIdJoke()).update(updates);
                                Toast.makeText(holder.itemView.getContext(), "Gosto feito com sucesso", Toast.LENGTH_SHORT).show();

                            }else{

                                for(int i=0; i<like.size();i++){

                                    if(like.get(i).equals(utilizadores.getId())) {
                                        Liked = true;
                                    }

                                }

                                if(Liked){
                                    Toast.makeText(holder.itemView.getContext(), "Já deste like", Toast.LENGTH_SHORT).show();
                                }else{

                                    int var = Integer.parseInt(likes.getText().toString()) + 1;
                                    likes.setText(String.valueOf(var));

                                    if(data.get(Pos).getLikes() == null){

                                        List<String> likeds = new ArrayList<>();
                                        likeds.add(utilizadores.getId());
                                        data.get(Pos).setLikes(likeds);

                                    }else{
                                        data.get(Pos).getLikes().add(utilizadores.getId());
                                    }


                                    Map<String, Object> updates = new HashMap<>();
                                    updates.put("Likes", data.get(Pos).getLikes());
                                    db.collection("comedy").document(data.get(Pos).getIdJoke()).update(updates);
                                    Toast.makeText(holder.itemView.getContext(), "Gosto feito com sucesso", Toast.LENGTH_SHORT).show();
                                }
                            }





                        }
                    }
                });

            }
        });

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
            play = itemView.findViewById(R.id.playAudio);
            seekBar = itemView.findViewById(R.id.seekaudiobar);
            player_duration = itemView.findViewById(R.id.player_duration);
            btnLike = itemView.findViewById(R.id.likeButton);
            idPost = itemView.findViewById(R.id.postId);

        }
    }




}

