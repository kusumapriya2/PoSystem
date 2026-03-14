import React, { useEffect, useState } from "react";
import PageHeader from "../components/PageHeader";
import AlertMessage from "../components/AlertMessage";
import { getMessage, unwrap } from "../services/api";
import { createProduct, deleteProduct, getAllProducts } from "../services/productService";
import { getAllVendors } from "../services/vendorService";

const initialForm = {
  productName: "",
  productType: "",
  productStock: "",
  productStatus: "ACTIVE",
  vendorId: "",
  price: ""
};

function ProductPage() {
  const [products, setProducts] = useState([]);
  const [vendors, setVendors] = useState([]);
  const [form, setForm] = useState(initialForm);
  const [message, setMessage] = useState("");
  const [type, setType] = useState("info");

  const loadData = async () => {
    try {
      const [productsResponse, vendorsResponse] = await Promise.all([getAllProducts(), getAllVendors()]);
      setProducts(unwrap(productsResponse) || []);
      setVendors(unwrap(vendorsResponse) || []);
    } catch (err) {
      setType("danger");
      setMessage(getMessage(err, "Failed to load products"));
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      const payload = {
        ...form,
        productStock: Number(form.productStock),
        vendorId: Number(form.vendorId),
        price: Number(form.price)
      };
      const response = await createProduct(payload);
      setType("success");
      setMessage(response.data.message || "Product created successfully");
      setForm(initialForm);
      loadData();
    } catch (err) {
      setType("danger");
      setMessage(getMessage(err, "Failed to create product"));
    }
  };

  const handleDelete = async (id) => {
    try {
      const response = await deleteProduct(id);
      setType("success");
      setMessage(response.data.message || "Product deleted successfully");
      loadData();
    } catch (err) {
      setType("danger");
      setMessage(getMessage(err, "Failed to delete product"));
    }
  };

  return (
    <div>
      <PageHeader title="Product Management" subtitle="Map products to vendors and keep stock updated" />
      <AlertMessage message={message} type={type} onClose={() => setMessage("")} />

      <div className="row g-4">
        <div className="col-lg-4">
          <div className="card app-card">
            <div className="card-body">
              <h5 className="mb-3">Add Product</h5>
              <form onSubmit={handleSubmit} className="d-grid gap-3">
                <input className="form-control" placeholder="Product name" value={form.productName} onChange={(e) => setForm({ ...form, productName: e.target.value })} required />
                <input className="form-control" placeholder="Product type" value={form.productType} onChange={(e) => setForm({ ...form, productType: e.target.value })} required />
                <input className="form-control" type="number" placeholder="Stock" value={form.productStock} onChange={(e) => setForm({ ...form, productStock: e.target.value })} required />
                <select className="form-select" value={form.productStatus} onChange={(e) => setForm({ ...form, productStatus: e.target.value })}>
                  <option value="ACTIVE">ACTIVE</option>
                  <option value="INACTIVE">INACTIVE</option>
                </select>
                <select className="form-select" value={form.vendorId} onChange={(e) => setForm({ ...form, vendorId: e.target.value })} required>
                  <option value="">Select vendor</option>
                  {vendors.map((vendor) => <option key={vendor.vendorId} value={vendor.vendorId}>{vendor.vendorName}</option>)}
                </select>
                <input className="form-control" type="number" placeholder="Price" value={form.price} onChange={(e) => setForm({ ...form, price: e.target.value })} required />
                <button type="submit" className="btn btn-primary">Save Product</button>
              </form>
            </div>
          </div>
        </div>

        <div className="col-lg-8">
          <div className="card app-card">
            <div className="card-body">
              <div className="d-flex justify-content-between align-items-center mb-3">
                <h5 className="mb-0">Product List</h5>
                <span className="badge text-bg-primary">{products.length}</span>
              </div>
              <div className="table-responsive">
                <table className="table table-hover align-middle mb-0">
                  <thead>
                    <tr>
                      <th>ID</th>
                      <th>Name</th>
                      <th>Type</th>
                      <th>Stock</th>
                      <th>Status</th>
                      <th>Vendor</th>
                      <th>Price</th>
                      <th></th>
                    </tr>
                  </thead>
                  <tbody>
                    {products.length > 0 ? products.map((product) => (
                      <tr key={product.productId}>
                        <td>{product.productId}</td>
                        <td>{product.productName}</td>
                        <td>{product.productType}</td>
                        <td>{product.productStock}</td>
                        <td>{product.productStatus}</td>
                        <td>{product.vendorId}</td>
                        <td>{product.price}</td>
                        <td className="text-end">
                          <button className="btn btn-sm btn-outline-danger" onClick={() => handleDelete(product.productId)}>Delete</button>
                        </td>
                      </tr>
                    )) : (
                      <tr><td colSpan="8" className="text-center py-4">No products available</td></tr>
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

export default ProductPage;
