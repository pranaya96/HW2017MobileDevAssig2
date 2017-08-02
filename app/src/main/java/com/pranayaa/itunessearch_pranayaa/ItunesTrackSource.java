package com.pranayaa.itunessearch_pranayaa;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ItunesTrackSource {


    public interface ItunesTrackListener {
        void onItunesTrackResponse(List<ItunesTrack> itunesTrackList);
    }

    private final static int IMAGE_CACHE_COUNT = 100;
    private static ItunesTrackSource sItunesTrackSourceInstance;


    private Context mContext;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    public static ItunesTrackSource get(Context context) {
        if (sItunesTrackSourceInstance == null) {
            sItunesTrackSourceInstance = new ItunesTrackSource(context);
        }
        return sItunesTrackSourceInstance;
    }

    public ItunesTrackSource(Context context) {
        mContext = context.getApplicationContext();
        mRequestQueue = Volley.newRequestQueue(mContext);

        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<>(IMAGE_CACHE_COUNT);
            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }
            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });
    }

    public void getItunesResults(String query, ItunesTrackListener resultsListener) {
        final ItunesTrackListener itunesTrackListenerInternal = resultsListener;

        String url = "https://itunes.apple.com/search?term="+query+"&entity=musicTrack";
        JsonObjectRequest jsonObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            List<ItunesTrack> itunesTrackList = new ArrayList<ItunesTrack>();
                            // Get the map of itunes track
                            JSONArray itunesTrackArr = response.getJSONArray("results");
                            for(int i =0; i < itunesTrackArr.length() ; i++){
                                JSONObject itunesTrackObj = itunesTrackArr.getJSONObject(i);
                                ItunesTrack itunesTrack = new ItunesTrack(itunesTrackObj);
                                itunesTrackList.add(itunesTrack);
                            }
                            itunesTrackListenerInternal.onItunesTrackResponse(itunesTrackList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            itunesTrackListenerInternal.onItunesTrackResponse(null);
                            Toast.makeText(mContext, "Could not get itunes track.", Toast.LENGTH_SHORT);
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        itunesTrackListenerInternal.onItunesTrackResponse(null);
                        Toast.makeText(mContext, "Could not get itunes track.", Toast.LENGTH_SHORT);
                    }
                });

        mRequestQueue.add(jsonObjRequest);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

}



