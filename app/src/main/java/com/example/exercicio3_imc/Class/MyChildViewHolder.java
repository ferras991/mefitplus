package com.example.exercicio3_imc.Class;

import android.text.Html;
import android.view.View;
import android.widget.TextView;
import com.example.exercicio3_imc.R;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

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

        for (String str : Sousdoc.split(", ")) {
            String[] str2 = str.split("=");

            if (str2[0].equals("time")) time = str2[1].replace("-", ":");
            if (str2[0].equals("imc")) imc = str2[1];
        }
        
        String timeTxt = itemView.getContext().getResources().getString(R.string.timeTxt);
        StringBuilder builder = new StringBuilder();
        builder.append("<b>" + timeTxt + " </b>" + time + "<br><b>Imc: </b>" + imc);
        listChild.setText(Html.fromHtml(builder.toString()));
    }
}
