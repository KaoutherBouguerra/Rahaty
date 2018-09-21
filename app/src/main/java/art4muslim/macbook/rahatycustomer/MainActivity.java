package art4muslim.macbook.rahatycustomer;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import art4muslim.macbook.rahatycustomer.application.BaseApplication;
import art4muslim.macbook.rahatycustomer.fragments.AboutUsFragment;
import art4muslim.macbook.rahatycustomer.fragments.CartFragment;
import art4muslim.macbook.rahatycustomer.fragments.ContactUsFragment;
import art4muslim.macbook.rahatycustomer.fragments.MainFragment;
import art4muslim.macbook.rahatycustomer.fragments.NotificationFragment;
import art4muslim.macbook.rahatycustomer.fragments.ShareWithFriendsFragment;
import art4muslim.macbook.rahatycustomer.fragments.TermsAndConditionsFragment;
import art4muslim.macbook.rahatycustomer.fragments.UserInfoFragment;
import art4muslim.macbook.rahatycustomer.fragments.orders.ListOrdersFragment;
import art4muslim.macbook.rahatycustomer.fragments.orders.OrderDetailsFragment;
import art4muslim.macbook.rahatycustomer.session.Constants;
import art4muslim.macbook.rahatycustomer.utils.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import static art4muslim.macbook.rahatycustomer.session.SessionManager.KEY_NAME;
import static art4muslim.macbook.rahatycustomer.session.SessionManager.Key_UserIMAGE;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawer;
    NavigationView navigationView;
    String languageToLoad;
    TextView language;
    boolean isFromNotif = false;
    public  static Intent detaiintent;
    String id_cat;
    String orderid;
    String status;
    String tag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById( R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,   R.string.navigation_drawer_open,  R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();



        navigationView = (NavigationView) findViewById(  R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        languageToLoad = BaseApplication.session.getKey_LANGUAGE();

        setHeaderInfo(navigationView);
        if (languageToLoad.equals("en")) {
            language.setText("AR");
        } else  {
            language.setText("EN");
        }

        setTitle( R.string.item_Home);

        detaiintent = getIntent();
        id_cat = detaiintent.getStringExtra("ID_CAT");
        orderid = detaiintent.getStringExtra("orderId");
        status = detaiintent.getStringExtra("status" );
        isFromNotif = detaiintent.getBooleanExtra("isFromNorif",false);
        tag = detaiintent.getStringExtra("tag");

        if (!isFromNotif) {
            MainFragment schedule = new MainFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame,schedule,"First Fragment");
            fragmentTransaction.commit();
        }else{
            OrderDetailsFragment schedule = new OrderDetailsFragment();
            Bundle args =new Bundle();


            args.putString("ORDER_ID", orderid);
            args.putString("ID", id_cat);
            args.putString("STATUS", status);

            schedule.setArguments(args);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame,schedule,"First Fragment");
            fragmentTransaction.commit();
        }

    }

    private void setHeaderInfo(NavigationView navigationView){
        View header=navigationView.getHeaderView(0);

        TextView name = (TextView)header.findViewById(R.id.txtName);
        language = (TextView)header.findViewById(R.id.img_settings);
        LinearLayout linear_name = (LinearLayout)header.findViewById(R.id.linear_name);
        CircularImageView imview = (CircularImageView) header.findViewById(R.id.imDriver);

        name.setText(BaseApplication.session.getUserDetails().get(KEY_NAME));
        String img_url = Constants.image_baseUrl+BaseApplication.session.getUserDetails().get(Key_UserIMAGE);

        Picasso.with(MainActivity.this).load( img_url)
                .placeholder(R.drawable.users)
                .into(imview);

        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // languageToLoad  = language.getText().toString().toLowerCase();
                Log.e("TAG","languageToLoad == "+languageToLoad);

                if (languageToLoad.equals("en")) {
                    language.setText("EN");
                    languageToLoad="ar";
                    BaseApplication.session.saveUserLanguage(languageToLoad);


                } else  {
                    language.setText("AR");
                    languageToLoad="en";
                    BaseApplication.session.saveUserLanguage(languageToLoad);


                }
                setLocale(getApplicationContext(), languageToLoad);
                Log.e("TAG","languageToLoad == APRES "+languageToLoad);
                finish();
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);



            }
        });

        linear_name.setOnClickListener(new View.OnClickListener() {
          @Override
             public void onClick(View v) {
                UserInfoFragment schedule = new UserInfoFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame,schedule,"First Fragment");
                fragmentTransaction.commit();
                drawer.closeDrawer(GravityCompat.START);
          }
        });

    }

    protected static void setLocale(final Context ctx, final String lang)
    {
        final Locale loc = new Locale(lang);
        Locale.setDefault(loc);
        final Configuration cfg = new Configuration();
        cfg.locale = loc;
        ctx.getResources().updateConfiguration(cfg, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.item_back) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id ==  R.id.item_Home) {
            setTitle( R.string.item_Home);
            MainFragment schedule = new MainFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace( R.id.frame,schedule,"home Fragment");
            fragmentTransaction.commit();
        }
       else if (id ==  R.id.item_Myorders) {
            setTitle( R.string.item_Myorders);
            ListOrdersFragment schedule = new ListOrdersFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace( R.id.frame,schedule,"home Fragment");
            fragmentTransaction.commit();
        }
       else if (id ==  R.id.item_Card) {
            setTitle( R.string.card);
            CartFragment schedule = new CartFragment();
            Bundle args = new Bundle();
            args.putBoolean("EDITING", false);
            schedule.setArguments(args);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace( R.id.frame,schedule,"home Fragment");
            fragmentTransaction.commit();
        }
       else if (id ==  R.id.item_Notif) {
            setTitle( R.string.item_Notif);
            NotificationFragment schedule = new NotificationFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace( R.id.frame,schedule,"home Fragment");
            fragmentTransaction.commit();
        }

        else if (id ==  R.id.item_about) {
            setTitle( R.string.about);
            AboutUsFragment schedule = new AboutUsFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace( R.id.frame,schedule,"home Fragment");
            fragmentTransaction.commit();


        }  else if (id ==  R.id.item_terms) {
            setTitle( R.string.txt_terms);
            TermsAndConditionsFragment schedule = new TermsAndConditionsFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace( R.id.frame,schedule,"home Fragment");
            fragmentTransaction.commit();


        }else if (id ==  R.id.item_friends) {
            setTitle( R.string.callfriends);
            ShareWithFriendsFragment schedule = new ShareWithFriendsFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace( R.id.frame,schedule,"home Fragment");
            fragmentTransaction.commit();

        } else if (id == R.id.item_contac) {
            setTitle( R.string.item_contact);
            ContactUsFragment schedule = new ContactUsFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace( R.id.frame,schedule,"home Fragment");
            fragmentTransaction.commit();

        }else if (id == R.id.item_logout) {

            BaseApplication.session.logoutUser();
            this.finish();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            // exit = true;

            //this.finish();
            moveTaskToBack(true);

            super.onBackPressed();
        }
    }


}
