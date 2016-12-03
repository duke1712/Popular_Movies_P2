package com.pritesh.popular_movies;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.pritesh.popular_movies.data.FavContracts;
import com.pritesh.popular_movies.data.FavProvider;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class main extends Fragment {
    String img_url;
    Context context;
    FavProvider mprovider=new FavProvider();
    Intent intent;
    int position;
    TextView movieName,date,synopsis;
    TextView ratingBar;
    ImageView imageView;
    ProgressDialog mProg;
    OkHttpClient client = new OkHttpClient();
    String id,rDate,title,rating,syn;
    ArrayList<String> videoId=new ArrayList<>();
    ListView trailer,review;
    ArrayAdapter<String> listAdapter,reviewAdapter;
    Button fav1,fav2;
    update call;
    public main() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
        call=(update)(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        Bundle bundle = getArguments();
        // intent=getIntent();
        mProg = new ProgressDialog(context, ProgressDialog.STYLE_SPINNER);
        trailer = (ListView) view.findViewById(R.id.trailer);
        review = (ListView) view.findViewById(R.id.review);
        movieName = (TextView) view.findViewById(R.id.movieName);
        date = (TextView) view.findViewById(R.id.releaseDate);
        synopsis = (TextView) view.findViewById(R.id.synopsis);
        ratingBar = (TextView) view.findViewById(R.id.textView3);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        fav1 = (Button) view.findViewById(R.id.fav1);
        fav2 = (Button) view.findViewById(R.id.fav2);
        final Uri uri = Uri.parse(FavContracts.BASE_CONTENT_URI + FavContracts.CONTENT_AUTHORITY + FavContracts.FavEntry.TABLE_FAV);

        fav1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fav2.setVisibility(View.VISIBLE);
                fav1.setVisibility(View.GONE);

                ContentValues values = new ContentValues();
                values.put(FavContracts.FavEntry.COLUMN_ID, id);
                values.put(FavContracts.FavEntry.COLUMN_DATE, rDate);
                values.put(FavContracts.FavEntry.COLUMN_TITLE, title);
                values.put(FavContracts.FavEntry.COLUMN_RATING, rating);
                values.put(FavContracts.FavEntry.COLUMN_SUMMARY, syn);
                values.put(FavContracts.FavEntry.COLUMN_IMAGE, "/"+img_url);

                Uri new_uri = context.getContentResolver().insert(FavContracts.FavEntry.CONTENT_URI, values);

            }
        });
        fav2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fav1.setVisibility(View.VISIBLE);
                fav2.setVisibility(View.GONE);
                Uri uri = Uri.parse(FavContracts.BASE_CONTENT_URI + FavContracts.CONTENT_AUTHORITY + FavContracts.FavEntry.TABLE_FAV);
                String selection = FavContracts.FavEntry.COLUMN_ID + "=?";
                String[] selection_args = {id};
                context.getContentResolver().delete(FavContracts.FavEntry.CONTENT_URI, selection, selection_args);
//                ((MainActivity)context).getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.fragment, new menu())
//                        .commit();
                call.up();
                menu.cursorAdaptor.notifyDataSetChanged();

            }
        });
        try {
            if (bundle != null) {
                Boolean i = bundle.getBoolean("FLAG");
                id = bundle.getString("id");

                Uri u = ContentUris.withAppendedId(FavContracts.FavEntry.CONTENT_URI, Long.parseLong(id));
                // String proj[] = {FavContracts.FavEntry.COLUMN_ID};
                Cursor cursor = context.getContentResolver().query(u, null, null, new String[10], null);
                if (cursor.getCount() == 0) {
                    fav1.setVisibility(View.VISIBLE);
                    fav2.setVisibility(View.GONE);
                } else {
                    fav2.setVisibility(View.VISIBLE);
                    fav1.setVisibility(View.GONE);
                }

                if (!i) {
                    position = Integer.parseInt(bundle.getString("POSITION", ""));
                    JSONObject movie = menu.results.getJSONObject(position);
                    id = movie.getString("id");
                    rDate = menu.results.getJSONObject(position).getString("release_date");
                    title = menu.results.getJSONObject(position).getString("original_title");
                    syn = menu.results.getJSONObject(position).getString("overview");
                    rating = menu.results.getJSONObject(position).getDouble("vote_average") + "/10";
                    img_url = menu.results.getJSONObject(position).getString("poster_path");

                } else {
                    cursor.moveToPosition(0);
                    id = cursor.getString(0);
                    rDate = cursor.getString(1);
                    title = cursor.getString(2);
                    rating = cursor.getString(3);
                    img_url = cursor.getString(4);
                    syn = cursor.getString(5);


                }


                movieName.setText(title);
                date.setText(rDate);
                synopsis.setText(syn);
                ratingBar.setText(rating);
                img_url = img_url.substring(1);
                Picasso.with(context.getApplicationContext()).load(Constants.IMAGE_BASE_URL + img_url + "?" + Constants.API_KEY).fit().into(imageView);

                String url[] = {Constants.BASE_URL + "/movie/" + id + "/videos?" + Constants.API_KEY, Constants.BASE_URL + "/movie/" + id + "/reviews?" + Constants.API_KEY};

                new getVideo().execute(url);
            }
        }catch(JSONException e){
                e.printStackTrace();
            }


        return  view;
    }
    public interface update
    {
        void up();
    }
    void update_video(JSONArray results, final ArrayList<String> videoId)
    {
        String a[]=new String[videoId.size()];
//        if(a.length)
        for(int i=0;i<a.length;i++)
        {
            a[i]="Trailer "+(i+1) ;
        }
        listAdapter=new ArrayAdapter<>(context.getApplicationContext(),android.R.layout.simple_list_item_1,a);
        trailer.setAdapter(listAdapter);

        trailer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.YOUTUBEAPP_BASE_LINK + videoId.get(i)));
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + videoId.get(i)));
                try {
                    startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    startActivity(webIntent);
                }
            }
        });
    }
    String doGetRequest(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
    public class getVideo extends AsyncTask<String,Integer,Long> {
        JSONArray results;
        ArrayList<JSONObject> reviewobj=new ArrayList<>();

        @Override
        public Long doInBackground(String... strings) {
            try {
                String Json_video=doGetRequest(strings[0]);
                JSONObject obj=new JSONObject(Json_video);
                results =obj.getJSONArray("results");
                int count=results.length();
                for(int i=0;i<count;i++)
                {
                    videoId.add(results.getJSONObject(i).getString("key"));
                }
                String Json_review=doGetRequest(strings[1]);
                JSONObject obj1=new JSONObject(Json_review);
                results=obj1.getJSONArray("results");
                count=results.length();
                for(int i=0;i<count;i++)
                {
                    reviewobj.add(results.getJSONObject(i));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPreExecute() {
            mProg.show();
        }
        @Override
        protected void onPostExecute(Long aLong) {

            try {
                update_video(results,videoId);
                update_review(reviewobj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mProg.cancel();
        }
    }

    private void update_review(final ArrayList<JSONObject> reviewobj) throws JSONException {
        String a[]=new String[reviewobj.size()];
        for(int i=0;i<a.length;i++)
        {
            a[i]=reviewobj.get(i).getString("author");
        }
        reviewAdapter=new ArrayAdapter<>(context.getApplicationContext(),android.R.layout.simple_list_item_1,a);
        review.setAdapter(reviewAdapter);
        review.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    Intent reviewIntent=new Intent(context.getApplicationContext(),Review.class);
                    reviewIntent.putExtra("AUTHOR",reviewobj.get(i).getString("author"));
                    reviewIntent.putExtra("CONTENT",reviewobj.get(i).getString("content"));
                    startActivity(reviewIntent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }
}
