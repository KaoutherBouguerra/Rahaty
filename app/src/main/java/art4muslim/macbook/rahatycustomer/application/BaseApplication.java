package art4muslim.macbook.rahatycustomer.application;


import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import art4muslim.macbook.rahatycustomer.models.Category;
import art4muslim.macbook.rahatycustomer.models.CurrentOrder;
import art4muslim.macbook.rahatycustomer.models.Product;
import art4muslim.macbook.rahatycustomer.models.ProductToCart;
import art4muslim.macbook.rahatycustomer.models.SettingsModel;
import art4muslim.macbook.rahatycustomer.session.SessionManager;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by macbook on 16/10/2017.
 */

public class BaseApplication extends Application {
   public static SessionManager session;
   public SettingsModel settingsModel;
   ArrayList<Product> products = new ArrayList<Product>();
   ArrayList<ProductToCart> productsToCart = new ArrayList<ProductToCart>();

   String orderType;

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public ArrayList<ProductToCart> getProductsToCart() {
        return productsToCart;
    }

    public void setProductsToCart(ArrayList<ProductToCart> productsToCart) {
        this.productsToCart = productsToCart;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public CurrentOrder getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(CurrentOrder currentOrder) {
        this.currentOrder = currentOrder;
    }

    public CurrentOrder currentOrder;

    public SettingsModel getSettingsModel() {
        return settingsModel;
    }

    public void setSettingsModel(SettingsModel settingsModel) {
        this.settingsModel = settingsModel;
    }

    public static ArrayList<Category> categories = new ArrayList<Category>();
    private RequestQueue mRequestQueue;

    public static ArrayList<Category> getCategories() {
        return categories;
    }

    public static void setCategories(ArrayList<Category> categories) {
        BaseApplication.categories = categories;
    }

    private static BaseApplication mInstance;
    public static final String TAG = BaseApplication.class.getSimpleName();
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        session = new SessionManager(getApplicationContext());

      //  SystemClock.sleep(3000);
    }

    public static synchronized BaseApplication getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }


    public static byte[] getFileDataFromDrawable(Context context, Bitmap bitmap) {
        //Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

}
