package art4muslim.macbook.rahatycustomer.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import art4muslim.macbook.rahatycustomer.R;
import art4muslim.macbook.rahatycustomer.application.BaseApplication;
import art4muslim.macbook.rahatycustomer.fragments.DetailSectionFragment;
import art4muslim.macbook.rahatycustomer.fragments.DetailsSectionNonPriceFragment;
import art4muslim.macbook.rahatycustomer.models.Category;
import art4muslim.macbook.rahatycustomer.session.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static art4muslim.macbook.rahatycustomer.session.Constants.baseUrlImages;

/**
 * Created by macbook on 29/12/2017.
 */

public class CustomGrid extends BaseAdapter {
    private Context mContext;
    String languageToLoad;
    ArrayList<Category> categories;
    FragmentTransaction fragmentTransaction;
    public CustomGrid(Context c, ArrayList<Category> categories, FragmentTransaction fragmentTransaction ) {
        mContext = c;
        this.categories = categories;
        this.fragmentTransaction = fragmentTransaction;
        languageToLoad = BaseApplication.session.getKey_LANGUAGE();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return categories.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final Category cat = categories.get(position);
        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_single, null);
            TextView textView = (TextView) grid.findViewById(R.id.grid_text);
            ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);
            if (languageToLoad.equals(Constants.arabic)){
                textView.setText(cat.getName_ar());
            }else{
                textView.setText(cat.getName());
            }

          //  imageView.setImageResource(cat.getImage());

            Picasso.with(mContext)
                    .load(baseUrlImages+cat.getThumbnail())
                    .fit()
                    .into(imageView);

            assert convertView != null;
            grid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    int id =  cat.getId();
                    int has_price =  cat.getHas_price();
                    String description;
                    String title ;

                    if (languageToLoad.equals(Constants.arabic)){
                        title =  cat.getName_ar();
                        description =  cat.getDescription();
                    }else{
                        title =  cat.getName();
                        description =  cat.getDescription();
                    }
                    if (has_price == 1){
                        DetailSectionFragment schedule = new DetailSectionFragment();
                        Bundle args = new Bundle();
                        args.putInt("ID", id);
                        args.putInt("HAS_PRICE", has_price);
                        args.putString("TITLE", title);

                        schedule.setArguments(args);
                        fragmentTransaction.replace(R.id.frame,schedule,"First Fragment");

                    } else {
                        DetailsSectionNonPriceFragment schedule = new DetailsSectionNonPriceFragment();

                        Bundle args = new Bundle();
                        args.putInt("ID", id);
                        args.putInt("HAS_PRICE", has_price);
                        args.putString("DESCRIPTION", description);
                        args.putString("TITLE", title);
                        schedule.setArguments(args);

                        fragmentTransaction.replace(R.id.frame,schedule,"First Fragment");

                    }

                    fragmentTransaction.commit();

                }
            });
        } else {
            grid = (View) convertView;
        }




        return grid;
    }
}
