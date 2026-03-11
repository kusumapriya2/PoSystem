import React from "react";
import Layout from "../components/Layout";

function DeliveryPage() {
  return (
    <Layout
      title="Delivery Management"
      subtitle="Track shipments, courier assignment, and delivery status"
      buttonText="Assign Courier"
    >
      <div className="row">
        <div className="col-md-4">
          <div className="card shadow-sm">
            <div className="card-body">
              <h4 className="mb-3">Delivery Form</h4>

              <form>
                <div className="mb-3">
                  <label className="form-label">Tracking ID</label>
                  <input
                    type="text"
                    className="form-control"
                    placeholder="Enter tracking ID"
                  />
                </div>

                <div className="mb-3">
                  <label className="form-label">Order ID</label>
                  <select className="form-select">
                    <option>Select order</option>
                    <option>PO101</option>
                    <option>PO102</option>
                    <option>PO103</option>
                    <option>PO104</option>
                  </select>
                </div>

                <div className="mb-3">
                  <label className="form-label">Courier Name</label>
                  <select className="form-select">
                    <option>Select courier</option>
                    <option>BlueDart</option>
                    <option>Delhivery</option>
                    <option>DTDC</option>
                    <option>Ekart</option>
                  </select>
                </div>

                <div className="mb-3">
                  <label className="form-label">Dispatch Date</label>
                  <input type="date" className="form-control" />
                </div>

                <div className="mb-3">
                  <label className="form-label">Expected Delivery</label>
                  <input type="date" className="form-control" />
                </div>

                <div className="mb-3">
                  <label className="form-label">Status</label>
                  <select className="form-select">
                    <option>Select status</option>
                    <option>Pending</option>
                    <option>Shipped</option>
                    <option>In Transit</option>
                    <option>Delivered</option>
                  </select>
                </div>

                <button type="submit" className="btn btn-success w-100">
                  Save Delivery
                </button>
              </form>
            </div>
          </div>

          <div className="card shadow-sm mt-4">
            <div className="card-body">
              <h4 className="mb-3">Delivery Summary</h4>

              <div className="summary-box mb-3">
                <h6>Total Shipments</h6>
                <h3>37</h3>
              </div>

              <div className="summary-box mb-3">
                <h6>In Transit</h6>
                <h3>11</h3>
              </div>

              <div className="summary-box">
                <h6>Delivered</h6>
                <h3>26</h3>
              </div>
            </div>
          </div>
        </div>

        <div className="col-md-8">
          <div className="card shadow-sm">
            <div className="card-body">
              <h4 className="mb-3">Delivery Tracking List</h4>

              <table className="table table-bordered table-hover">
                <thead className="table-dark">
                  <tr>
                    <th>Tracking ID</th>
                    <th>Order ID</th>
                    <th>Courier</th>
                    <th>Dispatch Date</th>
                    <th>Expected Date</th>
                    <th>Status</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td>TRK101</td>
                    <td>PO101</td>
                    <td>BlueDart</td>
                    <td>2026-03-10</td>
                    <td>2026-03-13</td>
                    <td>
                      <span className="badge bg-warning text-dark">Pending</span>
                    </td>
                  </tr>
                  <tr>
                    <td>TRK102</td>
                    <td>PO102</td>
                    <td>Delhivery</td>
                    <td>2026-03-09</td>
                    <td>2026-03-12</td>
                    <td>
                      <span className="badge bg-primary">Shipped</span>
                    </td>
                  </tr>
                  <tr>
                    <td>TRK103</td>
                    <td>PO103</td>
                    <td>DTDC</td>
                    <td>2026-03-08</td>
                    <td>2026-03-11</td>
                    <td>
                      <span className="badge bg-info text-dark">In Transit</span>
                    </td>
                  </tr>
                  <tr>
                    <td>TRK104</td>
                    <td>PO104</td>
                    <td>Ekart</td>
                    <td>2026-03-07</td>
                    <td>2026-03-10</td>
                    <td>
                      <span className="badge bg-success">Delivered</span>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>

          <div className="card shadow-sm mt-4">
            <div className="card-body">
              <h4 className="mb-3">Recent Delivery Activity</h4>

              <ul className="activity-list">
                <li>TRK101 created and waiting for dispatch</li>
                <li>TRK102 shipped through Delhivery</li>
                <li>TRK103 is currently in transit</li>
                <li>TRK104 delivered successfully to customer</li>
              </ul>
            </div>
          </div>

          <div className="card shadow-sm mt-4">
            <div className="card-body">
              <h4 className="mb-3">Delivery Status Flow</h4>

              <div className="status-flow d-flex justify-content-between align-items-center flex-wrap">
                <div className="flow-step completed">Order Placed</div>
                <div className="flow-step completed">Approved</div>
                <div className="flow-step completed">Shipped</div>
                <div className="flow-step active">In Transit</div>
                <div className="flow-step">Delivered</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </Layout>
  );
}

export default DeliveryPage;