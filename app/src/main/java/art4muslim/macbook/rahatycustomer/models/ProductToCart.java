package art4muslim.macbook.rahatycustomer.models;

/**
 * Created by macbook on 23/01/2018.
 */

public class ProductToCart {

    Product product;
    int num;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public ProductToCart(Product product, int num) {

        this.product = product;
        this.num = num;
    }
}
