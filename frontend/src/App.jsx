import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import HomePage from "./pages/HomePage";
import VendorPage from "./pages/VendorPage";
import ProductPage from "./pages/ProductPage";
import CustomerPage from "./pages/CustomerPage";
import OrderPage from "./pages/OrderPage";
import PaymentPage from "./pages/PaymentPage";
import DeliveryPage from "./pages/DeliveryPage";
import "./styles/CommonLayout.css";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/vendors" element={<VendorPage />} />
        <Route path="/products" element={<ProductPage />} />
        <Route path="/customers" element={<CustomerPage />} />
        <Route path="/orders" element={<OrderPage />} />
        <Route path="/payments" element={<PaymentPage />} />
        <Route path="/delivery" element={<DeliveryPage />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;