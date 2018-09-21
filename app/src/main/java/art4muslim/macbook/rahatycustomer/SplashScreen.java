package art4muslim.macbook.rahatycustomer;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import art4muslim.macbook.rahatycustomer.application.BaseApplication;

import java.util.Locale;

public class SplashScreen extends AppCompatActivity {

    Button btn_english,btn_arab;
    String languageToLoad=  "ar";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        initFields();
        btn_english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // your language

                languageToLoad  = "en";
                setLocale(getApplicationContext(), languageToLoad);


                BaseApplication.session.saveUserLanguage(languageToLoad);

                if (!BaseApplication.session.isLoggedIn()){
                    Intent refresh=new Intent(getApplicationContext(),LoginActivity.class);
                    refresh.putExtra("languageToLoad",languageToLoad);
                    startActivity(refresh);
                    finish();
                }else BaseApplication.session.checkLogin();



            }
        });
        btn_arab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // your language

                languageToLoad  = "ar";
                setLocale(getApplicationContext(), languageToLoad);

                BaseApplication.session.saveUserLanguage(languageToLoad);

                if (!BaseApplication.session.isLoggedIn()){
                    Intent refresh=new Intent(getApplicationContext(),LoginActivity.class);
                    refresh.putExtra("languageToLoad",languageToLoad);
                    startActivity(refresh);
                    finish();
                }else BaseApplication.session.checkLogin();


            }
        });
      /*  Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(3000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                    // ActivityTransitionLauncher.with(SplashScreen.this).from(imgLogo.getRootView() ).launch(intent);
                    startActivity(intent);
                    // overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
                }
            }
        };
        timerThread.start();


        */
    }

    private void initFields(){

        btn_english= (Button)findViewById(R.id.btn_eng);
        btn_arab= (Button)findViewById(R.id.btn_arabic);

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
    public void onBackPressed() {


        moveTaskToBack(true);

        super.onBackPressed();

    }
}
