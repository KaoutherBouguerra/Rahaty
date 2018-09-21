package art4muslim.macbook.rahatycustomer.fragments.orders;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import art4muslim.macbook.rahatycustomer.R;
import art4muslim.macbook.rahatycustomer.application.BaseApplication;
import art4muslim.macbook.rahatycustomer.fragments.MainFragment;
import art4muslim.macbook.rahatycustomer.session.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListOrdersFragment extends Fragment {


    View v;
    boolean isRightToLeft;
    TextView _txt_last,_txt_current;
    Intent intent;
    public static final String BROADCAST_ACTION = "com.websmithing.broadcasttest.displayevent";
    private final Handler handler = new Handler();
    private static final String TAG = ListOrdersFragment.class.getSimpleName();
    String languageToLoad;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_list_orders, container, false);
        isRightToLeft = getResources().getBoolean(R.bool.is_right_to_left);

        initFields();
        languageToLoad = BaseApplication.session.getKey_LANGUAGE();
        if (languageToLoad.equals(Constants.arabic)){
            _txt_current.setBackgroundResource(R.drawable.round_left_rect_shape_b);
            _txt_current.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.white));
            _txt_last.setBackgroundResource(R.drawable.round_right_rect_shape_w);
            _txt_last.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
        }else{
            _txt_current.setBackgroundResource(R.drawable.round_right_rect_shape_b);
            _txt_current.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.white));
            _txt_last.setBackgroundResource(R.drawable.round_left_rect_shape_w);
            _txt_last.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
        }
        _txt_current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (languageToLoad.equals(Constants.arabic)){
                    _txt_current.setBackgroundResource(R.drawable.round_left_rect_shape_b);
                    _txt_current.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.white));
                    _txt_last.setBackgroundResource(R.drawable.round_right_rect_shape_w);
                    _txt_last.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                }else{
                    _txt_current.setBackgroundResource(R.drawable.round_right_rect_shape_b);
                    _txt_current.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.white));
                    _txt_last.setBackgroundResource(R.drawable.round_left_rect_shape_w);
                    _txt_last.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                }


                handler.removeCallbacks(createRunnable("0"));
                handler.postDelayed(createRunnable("0"), 0);
            }
        });
        _txt_last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (languageToLoad.equals(Constants.arabic)){
                    _txt_last.setBackgroundResource(R.drawable.round_right_rect_shape_b);
                    _txt_last.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.white));
                    _txt_current.setBackgroundResource(R.drawable.round_left_rect_shape_w);
                    _txt_current.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                }else{
                    _txt_last.setBackgroundResource(R.drawable.round_left_rect_shape_b);
                    _txt_last.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.white));
                    _txt_current.setBackgroundResource(R.drawable.round_right_rect_shape_w);
                    _txt_current.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                }

                handler.removeCallbacks(createRunnable("1"));
                handler.postDelayed(createRunnable("1"), 0);
            }
        });


        TabOrdersFragment schedule = new TabOrdersFragment();
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace( R.id.frame,schedule,"home Fragment");
        fragmentTransaction.commit();
        return v;
    }

    private void initFields(){
        _txt_current = (TextView)v.findViewById(R.id.txt_current);
        _txt_last = (TextView)v.findViewById(R.id.txt_last);
    }

    @Override
    public void onResume() {
        super.onResume();
        intent = new Intent(BROADCAST_ACTION);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }



    private Runnable createRunnable(final String type){

        Runnable sendUpdatesToUI = new Runnable() {
            public void run() {
                Log.e(TAG, "createRunnable type === "+type);
                DisplayLoggingInfo(type);
            }
        };

        return sendUpdatesToUI;

    }


    private void DisplayLoggingInfo(String type) {
        Log.d(TAG, "entered DisplayLoggingInfo");

        intent.putExtra("TYPE", type);
        getActivity().sendBroadcast(intent);
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.action_user).setVisible(false);

        if (!isRightToLeft ) {
            menu.findItem(R.id.item_back).setIcon(getResources().getDrawable(R.mipmap.backright));
        }else  menu.findItem(R.id.item_back).setIcon(getResources().getDrawable(R.mipmap.back));

        menu.findItem(R.id.item_back).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                MainFragment schedule1 = new MainFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame,schedule1,"home Fragment");
                fragmentTransaction.commit();

                return false;
            }
        });

        super.onPrepareOptionsMenu(menu);
    }
}
