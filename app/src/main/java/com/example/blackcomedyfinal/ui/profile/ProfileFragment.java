package com.example.blackcomedyfinal.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.blackcomedyfinal.Adapter.jokesAdapter;
import com.example.blackcomedyfinal.databinding.FragmentProfileBinding;
import com.example.blackcomedyfinal.models.Utilizadores;
import com.example.blackcomedyfinal.singleton.singleJoke;
import com.example.blackcomedyfinal.singleton.singleuser;

import org.json.JSONArray;
import org.w3c.dom.Text;

public class ProfileFragment extends Fragment {

    private  FragmentProfileBinding binding;
    private com.example.blackcomedyfinal.Adapter.jokesAdapter jokesAdapter;
    private singleJoke jokesArray =  singleJoke.getInstance();
    Utilizadores utilizador;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        utilizador = singleuser.utilizador.get(0);

        //Nome do User
        TextView nome = binding.profileName;
        nome.setText(utilizador.getNome());

        //Bio do User
        TextView bio = binding.profileBio;
        bio.setText(utilizador.getBio());

        //Count Followers
        TextView followers = binding.countFollowers;
        Object users = utilizador.getFollowers();


        if(users == null){
            followers.setText("0");
        }else{
            followers.setText(String.valueOf(((JSONArray)users).length()));
        }

        //


        return root;
    }

    public void ListOneUser(String idUser){


    }

    public void ListPostsFromOneUser(String idUser){

    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
