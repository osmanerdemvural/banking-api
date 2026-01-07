import { http } from "./http";
import type { Account, AccountType } from "../types/account";
import type { Transaction, TransferResponse } from "../types/transaction";

export type CreateAccountReq = {
  customerId: number;
  accountNumber: string;
  type: AccountType;
  currency: string;
};

export type MoneyReq = {
  amount: number;
  description?: string;
};

export type TransferReq = {
  fromAccountId: number;
  toAccountId: number;
  amount: number;
  description?: string;
};

export async function createAccount(body: CreateAccountReq): Promise<Account> {
  const res = await http.post<Account>("/accounts", body);
  return res.data;
}

export async function listAccounts(customerId?: number): Promise<Account[]> {
  const res = await http.get<Account[]>("/accounts", {
    params: customerId ? { customerId } : {},
  });
  return res.data;
}

export async function getAccount(id: number): Promise<Account> {
  const res = await http.get<Account>(`/accounts/${id}`);
  return res.data;
}

export async function deposit(accountId: number, body: MoneyReq): Promise<Transaction> {
  const res = await http.post<Transaction>(`/accounts/${accountId}/deposit`, body);
  return res.data;
}

export async function withdraw(accountId: number, body: MoneyReq): Promise<Transaction> {
  const res = await http.post<Transaction>(`/accounts/${accountId}/withdraw`, body);
  return res.data;
}

export async function transfer(body: TransferReq): Promise<TransferResponse> {
  const res = await http.post<TransferResponse>(`/accounts/transfer`, body);
  return res.data;
}

export async function listTransactions(accountId: number): Promise<Transaction[]> {
  const res = await http.get<Transaction[]>(`/accounts/${accountId}/transactions`);
  return res.data;
}
