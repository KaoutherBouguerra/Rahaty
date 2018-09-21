package art4muslim.macbook.rahatycustomer.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import art4muslim.macbook.rahatycustomer.R;
import art4muslim.macbook.rahatycustomer.adapters.SpinAdapter;
import art4muslim.macbook.rahatycustomer.application.BaseApplication;
import art4muslim.macbook.rahatycustomer.models.Category;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {


    EditText _et_title;
    Spinner _spinner;
    Button _btn_SendComplain;
    boolean isRightToLeft  ;
    private static String TAG = SearchFragment.class.getSimpleName();
    View v;
    ProgressBar _progressBar;
    LinearLayout _linearLayout;
    BaseApplication app;
    int id_cat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_search, container, false);
        initFields();
        isRightToLeft = getResources().getBoolean(R.bool.is_right_to_left);
        app = (BaseApplication) getActivity().getApplicationContext();
        getActivity().setTitle(getString(R.string.search_txt));
        _btn_SendComplain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailSectionFragment schedule1 = new DetailSectionFragment();
                Bundle args = new Bundle();
                if (!_et_title.getText().toString().isEmpty())
                    args.putString("KEYWORD", _et_title.getText().toString());
                args.putInt("ID", id_cat);


                schedule1.setArguments(args);
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame,schedule1,"home Fragment");
                fragmentTransaction.commit();
            }
        });


        final SpinAdapter  adapter = new SpinAdapter(getActivity().getApplicationContext(),
                android.R.layout.simple_spinner_item,
                app.getCategories());

        _spinner.setAdapter(adapter);
        // You can create an anonymous listener to handle the event when is selected an spinner item
        _spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                // Here you get the current item (a User object) that is selected by its position
                Category category = adapter.getItem(position);
                // Here you can do the action you want to...
                id_cat = category.getId();

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });




        return v;
    }
    private void initFields(){
        _et_title = v.findViewById(R.id.et_title);
        _spinner = v.findViewById(R.id.spinner);
        _btn_SendComplain = v.findViewById(R.id.btn_SendComplain);
        _progressBar=(ProgressBar) v.findViewById(R.id.progressBar);
        _linearLayout = (LinearLayout) v.findViewById(R.id.linearLayout4);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
