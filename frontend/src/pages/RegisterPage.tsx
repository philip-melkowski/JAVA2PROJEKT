import {useState} from "react";
import {Box, Button, TextField, Typography, Paper, Stack, Alert} from "@mui/material";
import {useNavigate} from "react-router-dom";
import {register} from "../api/authApi.ts";
import PersonAddIcon from '@mui/icons-material/PersonAdd';
import LoginIcon from '@mui/icons-material/Login';
import MenuBookIcon from '@mui/icons-material/MenuBook';
import CheckCircleIcon from '@mui/icons-material/CheckCircle';

export default function RegisterPage() {

    const navigate = useNavigate();

    const [username, setUsername] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");

    const [usernameError, setUsernameError] = useState("");
    const [emailError, setEmailError] = useState("");
    const [passwordError, setPasswordError] = useState("");
    const [confirmPasswordError, setConfirmPasswordError] = useState("");
    const [generalError, setGeneralError] = useState("");

    const [isLoading, setIsLoading] = useState(false);
    const [registrationSuccess, setRegistrationSuccess] = useState(false);

    const handleRegister = async () => {
        let valid = true;
        setUsernameError("");
        setEmailError("");
        setPasswordError("");
        setConfirmPasswordError("");
        setGeneralError("");

        // Username validation
        if (!username) {
            setUsernameError("Username is required");
            valid = false;
        } else if (username.length < 3) {
            setUsernameError("Username must be at least 3 characters");
            valid = false;
        }

        // Email validation
        if (!email) {
            setEmailError("Email is required");
            valid = false;
        } else if (!email.includes("@")) {
            setEmailError("Email is invalid");
            valid = false;
        }

        // Password validation
        if (!password) {
            setPasswordError("Password is required");
            valid = false;
        } else if (password.length < 6) {
            setPasswordError("Password must be at least 6 characters");
            valid = false;
        }

        // Confirm password validation
        if (!confirmPassword) {
            setConfirmPasswordError("Please confirm your password");
            valid = false;
        } else if (password !== confirmPassword) {
            setConfirmPasswordError("Passwords do not match");
            valid = false;
        }

        if (!valid) return;

        try {
            setIsLoading(true);

            await register(email, username, password);

            setRegistrationSuccess(true);
        } catch (err: any) {
            setGeneralError(err.message);
        } finally {
            setIsLoading(false);
        }
    };

    const handleBackToLogin = () => {
        navigate('/login');
    };

    // Success screen
    if (registrationSuccess) {
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
                        width: 500,
                        borderRadius: '24px',
                        position: 'relative',
                        zIndex: 1,
                        textAlign: 'center',
                    }}
                >
                    <Box
                        sx={{
                            width: 80,
                            height: 80,
                            borderRadius: '50%',
                            background: 'linear-gradient(135deg, #4caf50 0%, #66bb6a 100%)',
                            display: 'flex',
                            alignItems: 'center',
                            justifyContent: 'center',
                            margin: '0 auto 24px',
                        }}
                    >
                        <CheckCircleIcon sx={{ color: '#fff', fontSize: 48 }} />
                    </Box>

                    <Typography
                        variant="h4"
                        sx={{
                            fontWeight: 700,
                            color: '#333',
                            mb: 2,
                        }}
                    >
                        Registration Successful!
                    </Typography>

                    <Typography variant="body1" sx={{ color: '#666', mb: 1 }}>
                        We've sent an activation link to:
                    </Typography>
                    <Typography
                        variant="h6"
                        sx={{
                            color: '#667eea',
                            fontWeight: 600,
                            mb: 3,
                        }}
                    >
                        {email}
                    </Typography>

                    <Alert severity="info" sx={{ mb: 3, textAlign: 'left' }}>
                        <Typography variant="body2">
                            <strong>Important:</strong> Please check your email and click the activation link to complete your registration.
                            Don't forget to check your spam folder!
                        </Typography>
                    </Alert>

                    <Button
                        variant="contained"
                        fullWidth
                        size="large"
                        startIcon={<LoginIcon />}
                        onClick={handleBackToLogin}
                        sx={{
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
                        Back to Login
                    </Button>
                </Paper>
            </Box>
        );
    }

    // Registration form
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
                py: 4,
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
                    Create Account
                </Typography>
                <Typography variant="body2" sx={{ color: '#666', mb: 3 }}>
                    Join our community of book lovers
                </Typography>

                {/* Error Alert */}
                {generalError && (
                    <Alert severity="error" sx={{ mb: 3 }}>
                        {generalError}
                    </Alert>
                )}

                {/* Registration Form */}
                <Stack spacing={2.5}>
                    <TextField
                        label="Username"
                        type="text"
                        fullWidth
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        error={!!usernameError}
                        helperText={usernameError}
                        disabled={isLoading}
                        sx={{
                            '& .MuiOutlinedInput-root': {
                                borderRadius: '12px',
                            }
                        }}
                    />
                    <TextField
                        label="Email"
                        type="email"
                        fullWidth
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        error={!!emailError}
                        helperText={emailError}
                        disabled={isLoading}
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
                        sx={{
                            '& .MuiOutlinedInput-root': {
                                borderRadius: '12px',
                            }
                        }}
                    />
                    <TextField
                        label="Confirm Password"
                        type="password"
                        fullWidth
                        value={confirmPassword}
                        onChange={(e) => setConfirmPassword(e.target.value)}
                        error={!!confirmPasswordError}
                        helperText={confirmPasswordError}
                        disabled={isLoading}
                        onKeyPress={(e) => e.key === 'Enter' && handleRegister()}
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
                        startIcon={<PersonAddIcon />}
                        onClick={handleRegister}
                        disabled={isLoading}
                        sx={{
                            mt: 1,
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
                        {isLoading ? 'Creating Account...' : 'Create Account'}
                    </Button>
                </Stack>

                {/* Back to Login */}
                <Box sx={{ textAlign: 'center', mt: 3 }}>
                    <Typography variant="body2" sx={{ color: '#666' }}>
                        Already have an account?{' '}
                        <Typography
                            component="span"
                            sx={{
                                color: '#667eea',
                                fontWeight: 600,
                                cursor: 'pointer',
                                '&:hover': {
                                    textDecoration: 'underline',
                                }
                            }}
                            onClick={handleBackToLogin}
                        >
                            Sign In
                        </Typography>
                    </Typography>
                </Box>

                {/* Footer */}
                <Typography
                    variant="caption"
                    sx={{
                        display: 'block',
                        textAlign: 'center',
                        color: '#999',
                        mt: 3,
                    }}
                >
                    © 2026 GoodReadsPL. All rights reserved.
                </Typography>
            </Paper>
        </Box>
    );
}