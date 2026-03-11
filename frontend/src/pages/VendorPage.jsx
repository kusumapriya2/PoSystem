import React, { useEffect, useState } from "react";
import Layout from "../components/Layout";
import { getAllVendors, saveVendor, deleteVendor } from "../services/vendorService";

function VendorPage() {
  const [vendors, setVendors] = useState([]);
  const [message, setMessage] = useState("");

  const [vendorData, setVendorData] = useState({
    vendorName: "",
    email: "",
    phone: "",
    address: ""
  });

  const loadVendors = async () => {
    try {
      const response = await getAllVendors();
      setVendors(response.data.data || []);
    } catch (error) {
      console.error("Error fetching vendors:", error);
      setMessage("Failed to load vendors");
    }
  };

  useEffect(() => {
    loadVendors();
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setVendorData({
      ...vendorData,
      [name]: value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await saveVendor(vendorData);
      setMessage(response.data.message || "Vendor saved successfully");

      setVendorData({
        vendorName: "",
        email: "",
        phone: "",
        address: ""
      });

      loadVendors();
    } catch (error) {
      console.error("Error saving vendor:", error);
      setMessage("Failed to save vendor");
    }
  };

  const handleDelete = async (vendorId) => {
    try {
      await deleteVendor(vendorId);
      setMessage("Vendor deleted successfully");
      loadVendors();
    } catch (error) {
      console.error("Error deleting vendor:", error);
      setMessage("Failed to delete vendor");
    }
  };

  return (
    <Layout
      title="Vendor Management"
      subtitle="Add and manage vendor details for purchase orders"
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
                    value={vendorData.vendorName}
                    onChange={handleChange}
                  />
                </div>

                <div className="mb-3">
                  <label className="form-label">Email</label>
                  <input
                    type="email"
                    name="email"
                    className="form-control"
                    placeholder="Enter vendor email"
                    value={vendorData.email}
                    onChange={handleChange}
                  />
                </div>

                <div className="mb-3">
                  <label className="form-label">Phone</label>
                  <input
                    type="text"
                    name="phone"
                    className="form-control"
                    placeholder="Enter vendor phone"
                    value={vendorData.phone}
                    onChange={handleChange}
                  />
                </div>

                <div className="mb-3">
                  <label className="form-label">Address</label>
                  <textarea
                    name="address"
                    className="form-control"
                    rows="3"
                    placeholder="Enter vendor address"
                    value={vendorData.address}
                    onChange={handleChange}
                  ></textarea>
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
                    <th>ID</th>
                    <th>Vendor Name</th>
                    <th>Email</th>
                    <th>Phone</th>
                    <th>Address</th>
                    <th>Action</th>
                  </tr>
                </thead>
                <tbody>
                  {vendors.length > 0 ? (
                    vendors.map((vendor) => (
                      <tr key={vendor.vendorId}>
                        <td>{vendor.vendorId}</td>
                        <td>{vendor.vendorName}</td>
                        <td>{vendor.email}</td>
                        <td>{vendor.phone}</td>
                        <td>{vendor.address}</td>
                        <td>
                          <button
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
                    <h6>Active Vendors</h6>
                    <h3>{vendors.length}</h3>
                  </div>
                </div>

                <div className="col-md-4">
                  <div className="summary-box">
                    <h6>Latest Added</h6>
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