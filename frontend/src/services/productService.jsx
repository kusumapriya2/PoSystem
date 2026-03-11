import axios from "axios";

const PRODUCT_BASE_URL = "http://localhost:8080/products";
const VENDOR_BASE_URL = "http://localhost:8080/vendors";

export const getAllProducts = () => {
  return axios.get(`${PRODUCT_BASE_URL}/getAllProducts`);
};

export const getProductById = (id) => {
  return axios.get(`${PRODUCT_BASE_URL}/getByIdProducts/${id}`);
};

export const createProduct = (productData) => {
  return axios.post(`${VENDOR_BASE_URL}/createProducts`, productData);
};

export const updateProduct = (productId, productData) => {
  return axios.put(`${PRODUCT_BASE_URL}/updateProductById/${productId}`, productData);
};

export const deleteProduct = (id) => {
  return axios.delete(`${VENDOR_BASE_URL}/deleteProducts/${id}`);
};