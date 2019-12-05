package com.example.exercicio3_imc;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.exercicio3_imc.Class.ChildList;
import com.example.exercicio3_imc.Class.MyChildViewHolder;
import com.example.exercicio3_imc.Class.MyParentViewHolder;
import com.example.exercicio3_imc.Class.ParentList;
import com.example.exercicio3_imc.Globals.Globals;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.core.OrderBy;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
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

        recycler_view = findViewById(R.id.recycler_Expand);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));

        final FirebaseDatabase database = FirebaseDatabase.getInstance();


        DatabaseReference parentReference = database.getReference().child("imcs").child(Globals.id);

        parentReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<ParentList> Parent = new ArrayList<>();

                for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String parentKey = snapshot.getKey();

                    long cenas = 9999999999999L - Long.parseLong(parentKey);

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(Long.parseLong(cenas + ""));

                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    int month = calendar.get(Calendar.MONTH) + 1;
                    int year = calendar.get(Calendar.YEAR);

                    String day2 = day + "";
                    String month2 = month + "";
                    day2 = (day2.length() == 1) ? "0" + day+"" : day2+"";
                    month2 = (month2.length() == 1) ? "0" + month : month2+"";

                    final String ParentKey = day2 + "-" + month2+ "-" + year;

                    snapshot.child("titre").getValue();

                    DatabaseReference childReference =
                            FirebaseDatabase.getInstance().getReference().child("imcs").child(Globals.id).child(parentKey);

                    childReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final ArrayList<ChildList> Child = new ArrayList<>();

                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                final String ChildValue =  ds.getValue().toString();
                                ds.child("titre").getValue();
                                Child.add(new ChildList(ChildValue));
                            }

                            Parent.add(new ParentList(ParentKey, Child));

//                            Collections.reverse(Parent);
//                            Collections.sort(Parent, Collections.reverseOrder());


                            DocExpandableRecyclerAdapter adapter = new DocExpandableRecyclerAdapter(Parent);
                            recycler_view.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            System.out.println("Failed to read value." + error.toException());
                        }

                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mAuth = FirebaseAuth.getInstance();
    }





    public class DocExpandableRecyclerAdapter extends ExpandableRecyclerViewAdapter<MyParentViewHolder,MyChildViewHolder> {

        public DocExpandableRecyclerAdapter(List<ParentList> groups) {
            super(groups);
        }

        @Override
        public MyParentViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_parent, parent, false);
            return new MyParentViewHolder(view, MainActivity.this);
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

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, ShowUserImcActivity.class);
                    intent.putExtra("time+imc", childItem.getTitle());
                    startActivity(intent);
                }
            });
        }

        @Override
        public void onBindGroupViewHolder(MyParentViewHolder holder, int flatPosition, final ExpandableGroup group) {
            holder.setParentTitle(group);

            if(group.getItems() == null) {
                holder.listGroup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast toast = Toast.makeText(getApplicationContext(), group.toString(), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });

            }
        }
    }
}