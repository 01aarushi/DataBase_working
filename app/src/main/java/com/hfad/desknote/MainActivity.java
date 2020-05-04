package com.hfad.desknote;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.hfad.desknote.model.Adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    NavigationView nav_view;

    FirebaseFirestore firebaseFirestore;
    FirestoreRecyclerAdapter<Note,NoteViewHolder> noteadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        RecyclerView notelist;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseFirestore= FirebaseFirestore.getInstance();
        Query query = firebaseFirestore.collection("NOTES").orderBy("title", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Note> allnotes= new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query,Note.class)
                .build();

        noteadapter = new FirestoreRecyclerAdapter< Note, NoteViewHolder>(allnotes) {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i, @NonNull final Note note) {
                noteViewHolder.Notetitle.setText(note.getTitle());
                noteViewHolder.Notecontent.setText(note.getContent());
                final int code=getBackgroudColor();
                noteViewHolder.mCardview.setCardBackgroundColor(noteViewHolder.view.getResources().getColor(code,null));

                noteViewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(v.getContext(), NoteDetails.class);
                        intent.putExtra("content",note.getContent());
                        intent.putExtra("title",note.getTitle());
                        intent.putExtra("code",code);
                        v.getContext().startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.note_view_layout,parent,false);
                return new NoteViewHolder(view);
            }
        };




        notelist=findViewById(R.id.notelist);
        drawer=findViewById(R.id.drawer);
        nav_view=findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(this);


        toggle=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.open,R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();


        notelist.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        notelist.setAdapter(noteadapter);


        FloatingActionButton fab = findViewById(R.id.addNotefloat);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(),AddNote.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.add :
                Intent intent=new Intent(this,AddNote.class);
                startActivity(intent);
                break;
            default:
                Toast.makeText(this,"Coming Soon",Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.option_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.settings){
            Toast.makeText(this,"Settings Option Selected",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder{
        TextView Notecontent,Notetitle;
        View view;
        CardView mCardview;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            Notetitle=itemView.findViewById(R.id.titles);
            Notecontent=itemView.findViewById(R.id.content);
            mCardview=itemView.findViewById(R.id.noteCard);
            view=itemView;
        }
    }

    private int getBackgroudColor() {
        List<Integer> colorcode=new ArrayList<>();
        colorcode.add(R.color.blue);
        colorcode.add(R.color.darkblue);
        colorcode.add(R.color.dark);
        colorcode.add(R.color.orange);
        colorcode.add(R.color.purple);
        colorcode.add(R.color.red);
        colorcode.add(R.color.yellow);
        colorcode.add(R.color.green);
        colorcode.add(R.color.grey);
        colorcode.add(R.color.pink);

        Random random=new Random();
        int number=random.nextInt(colorcode.size());
        return colorcode.get(number);
    }


    @Override
    protected void onStart() {
        super.onStart();
        noteadapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(noteadapter!=null){
            noteadapter.stopListening();
        }
    }
}
