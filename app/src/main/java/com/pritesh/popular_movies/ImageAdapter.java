package com.pritesh.popular_movies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by prittesh on 23/11/16.
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    ArrayList<String> arrayList;
   // int a[]={R.drawable.cc,R.drawable.cc};

    public ImageAdapter(ArrayList<String> arrayList,Context c) {
        mContext=c;
        this.arrayList=arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ImageView imageView;
        if(view==null)
        {
            imageView=new ImageView(mContext);
            if(MainActivity.mTwoPane)
            imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,400));
            else
                imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,800));
            ViewGroup.MarginLayoutParams marginLayoutParams=new ViewGroup.MarginLayoutParams(imageView.getLayoutParams());
         //   marginLayoutParams.setMargins(10,10,10,10);
            imageView.setLayoutParams(marginLayoutParams);
            if(i==0&&MainActivity.mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putString("POSITION", String.valueOf(i));
                arguments.putBoolean("FLAG", false);
                try {
                    arguments.putString("id", menu.results.getJSONObject(i).getString("id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Main fragment = new Main();
                fragment.setArguments(arguments);

                ((MainActivity) mContext).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();
            }
        }
        else {
            imageView = (ImageView) view;
        }
       // ArrayList<Uri> uri=arrayList.;
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                if(MainActivity.mTwoPane)
                {
                    Bundle arguments = new Bundle();
                    arguments.putString("POSITION",String.valueOf(i));
                    arguments.putBoolean("FLAG",false);
                    arguments.putString("id",menu.results.getJSONObject(i).getString("id"));
                    Main fragment = new Main();
                    fragment.setArguments(arguments);

                    ((MainActivity)mContext).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, fragment)
                            .commit();
                }
                else {
                    Intent detaill_intent = new Intent(mContext, Details_Activity.class);
                    detaill_intent.putExtra("POSITION", i);
                    detaill_intent.putExtra("FLAG", false);

                        detaill_intent.putExtra("id", menu.results.getJSONObject(i).getString("id"));

                    mContext.startActivity(detaill_intent);
                }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Picasso.with(mContext).load(arrayList.get(i)).fit().into(imageView);
   //     imageView.setImageResource(ar);
        return imageView;
    }

}
