import {useState} from "react";
import {Box, Button, TextField, Typography, Paper} from "@mui/material";
import {login} from "../auth/authApi";

export default function LoginPage() {

    // stan formularza
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const [emailError, setEmailError] = useState("");
    const [passwordError, setPasswordError] = useState("");

    const handleLogin = async () =>
    {
        let valid = true;
        setEmailError("");
        setPasswordError("");

        if(!email)
        {
            setEmailError("Email jest wymagany");
            valid = false;
        }
        else if(!email.includes("@"))
        {
            setEmailError("Email jest niepoprawny");
            valid = false;
        }
        if(!password)
        {
            setPasswordError("Haslo jest wymagane");
            valid = false;
        }
        if(!valid) return;

        try
        {
            const result = await login(email, password);
            console.log("zalogowano, token: ", result.token);
        }
        catch(err: any)
        {
            setPasswordError(err.message);
        }
    };

    return (
    <Box
    display="flex" // dzieci boxa we flexie.
    justifyContent="center" //wyśrodkuj poziomo w osi X
    alignItems="center" // wyśrodkuj pionowo w osi Y
    height="100vh" // wysokość okna przeglądarki - 100vh -> 100% widoku
    bgcolor="#f5f5f5">
        <Paper elevation={10} // elevation - poziom cienia
               sx={{ padding: 4, width: 350}} // sx - inline stylowanie 4 -> 4 * 8 = 32px, 350 -> 350px
        >
            <Typography variant="h5" align="left" mb={2}>Logowanie</Typography>
            <TextField label="email"
                       type="email"
                       fullWidth
                       margin="normal"
                       value={email}
                       onChange={(e) => setEmail(e.target.value)}
                       error={!!emailError}
                       helperText={emailError}
            ></TextField>
            <TextField
                label="Hasło"
                type="password"
                fullWidth
                margin="normal"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                error={!!passwordError}
                helperText={passwordError}
            />

            <Button
                variant="contained"
                fullWidth
                sx={{ mt: 2 }}
                onClick={handleLogin}
            >
                Zaloguj
            </Button>
        </Paper>

    </Box>
    );

}