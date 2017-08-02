package com.pranayaa.itunessearch_pranayaa;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class PrimaryFragment extends Fragment {
    private List<ItunesTrack> mItunesTrack;
    private ListView mListView;
    private ItunesTrackAdapter mItunesTrackAdapter;
    private MediaPlayer mMediaPlayer;
    private String mCurrentlyPlayingUrl;
    private String queryTerm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        queryTerm = getArguments().getString("data");
        Log.i("sa", queryTerm);
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_primary, container, false);
         //Set up the list view.
        mListView = (ListView) v.findViewById(R.id.list_view);
        mItunesTrackAdapter = new ItunesTrackAdapter(getActivity());
        mListView.setAdapter(mItunesTrackAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItunesTrack itunesTrack = (ItunesTrack) parent.getAdapter().getItem(position);
                // Add your click handling code here.
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "This track is good");
                shareIntent.putExtra(Intent.EXTRA_TEXT, itunesTrack. getmTtrackViewUrl());
                startActivity(Intent.createChooser(shareIntent, "Share link using"));
            }
        });
        if (mItunesTrack != null) {
            mItunesTrackAdapter.setItems(mItunesTrack);
        } else {
            refreshItunesTrack();
        }
        return v;
    }


    private void clickedAudioURL(String url) {
        if (mMediaPlayer.isPlaying()) {
            if (mCurrentlyPlayingUrl.equals(url)) {
                mMediaPlayer.stop();
                mItunesTrackAdapter.notifyDataSetChanged();
                return;
            }
        }

        mCurrentlyPlayingUrl = url;
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.prepareAsync(); // might take long! (for buffering, etc)
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    mItunesTrackAdapter.notifyDataSetChanged();
                }
            });
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mItunesTrackAdapter.notifyDataSetChanged();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    private void refreshItunesTrack() {
        ItunesTrackSource.get(getContext()).getItunesResults(queryTerm, new ItunesTrackSource.ItunesTrackListener() {
            @Override
            public void onItunesTrackResponse(List<ItunesTrack> itunesList) {
                mItunesTrack = itunesList;
                // Stop the spinner and update the list view.
                mItunesTrackAdapter.setItems(mItunesTrack);
            }
        });
    }


    private class ItunesTrackAdapter extends BaseAdapter {
        private TextView mTrackNameTextView;
        private TextView mArtistNameTextView;
        private TextView mCollectionNameTextView;
        private NetworkImageView imageView;

        private Context mContext;
        private LayoutInflater mInflater;
        private List<ItunesTrack> mDataSource;

        private ItunesTrackAdapter(Context context) {
            mContext = context;
            mDataSource = new ArrayList<>();
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void setItems(List<ItunesTrack> itunesTrackList) {
            mDataSource.clear();
            mDataSource.addAll(itunesTrackList);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mDataSource.size();
        }

        @Override
        public Object getItem(int position) {
            return mDataSource.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get model item for this row, and generate view for this row.
            final ItunesTrack itunesTrack = mDataSource.get(position);
            View rowView = mInflater.inflate(R.layout.fragment_primary_list, parent, false);

            // Add your code here. You'll want to:
            // 1) Get local references to the trackName TextView, artistName TextView, and collectionName TextView and set the
            // itunesTrack contents on them.
            // 2) Get a reference to the NetworkImageView, and use the ImageLoader vended by
            // ItunesTrackSource to set the image.
            mTrackNameTextView = (TextView) rowView.findViewById(R.id.textView_trackName);
            String trackNameText = itunesTrack.getmTrackName();
            mTrackNameTextView.setText(trackNameText);

            mArtistNameTextView = (TextView) rowView.findViewById(R.id.textView_artistName);
            String artistNameText = itunesTrack.getmArtistName();
            mArtistNameTextView.setText(artistNameText);


            mCollectionNameTextView = (TextView) rowView.findViewById(R.id.textView_collectionName);
            String collectionNameText = itunesTrack.getmCollectionName();
            mCollectionNameTextView.setText(collectionNameText);

            imageView = (NetworkImageView) rowView.findViewById(R.id.thumbnail);
            ImageLoader loader = ItunesTrackSource.get(getContext()).getImageLoader();
            imageView.setImageUrl(itunesTrack.getmArtworkUrl60(), loader);


            final ImageButton playButton = (ImageButton) rowView.findViewById(R.id.play_button);
            boolean isPlaying = mMediaPlayer.isPlaying() &&
                    mCurrentlyPlayingUrl.equals(itunesTrack.getmPreviewUrl());
            // Here, add code to set the play/pause button icon based on isPlaying
            
            if (isPlaying){
                playButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_pause));
            }else{
                playButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_play));
            }

            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickedAudioURL(itunesTrack.getmPreviewUrl());
                }
            });


            return rowView;
        }

    }



}


