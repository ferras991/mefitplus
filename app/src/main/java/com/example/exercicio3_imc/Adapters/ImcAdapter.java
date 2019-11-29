package com.example.exercicio3_imc.Adapters;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exercicio3_imc.Class.Imc;
import com.example.exercicio3_imc.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ImcAdapter extends RecyclerView.Adapter<ImcAdapter.MyViewHolder> {

    private Context mContext;
    ArrayList<Imc> list;

    public ImcAdapter(Context context, ArrayList<Imc> list) {
        mContext = context;
        this.list = list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.show_imc, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
        Imc imc = list.get(i);
        myViewHolder.imcDate.setText(imc.getDate());
        myViewHolder.imcImc.setText(imc.getImc() + "");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView imcDate, imcImc;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imcDate = itemView.findViewById(R.id.imcDate);
            imcImc = itemView.findViewById(R.id.imcImc);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, "cenas", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
