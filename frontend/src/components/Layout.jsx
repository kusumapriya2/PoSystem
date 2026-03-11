import React from "react";
import Sidebar from "./Sidebar";

function Layout({ title, subtitle, buttonText, children }) {
  return (
    <div className="common-page">
      <div className="container-fluid">
        <div className="row">
          <Sidebar />

          <div className="col-md-10 main-content p-4">
            <div className="top-section d-flex justify-content-between align-items-center">
              <div>
                <h2>{title}</h2>
                <p className="text-muted mb-0">{subtitle}</p>
              </div>
              {buttonText && <button className="btn btn-primary">{buttonText}</button>}
            </div>

            <div className="mt-4">{children}</div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Layout;