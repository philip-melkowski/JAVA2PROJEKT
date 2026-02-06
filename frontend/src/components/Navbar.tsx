import {Stack, Button} from "@mui/material";
import {useAuth} from "../auth/AuthContext";
import NavButton from "./NavButton";

export default function Navbar() {
    const {isAuthenticated, role, logout} = useAuth();

    return (
        <Stack direction="row" spacing={2} sx={{p: 2, borderBottom: "1px solid #ddd"}}>

            {isAuthenticated && (
                <>
                    <NavButton to="/books" label="Books" />
                    <NavButton to="/my-reviews" label="My reviews" />

                    {role === "ADMIN" && (
                        <NavButton to="/admin/books" label="Manage books" />
                    )}

                    <Button color="error" onClick={logout}>
                        Logout
                    </Button>
                </>
            )}

        </Stack>
    );
}