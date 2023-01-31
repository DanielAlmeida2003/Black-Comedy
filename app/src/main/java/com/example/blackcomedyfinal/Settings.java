package com.example.blackcomedyfinal;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.blackcomedyfinal.models.Utilizadores;
import com.example.blackcomedyfinal.singleton.singleuser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Calendar;
import java.util.Date;

public class Settings extends AppCompatActivity {

    Utilizadores utilizador = singleuser.utilizador.get(0);
    ImageView profileImage;
    public static final int PICK_IMAGE = 1;
    EditText nome, password , email,bio;
    Uri downloadUrl;

    Uri filePath;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode ==RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                // Obtenha o bitmap da imagem selecionada
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                // Defina o bitmap como a imagem do ImageView
                profileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_account);

        profileImage = findViewById(R.id.AccountImage);
        nome = findViewById(R.id.editTextTextPersonName);
        password = findViewById(R.id.editTextTextPersonName3);
        email = findViewById(R.id.editTextTextPersonName2);
        bio  = findViewById(R.id.bioText);


        nome.setText(utilizador.getNome());
        password.setText(utilizador.getPassword());
        email.setText(utilizador.getEmail());
        bio.setText(utilizador.getBio());

        Glide.with(getApplicationContext()).load(utilizador.getImage()).into(profileImage);

        Button btnUpdate = findViewById(R.id.update);
        Button btnBack = findViewById(R.id.back);
        Button btnLogout = findViewById(R.id.logout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(Settings.this, MainActivity.class);
                startActivity(in);
                finishAffinity();
            }
        });


        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent();
                in.setType("image/*");
                in.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(in, "Select Picture"),PICK_IMAGE);

            }
        });



        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(nome.getText().toString().equals("") || email.getText().toString().equals("") || password.getText().toString().equals("") ){

                    if(nome.getText().toString().equals("")){
                        nome.setError("This is field is empty");
                    }
                    if(email.getText().toString().equals("")){
                        email.setError("This is field is empty");
                    }
                    if(password.getText().toString().equals("")){
                        password.setError("This is field is empty");
                    }


                }else{

                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    //Firebase Storage
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReference();
                    StorageReference imagesRef = storageRef.child("profiles");

                    //Get the image from the ImageView and convert it to a byte array:
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) profileImage.getDrawable();
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();

                    Date currentTime = Calendar.getInstance().getTime();

                    String newName = nome.getText().toString()  + "-" + "-" + currentTime.toString() +  "profile.png";

                    //Create a new StorageReference for the image you want to upload and upload the image as a byte array:
                    StorageReference imageRef = imagesRef.child(newName);
                    UploadTask uploadTask = imageRef.putBytes(data);

                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            downloadUrl = taskSnapshot.getUploadSessionUri();
                            StorageReference imageRef = storage.getReference("profiles/" + newName);

                            //Obtenha a URL da imagem:
                            imageRef.getDownloadUrl()
                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    Map<String, Object> updates = new HashMap<>();
                                    String urlImage = uri.toString();

                                    updates.put("Nome", nome.getText().toString());
                                    updates.put("Password", password.getText().toString());
                                    updates.put("Email", email.getText().toString());
                                    updates.put("Bio", bio.getText().toString());

                                    updates.put("ImagemDePerfil", urlImage);

                                    Toast.makeText(Settings.this, "Atualizados com sucesso", Toast.LENGTH_SHORT).show();
                                    db.collection("users").document(utilizador.getId()).update(updates);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error adding Imagem", e);
                                }
                            });


                            //Toast.makeText(Settings.this, downloadUrl.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });



                    //;


                    //singleuser.utilizador.get(0).setNome(nome.getText().toString());
                    //singleuser.utilizador.get(0).setEmail(email.getText().toString());
                    //singleuser.utilizador.get(0).setPassword(password.getText().toString());
                    //singleuser.utilizador.get(0).setBio(bio.getText().toString());

                    //singleuser.utilizador.get(0).setImage();




                }


            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(Settings.this, HomeActivity.class);
                startActivity(in);
            }
        });


    }

}