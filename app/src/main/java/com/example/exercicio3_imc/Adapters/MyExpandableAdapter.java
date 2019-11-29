package com.example.exercicio3_imc.Adapters;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exercicio3_imc.Class.Imc;
import com.example.exercicio3_imc.Globals.Globals;
import com.example.exercicio3_imc.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MyExpandableAdapter extends BaseExpandableListAdapter{
    private DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("imcs").child(Globals.id);

    private Context mContext;
    ArrayList<String> header;
    HashMap<String, ArrayList> headeritem;

    public MyExpandableAdapter(Context context, ArrayList<String> header, HashMap<String, ArrayList> headeritem) {
        mContext = context;
        this.header = header;
        this.headeritem = headeritem;
    }




//    private Context con;
//    private List<String> headers;
//    private HashMap<String,List<Imc>> headeritems;
//    private HashMap<String,List<String>> headeritems;


//    public MyExpandableAdapter(Context context, List<String> listheaders, HashMap<String,List<String>> headerchilds)
//    {
//        this.con=context;
//        this.headers=listheaders;
//        this.headeritems=headerchilds;
//    }
    // get child of header
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.headeritem.get(this.header.get(groupPosition)).get(childPosition);
    }
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
    // return the view of child
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childname=(String)getChild(groupPosition,childPosition);
        if(convertView==null)
        {
            LayoutInflater inflater=(LayoutInflater)LayoutInflater.from(mContext);
            convertView=inflater.inflate(R.layout.headers_items,null);
        }
        TextView listchild=(TextView)convertView.findViewById(R.id.headeritem);
        listchild.setText(childname);
        return convertView;
    }
    // returns children count of header
    @Override
    public int getChildrenCount(int groupPosition) {
        return this.headeritem.get(header.get(groupPosition)).size();
    }
    @Override
    public Object getGroup(int groupPosition) {
        return this.header.get(groupPosition);
    }
    @Override
    public int getGroupCount() {
        return this.header.size();
    }
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    //returns the view of group
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String dates = header.get(groupPosition);

        String headername=(String)getGroup(groupPosition);
//        String headername=imc.getImc() + "";

        if(convertView==null) {
            LayoutInflater inflater=LayoutInflater.from(mContext);
            convertView=inflater.inflate(R.layout.listview_header,null);
        }

        TextView header=(TextView)convertView.findViewById(R.id.expandheader);
        header.setText(headername);
        return convertView;
    }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    @Override
    public boolean hasStableIds() {
        return false;
    }
}