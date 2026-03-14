import api from "./api";

export const getAllVendors = () => api.get("/vendors/allVendors");
export const createVendor = (payload) => api.post("/vendors/create", payload);
export const deleteVendor = (id) => api.delete(`/vendors/delete/${id}`);
