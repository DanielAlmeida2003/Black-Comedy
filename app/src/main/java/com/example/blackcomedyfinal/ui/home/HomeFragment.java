package com.example.blackcomedyfinal.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.bumptech.glide.Glide;
import com.example.blackcomedyfinal.Adapter.jokesAdapter;
import com.example.blackcomedyfinal.databinding.FragmentHomeBinding;
import com.example.blackcomedyfinal.models.Jokes;
import com.example.blackcomedyfinal.models.Utilizadores;
import com.example.blackcomedyfinal.singleton.singleJoke;
import com.example.blackcomedyfinal.singleton.singleuser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private jokesAdapter jokesAdapter;
    private singleJoke jokesArray =  singleJoke.getInstance();

    Utilizadores utilizador;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        RecyclerView recyclerView = binding.recycleViewJokes;
        View root = binding.getRoot();

        //Escrever o nome do utilizaddores
        utilizador = singleuser.utilizador.get(0);
        TextView textView = binding.textView5;
        textView.setText("Hello " + utilizador.getNome() );

        ImageView imageView = binding.MainProfileImage;
        Glide.with(getActivity().getApplicationContext()).load(utilizador.getImage()).into(imageView);

        Button btnPublish = binding.btnPublish;
        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String joke = binding.comedyText.getText().toString();
                CreateJoke(joke);
                ListJokes();

            }
        });


        ListJokes();
        return root;
    }


    public void ListJokes(){

        RecyclerView recyclerView = binding.recycleViewJokes;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //Add All Jokes
        db.collection("comedy")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Jokes jokes = new Jokes();

                                jokes.setIdJoke(document.getId());

                                if(document.get("comedyText") != null){
                                    jokes.setComedyText(document.get("comedyText").toString());
                                }else{
                                    jokes.setComedyText(null);
                                }

                                jokes.setJokeDate(document.get("comedyDate").toString());

                                Object user = document.get("user");
                                Map<String, String> map = (Map<String, String>) document.get("user");
                                List<String> likes = new ArrayList<String>(map.values());


                                //Toast.makeText(getActivity(), ((Map<String, Object>) user).get("image").toString(), Toast.LENGTH_SHORT).show();

                                jokes.setUser(user);
                                jokes.setLikes(likes);


                                if(document.get("comedyAudio") != null){
                                    jokes.setAudioFormat(document.get("comedyAudio").toString());
                                }else{
                                    jokes.setAudioFormat(null);
                                }

                                jokes.setLikes((List<String>) document.get("Likes"));


                                jokesArray.add(jokes);
                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });


        jokesAdapter = new jokesAdapter(getActivity(), jokesArray);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(jokesAdapter);


    }

    public void CreateJoke(String joke){

        EditText jokeText = binding.comedyText;

        if(joke.isEmpty()){

            jokeText.setError("Este campo é obrigatório!");
            jokeText.requestFocus();

        }else{

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Date currentTime = Calendar.getInstance().getTime();

            // Create a new user with a first and last name
            Map<String, Object> comedy = new HashMap<>();

            comedy.put("comedyText", joke);
            comedy.put("comedyAudio", null);
            comedy.put("comedyDate",currentTime.toString());
            comedy.put("Likes", null);
            comedy.put("user", utilizador);

            // Add a new document with a generated ID
            db.collection("comedy")
                    .add(comedy)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                            jokeText.setText("");
                            Toast.makeText(getActivity().getApplicationContext(), "Inserido com sucesso", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("TAG", "Error adding document", e);
                        }
                    });
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}