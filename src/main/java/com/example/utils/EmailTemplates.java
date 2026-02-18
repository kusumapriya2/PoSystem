package com.example.utils;

import com.example.entity.Order;
import com.example.entity.DeliveryDetails;

public class EmailTemplates {

    public static String orderPlaced(Order order) {
        return "Hi " + order.getCustomer().getCustomerName() + ",\n\n"
                + "Your order has been placed successfully.\n"
                + "PO Number: " + order.getPoNumber() + "\n"
                + "Total Amount: " + order.getNetTotal() + "\n"
                + "Status: " + order.getPoStatus() + "\n\n"
                + "Thank you for shopping with us.\nPoSystem Team";
    }

    public static String orderApproved(Order order) {
        return "Hi " + order.getCustomer().getCustomerName() + ",\n\n"
                + "Good news! Your order has been APPROVED.\n"
                + "PO Number: " + order.getPoNumber() + "\n"
                + "Expected Delivery Date: " + order.getExpectedDeliveryDate() + "\n\n"
                + "PoSystem Team";
    }

    public static String deliveryAssigned(Order order, DeliveryDetails delivery) {
        return "Hi " + order.getCustomer().getCustomerName() + ",\n\n"
                + "Your delivery has been assigned.\n"
                + "Courier: " + delivery.getCourier().getName() + "\n"
                + "Tracking Number: " + delivery.getTrackingNumber() + "\n\n"
                + "PoSystem Team";
    }

    public static String inTransit(Order order, DeliveryDetails delivery) {
        return "Hi " + order.getCustomer().getCustomerName() + ",\n\n"
                + "Your order is now IN TRANSIT.\n"
                + "Tracking Number: " + delivery.getTrackingNumber() + "\n"
                + "ETA: " + delivery.getEtaDate() + "\n\n"
                + "PoSystem Team";
    }

    public static String shipped(Order order, DeliveryDetails delivery) {
        return "Hi " + order.getCustomer().getCustomerName() + ",\n\n"
                + "Your order has been SHIPPED.\n"
                + "Tracking Number: " + delivery.getTrackingNumber() + "\n\n"
                + "PoSystem Team";
    }

    public static String delivered(Order order, DeliveryDetails savedDD) {
        return "Hi " + order.getCustomer().getCustomerName() + ",\n\n"
                + "Your order has been DELIVERED successfully.\n"
                + "PO Number: " + order.getPoNumber() + "\n\n"
                + "Thank you for choosing us!\nPoSystem Team";
    }

    public static String orderRejected(Order order, String reason) {
        return "Hi " + order.getCustomer().getCustomerName() + ",\n\n"
                + "We regret to inform you that your order has been REJECTED.\n\n"
                + "PO Number: " + order.getPoNumber() + "\n"
                + "Reason: " + reason + "\n\n"
                + "If you have any questions, please contact support.\n\n"
                + "PoSystem Team";
    }

}
