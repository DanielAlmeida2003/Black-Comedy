package com.example.blackcomedyfinal;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class Register extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button btnRegister = findViewById(R.id.btnRegister);

        EditText editNome = findViewById(R.id.inputName2);
        EditText editEmail = findViewById(R.id.inputEmail);
        EditText editPass = findViewById(R.id.inputPassword2);
        EditText editcPass = findViewById(R.id.inputRepeatPassword);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                registo(editNome.getText().toString(), editEmail.getText().toString(), editPass.getText().toString(), editcPass.getText().toString());

            }
        });

    }

    public void goToLogin(View v) {
        Intent in  = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(in);
    }

    public void registo(String nome, String email, String pass, String cpass){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        EditText editNome = findViewById(R.id.inputName2);
        EditText editEmail = findViewById(R.id.inputEmail);
        EditText editPass = findViewById(R.id.inputPassword2);
        EditText editcPass = findViewById(R.id.inputRepeatPassword);


        if(nome.equals("") || email.equals("") || pass.equals("") || cpass.equals("")){

            if(nome.equals("")){
                editNome.setError("O nome está vazio!");
                editNome.requestFocus();
            }
            if(email.equals("")){
                editEmail.setError("O email está vazio!");
                editEmail.requestFocus();
            }
            if(pass.equals("")){
                editPass.setError("A password está vazio !");
                editPass.requestFocus();
            }
            if(cpass.equals("")){
                editcPass.setError("A confirmação da password está vazio!");
                editcPass.requestFocus();
            }

        }else{

            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

            if(!email.matches(emailPattern)){

                editEmail.setError("O email está inválido!");
                editEmail.requestFocus();

            }else{

                if (!pass.equals(cpass)){

                    editPass.setError("As passwords não coincidem!");
                    editPass.requestFocus();

                }else{

                    // Create a new user with a first and last name
                    Map<String, Object> user = new HashMap<>();
                    user.put("Nome", nome);
                    user.put("Email", email);
                    user.put("Password", pass);

                    // Add a new document with a generated ID
                    db.collection("users")
                            .add(user)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {

                                    Toast.makeText(Register.this, "Registado com sucesso", Toast.LENGTH_SHORT).show();
                                    Intent in = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(in);

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error adding document", e);
                                }
                            });



                }

            }


        }

    }



}