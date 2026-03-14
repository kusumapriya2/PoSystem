import React, { useEffect, useMemo, useState } from "react";
import PageHeader from "../components/PageHeader";
import AlertMessage from "../components/AlertMessage";
import { getMessage, unwrap } from "../services/api";
import { getAllCustomers } from "../services/customerService";
import { getAllProducts } from "../services/productService";
import { createOrder, deleteOrder, getAllOrders } from "../services/orderService";
import { getAllVendors } from "../services/vendorService";

const today = new Date().toISOString().split("T")[0];
const nextWeek = new Date(Date.now() + 7 * 24 * 60 * 60 * 1000).toISOString().split("T")[0];

const initialForm = {
  customerId: "",
  vendorId: "",
  productId: "",
  quantity: 1,
  shippingPincode: "",
  shippingAddressLine: "",
  shippingCity: "",
  shippingState: "",
  paymentMethod: "COD",
  expectedDeliveryDate: nextWeek
};

function OrderPage() {
  const [orders, setOrders] = useState([]);
  const [customers, setCustomers] = useState([]);
  const [vendors, setVendors] = useState([]);
  const [products, setProducts] = useState([]);
  const [form, setForm] = useState(initialForm);
  const [message, setMessage] = useState("");
  const [type, setType] = useState("info");

  const loadData = async () => {
    try {
      const [ordersRes, customersRes, vendorsRes, productsRes] = await Promise.all([
        getAllOrders(),
        getAllCustomers(),
        getAllVendors(),
        getAllProducts()
      ]);
      setOrders(unwrap(ordersRes) || []);
      setCustomers(unwrap(customersRes) || []);
      setVendors(unwrap(vendorsRes) || []);
      setProducts(unwrap(productsRes) || []);
    } catch (err) {
      setType("danger");
      setMessage(getMessage(err, "Failed to load orders"));
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const filteredProducts = useMemo(() => {
    if (!form.vendorId) return products;
    return products.filter((product) => String(product.vendorId) === String(form.vendorId));
  }, [form.vendorId, products]);

  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      const selectedProduct = products.find((product) => String(product.productId) === String(form.productId));
      const unitPrice = Number(selectedProduct?.price || 0);
      const quantity = Number(form.quantity || 1);
      const subTotal = unitPrice * quantity;
      const taxAmount = Number((subTotal * 0.18).toFixed(2));
      const shippingCharge = subTotal >= 50000 ? 0 : 150;
      const grandTotal = Number((subTotal + taxAmount + shippingCharge).toFixed(2));

      const payload = {
        shippingPincode: Number(form.shippingPincode),
        shippingAddressLine: form.shippingAddressLine,
        shippingCity: form.shippingCity,
        shippingState: form.shippingState,
        paymentMethod: form.paymentMethod,
        expectedDeliveryDate: form.expectedDeliveryDate || today,
        customerId: Number(form.customerId),
        vendorId: Number(form.vendorId),
        subTotal,
        taxAmount,
        shippingCharge,
        discountAmount: 0,
        grandTotal,
        amountPaid: 0,
        items: [
          {
            productId: Number(form.productId),
            quantity,
            unitPrice,
            lineTotal: Number((unitPrice * quantity).toFixed(2))
          }
        ]
      };

      const response = await createOrder(payload);
      setType("success");
      setMessage(response.data.message || "Order created successfully");
      setForm(initialForm);
      loadData();
    } catch (err) {
      setType("danger");
      setMessage(getMessage(err, "Failed to create order"));
    }
  };

  const handleDelete = async (id) => {
    try {
      const response = await deleteOrder(id);
      setType("success");
      setMessage(response.data.message || "Order deleted successfully");
      loadData();
    } catch (err) {
      setType("danger");
      setMessage(getMessage(err, "Failed to delete order"));
    }
  };

  return (
    <div>
      <PageHeader title="Order Management" subtitle="Create purchase orders using customer, vendor, and product data" />
      <AlertMessage message={message} type={type} onClose={() => setMessage("")} />

      <div className="row g-4">
        <div className="col-lg-4">
          <div className="card app-card">
            <div className="card-body">
              <h5 className="mb-3">Create Order</h5>
              <form onSubmit={handleSubmit} className="d-grid gap-3">
                <select className="form-select" value={form.customerId} onChange={(e) => setForm({ ...form, customerId: e.target.value })} required>
                  <option value="">Select customer</option>
                  {customers.map((customer) => <option key={customer.customerId} value={customer.customerId}>{customer.customerName}</option>)}
                </select>
                <select className="form-select" value={form.vendorId} onChange={(e) => setForm({ ...form, vendorId: e.target.value, productId: "" })} required>
                  <option value="">Select vendor</option>
                  {vendors.map((vendor) => <option key={vendor.vendorId} value={vendor.vendorId}>{vendor.vendorName}</option>)}
                </select>
                <select className="form-select" value={form.productId} onChange={(e) => setForm({ ...form, productId: e.target.value })} required>
                  <option value="">Select product</option>
                  {filteredProducts.map((product) => <option key={product.productId} value={product.productId}>{product.productName}</option>)}
                </select>
                <input className="form-control" type="number" min="1" placeholder="Quantity" value={form.quantity} onChange={(e) => setForm({ ...form, quantity: e.target.value })} required />
                <input className="form-control" placeholder="Shipping pincode" value={form.shippingPincode} onChange={(e) => setForm({ ...form, shippingPincode: e.target.value })} required />
                <input className="form-control" placeholder="Address line" value={form.shippingAddressLine} onChange={(e) => setForm({ ...form, shippingAddressLine: e.target.value })} required />
                <input className="form-control" placeholder="City" value={form.shippingCity} onChange={(e) => setForm({ ...form, shippingCity: e.target.value })} required />
                <input className="form-control" placeholder="State" value={form.shippingState} onChange={(e) => setForm({ ...form, shippingState: e.target.value })} required />
                <select className="form-select" value={form.paymentMethod} onChange={(e) => setForm({ ...form, paymentMethod: e.target.value })}>
                  <option value="COD">COD</option>
                  <option value="UPI">UPI</option>
                  <option value="CREDITCARD">CREDITCARD</option>
                  <option value="DEBITCARD">DEBITCARD</option>
                  <option value="WALLET">WALLET</option>
                </select>
                <input className="form-control" type="date" min={today} value={form.expectedDeliveryDate} onChange={(e) => setForm({ ...form, expectedDeliveryDate: e.target.value })} required />
                <button type="submit" className="btn btn-primary">Save Order</button>
              </form>
            </div>
          </div>
        </div>

        <div className="col-lg-8">
          <div className="card app-card">
            <div className="card-body">
              <div className="d-flex justify-content-between align-items-center mb-3">
                <h5 className="mb-0">Order List</h5>
                <span className="badge text-bg-primary">{orders.length}</span>
              </div>
              <div className="table-responsive">
                <table className="table table-hover align-middle mb-0">
                  <thead>
                    <tr>
                      <th>ID</th>
                      <th>Customer</th>
                      <th>Vendor</th>
                      <th>Net Total</th>
                      <th>Payment</th>
                      <th>Status</th>
                      <th></th>
                    </tr>
                  </thead>
                  <tbody>
                    {orders.length > 0 ? orders.map((order) => (
                      <tr key={order.orderId}>
                        <td>{order.orderId}</td>
                        <td>{order.customerId}</td>
                        <td>{order.vendorId}</td>
                        <td>{order.netTotal ?? order.grandTotal}</td>
                        <td>{order.paymentMethod}</td>
                        <td>{order.poStatus}</td>
                        <td className="text-end"><button className="btn btn-sm btn-outline-danger" onClick={() => handleDelete(order.orderId)}>Delete</button></td>
                      </tr>
                    )) : (
                      <tr><td colSpan="7" className="text-center py-4">No orders available</td></tr>
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

export default OrderPage;
