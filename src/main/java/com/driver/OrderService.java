package com.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderService() {
    }

    public void addOrder(Order order) {
        orderRepository.addOrder(order);
    }

    public void addPartner(String partnerId) {
        DeliveryPartner partner=new DeliveryPartner(partnerId);
        orderRepository.addPartner(partner);
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        Optional<Order> orderOpt= OrderRepository.getOrderById(orderId);
        Optional<DeliveryPartner> deliveryPartnerOpt = OrderRepository.getPartnerId(partnerId);
        if(orderOpt.isPresent() && deliveryPartnerOpt.isPresent()){
            DeliveryPartner p = deliveryPartnerOpt.get();
            Integer initialOrders = p.getNumberOfOrders ();
            initialOrders++;
            p.setNumberOfOrders (initialOrders);
            orderRepository.addPartner(p);
            orderRepository.addOrderPartnerPair(orderId,partnerId);
        }
    }

    public Order getOrderByID(String orderId) {
        Optional<Order> orderOpt= OrderRepository.getOrderById(orderId);
        if(orderOpt.isPresent()){
            return orderOpt.get();
        }
        throw new RuntimeException("Order does not exit for id");
    }

    public Integer getOrderCountForPartner(String partnerId) {
        Optional<DeliveryPartner> p=OrderRepository.getPartnerId(partnerId);
        if(p.isPresent()){
            return p.get().getNumberOfOrders();
        }
        return 0;
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        Optional<DeliveryPartner> partnerOpt= OrderRepository.getPartnerId(partnerId);
        if(partnerOpt.isPresent()){
            return partnerOpt.get();
        }
        throw new RuntimeException("Partner does not exit for id");
    }

    public List<String> getOrdersByPartnerId(String partnerId)
    {
//        Map<String,String> orderPartnerMap=orderRepository.getallorderpartnerMappings();
//        List<String> orderIds = new ArrayList<>();
//        for(var entry: orderPartnerMap.entrySet()) {
//            if (entry.getValue().equals(partnerId)) {
//                orderIds.add(entry.getKey());
//            }
//        }
//        return orderIds;
        return orderRepository.getOrdersByPartnerId(partnerId);
    }


    public List<String> getAllOrders() {
        return orderRepository.getAllOrders();
    }

    public Integer getCountOfUnassignedOrders() {
        return orderRepository.getAllOrders().size()-orderRepository.getAssingnedOrders().size();
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        List<String> orderIds = orderRepository.getOrdersByPartnerId(partnerId);
        int currTime = Order.covertDeliveryTime(time);
        int ordersLeft = 0;
        for (String orderId: orderIds) {
            int deliveryTime = orderRepository.getOrderById(orderId).get().getDeliveryTime();
            if (currTime < deliveryTime) {
                ordersLeft++;
            }
        }
        return ordersLeft;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        List<String> orderIds = orderRepository.getOrdersByPartnerId(partnerId);
        int max=0;
        for (String orderId: orderIds) {
            int deliveryTime = orderRepository.getOrderById(orderId).get().getDeliveryTime();
            if(deliveryTime>max){
                max=deliveryTime;
            }

        }
        return Order.convertDeliveryTime(max);
    }

    public void deletePartnerbyId(String partnerId) {
        List<String> orders = orderRepository.getOrdersByPartnerId(partnerId);
        orderRepository.deletePartnerbyId(partnerId);
        for(String orderId : orders){
            orderRepository.removePartnerOrderMapping(orderId);
        }

    }

    public void deleteOrderById(String orderId) {
        String partnerId = orderRepository.getPartnerForOrder(orderId);
        orderRepository.deleteOrder(orderId);
        if(Objects.nonNull(partnerId)) {
            DeliveryPartner p = orderRepository.getPartnerId(partnerId).get();
            Integer initialOrders = p.getNumberOfOrders ();
            initialOrders--;
            p.setNumberOfOrders (initialOrders);
            orderRepository.addPartner (p);
            orderRepository.removeOrderForPartner (partnerId, orderId);

        }

    }
}

