import { useCallback, useEffect, useState } from "react";
import { createCustomer, listCustomers } from "../api/customers";
import { getApiErrorMessage } from "../api/http";
import type { Customer } from "../types/customer";

type Props = {
  selectedCustomerId: number | null;
  onSelectCustomer: (id: number) => void;
};

export default function CustomersPage({ selectedCustomerId, onSelectCustomer }: Props) {
  const [customers, setCustomers] = useState<Customer[]>([]);
  const [fullName, setFullName] = useState("");
  const [email, setEmail] = useState("");

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const refresh = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await listCustomers();
      setCustomers(data);
    } catch (e) {
      setError(getApiErrorMessage(e));
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    void refresh();
  }, [refresh]);

  async function onCreate(e: React.FormEvent) {
    e.preventDefault(); 
    setError(null);

    try {
      const created = await createCustomer({ fullName, email });
      setFullName("");
      setEmail("");
      await refresh();
      onSelectCustomer(created.id); 
    } catch (e) {
      setError(getApiErrorMessage(e));
    }
  }

  return (
    <div className="card">
      <h2>Customers</h2>

      {error && <div style={{ color: "crimson", marginBottom: 8 }}>{error}</div>}

      <form onSubmit={onCreate} style={{ display: "flex", gap: 8, marginBottom: 12 }}>
        <input
          value={fullName}
          onChange={(e) => setFullName(e.target.value)}
          placeholder="Full name"
        />
        <input value={email} onChange={(e) => setEmail(e.target.value)} placeholder="Email" />
        <button type="submit" disabled={loading}>
          Create
        </button>
      </form>

      <ul style={{ listStyle: "disc", paddingLeft: 16 }}>
        {customers.map((c) => (
          <li key={c.id} style={{ marginBottom: 6 }}>
            <button
              type="button" 
              onClick={() => onSelectCustomer(c.id)}
              style={{
                border: "1px solid #ddd",
                padding: "6px 10px",
                borderRadius: 8,
                background: selectedCustomerId === c.id ? "#eef6ff" : "white",
                cursor: "pointer",
              }}
            >
              #{c.id} {c.fullName} ({c.email})
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
}
