package com.example.blackcomedyfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.blackcomedyfinal.models.Utilizadores;
import com.example.blackcomedyfinal.singleton.singleuser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        EditText emailField = findViewById(R.id.inputName);
        EditText passField = findViewById(R.id.inputPassword);

        Button btnLogin = findViewById(R.id.button);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = emailField.getText().toString();
                String pass = passField.getText().toString();

                //login
                login(email, pass);
            }
        });

    }


    public void login(String email, String pass) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        EditText emailField = findViewById(R.id.inputName);
        EditText passField = findViewById(R.id.inputPassword);

        FirebaseAuth mAutth;

        //verifcar se os camos estão vazios
        if (email.isEmpty() || pass.isEmpty()) {

            if (email.isEmpty()) {
                emailField.setError("O email é obrigatório!");
                emailField.requestFocus();
            }
            if (pass.isEmpty()) {
                passField.setError("A password é obrigatório!");
                passField.requestFocus();
            }

        } else {

            //Fazer Login
            db.collection("users")
                    .whereEqualTo("Email", email).whereEqualTo("Password", pass)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {

                                String idUser = "";
                                String nomeUser = "";
                                String emailUser = "";
                                String image = "";
                                String path = "";

                                int i = 0;

                                for(QueryDocumentSnapshot documentSnapshot : task.getResult()){

                                     idUser = documentSnapshot.getId().toString();
                                     nomeUser = documentSnapshot.get("Nome").toString();
                                     image = documentSnapshot.get("ImagemDePerfil").toString();
                                     path = documentSnapshot.get("CaminhoImagem").toString();

                                    i = i + 1;
                                }


                                if(i == 0){
                                    Toast.makeText(MainActivity.this, "Wrong Credentionals", Toast.LENGTH_SHORT).show();
                                }else{

                                    Utilizadores user = new Utilizadores();

                                    user.setId(idUser);
                                    user.setNome(nomeUser);
                                    user.setEmail(emailUser);
                                    user.setPassword(pass);
                                    user.setImage(image);
                                    user.setPathImage(path);

                                    singleuser s = singleuser.getInstance();

                                    s.utilizador.add(user);

                                    MainActivity.this.finish();
                                    Intent in = new Intent(getApplicationContext(), HomeActivity.class);
                                    startActivity(in);

                                }


                            } else {

                            }
                        }
                    });
        }
    }

    public void goToRegister(View v) {

        Intent in = new Intent(getApplicationContext(), Register.class);
        startActivity(in);

    }

}


