package com.driver;



import java.util.Arrays;
import java.util.List;


public class Order {


    private String id;
    private int deliveryTime;

    public Order(String id, int deliveryTime) {
        this.id = id;
        this.deliveryTime = deliveryTime;
    }

    public Order(String id, String deliveryTime) {

        // The deliveryTime has to converted from string to int and then stored in the attribute
        //deliveryTime  = HH*60 + MM
        this.deliveryTime = covertDeliveryTime(deliveryTime);
        this.id = id;
    }
    public Order() {
    }
    public void setId(String id) {
        this.id = id;
    }

    public void setDeliveryTime(int deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {

        this.deliveryTime = covertDeliveryTime(deliveryTime);
    }

    public static int covertDeliveryTime(String deliveryTime) {
        List<String> list = Arrays.asList(deliveryTime.split(":"));
        int HH = Integer.parseInt(list.get(0));
        int MM = Integer.parseInt(list.get(1));
        return HH * 60 + MM;
    }

    public String getId() {
        return id;
    }

    public int getDeliveryTime() {
        return deliveryTime;
    }



    public static String convertDeliveryTime(int deliveryTime) {
        int HH = deliveryTime / 60;
        int MM = deliveryTime % 60;
        String hh = String.valueOf(HH);
        String mm = String.valueOf(MM);
        if (hh.length() == 1) {
            hh = '0' + hh;
        }
        if (mm.length() == 1) {
            mm = '0' + mm;
        }
        return hh + ":" + mm;
    }

}