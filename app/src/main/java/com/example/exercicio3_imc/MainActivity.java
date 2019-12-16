package com.example.exercicio3_imc;

import android.content.DialogInterface;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recycler_view;

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
            case R.id.settings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                break;
            case R.id.logout:
                try {
                    FirebaseAuth.getInstance().signOut();
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

        getData(parentReference);
    }


    private void getData(DatabaseReference parentReference) {
        try {
            parentReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final ArrayList<ParentList> Parent = new ArrayList<>();

                    if (dataSnapshot.exists()) {

                        for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String parentKey = snapshot.getKey();

                            long time = 9999999999999L - Long.parseLong(parentKey);

                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(Long.parseLong(time + ""));

                            int day = calendar.get(Calendar.DAY_OF_MONTH);
                            int month = calendar.get(Calendar.MONTH) + 1;
                            int year = calendar.get(Calendar.YEAR);

                            String day2 = day + "";
                            String month2 = month + "";
                            day2 = (day2.length() == 1) ? "0" + day + "" : day2 + "";
                            month2 = (month2.length() == 1) ? "0" + month : month2 + "";

                            final String ParentKey = day2 + "-" + month2 + "-" + year;

                            snapshot.child("titre").getValue();

                            DatabaseReference childReference =
                                    FirebaseDatabase.getInstance().getReference().child("imcs").child(Globals.id).child(parentKey);

                            childReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    ArrayList<ChildList> Child = new ArrayList<>();

                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        final String ChildValue = ds.getValue().toString();
                                        ds.child("titre").getValue();
                                        Child.add(new ChildList(ChildValue));
                                    }

                                    Parent.add(new ParentList(ParentKey, Child));

                                    DocExpandableRecyclerAdapter adapter = new DocExpandableRecyclerAdapter(Parent);
                                    recycler_view.setAdapter(adapter);
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    System.out.println("Failed to read value." + error.toException());
                                }

                            });
                        }
                    } else {
                        Parent.clear();

                        DocExpandableRecyclerAdapter adapter = new DocExpandableRecyclerAdapter(Parent);
                        recycler_view.setAdapter(adapter);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
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
        public void onBindChildViewHolder(final MyChildViewHolder holder, int flatPosition, final ExpandableGroup group, int childIndex) {
            final ChildList childItem = ((ParentList) group).getItems().get(childIndex);
            holder.onBind(childItem.getTitle());

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle(getResources().getString(R.string.delEntryTitle))
                            .setMessage(getResources().getString(R.string.delEntryText))
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String time = holder.listChild.getText().toString();
                                    String dateParent = group.getTitle();
                                    delDBInfo(dateParent, time);

                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .show();

                    return false;
                }
            });

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

            if (group.getItems() == null) {
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


    private void delDBInfo(String date, String time) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date dt = simpleDateFormat.parse(date);
            long millis = dt.getTime();

            SimpleDateFormat timeFormat = new SimpleDateFormat("HH-mm-ss"); //set date format
            time = time.split("\\n")[0].split("Time:")[1].replace(":", "-").toString();

            Date time2 = timeFormat.parse(time);

            long cenas = 9999999999999L;

            DatabaseReference delImc = FirebaseDatabase.getInstance().getReference("imcs")
                    .child(Globals.id).child((cenas - millis) + "").child(timeFormat.format(time2));

            delImc.removeValue().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            getData(FirebaseDatabase.getInstance().getReference("imcs").child(Globals.id));

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }
}