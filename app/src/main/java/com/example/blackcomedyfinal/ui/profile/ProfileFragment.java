package com.example.blackcomedyfinal.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.blackcomedyfinal.Adapter.jokesAdapter;
import com.example.blackcomedyfinal.databinding.FragmentProfileBinding;
import com.example.blackcomedyfinal.models.Utilizadores;
import com.example.blackcomedyfinal.singleton.singleJoke;
import com.example.blackcomedyfinal.singleton.singletonposts;
import com.example.blackcomedyfinal.singleton.singleuser;

import org.json.JSONArray;

import java.util.Map;

public class ProfileFragment extends Fragment {

    private  FragmentProfileBinding binding;
    private jokesAdapter jokesAdapter;
    private singleJoke jokesArray =  singleJoke.getInstance();
    private singletonposts postsArray = singletonposts.getInstances();

    Utilizadores utilizador;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ListOneUser();
        ListPostsFromOneUser(utilizador.getId());
        return root;
    }

    public void ListOneUser(){

        utilizador = singleuser.utilizador.get(0);

        //Nome do User
        TextView nome = binding.profileName;
        nome.setText(utilizador.getNome());

        //Bio do User
        TextView bio = binding.profileBio;
        bio.setText(utilizador.getBio());

        //User Image
        ImageView imageView = binding.profileImage;
        Glide.with(getActivity().getApplicationContext()).load(utilizador.getImage()).into(imageView);

        //Count Followers
        TextView followers = binding.countFollowers;
        Object users = utilizador.getFollowers();
        Object usersFollowing = utilizador.getFollowings();

        if(users == null){
            followers.setText("0");
        }else{
            followers.setText(String.valueOf(((JSONArray)users).length()));
        }

        //Count Followings
        TextView following = binding.countFollowings;
        if(usersFollowing == null){
            following.setText("0");
        }else{
            following.setText(String.valueOf(((JSONArray)users).length()));
        }

    }

    public void ListPostsFromOneUser(String idUser){


        for (int i = 0; i < jokesArray.size() - 1; i++){

            Object user = jokesArray.get(i).getUser();
            String UserId = (String) ((Map<String, Object>) user).get("id");

            if(UserId == idUser){
                postsArray.add(jokesArray.get(i));
            }
        }

        jokesAdapter = new jokesAdapter(getActivity(), jokesArray);

        RecyclerView jokesProfiles = binding.jokesProfiles;

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        jokesProfiles.setLayoutManager(layoutManager);
        jokesProfiles.setItemAnimator(new DefaultItemAnimator());
        jokesProfiles.setAdapter(jokesAdapter);


        /*
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("comedy")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Object user = document.get("user");
                                String id = (String) ((Map<String, Object>)document.get("user")).get("id");




                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });

         */

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
