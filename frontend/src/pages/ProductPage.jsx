import React, { useEffect, useState } from "react";
import Layout from "../components/Layout";
import {
  getAllProducts,
  createProduct,
  deleteProduct
} from "../services/productService";
import { getAllVendors } from "../services/vendorService";

function ProductPage() {
  const [products, setProducts] = useState([]);
  const [vendors, setVendors] = useState([]);
  const [message, setMessage] = useState("");

  const [formData, setFormData] = useState({
    productName: "",
    productType: "",
    productStock: "",
    productStatus: "AVAILABLE",
    vendorId: "",
    price: ""
  });

  const loadProducts = async () => {
    try {
      const response = await getAllProducts();
      setProducts(response.data.data || []);
    } catch (error) {
      console.error("Error loading products:", error);
      setMessage("Failed to load products");
    }
  };

  const loadVendors = async () => {
    try {
      const response = await getAllVendors();
      setVendors(response.data.data || []);
    } catch (error) {
      console.error("Error loading vendors:", error);
    }
  };

  useEffect(() => {
    loadProducts();
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

    const payload = {
      ...formData,
      productStock: Number(formData.productStock),
      vendorId: Number(formData.vendorId),
      price: Number(formData.price)
    };

    try {
      const response = await createProduct(payload);
      setMessage(response.data.message || "Product created successfully");

      setFormData({
        productName: "",
        productType: "",
        productStock: "",
        productStatus: "AVAILABLE",
        vendorId: "",
        price: ""
      });

      loadProducts();
    } catch (error) {
      console.error("Error creating product:", error);
      setMessage(error?.response?.data?.message || "Failed to create product");
    }
  };

  const handleDelete = async (productId) => {
    try {
      const response = await deleteProduct(productId);
      setMessage(response.data.message || "Product deleted successfully");
      loadProducts();
    } catch (error) {
      console.error("Error deleting product:", error);
      setMessage(error?.response?.data?.message || "Failed to delete product");
    }
  };

  return (
    <Layout
      title="Product Management"
      subtitle="Add and manage products, pricing, stock, and vendor mapping"
      buttonText="Add Product"
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
              <h4 className="mb-3">Product Form</h4>

              <form onSubmit={handleSubmit}>
                <div className="mb-3">
                  <label className="form-label">Product Name</label>
                  <input
                    type="text"
                    name="productName"
                    className="form-control"
                    placeholder="Enter product name"
                    value={formData.productName}
                    onChange={handleChange}
                  />
                </div>

                <div className="mb-3">
                  <label className="form-label">Product Type</label>
                  <input
                    type="text"
                    name="productType"
                    className="form-control"
                    placeholder="Enter product type"
                    value={formData.productType}
                    onChange={handleChange}
                  />
                </div>

                <div className="mb-3">
                  <label className="form-label">Product Stock</label>
                  <input
                    type="number"
                    name="productStock"
                    className="form-control"
                    placeholder="Enter stock"
                    value={formData.productStock}
                    onChange={handleChange}
                  />
                </div>

                <div className="mb-3">
                  <label className="form-label">Product Status</label>
                  <select
                    name="productStatus"
                    className="form-select"
                    value={formData.productStatus}
                    onChange={handleChange}
                  >
                    <option value="AVAILABLE">AVAILABLE</option>
                    <option value="OUT_OF_STOCK">OUT_OF_STOCK</option>
                    <option value="DISCONTINUED">DISCONTINUED</option>
                  </select>
                </div>

                <div className="mb-3">
                  <label className="form-label">Vendor</label>
                  <select
                    name="vendorId"
                    className="form-select"
                    value={formData.vendorId}
                    onChange={handleChange}
                  >
                    <option value="">Select vendor</option>
                    {vendors.map((vendor) => (
                      <option key={vendor.vendorId} value={vendor.vendorId}>
                        {vendor.vendorName}
                      </option>
                    ))}
                  </select>
                </div>

                <div className="mb-3">
                  <label className="form-label">Price</label>
                  <input
                    type="number"
                    name="price"
                    className="form-control"
                    placeholder="Enter price"
                    value={formData.price}
                    onChange={handleChange}
                  />
                </div>

                <button type="submit" className="btn btn-success w-100">
                  Save Product
                </button>
              </form>
            </div>
          </div>
        </div>

        <div className="col-md-8">
          <div className="card shadow-sm">
            <div className="card-body">
              <h4 className="mb-3">Product List</h4>

              <table className="table table-bordered table-hover">
                <thead className="table-dark">
                  <tr>
                    <th>Product ID</th>
                    <th>Name</th>
                    <th>Type</th>
                    <th>Stock</th>
                    <th>Status</th>
                    <th>Vendor ID</th>
                    <th>Price</th>
                    <th>Action</th>
                  </tr>
                </thead>
                <tbody>
                  {products.length > 0 ? (
                    products.map((product) => (
                      <tr key={product.productId}>
                        <td>{product.productId}</td>
                        <td>{product.productName}</td>
                        <td>{product.productType}</td>
                        <td>{product.productStock}</td>
                        <td>{product.productStatus}</td>
                        <td>{product.vendorId}</td>
                        <td>{product.price}</td>
                        <td>
                          <button
                            type="button"
                            className="btn btn-danger btn-sm"
                            onClick={() => handleDelete(product.productId)}
                          >
                            Delete
                          </button>
                        </td>
                      </tr>
                    ))
                  ) : (
                    <tr>
                      <td colSpan="8" className="text-center">
                        No products found
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>
          </div>

          <div className="card shadow-sm mt-4">
            <div className="card-body">
              <h4 className="mb-3">Product Summary</h4>

              <div className="row">
                <div className="col-md-4">
                  <div className="summary-box">
                    <h6>Total Products</h6>
                    <h3>{products.length}</h3>
                  </div>
                </div>

                <div className="col-md-4">
                  <div className="summary-box">
                    <h6>Available</h6>
                    <h3>
                      {
                        products.filter(
                          (product) => product.productStatus === "AVAILABLE"
                        ).length
                      }
                    </h3>
                  </div>
                </div>

                <div className="col-md-4">
                  <div className="summary-box">
                    <h6>Out Of Stock</h6>
                    <h3>
                      {
                        products.filter(
                          (product) => product.productStatus === "OUT_OF_STOCK"
                        ).length
                      }
                    </h3>
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

export default ProductPage;