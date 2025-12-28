const BASE_URL = 'http://localhost:8080';

export async function apiFetch(
    endpoint: string,
    options: RequestInit = {}
)
{
    const token = localStorage.getItem("token");

    const response = await fetch(`${BASE_URL}/${endpoint}`, {
        ...options,
        headers: {
            "Content-Type": "application/json",
            ...(token ? { Authorization: `Bearer ${token}` } : {}),
            ...options.headers
        },
    })
    if(!response.ok)
    {
        let message = "Błąd serwera";

        try
        {
            message = await response.text();
        } catch { /* empty */ }
        throw new Error(message);

    }

    if(response.status === 204) return null;

    return response.json();
}