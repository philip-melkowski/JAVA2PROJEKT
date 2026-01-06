import {useAuth} from "../auth/AuthContext.tsx";
import {Box, Button, Typography} from "@mui/material";
import {apiFetch} from "../api/api.ts";
import {useEffect, useState} from "react";

export default function DashboardPage() {

    const [username, setUsername] = useState<string>("");
    const {logout} = useAuth();

    const fetchUsername = async () => {
        return await apiFetch("api/auth/me");
    }

    useEffect(() => {
        fetchUsername().then(res => setUsername(res.username));
    }, []);


    return (
        <Box
            p={3}
        >
            <Button
                sx = {{padding: 10, width: 50, height: 20, color: "blue"}}
                variant="contained"
                color="secondary"
                onClick={logout}
            >Wyloguj się</Button>
            <Typography variant="h5" align="center" mb={2}>
                Welcome, {username}!
            </Typography>



        </Box>
    )

}