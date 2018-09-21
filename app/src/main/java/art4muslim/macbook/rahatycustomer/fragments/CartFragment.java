package art4muslim.macbook.rahatycustomer.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import art4muslim.macbook.rahatycustomer.R;

import art4muslim.macbook.rahatycustomer.adapters.CustomListProducts;
import art4muslim.macbook.rahatycustomer.application.BaseApplication;
import art4muslim.macbook.rahatycustomer.models.Product;
import art4muslim.macbook.rahatycustomer.models.ProductToCart;
import art4muslim.macbook.rahatycustomer.models.SettingsModel;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {
    private FrameLayout redCircle;
    private static TextView countTextView;
    private static int alertCount = 0;

    View v;
    ListView _list_item;
    RelativeLayout _relative;
    Button _btn_go;
    ArrayList<Product> products = new ArrayList<Product>();
    private static final String TAG = CartFragment.class.getSimpleName();
    static BaseApplication app;
    static TextView _txt_total_amount;
    static TextView _txt_delevering_amount;
    static TextView _txt_order_amount;
    TextView _txt_msg;
    boolean isRightToLeft;
    boolean isForEdit= false;
    int id;
    int idCat;

    static SettingsModel settingsModel ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_cart, container, false);
        app = (BaseApplication)getActivity().getApplicationContext();
        settingsModel = app.getSettingsModel();
        isRightToLeft = getResources().getBoolean(R.bool.is_right_to_left);
        getActivity().setTitle(getString(R.string.card));
        isForEdit = getArguments().getBoolean("EDITING");
        initFields();
        id = getArguments().getInt("ORDER_ID");
        idCat = getArguments().getInt("ID");
        int amount=0;
        for (ProductToCart products:  app.getProductsToCart()){
            Product products1 = products.getProduct();
            int somme = Integer.parseInt(products1.getPrice())*products.getNum();
            Log.e(TAG," somme == "+somme);
            amount = amount + somme;
        }

        Log.e(TAG," amount == "+amount);
        Log.e(TAG," getDelivery_cost == "+settingsModel.getDelivery_cost());
        _txt_order_amount.setText(amount+" "+getString(R.string.ryal));
        _txt_delevering_amount.setText(settingsModel.getDelivery_cost()+" "+getString(R.string.ryal));
        int total = amount+Integer.parseInt(settingsModel.getDelivery_cost());
        _txt_total_amount.setText(total+" "+getString(R.string.ryal));

        final CustomListProducts adapter = new CustomListProducts(getActivity(), app.getProductsToCart());

        _list_item.setAdapter(adapter);
        if (app.getProductsToCart()==null){
            _list_item.setVisibility(View.GONE);
            _relative.setVisibility(View.GONE);
            _btn_go.setVisibility(View.GONE);
            _txt_msg.setVisibility(View.VISIBLE);

        }else if (app.getProductsToCart().size()==0){
            _list_item.setVisibility(View.GONE);
            _relative.setVisibility(View.GONE);
            _btn_go.setVisibility(View.GONE);
            _txt_msg.setVisibility(View.VISIBLE);
        }

        _btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to map
                if (app.getProductsToCart().size()!=0) {
                    MapCurrentFragment schedule = new MapCurrentFragment();
                    Bundle args = new Bundle();
                    args.putInt("ORDER_ID", id);
                    args.putInt("ID", idCat);
                    args.putBoolean("EDITING", isForEdit);
                    schedule.setArguments(args);
                    FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame, schedule, "First Fragment");
                    fragmentTransaction.commit();
                    _btn_go.setVisibility(View.GONE);
                }

            }
        });

        return v;
    }

    private void initFields() {

        _list_item = (ListView)v.findViewById(R.id.list_item);
        _relative = (RelativeLayout) v.findViewById(R.id.relative);
        _btn_go =(Button)v.findViewById(R.id.btn_go);
        _txt_total_amount =(TextView)v.findViewById(R.id.txt_total_amount);
        _txt_delevering_amount =(TextView)v.findViewById(R.id.txt_delevering_amount);
        _txt_order_amount =(TextView)v.findViewById(R.id.txt_order_amount);
        _txt_msg =(TextView)v.findViewById(R.id.txt_msg);

    }

     public static void updateText(int amount , String ryal){

        String[] strings = _txt_total_amount.getText().toString().split(" ");
        int somme = Integer.parseInt(strings[0]);
        int rest = somme - amount;
        _txt_order_amount.setText(rest+" " +ryal);

         _txt_delevering_amount.setText(settingsModel.getDelivery_cost() );
         int total = rest+Integer.parseInt(settingsModel.getDelivery_cost());
         _txt_total_amount.setText(total+" " +ryal);
       // _txt_total_amount.setText(rest+" ");
    }

     public static void updateAdd(int amount ,int i , String ryal){

        String[] strings = _txt_total_amount.getText().toString().split(" ");
        int somme = Integer.parseInt(strings[0]);
         int rest;
         if (i==1)
         rest = somme + amount;
         else rest = somme - amount;
        _txt_order_amount.setText(rest+" " +ryal);
         _txt_delevering_amount.setText(settingsModel.getDelivery_cost()+" " +ryal);
         int total = rest+Integer.parseInt(settingsModel.getDelivery_cost());
         _txt_total_amount.setText(total+" "+ryal );
       // _txt_total_amount.setText(rest+" ");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_search).setVisible(true);
        menu.findItem(R.id.action_user).setVisible(true);
        final MenuItem alertMenuItem = menu.findItem(R.id.action_user);
        FrameLayout rootView = (FrameLayout) alertMenuItem.getActionView();

        redCircle = (FrameLayout) rootView.findViewById(R.id.view_alert_red_circle);
        countTextView = (TextView) rootView.findViewById(R.id.view_alert_count_textview);
        updateAlertIcon();
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

        menu.findItem(R.id.action_search).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                SearchFragment schedule1 = new SearchFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame,schedule1,"home Fragment");
                fragmentTransaction.commit();

                return false;
            }
        });
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartFragment schedule1 = new CartFragment();
                Bundle args = new Bundle();
                args.putBoolean("EDITING", isForEdit);
                schedule1.setArguments(args);
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame,schedule1,"home Fragment");
                fragmentTransaction.commit();
            }
        });
        super.onPrepareOptionsMenu(menu);
    }


    public static void updateAlertIcon() {
        // if alert count extends into two digits, just show the red circle
        alertCount = app.getProductsToCart().size();
      //  if (0 < alertCount && alertCount < 10) {
        countTextView.setText(String.valueOf(alertCount));
      //  } else {
      //      countTextView.setText("0");
      //  }

        //  redCircle.setVisibility((alertCount > 0) ? VISIBLE : GONE);
    }
}
