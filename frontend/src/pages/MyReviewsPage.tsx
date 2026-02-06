import {
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    InputLabel,
    MenuItem,
    Select,
    Stack,
    TextField,
    Typography,
    Container,
    Box,
    CircularProgress,
    FormControl,
} from "@mui/material";
import {getMyReviews, type ReviewDTO, updateReview, deleteReview} from "../api/reviewApi.ts";
import {useEffect, useState} from "react";
import ReviewCard from "../components/ReviewCard.tsx";

export default function MyReviewsPage() {

    const [myReviews, setMyReviews] = useState<ReviewDTO[]>([]);
    const [loading, setLoading] = useState<boolean>(false);
    const [isError, setIsError] = useState<boolean>(false);
    const [selectedReview, setSelectedReview] = useState<ReviewDTO | null>(null);

    const MAX_COMMENT_LENGTH = 2000;

    const [isEditDialogOpen, setIsEditDialogOpen] = useState<boolean>(false);
    const [editedReviewId, setEditedReviewId] = useState<number | null>(null);
    const [editedRating, setEditedRating] = useState<number | null>(null);
    const [editedComment, setEditedComment] = useState<string | null>(null);
    const [isSubmitting, setIsSubmitting] = useState<boolean>(false);
    const [editedBookId, setEditedBookId] = useState<number | null>(null);

    const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState<boolean>(false);
    const [reviewIdToDelete, setReviewIdToDelete] = useState<number | null>(null);
    const [isDeleting, setIsDeleting] = useState<boolean>(false);

    const [shouldRefetch, setShouldRefetch] = useState<boolean>(true);
    const [errorEditing, setErrorEditing] = useState<string | null>(null);
    const [errorDeleting, setErrorDeleting] = useState<string | null>(null);

    const commentLength = editedComment?.length ?? 0;
    const isCommentTooLong = commentLength > MAX_COMMENT_LENGTH;
    const isRatingMissing = editedRating === null;

    useEffect(() => {
        const fetchReviews = async () => {
            try {
                setLoading(true);
                const reviews = await getMyReviews();
                setMyReviews(reviews);
                setIsError(false);
            } catch {
                setIsError(true);
            } finally {
                setLoading(false);
            }
        };
        fetchReviews();
    }, [shouldRefetch]);

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
                            My Reviews
                        </Typography>
                        <Typography
                            variant="subtitle1"
                            sx={{
                                color: '#666',
                                fontSize: '1.1rem',
                            }}
                        >
                            Manage your book reviews and ratings
                        </Typography>
                    </Box>

                    {/* Loading State */}
                    {loading && (
                        <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '400px' }}>
                            <Stack spacing={2} alignItems="center">
                                <CircularProgress sx={{ color: '#667eea' }} />
                                <Typography variant="h6" sx={{ color: '#667eea' }}>Loading your reviews...</Typography>
                            </Stack>
                        </Box>
                    )}

                    {/* Error State */}
                    {isError && !loading && (
                        <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '400px' }}>
                            <Typography variant="h6" sx={{ color: '#f44336' }}>Failed to load reviews</Typography>
                        </Box>
                    )}

                    {/* Empty State */}
                    {!isError && !loading && myReviews.length === 0 && (
                        <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '400px' }}>
                            <Stack spacing={2} alignItems="center">
                                <Typography variant="h5" sx={{ color: '#999' }}>
                                    You haven't reviewed any books yet
                                </Typography>
                                <Typography variant="body1" sx={{ color: '#999' }}>
                                    Start exploring books and share your thoughts!
                                </Typography>
                            </Stack>
                        </Box>
                    )}

                    {/* Reviews List */}
                    {!isError && !loading && myReviews.length > 0 && (
                        <Stack spacing={3}>
                            {myReviews.map(review => (
                                <ReviewCard
                                    key={review.id}
                                    review={review}
                                    disabled={isSubmitting || isDeleting}
                                    onEdit={() => {
                                        setIsEditDialogOpen(true);
                                        setEditedReviewId(review.id);
                                        setEditedRating(review.rating);
                                        setEditedComment(review.comment);
                                        setEditedBookId(review.bookId);
                                        setErrorEditing(null);
                                    }}
                                    onDelete={() => {
                                        setIsDeleteDialogOpen(true);
                                        setReviewIdToDelete(review.id);
                                        setErrorDeleting(null);
                                    }}
                                    onViewComment={() => setSelectedReview(review)}
                                />
                            ))}
                        </Stack>
                    )}
                </Stack>

                {/* View Full Comment Dialog */}
                <Dialog
                    open={selectedReview !== null}
                    onClose={() => setSelectedReview(null)}
                    maxWidth="md"
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
                        color: '#333',
                    }}>
                        Full Review
                    </DialogTitle>
                    <DialogContent>
                        <Typography sx={{ color: '#666', fontStyle: 'italic', lineHeight: 1.8 }}>
                            "{selectedReview?.comment}"
                        </Typography>
                    </DialogContent>
                    <DialogActions>
                        <Button
                            onClick={() => setSelectedReview(null)}
                            sx={{
                                textTransform: 'none',
                                fontWeight: 600,
                            }}
                        >
                            Close
                        </Button>
                    </DialogActions>
                </Dialog>

                {/* Edit Dialog */}
                <Dialog
                    open={isEditDialogOpen}
                    onClose={() => {
                        setIsEditDialogOpen(false);
                        setEditedReviewId(null);
                        setEditedRating(null);
                        setEditedComment(null);
                        setEditedBookId(null);
                        setErrorEditing(null);
                    }}
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
                        Edit Your Review
                    </DialogTitle>

                    <DialogContent>
                        <Stack spacing={3} sx={{ mt: 2 }}>
                            <FormControl fullWidth>
                                <InputLabel>Rating (1-10)</InputLabel>
                                <Select
                                    disabled={isSubmitting || isDeleting}
                                    label="Rating (1-10)"
                                    value={editedRating ?? ""}
                                    onChange={(e) => setEditedRating(Number(e.target.value))}
                                >
                                    {[1,2,3,4,5,6,7,8,9,10].map(v =>
                                        <MenuItem key={v} value={v}>{v}</MenuItem>
                                    )}
                                </Select>
                                {isRatingMissing && (
                                    <Typography color="error" variant="caption" sx={{ mt: 1 }}>
                                        Rating is required
                                    </Typography>
                                )}
                            </FormControl>

                            <TextField
                                label="Your Review (optional)"
                                multiline
                                rows={6}
                                value={editedComment ?? ""}
                                onChange={(e) => setEditedComment(e.target.value)}
                                fullWidth
                                disabled={isSubmitting || isDeleting}
                            />

                            <Typography
                                variant="body2"
                                sx={{
                                    color: isCommentTooLong ? '#f44336' : '#999',
                                    textAlign: 'right',
                                }}
                            >
                                {commentLength} / {MAX_COMMENT_LENGTH}
                            </Typography>

                            {isCommentTooLong && (
                                <Typography color="error" variant="body2">
                                    Comment is too long
                                </Typography>
                            )}

                            {errorEditing && (
                                <Typography color="error">{errorEditing}</Typography>
                            )}
                        </Stack>
                    </DialogContent>

                    <DialogActions sx={{ px: 3, pb: 2 }}>
                        <Button
                            disabled={isSubmitting || isDeleting}
                            onClick={() => {
                                setIsEditDialogOpen(false);
                                setEditedReviewId(null);
                                setEditedRating(null);
                                setEditedComment(null);
                                setEditedBookId(null);
                                setErrorEditing(null);
                            }}
                            sx={{
                                textTransform: 'none',
                                color: '#666',
                            }}
                        >
                            Cancel
                        </Button>
                        <Button
                            disabled={
                                isSubmitting ||
                                editedReviewId === null ||
                                editedBookId === null ||
                                isRatingMissing ||
                                isCommentTooLong
                            }
                            variant="contained"
                            onClick={async () => {
                                if (
                                    editedReviewId === null ||
                                    editedRating === null ||
                                    editedBookId === null
                                ) {
                                    return;
                                }
                                setIsSubmitting(true);
                                try {
                                    await updateReview({
                                        rating: editedRating,
                                        comment: editedComment,
                                        bookId: editedBookId
                                    }, editedReviewId);
                                    setShouldRefetch(prev => !prev);
                                    setIsEditDialogOpen(false);
                                    setEditedReviewId(null);
                                    setEditedRating(null);
                                    setEditedComment(null);
                                    setEditedBookId(null);
                                    setErrorEditing(null);
                                } catch (err) {
                                    if (err instanceof Error) {
                                        setErrorEditing(err.message);
                                    } else {
                                        setErrorEditing("Unexpected error");
                                    }
                                } finally {
                                    setIsSubmitting(false);
                                }
                            }}
                            sx={{
                                background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                                textTransform: 'none',
                                px: 3,
                                fontWeight: 600,
                                '&:hover': {
                                    background: 'linear-gradient(135deg, #764ba2 0%, #667eea 100%)',
                                }
                            }}
                        >
                            {isSubmitting ? "Saving..." : "Save Changes"}
                        </Button>
                    </DialogActions>
                </Dialog>

                {/* Delete Confirmation Dialog */}
                <Dialog
                    open={isDeleteDialogOpen}
                    onClose={() => {
                        if (isDeleting) return;
                        setIsDeleteDialogOpen(false);
                        setReviewIdToDelete(null);
                        setErrorDeleting(null);
                    }}
                    maxWidth="xs"
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
                    <DialogTitle sx={{ fontSize: '1.3rem', fontWeight: 700, color: '#f44336' }}>
                        Delete Review?
                    </DialogTitle>
                    <DialogContent>
                        <Typography sx={{ color: '#666' }}>
                            Are you sure you want to delete this review? This action cannot be undone.
                        </Typography>
                        {errorDeleting && (
                            <Typography color="error" sx={{mt: 2}}>
                                {errorDeleting}
                            </Typography>
                        )}
                    </DialogContent>
                    <DialogActions sx={{ px: 3, pb: 2 }}>
                        <Button
                            disabled={isDeleting || isSubmitting}
                            onClick={() => {
                                setIsDeleteDialogOpen(false);
                                setReviewIdToDelete(null);
                                setErrorDeleting(null);
                            }}
                            sx={{
                                textTransform: 'none',
                                color: '#666',
                            }}
                        >
                            Cancel
                        </Button>
                        <Button
                            disabled={isDeleting || isSubmitting || reviewIdToDelete === null}
                            variant="contained"
                            onClick={async () => {
                                if(reviewIdToDelete === null) return;
                                setIsDeleting(true);
                                try {
                                    await deleteReview(reviewIdToDelete);
                                    setShouldRefetch(prev => !prev);
                                    setIsDeleteDialogOpen(false);
                                    setReviewIdToDelete(null);
                                    setErrorDeleting(null);
                                } catch (err) {
                                    if (err instanceof Error) {
                                        setErrorDeleting(err.message);
                                    }
                                    else {
                                        setErrorDeleting("Unexpected error");
                                    }
                                }
                                finally {
                                    setIsDeleting(false);
                                }
                            }}
                            sx={{
                                backgroundColor: '#f44336',
                                textTransform: 'none',
                                px: 3,
                                fontWeight: 600,
                                '&:hover': {
                                    backgroundColor: '#d32f2f',
                                }
                            }}
                        >
                            {isDeleting ? "Deleting..." : "Delete"}
                        </Button>
                    </DialogActions>
                </Dialog>
            </Container>
        </Box>
    );
}