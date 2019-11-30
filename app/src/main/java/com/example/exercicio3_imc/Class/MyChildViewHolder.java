package com.example.exercicio3_imc.Class;

import android.view.View;
import android.widget.TextView;
import com.example.exercicio3_imc.R;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

import java.util.ArrayList;

public class MyChildViewHolder extends ChildViewHolder {

    public TextView listChild;

    public MyChildViewHolder(View itemView) {
        super(itemView);
        listChild = itemView.findViewById(R.id.listChild);
    }

    public void onBind(String Sousdoc) {
        String time = "";
        String imc = "";

        Sousdoc = Sousdoc.replace("{", "");
        Sousdoc = Sousdoc.replace("}", "");

        ArrayList<String> cenas = new ArrayList<>();
        String[] cenas2 = Sousdoc.split(", ");

        for (String cenas3 : cenas2) {
            String[] cenas1 = cenas3.split("=");

            if (cenas1[0].equals("time")) {
                time = cenas1[1];
            } else if (cenas1[0].equals("imc")) {
                imc = cenas1[1];
            }
        }

        listChild.setText(time + " - " + imc);
    }


}
