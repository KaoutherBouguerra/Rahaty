package art4muslim.macbook.rahatycustomer.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import art4muslim.macbook.rahatycustomer.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsSectionNonPriceFragment extends Fragment {


    int id;
    TextView _txt_text;
    EditText _et_details;
    Button _btn_go;
    String description, title;
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_details_section_non_price, container, false);
        id = getArguments().getInt("ID");
        initField();
        id = getArguments().getInt("ID");
        description = getArguments().getString("DESCRIPTION");
        title = getArguments().getString("TITLE");
        getActivity().setTitle(title);
        _txt_text.setText(Html.fromHtml(description));

        _btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MapCurrentFragment schedule = new MapCurrentFragment();
                Bundle args = new Bundle();
                args.putInt("ID", id);

                args.putBoolean("EDITING", false);

                schedule.setArguments(args);
                FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame1, schedule, "First Fragment");
                fragmentTransaction.commit();
                _btn_go.setVisibility(View.GONE);

            }
        });
        return v;
    }

   private void initField(){
       _txt_text = (TextView)v.findViewById(R.id.txt_text);
       _et_details = (EditText) v.findViewById(R.id.et_details);
       _btn_go = (Button) v.findViewById(R.id.btn_go);
    }
}
