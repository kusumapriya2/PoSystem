import axios from "axios";

const BASE_URL = "http://localhost:8080/payments";

export const getAllPayments = () => {
  return axios.get(`${BASE_URL}/getAllPayments`);
};

export const createPayment = (paymentData) => {
  return axios.post(`${BASE_URL}/createPayment`, paymentData);
};

export const getPaymentById = (id) => {
  return axios.get(`${BASE_URL}/getPaymentById/${id}`);
};

export const updatePayment = (id, paymentData) => {
  return axios.put(`${BASE_URL}/updatePayment/${id}`, paymentData);
};

export const deletePayment = (id) => {
  return axios.delete(`${BASE_URL}/deletePayment/${id}`);
};