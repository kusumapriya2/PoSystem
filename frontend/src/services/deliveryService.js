import api from "./api";

export const getAllDeliveries = () => api.get("/api/allDeliveries");
export const shipDelivery = (orderId, payload) => api.put(`/orders/deliveries/${orderId}/ship`, payload);
export const moveToTransit = (orderId, payload) => api.put(`/orders/deliveries/${orderId}/inTransit`, payload);
export const markDelivered = (orderId, payload) => api.put(`/orders/deliveries/${orderId}/deliver`, payload);
