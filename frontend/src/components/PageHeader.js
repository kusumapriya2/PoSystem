import React from "react";

function PageHeader({ title, subtitle }) {
  return (
    <div className="page-header-card mb-4">
      <h2 className="mb-1">{title}</h2>
      <p className="text-muted mb-0">{subtitle}</p>
    </div>
  );
}

export default PageHeader;
