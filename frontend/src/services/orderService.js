import api from "./api";

export const getAllOrders = () => api.get("/orders/getAll");
export const createOrder = (payload) => api.post("/orders/createOrders", payload);
export const deleteOrder = (id) => api.delete(`/orders/deleteByid/${id}`);
