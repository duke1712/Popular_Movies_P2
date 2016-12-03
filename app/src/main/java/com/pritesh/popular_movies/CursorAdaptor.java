package com.pritesh.popular_movies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.pritesh.popular_movies.data.FavContracts;
import com.squareup.picasso.Picasso;

import static java.lang.System.load;

/**
 * Created by prittesh on 26/11/16.
 */

public class CursorAdaptor extends CursorAdapter{
    ImageView imageView;
Context context;
    public CursorAdaptor(Context context, Cursor c) {
        super(context, c);
        context=context;

    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {


            imageView=new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,600));
            ViewGroup.MarginLayoutParams marginLayoutParams=new ViewGroup.MarginLayoutParams(imageView.getLayoutParams());
            //   marginLayoutParams.setMargins(10,10,10,10);
            imageView.setLayoutParams(marginLayoutParams);


        return imageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        if(convertView==null)
//        {
//            convertView=new ImageView(context);
//        }
//        convertView.setTag(position);
        return super.getView(position, convertView, parent);
    }

    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public void bindView(View view, final Context mContext, final Cursor cursor) {
        Log.d("PRIIIIIIIIIII", String.valueOf(cursor.getPosition()));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


               if(MainActivity.mTwoPane)
               {
                   Bundle arguments = new Bundle();
                   //   arguments.putString("POSITION",String.valueOf(i));
                   arguments.putBoolean("FLAG",true);
                   arguments.putString("id",String.valueOf(view.getTag()));
                   main fragment = new main();
                   fragment.setArguments(arguments);

                   ((MainActivity)mContext).getSupportFragmentManager().beginTransaction()
                           .replace(R.id.container, fragment)
                           .commit();
               }
                else {
                   Intent detaill_intent = new Intent(mContext, Details_Activity.class);

                   //  detaill_intent.putExtra("POSITION",i);
                   detaill_intent.putExtra("id", String.valueOf(view.getTag()));
                   detaill_intent.setFlags(1);
                   mContext.startActivity(detaill_intent);

               }

            }
        });
        view.setTag(cursor.getString(cursor.getColumnIndex(FavContracts.FavEntry.COLUMN_ID)));

        Picasso.with(mContext).setIndicatorsEnabled(true);
        Picasso.with(mContext).load(Constants.IMAGE_BASE_URL+cursor.getString(cursor.getColumnIndex(FavContracts.FavEntry.COLUMN_IMAGE))+"?"+Constants.API_KEY).fit().into((ImageView)view);
    }
}
