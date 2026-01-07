export type TransactionType = "DEPOSIT" | "WITHDRAW" | "TRANSFER";

export type Transaction = {
  id: number;
  accountId: number;
  type: TransactionType;
  amount: number;
  currency: string;
  description?: string | null;
  createdAt: string;
  balanceAfter: number;
  transferId?: string | null;
};

export type TransferResponse = {
  transferId: string;
  fromAccountId: number;
  toAccountId: number;
  currency: string;
  amount: number;
  fromBalanceAfter: number;
  toBalanceAfter: number;
};
