import React from "react";
import Layout from "../components/Layout";

function OrderPage() {
  return (
    <Layout
      title="Purchase Orders"
      subtitle="Create and manage purchase orders"
      buttonText="Create Order"
    >
      <div className="row">
        <div className="col-md-4">
          <div className="card shadow-sm">
            <div className="card-body">
              <h4 className="mb-3">Order Form</h4>

              <form>
                <div className="mb-3">
                  <label className="form-label">Order ID</label>
                  <input
                    type="text"
                    className="form-control"
                    placeholder="Enter order ID"
                  />
                </div>

                <div className="mb-3">
                  <label className="form-label">Customer</label>
                  <select className="form-select">
                    <option>Select customer</option>
                    <option>Ravi Kumar</option>
                    <option>Sneha Reddy</option>
                    <option>Arjun Rao</option>
                    <option>Priya Sharma</option>
                  </select>
                </div>

                <div className="mb-3">
                  <label className="form-label">Vendor</label>
                  <select className="form-select">
                    <option>Select vendor</option>
                    <option>ABC Supplies</option>
                    <option>Tech World</option>
                    <option>Office Mart</option>
                    <option>Prime Traders</option>
                  </select>
                </div>

                <div className="mb-3">
                  <label className="form-label">Product</label>
                  <select className="form-select">
                    <option>Select product</option>
                    <option>Laptop</option>
                    <option>Printer</option>
                    <option>Office Chair</option>
                    <option>Mouse</option>
                  </select>
                </div>

                <div className="mb-3">
                  <label className="form-label">Quantity</label>
                  <input
                    type="number"
                    className="form-control"
                    placeholder="Enter quantity"
                  />
                </div>

                <div className="mb-3">
                  <label className="form-label">Order Date</label>
                  <input type="date" className="form-control" />
                </div>

                <div className="mb-3">
                  <label className="form-label">Delivery Address</label>
                  <textarea
                    className="form-control"
                    rows="3"
                    placeholder="Enter delivery address"
                  ></textarea>
                </div>

                <button type="submit" className="btn btn-success w-100">
                  Save Order
                </button>
              </form>
            </div>
          </div>

          <div className="card shadow-sm mt-4">
            <div className="card-body">
              <h4 className="mb-3">Order Summary</h4>

              <div className="summary-box mb-3">
                <h6>Total Orders</h6>
                <h3>58</h3>
              </div>

              <div className="summary-box mb-3">
                <h6>Pending Approval</h6>
                <h3>14</h3>
              </div>

              <div className="summary-box">
                <h6>Approved Orders</h6>
                <h3>32</h3>
              </div>
            </div>
          </div>
        </div>

        <div className="col-md-8">
          <div className="card shadow-sm">
            <div className="card-body">
              <h4 className="mb-3">Order List</h4>

              <table className="table table-bordered table-hover">
                <thead className="table-dark">
                  <tr>
                    <th>Order ID</th>
                    <th>Customer</th>
                    <th>Vendor</th>
                    <th>Product</th>
                    <th>Qty</th>
                    <th>Amount</th>
                    <th>Status</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td>PO101</td>
                    <td>Ravi Kumar</td>
                    <td>ABC Supplies</td>
                    <td>Laptop</td>
                    <td>2</td>
                    <td>₹110000</td>
                    <td>
                      <span className="badge bg-warning text-dark">Pending</span>
                    </td>
                  </tr>
                  <tr>
                    <td>PO102</td>
                    <td>Sneha Reddy</td>
                    <td>Tech World</td>
                    <td>Printer</td>
                    <td>1</td>
                    <td>₹12000</td>
                    <td>
                      <span className="badge bg-success">Approved</span>
                    </td>
                  </tr>
                  <tr>
                    <td>PO103</td>
                    <td>Arjun Rao</td>
                    <td>Office Mart</td>
                    <td>Office Chair</td>
                    <td>4</td>
                    <td>₹26000</td>
                    <td>
                      <span className="badge bg-primary">Shipped</span>
                    </td>
                  </tr>
                  <tr>
                    <td>PO104</td>
                    <td>Priya Sharma</td>
                    <td>Prime Traders</td>
                    <td>Mouse</td>
                    <td>10</td>
                    <td>₹7000</td>
                    <td>
                      <span className="badge bg-secondary">Delivered</span>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>

          <div className="card shadow-sm mt-4">
            <div className="card-body">
              <h4 className="mb-3">Recent Order Activity</h4>

              <ul className="activity-list">
                <li>PO101 created and waiting for approval</li>
                <li>PO102 approved successfully</li>
                <li>PO103 shipped to customer location</li>
                <li>PO104 delivered successfully</li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </Layout>
  );
}

export default OrderPage;