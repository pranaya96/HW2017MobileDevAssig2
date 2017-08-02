package com.pranayaa.itunessearch_pranayaa;


import org.json.JSONException;
import org.json.JSONObject;

public class ItunesTrack {
    protected  String mTrackName;
    protected  String mArtistName;
    protected  String mCollectionName;
    protected  String mPreviewUrl;
    protected  String mArtworkUrl60;
    protected  String mTtrackViewUrl;

    public ItunesTrack(JSONObject itunesTrackObj){
        try{
           if(itunesTrackObj.getString("kind").equals("song")){
               mTrackName = itunesTrackObj.getString("trackName");
               mArtistName= itunesTrackObj.getString("artistName");
               mCollectionName = itunesTrackObj.getString("collectionName") ;
               mPreviewUrl = itunesTrackObj.getString("previewUrl") ;
               mArtworkUrl60= itunesTrackObj.getString("artworkUrl30");
               mTtrackViewUrl = itunesTrackObj.getString("trackViewUrl");
           }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }


    public String getmTrackName() {
        return mTrackName;
    }

    public String getmArtistName() {
        return mArtistName;
    }

    public String getmCollectionName() {
        return mCollectionName;
    }

    public String getmPreviewUrl() {
        return mPreviewUrl;
    }

    public String getmArtworkUrl60() {
        return mArtworkUrl60;
    }

    public String getmTtrackViewUrl() {
        return mTtrackViewUrl;
    }
}


