import React, { useEffect, useState } from "react";
import Layout from "../components/Layout";
import {
  getAllVendors,
  createVendor,
  deleteVendor
} from "../services/vendorService";

function VendorPage() {
  const [vendors, setVendors] = useState([]);
  const [message, setMessage] = useState("");

  const [formData, setFormData] = useState({
    vendorName: "",
    vendorEmail: "",
    gstNo: "",
    vendorPhone: ""
  });

  const loadVendors = async () => {
    try {
      const response = await getAllVendors();
      setVendors(response.data.data || []);
    } catch (error) {
      console.error("Error loading vendors:", error);
      setMessage("Failed to load vendors");
    }
  };

  useEffect(() => {
    loadVendors();
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;

    setFormData({
      ...formData,
      [name]: value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await createVendor(formData);
      setMessage(response.data.message || "Vendor created successfully");

      setFormData({
        vendorName: "",
        vendorEmail: "",
        gstNo: "",
        vendorPhone: ""
      });

      loadVendors();
    } catch (error) {
      console.error("Error creating vendor:", error);
      setMessage(
        error?.response?.data?.message || "Failed to create vendor"
      );
    }
  };

  const handleDelete = async (vendorId) => {
    try {
      const response = await deleteVendor(vendorId);
      setMessage(response.data.message || "Vendor deleted successfully");
      loadVendors();
    } catch (error) {
      console.error("Error deleting vendor:", error);
      setMessage(
        error?.response?.data?.message || "Failed to delete vendor"
      );
    }
  };

  return (
    <Layout
      title="Vendor Management"
      subtitle="Add and manage vendor details"
      buttonText="Add Vendor"
    >
      {message && (
        <div className="alert alert-info mb-3">
          {message}
        </div>
      )}

      <div className="row">
        <div className="col-md-4">
          <div className="card shadow-sm">
            <div className="card-body">
              <h4 className="mb-3">Vendor Form</h4>

              <form onSubmit={handleSubmit}>
                <div className="mb-3">
                  <label className="form-label">Vendor Name</label>
                  <input
                    type="text"
                    name="vendorName"
                    className="form-control"
                    placeholder="Enter vendor name"
                    value={formData.vendorName}
                    onChange={handleChange}
                  />
                </div>

                <div className="mb-3">
                  <label className="form-label">Vendor Email</label>
                  <input
                    type="email"
                    name="vendorEmail"
                    className="form-control"
                    placeholder="Enter vendor email"
                    value={formData.vendorEmail}
                    onChange={handleChange}
                  />
                </div>

                <div className="mb-3">
                  <label className="form-label">GST Number</label>
                  <input
                    type="text"
                    name="gstNo"
                    className="form-control"
                    placeholder="Enter GST number"
                    value={formData.gstNo}
                    onChange={handleChange}
                  />
                </div>

                <div className="mb-3">
                  <label className="form-label">Vendor Phone</label>
                  <input
                    type="text"
                    name="vendorPhone"
                    className="form-control"
                    placeholder="Enter vendor phone"
                    value={formData.vendorPhone}
                    onChange={handleChange}
                  />
                </div>

                <button type="submit" className="btn btn-success w-100">
                  Save Vendor
                </button>
              </form>
            </div>
          </div>
        </div>

        <div className="col-md-8">
          <div className="card shadow-sm">
            <div className="card-body">
              <h4 className="mb-3">Vendor List</h4>

              <table className="table table-bordered table-hover">
                <thead className="table-dark">
                  <tr>
                    <th>Vendor ID</th>
                    <th>Vendor Name</th>
                    <th>Vendor Email</th>
                    <th>GST No</th>
                    <th>Vendor Phone</th>
                    <th>Action</th>
                  </tr>
                </thead>
                <tbody>
                  {vendors.length > 0 ? (
                    vendors.map((vendor) => (
                      <tr key={vendor.vendorId}>
                        <td>{vendor.vendorId}</td>
                        <td>{vendor.vendorName}</td>
                        <td>{vendor.vendorEmail}</td>
                        <td>{vendor.gstNo}</td>
                        <td>{vendor.vendorPhone}</td>
                        <td>
                          <button
                            type="button"
                            className="btn btn-danger btn-sm"
                            onClick={() => handleDelete(vendor.vendorId)}
                          >
                            Delete
                          </button>
                        </td>
                      </tr>
                    ))
                  ) : (
                    <tr>
                      <td colSpan="6" className="text-center">
                        No vendors found
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>
          </div>

          <div className="card shadow-sm mt-4">
            <div className="card-body">
              <h4 className="mb-3">Vendor Summary</h4>

              <div className="row">
                <div className="col-md-4">
                  <div className="summary-box">
                    <h6>Total Vendors</h6>
                    <h3>{vendors.length}</h3>
                  </div>
                </div>

                <div className="col-md-4">
                  <div className="summary-box">
                    <h6>Visible Vendors</h6>
                    <h3>{vendors.length}</h3>
                  </div>
                </div>

                <div className="col-md-4">
                  <div className="summary-box">
                    <h6>Latest Session Add</h6>
                    <h3>{vendors.length > 0 ? 1 : 0}</h3>
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

export default VendorPage;