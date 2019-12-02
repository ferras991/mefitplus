package com.example.exercicio3_imc.Class;

import android.view.View;
import android.widget.TextView;
import com.example.exercicio3_imc.R;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

public class MyParentViewHolder extends GroupViewHolder {

    public TextView listGroup;

    public MyParentViewHolder(View itemView) {
        super(itemView);
        listGroup = itemView.findViewById(R.id.listParent);
    }

    public void setParentTitle(ExpandableGroup group) {
        listGroup.setText(group.getTitle());
    }


}