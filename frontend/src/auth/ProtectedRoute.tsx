import { Navigate } from "react-router-dom";
import { type ReactNode } from "react";
import { useAuth } from "./AuthContext";


type ProtectedRouteProps = {
    children: ReactNode;
}

export default function ProtectedRoute({children}: ProtectedRouteProps) {
    const {isAuthenticated} = useAuth();
    return isAuthenticated ? <>{children}</> : <Navigate to="/login" replace />; // replace - nie zostawia children w historii przeglądarki
}