import { useEffect, useState } from "react";
import { deposit, getAccount, listTransactions, transfer, withdraw } from "../api/accounts.ts";
import { getApiErrorMessage } from "../api/http";
import type { Account } from "../types/account";
import type { Transaction } from "../types/transaction";

type Props = {
  accountId: number | null;
};

export default function AccountDetailPage({ accountId }: Props) {
  const [account, setAccount] = useState<Account | null>(null);
  const [txs, setTxs] = useState<Transaction[]>([]);
  const [error, setError] = useState<string | null>(null);

  // forms
  const [depAmount, setDepAmount] = useState(10);
  const [depDesc, setDepDesc] = useState("cash");

  const [wdAmount, setWdAmount] = useState(5);
  const [wdDesc, setWdDesc] = useState("atm");

  const [toAccountId, setToAccountId] = useState<number>(0);
  const [trAmount, setTrAmount] = useState(1);
  const [trDesc, setTrDesc] = useState("rent");

  async function refresh() {
    setError(null);
    if (!accountId) {
      setAccount(null);
      setTxs([]);
      return;
    }
    try {
      const a = await getAccount(accountId);
      const t = await listTransactions(accountId);
      setAccount(a);
      setTxs(t);
    } catch (e) {
      setError(getApiErrorMessage(e));
    }
  }

  useEffect(() => {
    refresh();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [accountId]);

  async function onDeposit() {
    if (!accountId) return;
    setError(null);
    try {
      await deposit(accountId, { amount: depAmount, description: depDesc });
      await refresh();
    } catch (e) {
      setError(getApiErrorMessage(e));
    }
  }

  async function onWithdraw() {
    if (!accountId) return;
    setError(null);
    try {
      await withdraw(accountId, { amount: wdAmount, description: wdDesc });
      await refresh();
    } catch (e) {
      setError(getApiErrorMessage(e));
    }
  }

  async function onTransfer() {
    if (!accountId) return;
    setError(null);
    try {
      await transfer({
        fromAccountId: accountId,
        toAccountId,
        amount: trAmount,
        description: trDesc,
      });
      await refresh();
    } catch (e) {
      setError(getApiErrorMessage(e));
    }
  }

  return (
    <div style={{ border: "1px solid #ddd", padding: 12, borderRadius: 8 }}>
      <h2 style={{ marginTop: 0 }}>Account detail</h2>

      {!accountId && <div>Select an account.</div>}

      {error && <div style={{ color: "crimson", marginBottom: 8 }}>{error}</div>}

      {account && (
        <>
          <div style={{ marginBottom: 10 }}>
            <div><b>#{account.id}</b> {account.accountNumber}</div>
            <div>Currency: {account.currency} | Type: {account.type} | Status: {account.status}</div>
            <div style={{ fontSize: 18, marginTop: 6 }}><b>Balance:</b> {account.balance}</div>
          </div>

          <div style={{ display: "grid", gap: 10, gridTemplateColumns: "1fr 1fr 1fr" }}>
            <div style={{ border: "1px solid #eee", padding: 10, borderRadius: 8 }}>
              <b>Deposit</b>
              <div style={{ display: "flex", gap: 6, marginTop: 6 }}>
                <input
                  type="number"
                  value={depAmount}
                  onChange={(e) => setDepAmount(Number(e.target.value))}
                  style={{ width: 110 }}
                />
                <input value={depDesc} onChange={(e) => setDepDesc(e.target.value)} />
                <button onClick={onDeposit}>Go</button>
              </div>
            </div>

            <div style={{ border: "1px solid #eee", padding: 10, borderRadius: 8 }}>
              <b>Withdraw</b>
              <div style={{ display: "flex", gap: 6, marginTop: 6 }}>
                <input
                  type="number"
                  value={wdAmount}
                  onChange={(e) => setWdAmount(Number(e.target.value))}
                  style={{ width: 110 }}
                />
                <input value={wdDesc} onChange={(e) => setWdDesc(e.target.value)} />
                <button onClick={onWithdraw}>Go</button>
              </div>
            </div>

            <div style={{ border: "1px solid #eee", padding: 10, borderRadius: 8 }}>
              <b>Transfer</b>
              <div style={{ display: "flex", gap: 6, marginTop: 6, flexWrap: "wrap" }}>
                <input
                  type="number"
                  value={toAccountId}
                  onChange={(e) => setToAccountId(Number(e.target.value))}
                  placeholder="toAccountId"
                  style={{ width: 120 }}
                />
                <input
                  type="number"
                  value={trAmount}
                  onChange={(e) => setTrAmount(Number(e.target.value))}
                  style={{ width: 110 }}
                />
                <input value={trDesc} onChange={(e) => setTrDesc(e.target.value)} />
                <button onClick={onTransfer}>Go</button>
              </div>
              <div style={{ fontSize: 12, opacity: 0.7, marginTop: 6 }}>
                Not: Transfer aynı currency hesaplar arasında.
              </div>
            </div>
          </div>

          <h3 style={{ marginTop: 16 }}>Last transactions</h3>
          <div style={{ maxHeight: 260, overflow: "auto", border: "1px solid #eee", borderRadius: 8 }}>
            <table style={{ width: "100%", borderCollapse: "collapse" }}>
              <thead>
                <tr>
                  <th style={{ textAlign: "left", padding: 8 }}>ID</th>
                  <th style={{ textAlign: "left", padding: 8 }}>Type</th>
                  <th style={{ textAlign: "left", padding: 8 }}>Amount</th>
                  <th style={{ textAlign: "left", padding: 8 }}>BalanceAfter</th>
                  <th style={{ textAlign: "left", padding: 8 }}>Desc</th>
                  <th style={{ textAlign: "left", padding: 8 }}>Time</th>
                </tr>
              </thead>
              <tbody>
                {txs.map((t) => (
                  <tr key={t.id} style={{ borderTop: "1px solid #eee" }}>
                    <td style={{ padding: 8 }}>{t.id}</td>
                    <td style={{ padding: 8 }}>{t.type}</td>
                    <td style={{ padding: 8 }}>{t.amount} {t.currency}</td>
                    <td style={{ padding: 8 }}>{t.balanceAfter}</td>
                    <td style={{ padding: 8 }}>{t.description ?? ""}</td>
                    <td style={{ padding: 8 }}>{t.createdAt}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </>
      )}
    </div>
  );
}
