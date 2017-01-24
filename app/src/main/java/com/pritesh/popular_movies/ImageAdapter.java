package com.pritesh.popular_movies;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
    ArrayList<Integer> movieNo;
   // int a[]={R.drawable.cc,R.drawable.cc};

    public ImageAdapter(ArrayList<String> arrayList,ArrayList<Integer> movieNo,Context c) {
        mContext=c;
        this.arrayList=arrayList;
        this.movieNo=movieNo;
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
                    detaill_intent.putExtra("POSITION", movieNo.get(i));
                    detaill_intent.putExtra("FLAG", false);

                        detaill_intent.putExtra("id", menu.results.getJSONObject(movieNo.get(i)).getString("id"));

                    mContext.startActivity(detaill_intent);
                }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
//        Drawable dr = mContext.getResources().getDrawable(R.drawable.loading_animation);
//        Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
//// Scale it to 50 x 50
//        Drawable d = new BitmapDrawable(mContext.getResources(), Bitmap.createScaledBitmap(bitmap, 50, 50, true));
        Picasso.with(mContext).load(arrayList.get(i)).placeholder(R.drawable.loading_animation).fit().into(imageView);
   //     imageView.setImageResource(ar);
        return imageView;
    }

}
