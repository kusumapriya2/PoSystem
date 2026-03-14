import React from "react";
import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import Layout from "./components/Layout";
import DashboardPage from "./pages/DashboardPage";
import VendorPage from "./pages/VendorPage";
import ProductPage from "./pages/ProductPage";
import CustomerPage from "./pages/CustomerPage";
import OrderPage from "./pages/OrderPage";
import PaymentPage from "./pages/PaymentPage";
import DeliveryPage from "./pages/DeliveryPage";

function App() {
  return (
    <BrowserRouter>
      <Layout>
        <Routes>
          <Route path="/" element={<DashboardPage />} />
          <Route path="/vendors" element={<VendorPage />} />
          <Route path="/products" element={<ProductPage />} />
          <Route path="/customers" element={<CustomerPage />} />
          <Route path="/orders" element={<OrderPage />} />
          <Route path="/payments" element={<PaymentPage />} />
          <Route path="/deliveries" element={<DeliveryPage />} />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </Layout>
    </BrowserRouter>
  );
}

export default App;
