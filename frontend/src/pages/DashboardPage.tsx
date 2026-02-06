import {Box, Typography, Container, Paper, Stack, CircularProgress} from "@mui/material";
import {apiFetch} from "../api/api.ts";
import {useEffect, useState} from "react";
import MenuBookIcon from '@mui/icons-material/MenuBook';
import RateReviewIcon from '@mui/icons-material/RateReview';
import PersonIcon from '@mui/icons-material/Person';
import WavingHandIcon from '@mui/icons-material/WavingHand';

export default function DashboardPage() {

    const [username, setUsername] = useState<string>("");
    const [loading, setLoading] = useState<boolean>(true);

    const fetchUsername = async () => {
        return await apiFetch("api/auth/me");
    }

    useEffect(() => {
        fetchUsername()
            .then(res => setUsername(res.username))
            .finally(() => setLoading(false));
    }, []);


    return (
        <Box
            sx={{
                minHeight: '100vh',
                background: 'linear-gradient(to bottom, #f8f9fa 0%, #e9ecef 100%)',
                pb: 6,
            }}
        >
            <Container maxWidth="lg" sx={{ pt: 6 }}>
                {loading ? (
                    <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '400px' }}>
                        <CircularProgress sx={{ color: '#667eea' }} />
                    </Box>
                ) : (
                    <Stack spacing={4}>
                        {/* Welcome Header */}
                        <Paper
                            elevation={0}
                            sx={{
                                p: 5,
                                borderRadius: '20px',
                                background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                                position: 'relative',
                                overflow: 'hidden',
                                '&::before': {
                                    content: '""',
                                    position: 'absolute',
                                    top: '-50%',
                                    right: '-10%',
                                    width: '300px',
                                    height: '300px',
                                    background: 'rgba(255, 255, 255, 0.1)',
                                    borderRadius: '50%',
                                }
                            }}
                        >
                            <Stack direction="row" spacing={2} alignItems="center">
                                <WavingHandIcon sx={{ fontSize: 48, color: '#ffd700' }} />
                                <Box>
                                    <Typography
                                        variant="h3"
                                        sx={{
                                            color: '#fff',
                                            fontWeight: 700,
                                            mb: 1,
                                        }}
                                    >
                                        Welcome back, {username}!
                                    </Typography>
                                    <Typography
                                        variant="h6"
                                        sx={{
                                            color: 'rgba(255, 255, 255, 0.9)',
                                            fontWeight: 400,
                                        }}
                                    >
                                        Ready to discover your next great read?
                                    </Typography>
                                </Box>
                            </Stack>
                        </Paper>

                        {/* Quick Stats Cards */}
                        <Stack direction={{ xs: 'column', md: 'row' }} spacing={3}>
                            {/* Books Card */}
                            <Paper
                                elevation={0}
                                sx={{
                                    flex: 1,
                                    p: 4,
                                    borderRadius: '16px',
                                    border: '1px solid #e0e0e0',
                                    transition: 'all 0.3s ease',
                                    '&:hover': {
                                        transform: 'translateY(-8px)',
                                        boxShadow: '0 12px 24px rgba(102, 126, 234, 0.15)',
                                    }
                                }}
                            >
                                <Stack spacing={2}>
                                    <Box
                                        sx={{
                                            width: 56,
                                            height: 56,
                                            borderRadius: '12px',
                                            background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                                            display: 'flex',
                                            alignItems: 'center',
                                            justifyContent: 'center',
                                        }}
                                    >
                                        <MenuBookIcon sx={{ color: '#fff', fontSize: 32 }} />
                                    </Box>
                                    <Box>
                                        <Typography variant="h6" sx={{ color: '#666', fontWeight: 500 }}>
                                            Explore Books
                                        </Typography>
                                        <Typography variant="body2" sx={{ color: '#999', mt: 0.5 }}>
                                            Browse our collection and find your next favorite
                                        </Typography>
                                    </Box>
                                </Stack>
                            </Paper>

                            {/* Reviews Card */}
                            <Paper
                                elevation={0}
                                sx={{
                                    flex: 1,
                                    p: 4,
                                    borderRadius: '16px',
                                    border: '1px solid #e0e0e0',
                                    transition: 'all 0.3s ease',
                                    '&:hover': {
                                        transform: 'translateY(-8px)',
                                        boxShadow: '0 12px 24px rgba(240, 147, 251, 0.15)',
                                    }
                                }}
                            >
                                <Stack spacing={2}>
                                    <Box
                                        sx={{
                                            width: 56,
                                            height: 56,
                                            borderRadius: '12px',
                                            background: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
                                            display: 'flex',
                                            alignItems: 'center',
                                            justifyContent: 'center',
                                        }}
                                    >
                                        <RateReviewIcon sx={{ color: '#fff', fontSize: 32 }} />
                                    </Box>
                                    <Box>
                                        <Typography variant="h6" sx={{ color: '#666', fontWeight: 500 }}>
                                            Your Reviews
                                        </Typography>
                                        <Typography variant="body2" sx={{ color: '#999', mt: 0.5 }}>
                                            Manage and edit your book reviews
                                        </Typography>
                                    </Box>
                                </Stack>
                            </Paper>

                            {/* Profile Card */}
                            <Paper
                                elevation={0}
                                sx={{
                                    flex: 1,
                                    p: 4,
                                    borderRadius: '16px',
                                    border: '1px solid #e0e0e0',
                                    transition: 'all 0.3s ease',
                                    '&:hover': {
                                        transform: 'translateY(-8px)',
                                        boxShadow: '0 12px 24px rgba(25, 84, 123, 0.15)',
                                    }
                                }}
                            >
                                <Stack spacing={2}>
                                    <Box
                                        sx={{
                                            width: 56,
                                            height: 56,
                                            borderRadius: '12px',
                                            background: 'linear-gradient(135deg, #19547b 0%, #ffd89b 100%)',
                                            display: 'flex',
                                            alignItems: 'center',
                                            justifyContent: 'center',
                                        }}
                                    >
                                        <PersonIcon sx={{ color: '#fff', fontSize: 32 }} />
                                    </Box>
                                    <Box>
                                        <Typography variant="h6" sx={{ color: '#666', fontWeight: 500 }}>
                                            Your Profile
                                        </Typography>
                                        <Typography variant="body2" sx={{ color: '#999', mt: 0.5 }}>
                                            Logged in as {username}
                                        </Typography>
                                    </Box>
                                </Stack>
                            </Paper>
                        </Stack>

                        {/* Info Section */}
                        <Paper
                            elevation={0}
                            sx={{
                                p: 4,
                                borderRadius: '16px',
                                border: '1px solid #e0e0e0',
                                background: '#fff',
                            }}
                        >
                            <Typography variant="h5" sx={{ fontWeight: 700, mb: 2, color: '#333' }}>
                                Getting Started
                            </Typography>
                            <Stack spacing={2}>
                                <Typography variant="body1" sx={{ color: '#666' }}>
                                    📚 <strong>Browse Books</strong> - Navigate to the Books page to explore our collection
                                </Typography>
                                <Typography variant="body1" sx={{ color: '#666' }}>
                                    ⭐ <strong>Rate & Review</strong> - Share your thoughts on books you've read
                                </Typography>
                                <Typography variant="body1" sx={{ color: '#666' }}>
                                    🔍 <strong>Filter & Search</strong> - Use filters to find exactly what you're looking for
                                </Typography>
                                <Typography variant="body1" sx={{ color: '#666' }}>
                                    ✏️ <strong>Manage Reviews</strong> - Edit or delete your reviews anytime
                                </Typography>
                            </Stack>
                        </Paper>
                    </Stack>
                )}
            </Container>
        </Box>
    )

}