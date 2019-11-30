package com.example.exercicio3_imc;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exercicio3_imc.Class.ChildList;
import com.example.exercicio3_imc.Class.Imc;
import com.example.exercicio3_imc.Class.MyChildViewHolder;
import com.example.exercicio3_imc.Class.MyParentViewHolder;
import com.example.exercicio3_imc.Class.ParentList;
import com.example.exercicio3_imc.Globals.Globals;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
//    private DatabaseReference ref;
//
//    ArrayList<Imc> list;
//    ArrayList<String> dates;
//
//    HashMap<String, List<Imc>> itemsMap;
//
//    RecyclerView recyclerView;







    private RecyclerView recycler_view;





    private FirebaseAuth mAuth;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.cenas:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                break;
            case R.id.logout:
                try {
                    mAuth.signOut();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }


                break;
            default:
                return false;
        }

        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recycler_view = (RecyclerView) findViewById(R.id.recycler_Expand);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));

        //Initialize your Firebase app
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // Reference to your entire Firebase database
        DatabaseReference parentReference = database.getReference().child("imcs").child(Globals.id);

        //reading data from firebase
        parentReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<ParentList> Parent = new ArrayList<>();
                for (final DataSnapshot snapshot : dataSnapshot.getChildren()){


                    final String ParentKey = snapshot.getKey();

                    snapshot.child("titre").getValue();

                    DatabaseReference childReference =
                            FirebaseDatabase.getInstance().getReference().child("imcs").child(Globals.id).child(ParentKey);
                    childReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final List<ChildList> Child = new ArrayList<>();

                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                final String ChildValue =  ds.getValue().toString();

                                ds.child("titre").getValue();

                                Child.add(new ChildList(ChildValue));
                            }


                            Parent.add(new ParentList(ParentKey, Child));

                            DocExpandableRecyclerAdapter adapter = new DocExpandableRecyclerAdapter(Parent);

                            recycler_view.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            System.out.println("Failed to read value." + error.toException());
                        }

                    });}}

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });













//        ref = FirebaseDatabase.getInstance().getReference().child("imcs").child(Globals.id).child("27-11-2019");
//        ref = FirebaseDatabase.getInstance().getReference().child("imcs").child(Globals.id);

//        recyclerView = findViewById(R.id.rv);



        mAuth = FirebaseAuth.getInstance();

    }





    public class DocExpandableRecyclerAdapter extends ExpandableRecyclerViewAdapter<MyParentViewHolder,MyChildViewHolder> {


        public DocExpandableRecyclerAdapter(List<ParentList> groups) {
            super(groups);
        }

        @Override
        public MyParentViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_parent, parent, false);
            return new MyParentViewHolder(view);
        }

        @Override
        public MyChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_child, parent, false);
            return new MyChildViewHolder(view);
        }

        @Override
        public void onBindChildViewHolder(MyChildViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
            final ChildList childItem = ((ParentList) group).getItems().get(childIndex);
            holder.onBind(childItem.getTitle());
            final String TitleChild=group.getTitle();

            holder.listChild.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent intent = new Intent(MainActivity.this, ShowUserImcActivity.class);
//                    intent.putExtra("time+imc", childItem.getTitle());
//                    startActivity(intent);
                    Toast toast = Toast.makeText(getApplicationContext(), TitleChild, Toast.LENGTH_SHORT);
                    toast.show();
                }

            });

        }

        @Override
        public void onBindGroupViewHolder(MyParentViewHolder holder, int flatPosition, final ExpandableGroup group) {
            holder.setParentTitle(group);

            if(group.getItems()==null)
            {
                holder.listGroup.setOnClickListener(  new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Toast toast = Toast.makeText(getApplicationContext(), group.toString(), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });

            }
        }


    }










    @Override
    protected void onStart() {
        super.onStart();

//        if (ref != null) {
//            ref.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if (list != null) {
//                        list.clear();
//                    }
//
//                    if (dataSnapshot.exists()) {
//                        list = new ArrayList<>();
//                        dates = new ArrayList<>();
//
//                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                            dates.add(ds.getKey());
//                            list.add(ds.getValue(Imc.class));
//                        }
//
//                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                        builder.setMessage(dates.toString());
//                        builder.show();
//
//                        ImcAdapter imcAdapter = new ImcAdapter(MainActivity.this, list);
//                        recyclerView.setAdapter(imcAdapter);
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//        }
    }

}