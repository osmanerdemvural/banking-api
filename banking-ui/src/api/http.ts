import axios from "axios";

const envBase = (import.meta.env.VITE_API_BASE_URL ?? "").trim();
const baseURL = (envBase !== "" ? envBase : "/api").replace(/\/+$/, "");

export const http = axios.create({
  baseURL,
  timeout: 15000,
  headers: {
    "Content-Type": "application/json",
  },
});

type ApiErrorShape = {
  message?: string;
  error?: string;
};

function isObject(v: unknown): v is Record<string, unknown> {
  return typeof v === "object" && v !== null;
}

export function getApiErrorMessage(err: unknown): string {
  if (typeof err === "string") return err;

  if (axios.isAxiosError(err)) {
    const dataUnknown = err.response?.data;

    if (isObject(dataUnknown)) {
      const data = dataUnknown as ApiErrorShape;
      if (data.message) return data.message;
      if (data.error) return data.error;
    }

    return err.message || "Unexpected error";
  }

  return "Unexpected error";
}
