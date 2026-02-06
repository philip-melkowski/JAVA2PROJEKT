import type {BookDTO} from "../api/booksApi.ts";
import BookCard from "./BookCard.tsx";
import {Button, Box, Stack, Typography, CircularProgress} from "@mui/material";
import RateReviewIcon from '@mui/icons-material/RateReview';
import RefreshIcon from '@mui/icons-material/Refresh';

export type BooksListProps = {
    loading: boolean;
    isError: boolean;
    onRetry: () => void;
    books: BookDTO[];
    onAddReview: (bookId: number) => void;
}

export function BooksListSection(props: BooksListProps) {
    // Loading state
    if (props.loading && !props.isError) {
        return (
            <Box
                sx={{
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                    justifyContent: 'center',
                    minHeight: '400px',
                    gap: 2
                }}
            >
                <CircularProgress
                    size={60}
                    sx={{
                        color: '#667eea',
                    }}
                />
                <Typography
                    variant="h5"
                    sx={{
                        color: '#667eea',
                        fontWeight: 600,
                    }}
                >
                    Loading books...
                </Typography>
            </Box>
        );
    }

    // Error state
    if (!props.loading && props.isError) {
        return (
            <Box
                sx={{
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                    justifyContent: 'center',
                    minHeight: '400px',
                    gap: 3
                }}
            >
                <Typography
                    variant="h5"
                    sx={{
                        color: '#f44336',
                        fontWeight: 600,
                    }}
                >
                    Failed to load books
                </Typography>
                <Button
                    variant="contained"
                    startIcon={<RefreshIcon />}
                    onClick={props.onRetry}
                    sx={{
                        background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                        color: '#fff',
                        px: 4,
                        py: 1.5,
                        fontSize: '1rem',
                        fontWeight: 600,
                        textTransform: 'none',
                        borderRadius: '12px',
                        '&:hover': {
                            background: 'linear-gradient(135deg, #764ba2 0%, #667eea 100%)',
                            transform: 'translateY(-2px)',
                            boxShadow: '0 6px 20px rgba(102, 126, 234, 0.4)',
                        },
                        transition: 'all 0.3s ease',
                    }}
                >
                    Retry Loading
                </Button>
            </Box>
        );
    }

    // Empty state
    if (!props.loading && !props.isError && props.books.length === 0) {
        return (
            <Box
                sx={{
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    minHeight: '400px',
                }}
            >
                <Typography
                    variant="h5"
                    sx={{
                        color: '#999',
                        fontWeight: 500,
                        textAlign: 'center',
                    }}
                >
                    No books found. Try changing your filters.
                </Typography>
            </Box>
        );
    }

    // Books list
    return (
        <Stack spacing={3} sx={{ mt: 2 }}>
            {props.books.map(book => (
                <Stack
                    key={book.id}
                    direction="row"
                    spacing={2}
                    sx={{
                        alignItems: 'center',
                    }}
                >
                    <Box sx={{ flexGrow: 1 }}>
                        <BookCard book={book} />
                    </Box>
                    <Button
                        variant="contained"
                        startIcon={<RateReviewIcon />}
                        onClick={() => props.onAddReview(book.id)}
                        sx={{
                            background: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
                            color: '#fff',
                            px: 3,
                            py: 2,
                            fontSize: '0.95rem',
                            fontWeight: 600,
                            textTransform: 'none',
                            borderRadius: '12px',
                            minWidth: '140px',
                            height: 'fit-content',
                            alignSelf: 'center',
                            boxShadow: '0 4px 12px rgba(245, 87, 108, 0.3)',
                            '&:hover': {
                                background: 'linear-gradient(135deg, #f5576c 0%, #f093fb 100%)',
                                transform: 'translateY(-2px)',
                                boxShadow: '0 6px 20px rgba(245, 87, 108, 0.4)',
                            },
                            transition: 'all 0.3s ease',
                        }}
                    >
                        Add Review
                    </Button>
                </Stack>
            ))}
        </Stack>
    );
}