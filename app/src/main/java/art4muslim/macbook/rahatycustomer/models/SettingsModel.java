package art4muslim.macbook.rahatycustomer.models;

/**
 * Created by macbook on 23/01/2018.
 */

public class SettingsModel {
    String facebook;
    String google_plus;
    String linked_in;
    String instagram;
    String youtube;
    String twitter;
    String ar_about_us;
    String ar_terms;
    String en_about_us;
    String en_terms;
    String ar_share_content;
    String en_share_content;
    String delivery_cost;
    String app_commission;

    public SettingsModel(String facebook, String google_plus, String linked_in, String instagram, String youtube, String twitter, String ar_about_us, String ar_terms, String en_about_us, String en_terms, String ar_share_content, String en_share_content, String delivery_cost, String app_commission) {
        this.facebook = facebook;
        this.google_plus = google_plus;
        this.linked_in = linked_in;
        this.instagram = instagram;
        this.youtube = youtube;
        this.twitter = twitter;
        this.ar_about_us = ar_about_us;
        this.ar_terms = ar_terms;
        this.en_about_us = en_about_us;
        this.en_terms = en_terms;
        this.ar_share_content = ar_share_content;
        this.en_share_content = en_share_content;
        this.delivery_cost = delivery_cost;
        this.app_commission = app_commission;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getGoogle_plus() {
        return google_plus;
    }

    public void setGoogle_plus(String google_plus) {
        this.google_plus = google_plus;
    }

    public String getLinked_in() {
        return linked_in;
    }

    public void setLinked_in(String linked_in) {
        this.linked_in = linked_in;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getAr_about_us() {
        return ar_about_us;
    }

    public void setAr_about_us(String ar_about_us) {
        this.ar_about_us = ar_about_us;
    }

    public String getAr_terms() {
        return ar_terms;
    }

    public void setAr_terms(String ar_terms) {
        this.ar_terms = ar_terms;
    }

    public String getEn_about_us() {
        return en_about_us;
    }

    public void setEn_about_us(String en_about_us) {
        this.en_about_us = en_about_us;
    }

    public String getEn_terms() {
        return en_terms;
    }

    public void setEn_terms(String en_terms) {
        this.en_terms = en_terms;
    }

    public String getAr_share_content() {
        return ar_share_content;
    }

    public void setAr_share_content(String ar_share_content) {
        this.ar_share_content = ar_share_content;
    }

    public String getEn_share_content() {
        return en_share_content;
    }

    public void setEn_share_content(String en_share_content) {
        this.en_share_content = en_share_content;
    }

    public String getDelivery_cost() {
        return delivery_cost;
    }

    public void setDelivery_cost(String delivery_cost) {
        this.delivery_cost = delivery_cost;
    }

    public String getApp_commission() {
        return app_commission;
    }

    public void setApp_commission(String app_commission) {
        this.app_commission = app_commission;
    }
}
