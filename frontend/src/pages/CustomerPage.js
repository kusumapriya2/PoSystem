import React, { useEffect, useState } from "react";
import PageHeader from "../components/PageHeader";
import AlertMessage from "../components/AlertMessage";
import { createCustomer, deleteCustomer, getAllCustomers } from "../services/customerService";
import { getMessage, unwrap } from "../services/api";

const initialForm = {
  customerName: "",
  email: "",
  customerPhone: "",
  customerAddress: ""
};

function CustomerPage() {
  const [customers, setCustomers] = useState([]);
  const [form, setForm] = useState(initialForm);
  const [message, setMessage] = useState("");
  const [type, setType] = useState("info");

  const loadCustomers = async () => {
    try {
      const response = await getAllCustomers();
      setCustomers(unwrap(response) || []);
    } catch (err) {
      setType("danger");
      setMessage(getMessage(err, "Failed to load customers"));
    }
  };

  useEffect(() => {
    loadCustomers();
  }, []);

  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      const response = await createCustomer(form);
      setType("success");
      setMessage(response.data.message || "Customer created successfully");
      setForm(initialForm);
      loadCustomers();
    } catch (err) {
      setType("danger");
      setMessage(getMessage(err, "Failed to create customer"));
    }
  };

  const handleDelete = async (id) => {
    try {
      const response = await deleteCustomer(id);
      setType("success");
      setMessage(response.data.message || "Customer deleted successfully");
      loadCustomers();
    } catch (err) {
      setType("danger");
      setMessage(getMessage(err, "Failed to delete customer"));
    }
  };

  return (
    <div>
      <PageHeader title="Customer Management" subtitle="Create customers before placing orders" />
      <AlertMessage message={message} type={type} onClose={() => setMessage("")} />

      <div className="row g-4">
        <div className="col-lg-4">
          <div className="card app-card">
            <div className="card-body">
              <h5 className="mb-3">Add Customer</h5>
              <form onSubmit={handleSubmit} className="d-grid gap-3">
                <input className="form-control" placeholder="Customer name" value={form.customerName} onChange={(e) => setForm({ ...form, customerName: e.target.value })} required />
                <input className="form-control" type="email" placeholder="Email" value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} required />
                <input className="form-control" placeholder="Phone" value={form.customerPhone} onChange={(e) => setForm({ ...form, customerPhone: e.target.value })} required />
                <textarea className="form-control" rows="3" placeholder="Customer address" value={form.customerAddress} onChange={(e) => setForm({ ...form, customerAddress: e.target.value })}></textarea>
                <button type="submit" className="btn btn-primary">Save Customer</button>
              </form>
            </div>
          </div>
        </div>

        <div className="col-lg-8">
          <div className="card app-card">
            <div className="card-body">
              <div className="d-flex justify-content-between align-items-center mb-3">
                <h5 className="mb-0">Customer List</h5>
                <span className="badge text-bg-primary">{customers.length}</span>
              </div>
              <div className="table-responsive">
                <table className="table table-hover align-middle mb-0">
                  <thead>
                    <tr>
                      <th>ID</th>
                      <th>Name</th>
                      <th>Email</th>
                      <th>Phone</th>
                      <th>Address</th>
                      <th></th>
                    </tr>
                  </thead>
                  <tbody>
                    {customers.length > 0 ? customers.map((customer) => (
                      <tr key={customer.customerId}>
                        <td>{customer.customerId}</td>
                        <td>{customer.customerName}</td>
                        <td>{customer.email}</td>
                        <td>{customer.customerPhone}</td>
                        <td>{customer.customerAddress}</td>
                        <td className="text-end"><button className="btn btn-sm btn-outline-danger" onClick={() => handleDelete(customer.customerId)}>Delete</button></td>
                      </tr>
                    )) : (
                      <tr><td colSpan="6" className="text-center py-4">No customers available</td></tr>
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

export default CustomerPage;
