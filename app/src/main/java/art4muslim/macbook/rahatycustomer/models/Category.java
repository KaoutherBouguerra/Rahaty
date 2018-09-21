package art4muslim.macbook.rahatycustomer.models;

/**
 * Created by macbook on 31/12/2017.
 */

public class Category {

    private int id;
    private String name;
    private String name_ar;
    private int image;
    private String thumbnail;
    private String description;
    private int has_price;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName_ar() {
        return name_ar;
    }

    public void setName_ar(String name_ar) {
        this.name_ar = name_ar;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getHas_price() {
        return has_price;
    }

    public void setHas_price(int has_price) {
        this.has_price = has_price;
    }

    public Category(int id, String name, String name_ar, String thumbnail, int has_price, String description) {
        this.id = id;
        this.name = name;
        this.name_ar = name_ar;
        this.thumbnail = thumbnail;
        this.has_price = has_price;
        this.description = description;
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

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return name_ar;
    }
}
