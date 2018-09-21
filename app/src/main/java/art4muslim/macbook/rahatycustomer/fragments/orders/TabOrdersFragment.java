package art4muslim.macbook.rahatycustomer.fragments.orders;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import art4muslim.macbook.rahatycustomer.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabOrdersFragment extends Fragment {


    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 3 ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_tab_orders, container, false);

        tabLayout = (TabLayout) v.findViewById(R.id.tabs);

        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        /**
         *Set an Apater for the View Pager
         */
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

        /**
         * Now , this is a workaround ,
         * The setupWithViewPager dose't works without the runnable .
         * Maybe a Support Library Bug .
         */

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                if(isAdded()) {
                    tabLayout.setupWithViewPager(viewPager);
                }}
        });
        return v;
    }
    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return fragment with respect to Position .
         */

        @Override
        public Fragment getItem(int position)
        {
            switch (position){
                case 0 : return new WaitingOrderFragment();
                case 1 : return new InprogressFragment();
                case 2 : return new DoneOrderFragment();
            }
            return null;
        }

        @Override
        public int getCount() {

            return int_items;

        }

        /**
         * This method returns the title of the tab according to the position.
         */

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position){
                case 0 :
                    return  getResources().getString(R.string.tab_urgorder);
                case 1 :
                    return getResources().getString(R.string.tab_normlorder);
                case 2 :
                    return getResources().getString(R.string.tab_lastorder);
            }
            return null;
        }
    }
}
