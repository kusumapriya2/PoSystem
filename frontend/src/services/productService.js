import api from "./api";

export const getAllProducts = () => api.get("/products/getAllProducts");
export const createProduct = (payload) => api.post("/vendors/createProducts", payload);
export const deleteProduct = (id) => api.delete(`/vendors/deleteProducts/${id}`);
