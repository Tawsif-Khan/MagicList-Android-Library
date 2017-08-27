package com.idatlab.magiclist;

import android.app.job.JobInfo;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by tawsifkhan on 8/2/17.
 */

public class MagicListAdapter<T> extends BaseAdapter implements View.OnClickListener{

    public int btnId;

    Context context;
    ArrayList<T> objects;
    LayoutInflater inflater;
    View view;
    ViewHolder holder;
    HashMap<Integer,String> valueField = new HashMap<>();
    HashMap<Integer,String> valueImageField = new HashMap<>();
    HashMap<Integer,Integer> valueImageFieldResId = new HashMap<>();
    int layoutId;
    ArrayList<TextView> textViews = new ArrayList<>();
    ArrayList<ImageView> imageViews = new ArrayList<>();
    ArrayList<View> buttons = new ArrayList<>();
    private MagicListener magicListener;
    private int position;

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

    private View getLayout(){
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
        this.position = pos;
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = getLayout();

            holder = setHolderTextViews(holder,convertView);
            holder = setHolderImageViews(holder,convertView);
            holder = setClickListeneres(holder,convertView);

            convertView.setTag(holder);
            Log.d("msg","if");
        }else{
            holder = (ViewHolder) convertView.getTag();
            Log.d("msg","else");
        }

        for(int i=0;i<holder.buttons.size();i++){
            holder.buttons.get(i).setTag(pos);
        }

        T cls =  objects.get(pos);
        setText(holder,cls);
        setImageUrl(holder,cls);

        return convertView;
    }

    private void setText(ViewHolder holder, T cls){
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

    private void setImageUrl(ViewHolder holder, T cls){

        for(int i=0;i<holder.imageViews.size();i++){
            ImageView iv = holder.imageViews.get(i);
            try {
                Field field = cls.getClass().getField(valueImageField.get(iv.getId()));
                field.setAccessible(true);
                try {
                    Picasso.with(context).load((String) field.get(cls)).into(iv);
                } catch (Exception e) {
                    try {
                        Picasso.with(context).load((Integer) field.get(cls)).into(iv);
                    }catch (Exception e1){
                        try {
                            Picasso.with(context).load((File) field.get(cls)).into(iv);
                        }catch (Exception e2){
                            try {
                                Picasso.with(context).load((Uri) field.get(cls)).into(iv);
                            }catch (Exception e3){

                            }
                        }
                    }
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

    public void setImage(int id,String imageField){

        this.imageViews.add((ImageView) view.findViewById(id));
        valueImageField.put(id,imageField);
    }



    private ViewHolder setHolderTextViews(ViewHolder holder, View convertView){
        for(int i=0;i<textViews.size();i++) {
            holder.textViews.add((TextView) convertView.findViewById(textViews.get(i).getId()));
        }
        return holder;
    }

    private ViewHolder setHolderImageViews(ViewHolder holder, View convertView){
        for(int i=0;i<imageViews.size();i++) {
            holder.imageViews.add((ImageView) convertView.findViewById(imageViews.get(i).getId()));
        }
        return holder;
    }

    private ViewHolder setClickListeneres(ViewHolder holder, View convertView){
        for(int i=0;i<buttons.size();i++){
            holder.buttons.add(convertView.findViewById(buttons.get(i).getId()));
            holder.buttons.get(i).setOnClickListener(this);
        }
        return holder;
    }

    public void setOnMagicClickListener(int id,MagicListener magicListener){
        this.magicListener = magicListener;
        this.buttons.add(view.findViewById(id));
    }

    @Override
    public void onClick(View view) {

        magicListener.onMagicClick(view, (Integer) view.getTag());
    }


    private class ViewHolder{
        ArrayList<TextView> textViews = new ArrayList<>();
        ArrayList<ImageView> imageViews = new ArrayList<>();
        ArrayList<View> buttons = new ArrayList<>();

    }
}
