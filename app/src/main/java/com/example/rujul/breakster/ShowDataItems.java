package com.example.rujul.breakster;

/**
 * Created by Rujul on 7/7/2017.
 */

public class ShowDataItems {
    private String Image_URL;
    private String Image_Title;
    private String Price;
    private String Description;
    private String ItemCount;
    private String ItemName;

    public String getItemCount() {
        return ItemCount;
    }

    public void setItemCount(String itemCount) {
        ItemCount = itemCount;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

     //put this name same as Database Fields





    public ShowDataItems(String image_URL, String image_title, String image_Desc, String image_Price) {
        Image_URL = image_URL;
        Image_Title = image_title;
        Price = image_Price;
        Description = image_Desc;

    }
    public ShowDataItems() {
        //Empty Constructor Needed
    }

    public String getImage_URL() {
        return Image_URL;
    }

    public void setImage_URL(String image_URL) {
        Image_URL = image_URL;
    }

    public String getImage_Title() {
        return Image_Title;
    }

    public void setTitle(String title) {
        Image_Title = title;

    }
}