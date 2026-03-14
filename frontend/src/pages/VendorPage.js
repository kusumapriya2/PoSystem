import React, { useEffect, useState } from "react";
import PageHeader from "../components/PageHeader";
import AlertMessage from "../components/AlertMessage";
import { createVendor, deleteVendor, getAllVendors } from "../services/vendorService";
import { getMessage, unwrap } from "../services/api";

const initialForm = {
  vendorName: "",
  vendorEmail: "",
  gstNo: "",
  vendorPhone: ""
};

function VendorPage() {
  const [vendors, setVendors] = useState([]);
  const [form, setForm] = useState(initialForm);
  const [message, setMessage] = useState("");
  const [type, setType] = useState("info");

  const loadVendors = async () => {
    try {
      const response = await getAllVendors();
      setVendors(unwrap(response) || []);
    } catch (err) {
      setType("danger");
      setMessage(getMessage(err, "Failed to load vendors"));
    }
  };

  useEffect(() => {
    loadVendors();
  }, []);

  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      const response = await createVendor(form);
      setType("success");
      setMessage(response.data.message || "Vendor created successfully");
      setForm(initialForm);
      loadVendors();
    } catch (err) {
      setType("danger");
      setMessage(getMessage(err, "Failed to create vendor"));
    }
  };

  const handleDelete = async (id) => {
    try {
      const response = await deleteVendor(id);
      setType("success");
      setMessage(response.data.message || "Vendor deleted successfully");
      loadVendors();
    } catch (err) {
      setType("danger");
      setMessage(getMessage(err, "Failed to delete vendor"));
    }
  };

  return (
    <div>
      <PageHeader title="Vendor Management" subtitle="Create and manage vendors" />
      <AlertMessage message={message} type={type} onClose={() => setMessage("")} />

      <div className="row g-4">
        <div className="col-lg-4">
          <div className="card app-card">
            <div className="card-body">
              <h5 className="mb-3">Add Vendor</h5>
              <form onSubmit={handleSubmit} className="d-grid gap-3">
                <input className="form-control" placeholder="Vendor name" value={form.vendorName} onChange={(e) => setForm({ ...form, vendorName: e.target.value })} required />
                <input className="form-control" type="email" placeholder="Vendor email" value={form.vendorEmail} onChange={(e) => setForm({ ...form, vendorEmail: e.target.value })} required />
                <input className="form-control" placeholder="GST number" value={form.gstNo} onChange={(e) => setForm({ ...form, gstNo: e.target.value })} required />
                <input className="form-control" placeholder="Phone number" value={form.vendorPhone} onChange={(e) => setForm({ ...form, vendorPhone: e.target.value })} required />
                <button type="submit" className="btn btn-primary">Save Vendor</button>
              </form>
            </div>
          </div>
        </div>

        <div className="col-lg-8">
          <div className="card app-card">
            <div className="card-body">
              <div className="d-flex justify-content-between align-items-center mb-3">
                <h5 className="mb-0">Vendor List</h5>
                <span className="badge text-bg-primary">{vendors.length}</span>
              </div>
              <div className="table-responsive">
                <table className="table table-hover align-middle mb-0">
                  <thead>
                    <tr>
                      <th>ID</th>
                      <th>Name</th>
                      <th>Email</th>
                      <th>GST</th>
                      <th>Phone</th>
                      <th></th>
                    </tr>
                  </thead>
                  <tbody>
                    {vendors.length > 0 ? vendors.map((vendor) => (
                      <tr key={vendor.vendorId}>
                        <td>{vendor.vendorId}</td>
                        <td>{vendor.vendorName}</td>
                        <td>{vendor.vendorEmail}</td>
                        <td>{vendor.gstNo}</td>
                        <td>{vendor.vendorPhone}</td>
                        <td className="text-end">
                          <button className="btn btn-sm btn-outline-danger" onClick={() => handleDelete(vendor.vendorId)}>Delete</button>
                        </td>
                      </tr>
                    )) : (
                      <tr><td colSpan="6" className="text-center py-4">No vendors available</td></tr>
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

export default VendorPage;
