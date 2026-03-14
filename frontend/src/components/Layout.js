import React from "react";
import { NavLink } from "react-router-dom";

const navItems = [
  { path: "/", label: "Dashboard" },
  { path: "/vendors", label: "Vendors" },
  { path: "/products", label: "Products" },
  { path: "/customers", label: "Customers" },
  { path: "/orders", label: "Orders" },
  { path: "/payments", label: "Payments" },
  { path: "/deliveries", label: "Deliveries" }
];

function Layout({ children }) {
  return (
    <div className="app-shell">
      <aside className="sidebar-panel">
        <div className="brand-block">
          <div className="brand-badge">PO</div>
          <div>
            <h1 className="brand-title">PO System</h1>
            <p className="brand-subtitle">Frontend for your Spring Boot backend</p>
          </div>
        </div>

        <nav className="nav flex-column gap-2 mt-4">
          {navItems.map((item) => (
            <NavLink
              key={item.path}
              to={item.path}
              end={item.path === "/"}
              className={({ isActive }) =>
                `nav-link sidebar-link ${isActive ? "active" : ""}`
              }
            >
              {item.label}
            </NavLink>
          ))}
        </nav>
      </aside>

      <main className="content-panel">
        <div className="content-inner">{children}</div>
      </main>
    </div>
  );
}

export default Layout;
