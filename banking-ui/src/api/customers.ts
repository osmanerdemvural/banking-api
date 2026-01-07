import { http } from "./http";
import type { Customer } from "../types/customer";

export type CreateCustomerReq = {
  fullName: string;
  email: string;
};

export async function listCustomers(): Promise<Customer[]> {
  const res = await http.get<Customer[]>("/customers");
  return res.data;
}

export async function createCustomer(body: CreateCustomerReq): Promise<Customer> {
  const res = await http.post<Customer>("/customers", body);
  return res.data;
}
