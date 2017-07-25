package exampfi.daria.chillaxlinuxtryandroid.Views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;

import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import exampfi.daria.chillaxlinuxtryandroid.R;


public class NewsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static Integer[] mThumbIds = {

            R.drawable.a,R.drawable.rsz_aaaa,R.drawable.bbbb,R.drawable.bb,R.drawable.bbb, R.drawable.bbbbbb, R.drawable.ba,R.drawable.rsz_instr1,R.drawable.bbbbb

    };
    public static String[] mInstrIds = {
            "Chillax has it all:", "your friends", "your favorite places...", "Create meetings", "choose friends and place", "chat with friends", "remember old times", "find path", "tell what's on your mind..."
    };
    private ImageView iv;
    private TextView tv;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        iv=(ImageView)findViewById(R.id.to_be_displayed);
        tv=(TextView)findViewById(R.id.instructions);

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            int i=0;
            public void run() {
                iv.setImageResource(mThumbIds[i]);
                tv.setText(mInstrIds[i]);
                i++;
                if(i>mThumbIds.length-1)
                {
                    i=0;
                }
                handler.postDelayed(this, 1500);  //for interval...
            }
        };
        handler.postDelayed(runnable, 50); //for initial delay..


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewsActivity.this, DeveloperActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.news, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_page) {
            Intent intent = new Intent(NewsActivity.this, UserPageActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_friends) {
            Intent intent = new Intent(NewsActivity.this, FriendsOverviewActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_messages) {
            Intent intent = new Intent(NewsActivity.this, FriendsOverviewActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_create_appointment) {
            Intent intent = new Intent(NewsActivity.this, Appointment.class);
            startActivity(intent);

        } else if (id == R.id.nav_open_map) {
            Intent intent = new Intent(NewsActivity.this, MapsActivity.class);
            startActivity(intent);

        }else if (id == R.id.nav_photos) {
            Intent intent = new Intent(NewsActivity.this, Appointment.class);
            startActivity(intent);
            String toastMsg = String.format("Maybe first create a new appoitment?");
            Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            String toastMsg1 = String.format("If not - press on a book!");
            Toast.makeText(this, toastMsg1, Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
