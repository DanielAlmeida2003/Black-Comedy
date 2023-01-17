package com.example.blackcomedyfinal.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.Date;

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
import java.util.Map;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private jokesAdapter jokesAdapter;
    private singleJoke jokesArray =  singleJoke.getInstance();
    Utilizadores utilizador;

    @Override
    public void onResume() {
        super.onResume();
        ListJokes();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        RecyclerView recyclerView = binding.recycleViewJokes;
        View root = binding.getRoot();
        utilizador = singleuser.utilizador.get(0);

        TextView textView = binding.textView5;
        textView.setText("Hello " + utilizador.getNome() );
        Button btnPublish = binding.btnPublish;

        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String joke = binding.comedyText.getText().toString();
                CreateJoke(joke);
                ListJokes();
            }
        });


        return root;
    }


    @SuppressLint("NotifyDataSetChanged")
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
                                jokes.setComedyText(document.get("comedyText").toString());
                                jokes.setJokeDate(document.get("comedyDate").toString());
                                jokes.setUser(document.get("user"));


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

        jokesAdapter.notifyDataSetChanged();

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
            comedy.put("comedyUserLikes", null );
            comedy.put("comedyDate",currentTime.toString());
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
                            Log.w("TAG", "Error adding document", e);
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