export type AccountType = "CHECKING" | "SAVINGS";
export type AccountStatus = "ACTIVE" | "CLOSED";

export type Account = {
  id: number;
  customerId: number;
  accountNumber: string;
  type: AccountType;
  currency: string;
  balance: number;
  status: AccountStatus;
  createdAt: string;
  updatedAt: string;
};
