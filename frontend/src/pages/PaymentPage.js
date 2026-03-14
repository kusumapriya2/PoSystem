import React, { useEffect, useMemo, useState } from "react";
import PageHeader from "../components/PageHeader";
import AlertMessage from "../components/AlertMessage";
import { getMessage, unwrap } from "../services/api";
import { getAllOrders } from "../services/orderService";
import { createPayment } from "../services/paymentService";

const initialForm = {
  orderId: "",
  amountPaid: "",
  paymentMethod: "COD"
};

function PaymentPage() {
  const [orders, setOrders] = useState([]);
  const [form, setForm] = useState(initialForm);
  const [message, setMessage] = useState("");
  const [type, setType] = useState("info");

  const loadOrders = async () => {
    try {
      const response = await getAllOrders();
      setOrders(unwrap(response) || []);
    } catch (err) {
      setType("danger");
      setMessage(getMessage(err, "Failed to load orders for payment"));
    }
  };

  useEffect(() => {
    loadOrders();
  }, []);

  const payableOrders = useMemo(() => orders.filter((order) => order.orderId), [orders]);

  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      const payload = {
        orderId: Number(form.orderId),
        amountPaid: Number(form.amountPaid),
        paymentMethod: form.paymentMethod
      };
      const response = await createPayment(payload);
      setType("success");
      setMessage(response.data.message || "Payment added successfully");
      setForm(initialForm);
      loadOrders();
    } catch (err) {
      setType("danger");
      setMessage(getMessage(err, "Failed to add payment"));
    }
  };

  return (
    <div>
      <PageHeader title="Payment Management" subtitle="Record payments against created orders" />
      <AlertMessage message={message} type={type} onClose={() => setMessage("")} />

      <div className="row g-4">
        <div className="col-lg-4">
          <div className="card app-card">
            <div className="card-body">
              <h5 className="mb-3">Add Payment</h5>
              <form onSubmit={handleSubmit} className="d-grid gap-3">
                <select className="form-select" value={form.orderId} onChange={(e) => setForm({ ...form, orderId: e.target.value })} required>
                  <option value="">Select order</option>
                  {payableOrders.map((order) => <option key={order.orderId} value={order.orderId}>Order #{order.orderId}</option>)}
                </select>
                <input className="form-control" type="number" min="1" placeholder="Amount paid" value={form.amountPaid} onChange={(e) => setForm({ ...form, amountPaid: e.target.value })} required />
                <select className="form-select" value={form.paymentMethod} onChange={(e) => setForm({ ...form, paymentMethod: e.target.value })}>
                  <option value="COD">COD</option>
                  <option value="UPI">UPI</option>
                  <option value="CREDITCARD">CREDITCARD</option>
                  <option value="DEBITCARD">DEBITCARD</option>
                  <option value="WALLET">WALLET</option>
                </select>
                <button type="submit" className="btn btn-primary">Save Payment</button>
              </form>
            </div>
          </div>
        </div>

        <div className="col-lg-8">
          <div className="card app-card">
            <div className="card-body">
              <h5 className="mb-3">Order Payment Overview</h5>
              <p className="text-muted small">This screen uses order data because the backend payment listing endpoint is not stable.</p>
              <div className="table-responsive">
                <table className="table table-hover align-middle mb-0">
                  <thead>
                    <tr>
                      <th>Order ID</th>
                      <th>Net Total</th>
                      <th>Amount Paid</th>
                      <th>Method</th>
                      <th>Status</th>
                    </tr>
                  </thead>
                  <tbody>
                    {orders.length > 0 ? orders.map((order) => (
                      <tr key={order.orderId}>
                        <td>{order.orderId}</td>
                        <td>{order.netTotal ?? order.grandTotal}</td>
                        <td>{order.amountPaid ?? 0}</td>
                        <td>{order.paymentMethod}</td>
                        <td>{order.poStatus}</td>
                      </tr>
                    )) : (
                      <tr><td colSpan="5" className="text-center py-4">No orders available for payments</td></tr>
                    )}
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default PaymentPage;
