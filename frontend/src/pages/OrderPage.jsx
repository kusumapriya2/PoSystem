import React from "react";
import Layout from "../components/Layout";

function CustomerPage() {
  return (
    <Layout
      title="Customer Management"
      subtitle="Add and manage customer details for purchase orders"
      buttonText="Add Customer"
    >
      <div className="row">
        <div className="col-md-4">
          <div className="card shadow-sm">
            <div className="card-body">
              <h4 className="mb-3">Customer Form</h4>

              <form>
                <div className="mb-3">
                  <label className="form-label">Customer Name</label>
                  <input
                    type="text"
                    className="form-control"
                    placeholder="Enter customer name"
                  />
                </div>

                <div className="mb-3">
                  <label className="form-label">Email</label>
                  <input
                    type="email"
                    className="form-control"
                    placeholder="Enter customer email"
                  />
                </div>

                <div className="mb-3">
                  <label className="form-label">Phone</label>
                  <input
                    type="text"
                    className="form-control"
                    placeholder="Enter customer phone"
                  />
                </div>

                <div className="mb-3">
                  <label className="form-label">City</label>
                  <input
                    type="text"
                    className="form-control"
                    placeholder="Enter city"
                  />
                </div>

                <div className="mb-3">
                  <label className="form-label">Address</label>
                  <textarea
                    className="form-control"
                    rows="3"
                    placeholder="Enter customer address"
                  ></textarea>
                </div>

                <button type="submit" className="btn btn-success w-100">
                  Save Customer
                </button>
              </form>
            </div>
          </div>
        </div>

        <div className="col-md-8">
          <div className="card shadow-sm">
            <div className="card-body">
              <h4 className="mb-3">Customer List</h4>

              <table className="table table-bordered table-hover">
                <thead className="table-dark">
                  <tr>
                    <th>Customer ID</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Phone</th>
                    <th>City</th>
                    <th>Address</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td>C101</td>
                    <td>Ravi Kumar</td>
                    <td>ravi@gmail.com</td>
                    <td>9876543210</td>
                    <td>Hyderabad</td>
                    <td>Madhapur</td>
                  </tr>
                  <tr>
                    <td>C102</td>
                    <td>Sneha Reddy</td>
                    <td>sneha@gmail.com</td>
                    <td>9123456780</td>
                    <td>Bangalore</td>
                    <td>Whitefield</td>
                  </tr>
                  <tr>
                    <td>C103</td>
                    <td>Arjun Rao</td>
                    <td>arjun@gmail.com</td>
                    <td>9988776655</td>
                    <td>Chennai</td>
                    <td>Anna Nagar</td>
                  </tr>
                  <tr>
                    <td>C104</td>
                    <td>Priya Sharma</td>
                    <td>priya@gmail.com</td>
                    <td>9012345678</td>
                    <td>Pune</td>
                    <td>Hinjewadi</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>

          <div className="card shadow-sm mt-4">
            <div className="card-body">
              <h4 className="mb-3">Customer Summary</h4>

              <div className="row">
                <div className="col-md-4">
                  <div className="summary-box">
                    <h6>Total Customers</h6>
                    <h3>32</h3>
                  </div>
                </div>

                <div className="col-md-4">
                  <div className="summary-box">
                    <h6>Active Customers</h6>
                    <h3>26</h3>
                  </div>
                </div>

                <div className="col-md-4">
                  <div className="summary-box">
                    <h6>New This Month</h6>
                    <h3>6</h3>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </Layout>
  );
}

export default CustomerPage;