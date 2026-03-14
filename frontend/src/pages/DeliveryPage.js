import React, { useEffect, useState } from "react";
import PageHeader from "../components/PageHeader";
import AlertMessage from "../components/AlertMessage";
import { getMessage, unwrap } from "../services/api";
import { getAllDeliveries, markDelivered, moveToTransit, shipDelivery } from "../services/deliveryService";

function DeliveryPage() {
  const [deliveries, setDeliveries] = useState([]);
  const [message, setMessage] = useState("");
  const [type, setType] = useState("info");

  const loadDeliveries = async () => {
    try {
      const response = await getAllDeliveries();
      setDeliveries(unwrap(response) || []);
    } catch (err) {
      setType("danger");
      setMessage(getMessage(err, "Failed to load deliveries"));
    }
  };

  useEffect(() => {
    loadDeliveries();
  }, []);

  const runAction = async (action, orderId) => {
    try {
      const payload = { etaDate: new Date(Date.now() + 3 * 24 * 60 * 60 * 1000).toISOString().split("T")[0] };
      const response = await action(orderId, payload);
      setType("success");
      setMessage(response.data.message || "Delivery updated successfully");
      loadDeliveries();
    } catch (err) {
      setType("danger");
      setMessage(getMessage(err, "Failed to update delivery"));
    }
  };

  return (
    <div>
      <PageHeader title="Delivery Management" subtitle="Track assigned deliveries and move them through shipping states" />
      <AlertMessage message={message} type={type} onClose={() => setMessage("")} />

      <div className="card app-card">
        <div className="card-body">
          <div className="d-flex justify-content-between align-items-center mb-3">
            <h5 className="mb-0">Delivery List</h5>
            <span className="badge text-bg-primary">{deliveries.length}</span>
          </div>

          <div className="table-responsive">
            <table className="table table-hover align-middle mb-0">
              <thead>
                <tr>
                  <th>Delivery ID</th>
                  <th>Order ID</th>
                  <th>Courier ID</th>
                  <th>Courier Name</th>
                  <th>Tracking</th>
                  <th>Status</th>
                  <th>ETA</th>
                  <th className="text-end">Actions</th>
                </tr>
              </thead>
              <tbody>
                {deliveries.length > 0 ? deliveries.map((delivery) => (
                  <tr key={delivery.deliveryId}>
                    <td>{delivery.deliveryId}</td>
                    <td>{delivery.orderId}</td>
                    <td>{delivery.courierId}</td>
                    <td>{delivery.courierName}</td>
                    <td>{delivery.trackingNumber}</td>
                    <td>{delivery.deliveryStatus}</td>
                    <td>{delivery.etaDate}</td>
                    <td className="text-end d-flex gap-2 justify-content-end flex-wrap">
                      <button className="btn btn-sm btn-outline-primary" onClick={() => runAction(shipDelivery, delivery.orderId)}>Ship</button>
                      <button className="btn btn-sm btn-outline-warning" onClick={() => runAction(moveToTransit, delivery.orderId)}>Transit</button>
                      <button className="btn btn-sm btn-outline-success" onClick={() => runAction(markDelivered, delivery.orderId)}>Deliver</button>
                    </td>
                  </tr>
                )) : (
                  <tr><td colSpan="8" className="text-center py-4">No deliveries available. Approve an order first in the backend flow.</td></tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
}

export default DeliveryPage;
