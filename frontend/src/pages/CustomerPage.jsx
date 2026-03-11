import React from "react";
import Layout from "../components/Layout";

function ProductPage() {
  return (
    <Layout
      title="Product Management"
      subtitle="Add and manage products, pricing, and stock details"
      buttonText="Add Product"
    >
      <div className="row">
        <div className="col-md-4">
          <div className="card shadow-sm">
            <div className="card-body">
              <h4 className="mb-3">Product Form</h4>

              <form>
                <div className="mb-3">
                  <label className="form-label">Product Name</label>
                  <input
                    type="text"
                    className="form-control"
                    placeholder="Enter product name"
                  />
                </div>

                <div className="mb-3">
                  <label className="form-label">Category</label>
                  <select className="form-select">
                    <option>Select category</option>
                    <option>Electronics</option>
                    <option>Office Supplies</option>
                    <option>Furniture</option>
                    <option>Hardware</option>
                  </select>
                </div>

                <div className="mb-3">
                  <label className="form-label">Price</label>
                  <input
                    type="text"
                    className="form-control"
                    placeholder="Enter product price"
                  />
                </div>

                <div className="mb-3">
                  <label className="form-label">Stock Quantity</label>
                  <input
                    type="text"
                    className="form-control"
                    placeholder="Enter stock quantity"
                  />
                </div>

                <div className="mb-3">
                  <label className="form-label">Description</label>
                  <textarea
                    className="form-control"
                    rows="3"
                    placeholder="Enter product description"
                  ></textarea>
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
                    <th>Category</th>
                    <th>Price</th>
                    <th>Stock</th>
                    <th>Status</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td>P101</td>
                    <td>Laptop</td>
                    <td>Electronics</td>
                    <td>₹55,000</td>
                    <td>15</td>
                    <td>
                      <span className="badge bg-success">Available</span>
                    </td>
                  </tr>
                  <tr>
                    <td>P102</td>
                    <td>Printer</td>
                    <td>Electronics</td>
                    <td>₹12,000</td>
                    <td>4</td>
                    <td>
                      <span className="badge bg-warning text-dark">Low Stock</span>
                    </td>
                  </tr>
                  <tr>
                    <td>P103</td>
                    <td>Office Chair</td>
                    <td>Furniture</td>
                    <td>₹6,500</td>
                    <td>10</td>
                    <td>
                      <span className="badge bg-success">Available</span>
                    </td>
                  </tr>
                  <tr>
                    <td>P104</td>
                    <td>Mouse</td>
                    <td>Electronics</td>
                    <td>₹700</td>
                    <td>0</td>
                    <td>
                      <span className="badge bg-danger">Out of Stock</span>
                    </td>
                  </tr>
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
                    <h3>48</h3>
                  </div>
                </div>

                <div className="col-md-4">
                  <div className="summary-box">
                    <h6>Available</h6>
                    <h3>39</h3>
                  </div>
                </div>

                <div className="col-md-4">
                  <div className="summary-box">
                    <h6>Low Stock</h6>
                    <h3>9</h3>
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