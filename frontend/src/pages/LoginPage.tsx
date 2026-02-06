import {useState} from "react";
import {Box, Button, TextField, Typography, Paper, Stack, Divider} from "@mui/material";
import {useAuth} from "../auth/AuthContext";
import {useNavigate} from "react-router-dom";
import LoginIcon from '@mui/icons-material/Login';
import PersonAddIcon from '@mui/icons-material/PersonAdd';
import MenuBookIcon from '@mui/icons-material/MenuBook';

export default function LoginPage() {

    const { login, isAuthenticated } = useAuth();
    const navigate = useNavigate();

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const [emailError, setEmailError] = useState("");
    const [passwordError, setPasswordError] = useState("");
    const [isLoading, setIsLoading] = useState(false);

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
            setPasswordError("Hasło jest wymagane");
            valid = false;
        }
        if(!valid) return;

        try
        {
            setIsLoading(true);
            await login(email, password);
        }
        catch(err: any)
        {
            setPasswordError(err.message);
        }
        finally
        {
            setIsLoading(false);
        }
    };

    const handleRegister = () => {
        navigate('/register');
    };

    if(isAuthenticated)
    {
        return <div>You are logged in!</div>
    }

    return (
        <Box
            sx={{
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
                minHeight: '100vh',
                background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                position: 'relative',
                overflow: 'hidden',
                '&::before': {
                    content: '""',
                    position: 'absolute',
                    top: '-10%',
                    right: '-5%',
                    width: '500px',
                    height: '500px',
                    background: 'rgba(255, 255, 255, 0.1)',
                    borderRadius: '50%',
                },
                '&::after': {
                    content: '""',
                    position: 'absolute',
                    bottom: '-10%',
                    left: '-5%',
                    width: '400px',
                    height: '400px',
                    background: 'rgba(255, 255, 255, 0.1)',
                    borderRadius: '50%',
                }
            }}
        >
            <Paper
                elevation={24}
                sx={{
                    p: 5,
                    width: 450,
                    borderRadius: '24px',
                    position: 'relative',
                    zIndex: 1,
                }}
            >
                {/* Logo & Title */}
                <Stack direction="row" spacing={2} alignItems="center" sx={{ mb: 1 }}>
                    <Box
                        sx={{
                            width: 56,
                            height: 56,
                            borderRadius: '16px',
                            background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                            display: 'flex',
                            alignItems: 'center',
                            justifyContent: 'center',
                        }}
                    >
                        <MenuBookIcon sx={{ color: '#fff', fontSize: 32 }} />
                    </Box>
                    <Box>
                        <Typography
                            variant="h4"
                            sx={{
                                fontWeight: 700,
                                background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                                backgroundClip: 'text',
                                WebkitBackgroundClip: 'text',
                                WebkitTextFillColor: 'transparent',
                            }}
                        >
                            GoodReadsPL
                        </Typography>
                        <Typography variant="body2" sx={{ color: '#666' }}>
                            Your book review platform
                        </Typography>
                    </Box>
                </Stack>

                <Typography
                    variant="h5"
                    sx={{
                        fontWeight: 600,
                        color: '#333',
                        mb: 1,
                        mt: 3,
                    }}
                >
                    Welcome back!
                </Typography>
                <Typography variant="body2" sx={{ color: '#666', mb: 4 }}>
                    Sign in to continue to your account
                </Typography>

                {/* Login Form */}
                <Stack spacing={3}>
                    <TextField
                        label="Email"
                        type="email"
                        fullWidth
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        error={!!emailError}
                        helperText={emailError}
                        disabled={isLoading}
                        onKeyPress={(e) => e.key === 'Enter' && handleLogin()}
                        sx={{
                            '& .MuiOutlinedInput-root': {
                                borderRadius: '12px',
                            }
                        }}
                    />
                    <TextField
                        label="Password"
                        type="password"
                        fullWidth
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        error={!!passwordError}
                        helperText={passwordError}
                        disabled={isLoading}
                        onKeyPress={(e) => e.key === 'Enter' && handleLogin()}
                        sx={{
                            '& .MuiOutlinedInput-root': {
                                borderRadius: '12px',
                            }
                        }}
                    />

                    <Button
                        variant="contained"
                        fullWidth
                        size="large"
                        startIcon={<LoginIcon />}
                        onClick={handleLogin}
                        disabled={isLoading}
                        sx={{
                            mt: 2,
                            py: 1.5,
                            borderRadius: '12px',
                            background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                            textTransform: 'none',
                            fontSize: '1rem',
                            fontWeight: 600,
                            boxShadow: '0 4px 12px rgba(102, 126, 234, 0.4)',
                            '&:hover': {
                                background: 'linear-gradient(135deg, #764ba2 0%, #667eea 100%)',
                                boxShadow: '0 6px 20px rgba(102, 126, 234, 0.5)',
                            }
                        }}
                    >
                        {isLoading ? 'Signing in...' : 'Sign In'}
                    </Button>
                </Stack>

                {/* Divider */}
                <Divider sx={{ my: 4 }}>
                    <Typography variant="body2" sx={{ color: '#999' }}>
                        OR
                    </Typography>
                </Divider>

                {/* Register Section */}
                <Box sx={{ textAlign: 'center' }}>
                    <Typography variant="body2" sx={{ color: '#666', mb: 2 }}>
                        Don't have an account yet?
                    </Typography>
                    <Button
                        variant="outlined"
                        fullWidth
                        size="large"
                        startIcon={<PersonAddIcon />}
                        onClick={handleRegister}
                        disabled={isLoading}
                        sx={{
                            py: 1.5,
                            borderRadius: '12px',
                            borderColor: '#667eea',
                            color: '#667eea',
                            textTransform: 'none',
                            fontSize: '1rem',
                            fontWeight: 600,
                            borderWidth: '2px',
                            '&:hover': {
                                borderWidth: '2px',
                                borderColor: '#764ba2',
                                backgroundColor: 'rgba(102, 126, 234, 0.05)',
                            }
                        }}
                    >
                        Create Account
                    </Button>
                </Box>

                {/* Footer */}
                <Typography
                    variant="caption"
                    sx={{
                        display: 'block',
                        textAlign: 'center',
                        color: '#999',
                        mt: 4,
                    }}
                >
                    © 2026 GoodReadsPL. All rights reserved.
                </Typography>
            </Paper>
        </Box>
    );

}