import {Card, CardContent, Typography, Box, Chip, Stack} from "@mui/material";
import type {BookDTO} from "../api/booksApi.ts";
import CalendarTodayIcon from '@mui/icons-material/CalendarToday';
import PersonIcon from '@mui/icons-material/Person';
import CategoryIcon from '@mui/icons-material/Category';
import StarIcon from '@mui/icons-material/Star';

type BookCardProps = {
    book: BookDTO
}

export default function BookCard({book}: BookCardProps) {
    return (
        <Card
            sx={{
                background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                borderRadius: '16px',
                overflow: 'hidden',
                position: 'relative',
                height: '280px', // Stała wysokość
                display: 'flex',
                flexDirection: 'column',
                transition: 'all 0.3s ease',
                '&:hover': {
                    transform: 'translateY(-8px)',
                    boxShadow: '0 12px 24px rgba(102, 126, 234, 0.3)',
                },
                '&::before': {
                    content: '""',
                    position: 'absolute',
                    top: 0,
                    left: 0,
                    right: 0,
                    height: '4px',
                    background: 'linear-gradient(90deg, #ffd89b 0%, #19547b 100%)',
                }
            }}
        >
            <CardContent sx={{
                p: 3,
                display: 'flex',
                flexDirection: 'column',
                height: '100%',
                justifyContent: 'space-between',
            }}>
                <Box>
                    {/* Tytuł książki */}
                    <Typography
                        variant="h5"
                        sx={{
                            color: '#fff',
                            fontWeight: 700,
                            mb: 2,
                            fontSize: '1.3rem',
                            letterSpacing: '0.5px',
                            overflow: 'hidden',
                            textOverflow: 'ellipsis',
                            display: '-webkit-box',
                            WebkitLineClamp: 2,
                            WebkitBoxOrient: 'vertical',
                            minHeight: '2.6rem', // 2 linie
                        }}
                    >
                        {book.title}
                    </Typography>

                    {/* Informacje o autorze */}
                    <Stack direction="row" spacing={1} alignItems="center" sx={{ mb: 1.5 }}>
                        <PersonIcon sx={{ color: 'rgba(255, 255, 255, 0.8)', fontSize: 20 }} />
                        <Typography
                            variant="body1"
                            sx={{
                                color: 'rgba(255, 255, 255, 0.95)',
                                fontWeight: 500,
                                fontSize: '0.95rem',
                                overflow: 'hidden',
                                textOverflow: 'ellipsis',
                                whiteSpace: 'nowrap',
                            }}
                        >
                            {book.authorName} {book.authorSurname}
                        </Typography>
                    </Stack>

                    {/* Rok wydania i gatunek w jednej linii */}
                    <Stack direction="row" spacing={2} alignItems="center" sx={{ mb: 1.5 }}>
                        <Stack direction="row" spacing={0.5} alignItems="center">
                            <CalendarTodayIcon sx={{ color: 'rgba(255, 255, 255, 0.8)', fontSize: 16 }} />
                            <Typography
                                variant="body2"
                                sx={{
                                    color: 'rgba(255, 255, 255, 0.85)',
                                    fontSize: '0.9rem',
                                }}
                            >
                                {book.publishYear}
                            </Typography>
                        </Stack>

                        <Stack direction="row" spacing={0.5} alignItems="center">
                            <CategoryIcon sx={{ color: 'rgba(255, 255, 255, 0.8)', fontSize: 18 }} />
                            <Chip
                                label={book.genre}
                                size="small"
                                sx={{
                                    backgroundColor: 'rgba(255, 255, 255, 0.2)',
                                    color: '#fff',
                                    fontWeight: 600,
                                    border: '1px solid rgba(255, 255, 255, 0.3)',
                                    fontSize: '0.75rem',
                                    height: '24px',
                                }}
                            />
                        </Stack>
                    </Stack>
                </Box>

                {/* Ocena */}
                <Box
                    sx={{
                        pt: 2,
                        borderTop: '1px solid rgba(255, 255, 255, 0.2)',
                    }}
                >
                    {book.averageRating !== null ? (
                        <Stack direction="row" spacing={1} alignItems="center">
                            <StarIcon sx={{ color: '#ffd700', fontSize: 24 }} />
                            <Typography
                                variant="h6"
                                sx={{
                                    color: '#fff',
                                    fontWeight: 700,
                                    fontSize: '1.1rem',
                                }}
                            >
                                {book.averageRating.toFixed(1)}
                            </Typography>
                            <Typography
                                variant="body2"
                                sx={{
                                    color: 'rgba(255, 255, 255, 0.7)',
                                    fontSize: '0.85rem',
                                }}
                            >
                                / 10
                            </Typography>
                        </Stack>
                    ) : (
                        <Typography
                            variant="body2"
                            sx={{
                                color: 'rgba(255, 255, 255, 0.6)',
                                fontStyle: 'italic',
                            }}
                        >
                            No ratings yet
                        </Typography>
                    )}
                </Box>
            </CardContent>
        </Card>
    );
}