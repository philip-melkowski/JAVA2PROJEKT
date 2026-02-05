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
    Typography
} from "@mui/material";
import {getMyReviews, type ReviewDTO, updateReview} from "../api/reviewApi.ts";
import {useEffect, useState} from "react";

export default function MyReviewsPage() {

    const [myReviews, setMyReviews] = useState<ReviewDTO[]>([]);
    const [loading, setLoading] = useState<boolean>(false);
    const [isError, setIsError] = useState<boolean>(false);
    const [selectedReview, setSelectedReview] = useState<ReviewDTO | null>(null);

    const COMMENT_PREVIEW_LENGTH = 15;
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

    return <>
        {loading && <Typography variant="h6">Loading...</Typography>}
        {isError && <Typography variant="h6">Error</Typography>}
        {!isError && !loading && myReviews.length === 0 &&
            <Typography variant="h6">You have not reviewed any books yet!</Typography>
        }

            <Stack direction="row">
                <Typography sx={{width: 160, flexShrink: 0}} variant="h6">Author</Typography>
                <Typography sx={{width: 360, flexShrink: 0}} variant="h6">Title</Typography>
                <Typography sx={{width: 80, flexShrink: 0}} variant="h6">Rating</Typography>
                <Typography sx={{width: 200, flexShrink: 0}} variant="h6">Review</Typography>
            </Stack>

            {myReviews.map(rev => (
                <Stack direction="row" alignItems="center" key={rev.id}>

                    <Dialog
                        open={selectedReview !== null}
                        onClose={() => setSelectedReview(null)}
                    >
                        <DialogContent>
                            <Typography>{selectedReview?.comment}</Typography>
                        </DialogContent>
                    </Dialog>

                    <Typography sx={{width: 160, flexShrink: 0}} variant="h6">
                        {rev.authorSurname}
                    </Typography>

                    <Typography sx={{width: 360, flexShrink: 0}} variant="h6">
                        {rev.bookTitle}
                    </Typography>

                    <Typography sx={{width: 80, flexShrink: 0}} variant="h6">
                        {rev.rating}
                    </Typography>

                    <Typography
                        onClick={() => setSelectedReview(rev)}
                        sx={{
                            width: 200,
                            flexShrink: 0,
                            cursor: "pointer",
                            "&:hover": {color: "primary.main"}
                        }}
                        variant="h6"
                    >
                        {rev.comment.length > COMMENT_PREVIEW_LENGTH
                            ? rev.comment.slice(0, COMMENT_PREVIEW_LENGTH) + "..."
                            : rev.comment}
                    </Typography>

                    <Button
                        disabled={isSubmitting}
                        onClick={() => {
                            setIsEditDialogOpen(true);
                            setEditedReviewId(rev.id);
                            setEditedRating(rev.rating);
                            setEditedComment(rev.comment);
                            setEditedBookId(rev.bookId);
                            setErrorEditing(null);
                        }}
                    >
                        Edit review
                    </Button>

                    <Button>Delete review</Button>
                </Stack>
            ))}

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
            >
                <DialogTitle>Edit review</DialogTitle>

                <DialogContent>
                    <InputLabel id="rating-select-label">Rating</InputLabel>
                    <Select
                        disabled={isSubmitting}
                        labelId="rating-select-label"
                        value={editedRating ?? ""}
                        onChange={(e) => setEditedRating(Number(e.target.value))}
                        fullWidth
                    >
                        {[1,2,3,4,5,6,7,8,9,10].map(v =>
                            <MenuItem key={v} value={v}>{v}</MenuItem>
                        )}
                    </Select>

                    {isRatingMissing && (
                        <Typography color="error">
                            Rating is required
                        </Typography>
                    )}

                    <TextField
                        label="Review"
                        multiline
                        rows={4}
                        value={editedComment ?? ""}
                        onChange={(e) => setEditedComment(e.target.value)}
                        fullWidth
                        margin="normal"
                    />

                    <Typography
                        variant="body2"
                        color={isCommentTooLong ? "error" : "text.secondary"}
                    >
                        {commentLength} / {MAX_COMMENT_LENGTH}
                    </Typography>

                    {isCommentTooLong && (
                        <Typography color="error">
                            Comment is too long
                        </Typography>
                    )}

                    {errorEditing && (
                        <Typography color="error">{errorEditing}</Typography>
                    )}
                </DialogContent>

                <DialogActions>
                    <Button
                        disabled={
                            isSubmitting ||
                            editedReviewId === null ||
                            editedBookId === null ||
                            isRatingMissing ||
                            isCommentTooLong
                        }
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
                    >
                        {isSubmitting ? "Saving..." : "Edit review"}
                    </Button>

                    <Button
                        disabled={isSubmitting}
                        onClick={() => {
                            setIsEditDialogOpen(false);
                            setEditedReviewId(null);
                            setEditedRating(null);
                            setEditedComment(null);
                            setEditedBookId(null);
                            setErrorEditing(null);
                        }}
                    >
                        Cancel
                    </Button>
                </DialogActions>
            </Dialog>
    </>;
}