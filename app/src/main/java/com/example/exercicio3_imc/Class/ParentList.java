package com.example.exercicio3_imc.Class;


import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.ArrayList;
import java.util.List;

public class ParentList extends ExpandableGroup<ChildList> {


    public ParentList(String title, List<ChildList> items) {
        super(title, items);
    }



//    public ParentList(ArrayList<String> title, List<ChildList> items) {
//        super(title.get(0), items);
//    }

}
