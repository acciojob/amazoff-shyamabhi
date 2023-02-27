package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderRepository {

     Map<String,Order> orderdb = new HashMap<>();
    Map<String,DeliveryPartner> deliveryPartnerdb = new HashMap<>();
    Map<String,String> orderPartnerdb = new HashMap<>();
    Map<String, List<String>> partnerOrderdb=new HashMap<>();

    public void addOrder(Order order) {
        orderdb.put(order.getId(),order);
    }

    public void addPartner(String partnerId) {
        deliveryPartnerdb.put(partnerId,new DeliveryPartner(partnerId));
    }

    public Order getOrderById(String orderId) {
        return orderdb.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        return deliveryPartnerdb.get(partnerId);
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        if(orderdb.containsKey(orderId) && deliveryPartnerdb.containsKey(partnerId)){
            orderPartnerdb.put(orderId,partnerId);
            List<String> co = new ArrayList<>();
            if(partnerOrderdb.containsKey(partnerId)){
                co = partnerOrderdb.get(partnerId);
            }
            co.add(orderId);
            partnerOrderdb.put(partnerId,co);
            DeliveryPartner partner = deliveryPartnerdb.get(partnerId);
            partner.setNumberOfOrders(co.size());

        }
    }

    public Integer getOrderCountByPartnerId(String partnerId) {
        return partnerOrderdb.get(partnerId).size();
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        return partnerOrderdb.get(partnerId);
    }

    public List<String> getAllOrders() {
        List<String>orders = new ArrayList<>();
        for(String o:orderdb.keySet()){
            orders.add(o);
        }
        return orders;
    }

    public Integer getCountOfUnassignedOrders() {
        return orderdb.size()-orderPartnerdb.size();
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(int t, String partnerId) {
        Integer count=0;
        List<String>orders = partnerOrderdb.get(partnerId);
        for(String orderid:orders){
            int dt = orderdb.get(orderid).getDeliveryTime();
            if(dt>t){
                count++;
            }
        }
        return count;
    }

    public int getLastDeliveryTimeByPartnerId(String partnerId) {
        int maxtime = 0 ;
        List<String>orders = partnerOrderdb.get(partnerId);
        for(String order:orders){
            int ct = orderdb.get(order).getDeliveryTime();
            maxtime = Math.max(maxtime,ct);
        }
        return maxtime;
    }

    public void deletePartnerById(String partnerId) {
        deliveryPartnerdb.remove(partnerId);
        List<String>listoforders = partnerOrderdb.get(partnerId);
        partnerOrderdb.remove(partnerId);

        for(String order:listoforders){
            orderPartnerdb.remove(order);
        }
    }

    public void deleteOrderById(String orderId) {
        orderdb.remove(orderId);
        String partnerid = orderPartnerdb.get(orderId);
        orderPartnerdb.remove(orderId);
        partnerOrderdb.get(partnerid).remove(orderId);
        deliveryPartnerdb.get(partnerid).setNumberOfOrders(partnerOrderdb.get(partnerid).size());
    }
}
