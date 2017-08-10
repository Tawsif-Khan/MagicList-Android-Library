package com.idatlab.magiclist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by tawsifkhan on 8/2/17.
 */

public class MagicListAdapter<T> extends BaseAdapter {

    Context context;
    ArrayList<T> objects;
    LayoutInflater inflater;
    View view;
    ViewHolder holder;
    HashMap<Integer,String> valueField = new HashMap<>();
    int layoutId;
    ArrayList<TextView> textViews = new ArrayList<>();

    public MagicListAdapter(Context context, ArrayList<T> objects) {
        this.context = context;
        this.objects = objects;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        holder = new ViewHolder();

    }

    public void setLayout(int id){
        layoutId = id;
        view = inflater.inflate(layoutId,null);
    }

    public View getLayout(){
        return view = inflater.inflate(layoutId,null);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup viewGroup) {

        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = getLayout();
            holder = setHolderTextViews(holder,convertView);
            convertView.setTag(holder);
            Log.d("msg","if");
        }else{
            holder = (ViewHolder) convertView.getTag();
            Log.d("msg","else");
        }

        T cls =  objects.get(pos);
        setText(holder,cls);



        return convertView;
    }

    public void setText(ViewHolder holder, T cls){
        for(int i=0;i<holder.textViews.size();i++){
            TextView tv = holder.textViews.get(i);
            try {
                Field field = cls.getClass().getField(valueField.get(tv.getId()));
                field.setAccessible(true);
                try {
                    tv.setText((CharSequence) field.get(cls));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }

    public void setText(int id,String value){

        this.textViews.add((TextView) view.findViewById(id));
        valueField.put(id,value);
    }

    public ViewHolder setHolderTextViews(ViewHolder holder, View convertView){
        for(int i=0;i<textViews.size();i++) {
            holder.textViews.add((TextView) convertView.findViewById(textViews.get(i).getId()));
        }
        return holder;
    }

    class ViewHolder{
        ArrayList<TextView> textViews = new ArrayList<>();
        ArrayList<ImageView> imageViews = new ArrayList<>();
        ArrayList<Button> buttons = new ArrayList<>();

    }
}
