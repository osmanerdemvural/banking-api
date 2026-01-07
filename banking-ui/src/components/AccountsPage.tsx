// banking-ui/src/components/AccountsPage.tsx
import { useCallback, useEffect, useState } from "react";
import { createAccount, listAccounts } from "../api/accounts";
import { getApiErrorMessage } from "../api/http";
import type { Account, AccountType } from "../types/account";

type Props = {
  customerId: number | null;
  selectedAccountId: number | null;
  onSelectAccount: (id: number) => void;
};

export default function AccountsPage({
  customerId,
  selectedAccountId,
  onSelectAccount,
}: Props) {
  const [items, setItems] = useState<Account[]>([]);
  const [accountNumber, setAccountNumber] = useState("");
  const [type, setType] = useState<AccountType>("CHECKING");
  const [currency, setCurrency] = useState("CAD");

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const refresh = useCallback(async () => {
    if (!customerId) {
      setItems([]);
      return;
    }

    setLoading(true);
    setError(null);

    try {
      const data = await listAccounts(customerId);

      setItems(Array.isArray(data) ? data : []);
    } catch (e) {
      setError(getApiErrorMessage(e));
      setItems([]);
    } finally {
      setLoading(false);
    }
  }, [customerId]);

  useEffect(() => {
    refresh();
  }, [refresh]);

  async function onCreate(e: React.FormEvent) {
    e.preventDefault();
    if (!customerId) return;

    setError(null);
    try {
      const created = await createAccount({
        customerId,
        accountNumber: accountNumber.trim(),
        type,
        currency: currency.trim().toUpperCase(),
      });

      setAccountNumber("");
      await refresh();
      onSelectAccount(created.id);
    } catch (e) {
      setError(getApiErrorMessage(e));
    }
  }

  if (!customerId) {
    return <div>Select a customer first.</div>;
  }

  return (
    <div>
      {error && <div style={{ color: "crimson", marginBottom: 8 }}>{error}</div>}

      <form onSubmit={onCreate} style={{ display: "flex", gap: 8, marginBottom: 12 }}>
        <input
          value={accountNumber}
          onChange={(e) => setAccountNumber(e.target.value)}
          placeholder="Account number (e.g. ACC-0003)"
        />

        <select value={type} onChange={(e) => setType(e.target.value as AccountType)}>
          <option value="CHECKING">CHECKING</option>
          <option value="SAVINGS">SAVINGS</option>
        </select>

        <input
          value={currency}
          onChange={(e) => setCurrency(e.target.value)}
          placeholder="CAD"
          style={{ width: 70 }}
        />

        <button type="submit" disabled={loading || !accountNumber.trim()}>
          Create
        </button>
      </form>

      {loading && <div>Loading...</div>}

      <div style={{ display: "flex", flexDirection: "column", gap: 8 }}>
        {items.length === 0 && !loading ? (
          <div>No accounts.</div>
        ) : (
          items.map((a) => (
            <button
              key={a.id}
              onClick={() => onSelectAccount(a.id)}
              style={{
                textAlign: "left",
                padding: 10,
                borderRadius: 8,
                border: "1px solid #ddd",
                background: a.id === selectedAccountId ? "#f3f6ff" : "white",
                cursor: "pointer",
              }}
            >
              <div>
                <b>#{a.id}</b> {a.accountNumber} ({a.type})
              </div>
              <div style={{ fontSize: 13, opacity: 0.8 }}>
                {a.currency} • Balance: {a.balance} • {a.status}
              </div>
            </button>
          ))
        )}
      </div>
    </div>
  );
}
