import axios from "axios";

const BASE_URL = "http://localhost:8080/customers";

export const getAllCustomers = () => {
  return axios.get(`${BASE_URL}/getAllCustomers`);
};

export const createCustomer = (customerData) => {
  return axios.post(`${BASE_URL}/createCustomer`, customerData);
};

export const getCustomerById = (id) => {
  return axios.get(`${BASE_URL}/getCustomerById/${id}`);
};

export const updateCustomer = (id, customerData) => {
  return axios.put(`${BASE_URL}/updateCustomer/${id}`, customerData);
};

export const deleteCustomer = (id) => {
  return axios.delete(`${BASE_URL}/deleteCustomer/${id}`);
};