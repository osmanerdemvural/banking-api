export type Customer = {
  id: number;
  fullName: string;
  email: string;
  createdAt: string; // ISO string
};

export type CreateCustomerRequest = {
  fullName: string;
  email: string;
};
