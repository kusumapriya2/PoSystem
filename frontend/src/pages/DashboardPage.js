import React, { useEffect, useState } from "react";
import PageHeader from "../components/PageHeader";
import AlertMessage from "../components/AlertMessage";
import { getAllVendors } from "../services/vendorService";
import { getAllProducts } from "../services/productService";
import { getAllCustomers } from "../services/customerService";
import { getAllOrders } from "../services/orderService";
import { getAllDeliveries } from "../services/deliveryService";
import { getMessage, unwrap } from "../services/api";

function DashboardPage() {
  const [stats, setStats] = useState({ vendors: 0, products: 0, customers: 0, orders: 0, deliveries: 0 });
  const [recentOrders, setRecentOrders] = useState([]);
  const [error, setError] = useState("");

  useEffect(() => {
    const loadDashboard = async () => {
      try {
        const results = await Promise.allSettled([
          getAllVendors(),
          getAllProducts(),
          getAllCustomers(),
          getAllOrders(),
          getAllDeliveries()
        ]);

        const [vendorsRes, productsRes, customersRes, ordersRes, deliveriesRes] = results;
        const vendors = vendorsRes.status === "fulfilled" ? unwrap(vendorsRes.value) || [] : [];
        const products = productsRes.status === "fulfilled" ? unwrap(productsRes.value) || [] : [];
        const customers = customersRes.status === "fulfilled" ? unwrap(customersRes.value) || [] : [];
        const orders = ordersRes.status === "fulfilled" ? unwrap(ordersRes.value) || [] : [];
        const deliveries = deliveriesRes.status === "fulfilled" ? unwrap(deliveriesRes.value) || [] : [];

        setStats({
          vendors: vendors.length,
          products: products.length,
          customers: customers.length,
          orders: orders.length,
          deliveries: deliveries.length
        });
        setRecentOrders(orders.slice(-5).reverse());

        const rejected = results.filter((entry) => entry.status === "rejected");
        if (rejected.length > 0) {
          setError("Some dashboard data could not be loaded. Check that the backend is running on port 8080.");
        }
      } catch (err) {
        setError(getMessage(err, "Failed to load dashboard"));
      }
    };

    loadDashboard();
  }, []);

  const cards = [
    { label: "Total Vendors", value: stats.vendors },
    { label: "Total Products", value: stats.products },
    { label: "Total Customers", value: stats.customers },
    { label: "Total Orders", value: stats.orders },
    { label: "Total Deliveries", value: stats.deliveries }
  ];

  return (
    <div>
      <PageHeader
        title="Dashboard"
        subtitle="Live overview of your purchase order application"
      />

      <AlertMessage message={error} type="warning" onClose={() => setError("")} />

      <div className="row g-3 mb-4">
        {cards.map((card) => (
          <div className="col-md col-sm-6" key={card.label}>
            <div className="card app-card stat-card h-100">
              <div className="card-body">
                <p className="text-muted mb-2">{card.label}</p>
                <h3 className="mb-0">{card.value}</h3>
              </div>
            </div>
          </div>
        ))}
      </div>

      <div className="card app-card">
        <div className="card-body">
          <h5 className="mb-3">Recent Orders</h5>
          <div className="table-responsive">
            <table className="table table-hover align-middle mb-0">
              <thead>
                <tr>
                  <th>Order ID</th>
                  <th>Customer ID</th>
                  <th>Vendor ID</th>
                  <th>Net Total</th>
                  <th>Status</th>
                </tr>
              </thead>
              <tbody>
                {recentOrders.length > 0 ? (
                  recentOrders.map((order) => (
                    <tr key={order.orderId}>
                      <td>{order.orderId}</td>
                      <td>{order.customerId}</td>
                      <td>{order.vendorId}</td>
                      <td>{order.netTotal ?? order.grandTotal ?? 0}</td>
                      <td>{order.poStatus || "CREATED"}</td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan="5" className="text-center py-4">
                      No orders found yet.
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
}

export default DashboardPage;
