package com.example.blackcomedyfinal.ui.dashboard;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;



import com.example.blackcomedyfinal.databinding.FragmentDashboardBinding;
import com.example.blackcomedyfinal.models.Utilizadores;
import com.example.blackcomedyfinal.singleton.singleuser;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private static int MICROPHONE_PERMISSION_CODE = 200;

    TextView playPosition;
    Utilizadores utilizador = singleuser.utilizador.get(0);

    MediaRecorder mRecorder;
    MediaPlayer mediaPlayer;
    String AudioSavePath = null;

    private boolean ClickButton = false;

    File file;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Button btnSave = binding.btnSaveAudio;
        Toast.makeText(getActivity(), getRecordingFile(), Toast.LENGTH_SHORT).show();

        //Gravar Audio
        Button btnRecord = binding.btnRecord;
        SeekBar seekBar = binding.seekbarMusic;
        //Ler Audio
        ImageView btnP = binding.btnP;
        ImageView btnS = binding.btnS;
        Runnable runnable;

        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView end = binding.end;

                if(checkPermissions() == true){

                    if(ClickButton == false){
                        RecordAudio();
                        ClickButton = true;
                        btnRecord.setText("Stop Recording");

                        btnP.setVisibility(View.GONE);
                        btnS.setVisibility(View.GONE);

                    }else{
                        mRecorder.stop();
                        mRecorder.release();

                        ClickButton = false;
                        end.setText(getDuration());
                        btnP.setVisibility(View.VISIBLE);
                        btnS.setVisibility(View.GONE);
                        btnSave.setEnabled(true);


                        btnRecord.setText("Record Here");
                        Toast.makeText(getActivity(), "Stop Recording", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    ActivityCompat.requestPermissions(getActivity(), new String[]{
                            Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },1);
                }
            }
        });


        Handler handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this, 500);
            }
        };

        btnS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
            }
        });

        btnS.setVisibility(View.GONE);
        btnP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer = new MediaPlayer();

                try {

                    btnS.setVisibility(View.VISIBLE);
                    btnP.setVisibility(View.GONE);

                    mediaPlayer.setDataSource(getRecordingFile());
                    mediaPlayer.prepare();
                    mediaPlayer.start();

                    seekBar.setMax(mediaPlayer.getDuration());
                    handler.postDelayed(runnable,0);


                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        btnS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnP.setVisibility(View.VISIBLE);
                btnS.setVisibility(View.GONE);

                mediaPlayer.pause();
                handler.removeCallbacks(runnable);


            }
        });

        ImageView initialVoice = binding.initialVoice;
        ImageView endVoice = binding.listemAgain;
        playPosition = binding.intial;

        endVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int currentPost = mediaPlayer.getCurrentPosition();
                int duration = mediaPlayer.getDuration();

                if(mediaPlayer.isPlaying() && duration != currentPost){

                    mediaPlayer.seekTo(0);
                    playPosition.setText(currentTime(currentPost));
                    mediaPlayer.seekTo(currentPost);

                }
            }
        });

        initialVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int currentPost = mediaPlayer.getCurrentPosition();
                int duration = mediaPlayer.getDuration();

                if(mediaPlayer.isPlaying() && duration != currentPost){

                    mediaPlayer.seekTo(0);
                    playPosition.setText(currentTime(currentPost));
                    mediaPlayer.seekTo(currentPost);

                }
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if(fromUser){
                    mediaPlayer.seekTo(progress);
                }
                playPosition.setText(currentTime(mediaPlayer.getCurrentPosition()));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        btnSave.setEnabled(false);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Date currentTime = Calendar.getInstance().getTime();

                int min = 2;
                int max = 800000000;

                int random = new Random().nextInt((max - min) + 1) + min;

                StorageReference storageReference = FirebaseStorage.getInstance().getReference("audios/" + "BlackComedy" + random + ".3gp" );
                Uri audioUri = Uri.fromFile(new File(getRecordingFile()));

                // Enviar o arquivo de áudio
                storageReference.putFile(audioUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Obter a URL do arquivo de áudio
                                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        String audioUrl = uri.toString();
                                        // Adicionar uma referência ao arquivo de áudio ao Cloud Firestore
                                        Map<String, Object> comedy = new HashMap<>();

                                        comedy.put("comedyText", null);
                                        comedy.put("comedyAudio", audioUrl);
                                        comedy.put("comedyDate",currentTime.toString());
                                        comedy.put("Likes", null);
                                        comedy.put("user", utilizador);

                                        FirebaseFirestore.getInstance().collection("comedy").add(comedy)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {

                                                        Toast.makeText(getActivity(), "Audio guardado com sucesso", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                });
                            }
                        });

            }
        });


        return root;
    }

    private boolean checkPermissions(){

        int first = ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.RECORD_AUDIO);
        int second = ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        return first == PackageManager.PERMISSION_GRANTED && second == PackageManager.PERMISSION_GRANTED;

    }

    public void RecordAudio(){

        try {
            Utilizadores utilizador = singleuser.utilizador.get(0);
            mRecorder = new MediaRecorder();

            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setOutputFile(getRecordingFile());
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.prepare();
            mRecorder.start();

            Toast.makeText(getActivity(), "Record is starting", Toast.LENGTH_SHORT).show();

        }catch (IOException e){

            e.printStackTrace();

        }

    }


    private String getRecordingFile(){


        ContextWrapper contextWrapper = new ContextWrapper(getActivity().getApplicationContext());
        File musicDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        file = new File(musicDirectory, "BlackComedy" + ".3gp");
        return file.getPath();

    }




    private String currentTime(int duration) {
        return String.format("%02d:%02d",TimeUnit.MILLISECONDS.toMinutes(duration), TimeUnit.MILLISECONDS.toSeconds(duration), TimeUnit.MILLISECONDS.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
    }


    private String getDuration(){


        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(getRecordingFile());
        Long duration = Long.parseLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
        //Float second = (float) duration / 1000  ;
        return String.format("%02d:%02d",TimeUnit.MILLISECONDS.toMinutes(duration), TimeUnit.MILLISECONDS.toSeconds(duration), TimeUnit.MILLISECONDS.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}