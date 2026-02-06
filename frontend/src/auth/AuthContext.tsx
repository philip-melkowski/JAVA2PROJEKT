import {createContext, useContext, useState, type ReactNode, useEffect} from 'react';
import {login} from "../api/authApi.ts"
import {registerUnauthorizedHandler} from "../api/api.ts";

// typy danych w kontekscie
type AuthContextType = {
    token: string | null;
    isAuthenticated: boolean;
    login: (email: string, password: string) => Promise<void>;
    role: string | null;
    logout: () => void;
};

// context - globalny kontener danych, dane te są dostępne dla komponentów wewnątrz providera
const AuthContext = createContext<AuthContextType | null>(null);

// komponent, który dostarcza dane do innych komponentów
export const AuthProvider = (props: {children: ReactNode}) =>{
    const children = props.children;
    const [token, setToken] = useState<string | null>(localStorage.getItem("token"));
    const [role, setRole] = useState<string | null>(localStorage.getItem("role"));


    const loginUser = async (email: string, password: string) => {
        const result = await login(email, password);
        setRole(result.role);
        localStorage.setItem("role", result.role);
        setToken(result.token);
        localStorage.setItem("token", result.token);
    }

    const logoutUser = () => {
        setToken(null)
        setRole(null);
        localStorage.removeItem("token");
        localStorage.removeItem("role");
    }

    useEffect(() => {
        registerUnauthorizedHandler(logoutUser)
    }, []);

    return (
        <AuthContext.Provider value={{
            token,
            isAuthenticated: !!token, // !! oznacza - boolowska wersja wartości - czy token istnieje.
            role: role,
            login: loginUser,
            logout: logoutUser}}>
            {children}
        </AuthContext.Provider>
    )
}

// custom hook
//  żeby zamiast pisać useContext(AuthContext) moć napisać const {login, token} = useAuth();
export const useAuth = () =>
{
    const context = useContext(AuthContext);
    if(!context)
    {
        throw new Error("useAuth must be used inside AuthProvider");
    }
    return context;
}