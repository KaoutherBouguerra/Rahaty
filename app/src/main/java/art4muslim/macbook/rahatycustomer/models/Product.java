package art4muslim.macbook.rahatycustomer.models;

/**
 * Created by macbook on 31/12/2017.
 */

public class Product {

    private int id;
    private String name;
    private String name_ar;

    public String getName_ar() {
        return name_ar;
    }

    public void setName_ar(String name_ar) {
        this.name_ar = name_ar;
    }

    private String price;
    private String image;
    private String has_price;

    public String getHas_price() {
        return has_price;
    }

    public void setHas_price(String has_price) {
        this.has_price = has_price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Product(int id, String name,String name_ar, String price, String image, String has_price) {
        this.id = id;
        this.name = name;
        this.name_ar = name_ar;
        this.price = price;
        this.image = image;
        this.has_price = has_price;
    }
}
