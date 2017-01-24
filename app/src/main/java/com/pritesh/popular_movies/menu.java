package com.pritesh.popular_movies;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;

import com.pritesh.popular_movies.data.FavContracts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.Inflater;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class menu extends Fragment {

    static JSONArray results;
    static ImageAdapter image;
    private Spinner spinner;
    private GridView movies;
    private ArrayList<String> arrayList=new ArrayList<>();
    private ArrayList<String> moviewName=new ArrayList<>();
    private ArrayList<Integer> movieNo=new ArrayList<>();

    SearchView searchView;
    private String sort="Popular";
    ProgressDialog mProg;
    static CursorAdaptor cursorAdaptor;
    OkHttpClient client = new OkHttpClient();
    Context context;
    public menu() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.main_fragment, container, false);

        //context.setContentView(R.layout.activity_main);
        mProg=new ProgressDialog(context,ProgressDialog.STYLE_SPINNER);
        spinner=(Spinner)view.findViewById(R.id.spinner);
        movies=(GridView)view.findViewById(R.id.movies);
        searchView=(SearchView)view.findViewById(R.id.searchView);
        searchView.setQueryHint("Search Movie");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<String> newList=new ArrayList<String>();
                ArrayList<Integer> newNo=new ArrayList<>();
                for(int i=0;i<moviewName.size();i++)
                {
                    if(moviewName.get(i).toLowerCase().contains(newText.toLowerCase()))
                    {
                        newList.add(arrayList.get(i));
                        newNo.add(i);
                    }
                }
                image=new ImageAdapter(newList,newNo,context);
                movies.setAdapter(image);

                return false;
            }
        });
        //      mov.doInBackground(Constants.BASE_URL+Constants.POPULAR_URL+Constants.API_KEY);

        ArrayAdapter<CharSequence> arrayAdapter=ArrayAdapter.createFromResource(context,R.array.filter, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);





        image=new ImageAdapter(arrayList,movieNo,context);
        movies.setAdapter(image);


        return  view;
    }

    public void notif()
    {
        cursorAdaptor.notifyDataSetChanged();
    }
    public void update(String sort) throws IOException, JSONException {
        String url=Constants.BASE_URL;
        arrayList.clear();
        if(sort.equalsIgnoreCase("Popular")) {
            url += Constants.POPULAR_URL + Constants.API_KEY;
            new menu.getmovie().execute(url);
        }
        else if(sort.equalsIgnoreCase("Rated")){
            url += Constants.RATED_URL + Constants.API_KEY;
            new menu.getmovie().execute(url);
        }
        else
        {

            Cursor cursor=context.getContentResolver().query(FavContracts.FavEntry.CONTENT_URI,null,null,null,null);
            cursorAdaptor=new CursorAdaptor(context,cursor);
            movies.setAdapter(cursorAdaptor);

        }
    }


    @Override
    public void onResume() {
        super.onResume();
       // try {
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    sort=adapterView.getAdapter().getItem(i).toString();
                    try {
                        update(sort);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }



                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            //update(sort);
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//        catch (JSONException e) {
//            e.printStackTrace();
        //}
    }

    String doGetRequest(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }
    public class getmovie extends AsyncTask<String,Integer,Long> {
        @Override
        public Long doInBackground(String... strings) {
            try {
                String JSON_STRING = doGetRequest(strings[0]);
                results= new JSONObject(JSON_STRING).getJSONArray("results");
                int length = results.length();
                for (int i = 0; i < length; i++) {
                    JSONObject movie = results.getJSONObject(i);
                    String title=movie.getString("title");
                    String img_url = movie.getString("poster_path");
                    img_url = img_url.substring(1);
                    moviewName.add(title);
                    movieNo.add(i);
                    arrayList.add(Constants.IMAGE_BASE_URL + img_url +"?"+ Constants.API_KEY);

                }


            }
            catch (Exception e)
            {
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
            mProg.cancel();
//            if(moviewName.size()!=arrayList.size()) {
//                try {
//                    update(spinner.getSelectedItem().toString());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
           // else {
                movies.setAdapter(image);
                image.notifyDataSetChanged();
           // }
        }
    }

}
