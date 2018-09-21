package art4muslim.macbook.rahatycustomer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import art4muslim.macbook.rahatycustomer.R;
import art4muslim.macbook.rahatycustomer.application.BaseApplication;
import art4muslim.macbook.rahatycustomer.fragments.CartFragment;
import art4muslim.macbook.rahatycustomer.models.ProductToCart;
import art4muslim.macbook.rahatycustomer.session.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static art4muslim.macbook.rahatycustomer.session.Constants.baseUrlImages;

/**
 * Created by macbook on 29/12/2017.
 */

public class CustomListProducts extends BaseAdapter {
    private Context mContext;

    int i;
    ArrayList<ProductToCart> products;
    String languageToLoad;
    public CustomListProducts(Context c, ArrayList<ProductToCart> products ) {
        mContext = c;
        this.products = products;
        languageToLoad = BaseApplication.session.getKey_LANGUAGE();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return products.size();
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

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.list_single, null);
            TextView _txt_title = (TextView) grid.findViewById(R.id.txt_title);
            TextView _txt_price = (TextView) grid.findViewById(R.id.txt_price);
            ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);
            ImageView _img_moin = (ImageView)grid.findViewById(R.id.img_moin);
            ImageView _img_plus = (ImageView)grid.findViewById(R.id.img_plus);
            ImageView _img_cancel = (ImageView)grid.findViewById(R.id.img_cancel);
            final EditText _edt_num = (EditText)grid.findViewById(R.id.edt_num);
            _edt_num.setText(""+products.get(position).getNum());
            i = Integer.parseInt(_edt_num.getText().toString());

            if (languageToLoad.equals(Constants.arabic)){
                _txt_title.setText(products.get(position).getProduct().getName_ar());
            }else{
                _txt_title.setText(products.get(position).getProduct().getName());
            }
            _txt_price.setText(products.get(position).getProduct().getPrice());
          //  imageView.setImageResource(products.get(position).getImage());
            Picasso.with(mContext)
                    .load(baseUrlImages+products.get(position).getProduct().getImage())
                    .fit()
                    .into(imageView);

            if (i==0)
                _img_moin.setEnabled(false);
            else _img_moin.setEnabled(true);

            _img_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    i++;
                    products.get(position).setNum(i);
                    _edt_num.setText(""+i);
                    CartFragment.updateAdd(Integer.parseInt(products.get(position).getProduct().getPrice()),1, mContext.getString(R.string.ryal));

                }
            });
            _img_moin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    i--;
                    products.get(position).setNum(i);
                    _edt_num.setText(""+i);
                    CartFragment.updateAdd(Integer.parseInt(products.get(position).getProduct().getPrice()),-1, mContext.getString(R.string.ryal));


                }
            });
            _img_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // CartFragment cartFragment = new CartFragment();
                    CartFragment.updateText(Integer.parseInt(products.get(position).getProduct().getPrice())*products.get(position).getNum(), mContext.getString(R.string.ryal));
                    products.remove(position);
                    CartFragment.updateAlertIcon();

                   // _edt_num.setText(i);
                    notifyDataSetChanged();

                }
            });
        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}
