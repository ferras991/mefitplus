package com.example.exercicio3_imc.Class;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exercicio3_imc.R;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.util.ArrayList;

public class MyParentViewHolder extends GroupViewHolder {

    Context context;

    public TextView listGroup;

    public MyParentViewHolder(View itemView, Context context) {
        super(itemView);

        this.context = context;
        listGroup = itemView.findViewById(R.id.listParent);
    }

    public void setParentTitle(ExpandableGroup group) {
        listGroup.setText(group.getTitle());
    }


}