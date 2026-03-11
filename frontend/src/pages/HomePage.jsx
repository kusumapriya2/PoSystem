import React from "react";
import Layout from "../components/Layout";

function HomePage() {
  return (
    <Layout
      title="Welcome to Purchase Order System"
      subtitle="Manage vendors, products, customers, orders, payments, and delivery"
      buttonText="Create Order"
    >
      <div className="row">
        <div className="col-md-3">
          <div className="card dashboard-card shadow-sm">
            <div className="card-body">
              <h6>Total Orders</h6>
              <h3>120</h3>
            </div>
          </div>
        </div>

        <div className="col-md-3">
          <div className="card dashboard-card shadow-sm">
            <div className="card-body">
              <h6>Pending Orders</h6>
              <h3>25</h3>
            </div>
          </div>
        </div>

        <div className="col-md-3">
          <div className="card dashboard-card shadow-sm">
            <div className="card-body">
              <h6>Payments Pending</h6>
              <h3>10</h3>
            </div>
          </div>
        </div>

        <div className="col-md-3">
          <div className="card dashboard-card shadow-sm">
            <div className="card-body">
              <h6>Delivered Orders</h6>
              <h3>60</h3>
            </div>
          </div>
        </div>
      </div>

      <div className="row mt-4">
        <div className="col-md-8">
          <div className="card shadow-sm">
            <div className="card-body">
              <h4 className="mb-3">Recent Orders</h4>

              <table className="table table-bordered table-hover">
                <thead className="table-dark">
                  <tr>
                    <th>Order ID</th>
                    <th>Vendor</th>
                    <th>Status</th>
                    <th>Amount</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td>PO101</td>
                    <td>ABC Supplies</td>
                    <td>Pending</td>
                    <td>₹12,000</td>
                  </tr>
                  <tr>
                    <td>PO102</td>
                    <td>Tech World</td>
                    <td>Approved</td>
                    <td>₹18,500</td>
                  </tr>
                  <tr>
                    <td>PO103</td>
                    <td>Office Mart</td>
                    <td>Delivered</td>
                    <td>₹8,900</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>

        <div className="col-md-4">
          <div className="card shadow-sm">
            <div className="card-body">
              <h4 className="mb-3">Quick Actions</h4>

              <button className="btn btn-outline-primary w-100 mb-2">Add Vendor</button>
              <button className="btn btn-outline-success w-100 mb-2">Add Product</button>
              <button className="btn btn-outline-dark w-100 mb-2">View Orders</button>
              <button className="btn btn-outline-warning w-100">Track Delivery</button>
            </div>
          </div>

          <div className="card shadow-sm mt-4">
            <div className="card-body">
              <h4 className="mb-3">Recent Activity</h4>
              <ul className="activity-list">
                <li>Order PO101 created</li>
                <li>Payment received for PO096</li>
                <li>Courier assigned to PO099</li>
                <li>Order PO090 delivered</li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </Layout>
  );
}

export default HomePage;