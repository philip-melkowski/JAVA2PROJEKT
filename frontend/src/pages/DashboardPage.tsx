import {useAuth} from "../auth/AuthContext.tsx";
import {Box, Button, Typography} from "@mui/material";

export default function DashboardPage() {

    const {logout} = useAuth();

    return (
        <Box
            p={3}
        >
            <Button
                sx = {{padding: 10, width: 50, height: 20, color: "blue"}}
                variant="contained"
                color="secondary"
                align="right"
                onClick={logout}
            >Wyloguj się</Button>
            <Typography variant="h5" align="center" mb={2}>
                Jesteś zalogowany!
            </Typography>



        </Box>
    )

}