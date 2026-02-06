import {useEffect, useState} from "react";
import {
    Box, Button, Dialog, DialogContent, DialogContentText, DialogTitle, InputLabel, MenuItem,
    Pagination, Select,
    Stack, TextField,
    Typography, Container,
} from "@mui/material";
import {type AuthorDTO} from "../api/authorsApi.ts";
import {type Genre, GENRES} from "../types/Genre.ts";
import {BooksFiltersBar} from "../components/BooksFiltersBar.tsx";
import {BooksListSection} from "../components/BooksListSection.tsx";
import {useBooks} from "../hooks/useBooks.ts";
import {useAuthors} from "../hooks/useAuthors.ts";
import {createReview} from "../api/reviewApi.ts";


type SortField = "title" | "publishYear" | "genre";
type Order = "asc" | "desc";
type Title = string | null;




export default function BooksPage() {
    const [sortBy, setSortBy] = useState<SortField>("title");
    const [order, setOrder] = useState<Order>("desc");
    const [titleFilter, setTitleFilter] = useState<Title>(null);
    const [debounceTitleFilter, setDebounceTitleFilter] = useState<Title>(null);
    const [genreFilter, setGenreFilter] = useState<Genre | null>(null);
    const [authorFilter, setAuthorFilter] = useState<AuthorDTO | null>(null);
    const [selectedBookId, setSelectedBookId] = useState<number | null>(null);
    const [rating, setRating] = useState<number | "">("");
    const [comment, setComment] = useState<string>("");
    const [reviewError, setReviewError] = useState<string | null>(null);
    const [isSubmittingReview, setIsSubmittingReview] = useState<boolean>(false);


    const {
        booksList,
        currentPage,
        pageSize,
        totalPages,
        loading,
        isError,
        resetPage,
        retry,
        setCurrentPage,
        setPageSize
    } = useBooks({
        sortBy,
        order,
        genre: genreFilter ?? undefined,
        authorId: authorFilter?.id,
        title: debounceTitleFilter ?? undefined
    });

    const {authors, debounceAuthorInputValue, setAuthorInputValue} = useAuthors();


    useEffect(() => {
        const timeout = setTimeout(() => {
            setDebounceTitleFilter(titleFilter);
            if (titleFilter !== debounceTitleFilter) {
                resetPage();
            }
        }, 300);
        return () => clearTimeout(timeout);
    }, [titleFilter]);


    const handleAddReview = (bookId: number) => {
        setSelectedBookId(bookId);
        console.log("Review dla ksiazki o ID: ", bookId);
    }

    return (
        <Box
            sx={{
                minHeight: '100vh',
                background: 'linear-gradient(to bottom, #f8f9fa 0%, #e9ecef 100%)',
                pb: 6,
            }}
        >
            <Container maxWidth="lg" sx={{ pt: 4 }}>
                <Stack spacing={4}>
                    {/* Header */}
                    <Box sx={{ textAlign: 'center', mb: 2 }}>
                        <Typography
                            variant="h3"
                            sx={{
                                fontWeight: 700,
                                background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                                backgroundClip: 'text',
                                WebkitBackgroundClip: 'text',
                                WebkitTextFillColor: 'transparent',
                                mb: 1,
                            }}
                        >
                            Discover Books
                        </Typography>
                        <Typography
                            variant="subtitle1"
                            sx={{
                                color: '#666',
                                fontSize: '1.1rem',
                            }}
                        >
                            Browse our collection and share your reviews
                        </Typography>
                    </Box>

                    {/* Filters */}
                    <BooksFiltersBar
                        loading={loading}
                        isError={isError}
                        authors={authors}
                        authorFilter={authorFilter}
                        authorInputValue={debounceAuthorInputValue}
                        onAuthorChange={setAuthorFilter}
                        onAuthorInputChange={setAuthorInputValue}
                        genreFilter={genreFilter}
                        genres={GENRES}
                        onGenreChange={setGenreFilter}
                        titleFilter={titleFilter}
                        onTitleChange={setTitleFilter}
                        sortBy={sortBy}
                        order={order}
                        onSortChange={setSortBy}
                        onToggleChange={() => setOrder(prev => (prev === "asc" ? "desc" : "asc"))}
                        pageSize={pageSize}
                        onPageSizeChange={setPageSize}
                        onPageReset={resetPage}
                    />

                    {/* Books List */}
                    <BooksListSection
                        loading={loading}
                        isError={isError}
                        onRetry={retry}
                        books={booksList}
                        onAddReview={handleAddReview}
                    />

                    {/* Pagination */}
                    {!loading && !isError && booksList.length > 0 && (
                        <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}>
                            <Pagination
                                count={totalPages}
                                page={currentPage + 1}
                                onChange={(_, value) => setCurrentPage(value - 1)}
                                size="large"
                                sx={{
                                    '& .MuiPaginationItem-root': {
                                        fontSize: '1rem',
                                        fontWeight: 600,
                                        color: '#667eea',
                                        '&.Mui-selected': {
                                            background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                                            color: '#fff',
                                            '&:hover': {
                                                background: 'linear-gradient(135deg, #764ba2 0%, #667eea 100%)',
                                            }
                                        },
                                        '&:hover': {
                                            backgroundColor: 'rgba(102, 126, 234, 0.1)',
                                        }
                                    }
                                }}
                            />
                        </Box>
                    )}
                </Stack>

                {/* Review Dialog */}
                <Dialog
                    open={selectedBookId !== null}
                    onClose={() => setSelectedBookId(null)}
                    maxWidth="sm"
                    fullWidth
                    slotProps={{
                        paper: {
                            sx: {
                                borderRadius: '16px',
                                p: 2,
                            }
                        }
                    }}
                >
                    <DialogTitle sx={{
                        fontSize: '1.5rem',
                        fontWeight: 700,
                        background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                        backgroundClip: 'text',
                        WebkitBackgroundClip: 'text',
                        WebkitTextFillColor: 'transparent',
                    }}>
                        Add Your Review
                    </DialogTitle>
                    <DialogContent>
                        <DialogContentText sx={{ mb: 3, color: '#666' }}>
                            Share your thoughts about this book
                        </DialogContentText>
                        {reviewError && (
                            <Typography color="error" sx={{ mb: 2 }}>
                                {reviewError}
                            </Typography>
                        )}
                        <form
                            onSubmit={async (e) => {
                                e.preventDefault();
                                setIsSubmittingReview(true);
                                try {
                                    await createReview({ rating: Number(rating), comment, bookId: selectedBookId! });
                                    resetPage();
                                    retry();
                                    setSelectedBookId(null);
                                    setRating("");
                                    setComment("");
                                    setReviewError(null);
                                } catch (err) {
                                    if (err instanceof Error) {
                                        setReviewError(err.message);
                                    } else {
                                        setReviewError("Unexpected error");
                                    }
                                } finally {
                                    setIsSubmittingReview(false);
                                }
                            }}
                            id="add-review-form"
                        >
                            <InputLabel id="rating-select-label" sx={{ mb: 1, fontWeight: 600 }}>
                                Rating (1-10)
                            </InputLabel>
                            <Select
                                disabled={isSubmittingReview}
                                required={true}
                                labelId="rating-select-label"
                                id="rating-select"
                                value={rating}
                                onChange={(e) => setRating(Number(e.target.value))}
                                fullWidth
                                sx={{ mb: 3 }}
                            >
                                {[1, 2, 3, 4, 5, 6, 7, 8, 9, 10].map(num => (
                                    <MenuItem key={num} value={num}>{num}</MenuItem>
                                ))}
                            </Select>

                            <TextField
                                disabled={isSubmittingReview}
                                id="review-field"
                                label="Your Review (optional)"
                                multiline
                                rows={4}
                                value={comment}
                                onChange={(e) => setComment(e.target.value)}
                                fullWidth
                                sx={{ mb: 3 }}
                            />

                            <Stack direction="row" spacing={2} justifyContent="flex-end">
                                <Button
                                    onClick={() => {
                                        setSelectedBookId(null);
                                        setRating("");
                                        setReviewError(null);
                                    }}
                                    sx={{
                                        textTransform: 'none',
                                        color: '#666',
                                        '&:hover': {
                                            backgroundColor: 'rgba(0, 0, 0, 0.05)',
                                        }
                                    }}
                                >
                                    Cancel
                                </Button>
                                <Button
                                    disabled={isSubmittingReview}
                                    form="add-review-form"
                                    type="submit"
                                    variant="contained"
                                    sx={{
                                        background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                                        textTransform: 'none',
                                        px: 4,
                                        fontWeight: 600,
                                        '&:hover': {
                                            background: 'linear-gradient(135deg, #764ba2 0%, #667eea 100%)',
                                        }
                                    }}
                                >
                                    {isSubmittingReview ? 'Submitting...' : 'Submit Review'}
                                </Button>
                            </Stack>
                        </form>
                    </DialogContent>
                </Dialog>
            </Container>
        </Box>
    );
}