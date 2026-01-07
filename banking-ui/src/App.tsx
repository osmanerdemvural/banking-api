import { useState } from "react";
import CustomersPage from "./components/CustomersPage";
import AccountsPage from "./components/AccountsPage";
import AccountDetailPage from "./components/AccountDetailPage";
import "./App.css";

export default function App() {
  const [selectedCustomerId, setSelectedCustomerId] = useState<number | null>(null);
  const [selectedAccountId, setSelectedAccountId] = useState<number | null>(null);

  function handleSelectCustomer(id: number) {
    setSelectedCustomerId(id);
    setSelectedAccountId(null); 
  }

  return (
    <div style={{ padding: 24 }}>
      <h1 style={{ textAlign: "center" }}>Banking UI</h1>

      <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr 1fr", gap: 16, marginTop: 16 }}>
        <CustomersPage
          selectedCustomerId={selectedCustomerId}
          onSelectCustomer={handleSelectCustomer}
        />

        <AccountsPage
          customerId={selectedCustomerId}
          selectedAccountId={selectedAccountId}
          onSelectAccount={setSelectedAccountId}
        />

        <AccountDetailPage accountId={selectedAccountId} />
      </div>
    </div>
  );
}
