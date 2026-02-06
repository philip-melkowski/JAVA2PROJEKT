import {Stack, Button, Box, Typography} from "@mui/material";
import {useAuth} from "../auth/AuthContext";
import NavButton from "./NavButton";
import LogoutIcon from '@mui/icons-material/Logout';
import MenuBookIcon from '@mui/icons-material/MenuBook';

export default function Navbar() {
    const {isAuthenticated, role, logout} = useAuth();

    return (
        <Box
            sx={{
                background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                boxShadow: '0 4px 20px rgba(0, 0, 0, 0.15)',
                position: 'sticky',
                top: 0,
                zIndex: 1000,
            }}
        >
            <Stack
                direction="row"
                spacing={2}
                sx={{
                    px: 4,
                    py: 2,
                    alignItems: 'center',
                    maxWidth: '1400px',
                    margin: '0 auto',
                }}
            >
                {/* Logo/Brand */}
                <Box sx={{ display: 'flex', alignItems: 'center', mr: 4 }}>
                    <MenuBookIcon sx={{ color: '#fff', fontSize: 32, mr: 1 }} />
                    <Typography
                        variant="h5"
                        sx={{
                            color: '#fff',
                            fontWeight: 700,
                            letterSpacing: '0.5px',
                        }}
                    >
                        GoodReadsPL
                    </Typography>
                </Box>

                {isAuthenticated && (
                    <>
                        {/* Navigation Links */}
                        <Stack direction="row" spacing={1} sx={{ flexGrow: 1 }}>
                            <NavButton to="/books" label="Books" />
                            <NavButton to="/my-reviews" label="My Reviews" />

                            {role === "ADMIN" && (
                                <NavButton to="/admin/manageBooks" label="Manage Books" />
                            )}
                        </Stack>

                        {/* Logout Button */}
                        <Button
                            onClick={logout}
                            startIcon={<LogoutIcon />}
                            sx={{
                                color: '#fff',
                                backgroundColor: 'rgba(255, 255, 255, 0.15)',
                                fontWeight: 500,
                                textTransform: 'none',
                                px: 3,
                                py: 1,
                                borderRadius: '8px',
                                border: '1px solid rgba(255, 255, 255, 0.3)',
                                transition: 'all 0.3s ease',
                                '&:hover': {
                                    backgroundColor: 'rgba(244, 67, 54, 0.8)',
                                    border: '1px solid rgba(255, 255, 255, 0.5)',
                                    transform: 'translateY(-2px)',
                                    boxShadow: '0 4px 12px rgba(244, 67, 54, 0.3)',
                                }
                            }}
                        >
                            Logout
                        </Button>
                    </>
                )}
            </Stack>
        </Box>
    );
}