import axios from "axios";

const BASE_URL = "http://localhost:8080/vendors";

export const getAllVendors = () => {
  return axios.get(`${BASE_URL}/allVendors`);
};

export const saveVendor = (vendorData) => {
  return axios.post(`${BASE_URL}/create`, vendorData);
};

export const getVendorById = (vendorId) => {
  return axios.get(`${BASE_URL}/getById/${vendorId}`);
};

export const updateVendor = (id, vendorData) => {
  return axios.put(`${BASE_URL}/update/${id}`, vendorData);
};

export const deleteVendor = (vendorId) => {
  return axios.delete(`${BASE_URL}/delete/${vendorId}`);
};