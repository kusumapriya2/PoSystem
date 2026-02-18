package com.example.email;

import com.example.entity.Order;

public class EmailTemplates {
    public static String orderPlaced(Order order){
        return "Hi" +order.getCustomer().getCustomerName() + ",\n\n"
                + "Your order has been placed successfully!\n"
                +"PO Number: " +order.getPoNumber()+"\n"
                +"Total Amount: " +order.getNetTotal()+"\n"
                +"Status: "+order.getPoStatus()+"\n\n"
                +"Thank you for shopping with us.\nPoSystem Team";
    }
}
