package art4muslim.macbook.rahatycustomer.models;

/**
 * Created by macbook on 04/01/2018.
 */

public class Notification {
    private String idOder;
    private String date;
    private String time;

    public Notification(String idOder, String date, String time) {
        this.idOder = idOder;
        this.date = date;
        this.time = time;
    }

    public String getIdOder() {
        return idOder;
    }

    public void setIdOder(String idOder) {
        this.idOder = idOder;
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
