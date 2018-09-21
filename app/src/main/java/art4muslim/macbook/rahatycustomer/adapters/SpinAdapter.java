package art4muslim.macbook.rahatycustomer.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import art4muslim.macbook.rahatycustomer.application.BaseApplication;
import art4muslim.macbook.rahatycustomer.models.Category;
import art4muslim.macbook.rahatycustomer.session.Constants;

import java.util.ArrayList;

/**
 * Created by macbook on 27/01/2018.
 */

public class SpinAdapter extends ArrayAdapter<Category> {

    // Your sent context
    private Context context;
    // Your custom values for the spinner (User)
    private ArrayList<Category> values;
    String languageToLoad;
    public SpinAdapter(Context context, int textViewResourceId,
                       ArrayList<Category> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
        languageToLoad = BaseApplication.session.getKey_LANGUAGE();
    }

    @Override
    public int getCount(){
        return values.size();
    }

    @Override
    public Category getItem(int position){
        return values.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }


    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)

        if (languageToLoad.equals(Constants.arabic)){
            label.setText(values.get(position).getName_ar());
        }else
            label.setText(values.get(position).getName());

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);


        if (languageToLoad.equals(Constants.arabic)){
            label.setText(values.get(position).getName_ar());
        }else
            label.setText(values.get(position).getName());

        return label;
    }
}
