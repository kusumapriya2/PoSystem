import api from "./api";

export const getAllCustomers = () => api.get("/customers/getAllCustomers");
export const createCustomer = (payload) => api.post("/customers/createCustomer", payload);
export const deleteCustomer = (id) => api.delete(`/customers/delete/${id}`);
