package com.pranayaa.itunessearch_pranayaa;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         //Set the fragment into the container.
        FragmentManager fm = getSupportFragmentManager();
        Fragment existingFragment = fm.findFragmentById(R.id.container);
        if (existingFragment == null) {
            Fragment itunesTrackListFragment = new PrimaryFragment();
            fm.beginTransaction().replace(R.id.container, itunesTrackListFragment).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);

        MenuItem item = menu.findItem(R.id.action_search);

        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                //create a dialoge box
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

                // Dialog Title
                alertDialog.setTitle("Search for iTunes track");

                //Set EditText in the dialog box
                final EditText input = new EditText(MainActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);

                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.delete);

                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {

                        // Write your code here to invoke YES event
                        FragmentManager fm = getSupportFragmentManager();
                        Fragment existingFragment = fm.findFragmentById(R.id.container);
                        if (existingFragment == null) {
                            Fragment itunesTrackListFragment = new PrimaryFragment();
                            Bundle args = new Bundle();
                            args.putString("data", input.getText().toString());
                            itunesTrackListFragment.setArguments(args);
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, itunesTrackListFragment).commit();
                        }
                    }
                });

                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        Toast.makeText(getApplicationContext(), "You clicked Cancel", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });

                // Showing Alert Message
                alertDialog.show();

                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);

    }
}


