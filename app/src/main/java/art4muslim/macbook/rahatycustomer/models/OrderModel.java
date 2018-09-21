package art4muslim.macbook.rahatycustomer.models;

/**
 * Created by macbook on 02/01/2018.
 */

public class OrderModel {

    private String id;
    private String idCat;
    private String date;
    private String time;
    private String status;

    public String getIdCat() {
        return idCat;
    }

    public void setIdCat(String idCat) {
        this.idCat = idCat;
    }

    public OrderModel(String id, String date, String time, String status, String idCat) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.status = status;
        this.idCat = idCat;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
