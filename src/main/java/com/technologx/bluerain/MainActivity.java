package com.technologx.bluerain;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.joaquimley.faboptions.FabOptions;
import com.technologx.bluerain.fragments.KustomFragment;

import static com.technologx.bluerain.R.id.toolbar;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar Toolbar;
    private static final int CONTENT_VIEW_ID = 10101010;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar = (Toolbar) findViewById(toolbar);
        setSupportActionBar(Toolbar);

        FabOptions fabOptions = (FabOptions) findViewById(R.id.fab_options);
        fabOptions.setButtonsMenu(R.menu.menu_main);
        fabOptions.setBackgroundColor(R.color.fabOptionsBackgroundColor);
        fabOptions.setFabColor(R.color.fabOptionsFabColor);
        fabOptions.setOnClickListener(this);
            }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.faboptions_home:
                Intent home = new Intent(MainActivity.this, MainActivity.class);
                startActivity(home);
                break;

            case R.id.faboptions_kustom:
                Fragment newFragment = new KustomFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.add(CONTENT_VIEW_ID, newFragment).commit();
                break;


            case R.id.faboptions_zooper:
                Intent zooper = new Intent(MainActivity.this, MainActivity.class);
                startActivity(zooper);
                break;

            default:
                // no-op
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_extras, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
