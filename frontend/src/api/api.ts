const BASE_URL = 'http://localhost:8080';

// handler 401
let onUnauthorized: (() => void) | null = null;

export function registerUnauthorizedHandler(handler: () => void) {
    onUnauthorized = handler;
}

type ApiErrorCode =
    | "UNAUTHORIZED"
    | "FORBIDDEN"
    | "NOT_FOUND"
    | "VALIDATION_ERROR"
    | "CONFLICT"
    | "SERVER_ERROR";

export type ValidationErrors = Record<string, string>;

export type ApiError = {
    code: ApiErrorCode;
    message: string;
    details?: ValidationErrors;
};

export async function apiFetch(
    endpoint: string,
    options: RequestInit = {}
) {
    const token = localStorage.getItem("token");

    const response = await fetch(`${BASE_URL}/${endpoint}`, {
        ...options,
        headers: {
            "Content-Type": "application/json",
            ...(token ? { Authorization: `Bearer ${token}` } : {}),
            ...options.headers,
        },
    });

    if (!response.ok) {
        let payload: ValidationErrors | undefined = undefined;

        try {
            payload = await response.json();
        } catch {
            /* brak body lub nie-JSON */
        }

        const status = response.status;

        let error: ApiError;

        if (status === 401) {
            error = {
                code: "UNAUTHORIZED",
                message: "Your session has expired. Please log in again.",
            };
            if(onUnauthorized) onUnauthorized();

        } else if (status === 403) {
            error = {
                code: "FORBIDDEN",
                message: "You are not allowed to perform this action.",
            };
        } else if (status === 404) {
            error = {
                code: "NOT_FOUND",
                message: "Requested resource was not found.",
            };
        } else if (status === 409) {
            error = {
                code: "CONFLICT",
                message: "This resource already exists.",
            };
        } else if (status === 400) {
            error = {
                code: "VALIDATION_ERROR",
                message: "Validation error.",
                details: payload,
            };
        } else {
            error = {
                code: "SERVER_ERROR",
                message: "Server error. Please try again later.",
            };
        }

        throw error;
    }

    if (response.status === 204) {
        return null;
    }

    return response.json();
}