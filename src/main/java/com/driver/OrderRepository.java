package com.driver;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class OrderRepository {
    private static Map<String,Order> orderMap=new HashMap<>();
    private static Map<String,DeliveryPartner> partnerMap=new HashMap<>();

    public OrderRepository() {
    }

    public OrderRepository(Map<String, String> orderPartnerMap, Map<String, ArrayList<String>> partnerOrdersMap) {
        this.orderPartnerMap = orderPartnerMap;
        this.partnerOrdersMap = partnerOrdersMap;
    }

    private  Map<String,String> orderPartnerMap=new HashMap<>();

    private  Map<String, ArrayList<String>> partnerOrdersMap=new HashMap<>();

    public static Optional<DeliveryPartner> getPartnerId(String partnerId) {
        if(partnerMap.containsKey(partnerId)){
            return Optional.of(partnerMap.get(partnerId));
        }
        return Optional.empty();
    }
    public static Optional<Order> getOrderById(String orderId) {
        if(orderMap.containsKey(orderId)){
            return Optional.of(orderMap.get(orderId));
        }
        return Optional.empty();
    }


    public void addOrder(Order order) {
        orderMap.put(order.getId(),order);
    }

    public void addPartner(DeliveryPartner partnerId) {

        partnerMap.put(partnerId.getId(), partnerId);
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        orderPartnerMap.put(orderId,partnerId);
        ArrayList<String> updatedOrders = new ArrayList<>();
        if (partnerOrdersMap.containsKey(partnerId)) {
            updatedOrders = partnerOrdersMap.get(partnerId);
        }
        updatedOrders.add(orderId);
        partnerOrdersMap.put(partnerId, updatedOrders);
    }

    public Map<String, String> getallorderpartnerMappings() {
        return orderPartnerMap;
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        return partnerOrdersMap.get(partnerId);
    }

    public List<String> getAllOrders() {
        return new ArrayList<>(orderMap.keySet());
    }

    public List<String> getAssingnedOrders() {
        return new ArrayList (orderPartnerMap.keySet());
    }


    public void deletePartnerbyId(String partnerId) {
        partnerMap.remove(partnerId);
        partnerOrdersMap.remove(partnerId);
    }

    public void removePartnerOrderMapping(String orderId) {
        orderPartnerMap.remove(orderId);
    }

    public void deleteOrder(String orderId) {
        orderMap.remove(orderId);
        orderPartnerMap.remove(orderId);
    }

    public String getPartnerForOrder(String orderId) {
        return orderPartnerMap.get(orderId);
    }

    public void removeOrderForPartner(String partnerId, String orderId) {
        ArrayList<String> orderIds = partnerOrdersMap.get(partnerId);
        orderIds.remove(orderId);
        partnerOrdersMap.put(partnerId, orderIds);
    }
}
