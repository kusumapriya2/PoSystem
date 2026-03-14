import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080"
});

export function unwrap(response) {
  return response?.data?.data;
}

export function getMessage(error, fallback) {
  return (
    error?.response?.data?.message ||
    error?.response?.data?.error ||
    error?.message ||
    fallback
  );
}

export default api;
