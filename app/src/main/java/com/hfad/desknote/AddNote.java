package com.hfad.desknote;

import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class AddNote extends AppCompatActivity {
    FirebaseFirestore firestore;
    EditText addtitle,addcontent;
    ProgressBar pB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        addtitle=findViewById(R.id.addNoteTitle);
        addcontent=findViewById(R.id.addNoteContent);

        firestore=FirebaseFirestore.getInstance();
        pB=findViewById(R.id.progressBar);
        pB.setVisibility(View.INVISIBLE);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content=addcontent.getText().toString();
                String title=addtitle.getText().toString();
                if(content.isEmpty() || title.isEmpty()){
                    Toast.makeText(AddNote.this,"Enter both title and content",Toast.LENGTH_SHORT).show();
                    return;
                }

                pB.setVisibility(View.VISIBLE);


                DocumentReference docref=firestore.collection("NOTES").document();
                Map<String,Object> adding = new HashMap<>();
                adding.put("title",title);
                adding.put("content",content);

                docref.set(adding).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AddNote.this,"Note Added Successfully",Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddNote.this,"Error! Try again later..",Toast.LENGTH_SHORT).show();
                        pB.setVisibility(View.VISIBLE);
                    }
                });

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.close_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.close){
            Toast.makeText(AddNote.this,"Note Cancelled",Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
