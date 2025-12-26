const API_URL = 'http://localhost:8080/api/auth';

export async function login(username: string, password: string) {
    const response = await fetch(`${API_URL}/login`, {
        method: 'POST',
        body: JSON.stringify({username, password}),
        headers: {'Content-Type': 'application/json'},

    });

    if(!response.ok)
    {
        const errorText = await response.text();
        throw new Error(errorText || "Błąd logowania");
    }

    return response.json();
}