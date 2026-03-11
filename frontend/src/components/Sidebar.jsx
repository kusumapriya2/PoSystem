import React from "react";
import { Link } from "react-router-dom";

function Sidebar() {
  return (
    <div className="col-md-2 sidebar p-3">
      <h3 className="text-white mb-4">PO System</h3>

      <ul className="nav flex-column">
        <li className="nav-item">
          <Link className="nav-link text-white" to="/">Home</Link>
        </li>
        <li className="nav-item">
          <Link className="nav-link text-white" to="/vendors">Vendors</Link>
        </li>
        <li className="nav-item">
          <Link className="nav-link text-white" to="/products">Products</Link>
        </li>
        <li className="nav-item">
          <Link className="nav-link text-white" to="/customers">Customers</Link>
        </li>
        <li className="nav-item">
          <Link className="nav-link text-white" to="/orders">Orders</Link>
        </li>
        <li className="nav-item">
          <Link className="nav-link text-white" to="/payments">Payments</Link>
        </li>
        <li className="nav-item">
          <Link className="nav-link text-white" to="/delivery">Delivery</Link>
        </li>
      </ul>
    </div>
  );
}

export default Sidebar;