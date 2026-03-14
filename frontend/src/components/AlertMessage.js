import React from "react";

function AlertMessage({ message, type = "info", onClose }) {
  if (!message) return null;

  return (
    <div className={`alert alert-${type} d-flex justify-content-between align-items-start`}>
      <span>{message}</span>
      {onClose ? (
        <button type="button" className="btn-close" onClick={onClose} aria-label="Close"></button>
      ) : null}
    </div>
  );
}

export default AlertMessage;
