package com.example.mapper;
import com.example.dto.InvoiceDTO;
import com.example.entity.Invoice;
import com.example.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
@Mapper(componentModel="spring")
public interface InvoiceMapper {
    @Mapping(source = "order", target = "orderId", qualifiedByName = "ordersToOrderId")
    InvoiceDTO toDto(Invoice invoice);

    @Mapping(source = "orderId", target = "order", qualifiedByName = "orderIdToOrders")
    Invoice toEntity(InvoiceDTO dto);

    @Named("ordersToOrderId")
    default Long ordersToOrderId(Order orders) {
        if (orders == null) return null;
        return orders.getOrderId();
    }
    @Named("orderIdToOrders")
    default Order orderIdToOrders(Long orderId) {
        if (orderId == null) return null;
        Order o = new Order();
        o.setOrderId(orderId);
        return o;
    }
}
