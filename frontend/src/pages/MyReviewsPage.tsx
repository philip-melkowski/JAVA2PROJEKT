import {Button, Dialog, InputLabel, MenuItem, Select, Stack, TextField, Typography} from "@mui/material";
import {getMyReviews, type ReviewDTO, updateReview} from "../api/reviewApi.ts";
import {useEffect, useState} from "react";


export default function MyReviewsPage() {

    const [myReviews, setMyReviews] = useState<ReviewDTO[]>([]);
    const [loading, setLoading] = useState<boolean>(false);
    const [isError, setIsError] = useState<boolean>(false);
    const [selectedReview, setSelectedReview] = useState<ReviewDTO | null>(null);

    const COMMENT_PREVIEW_LENGTH = 15; // długość tekstu recenzji widocznego w tabeli

    // stany do edit review
    const [isEditDialogOpen, setIsEditDialogOpen] = useState<boolean>(false);
    const [editedReviewId, setEditedReviewId] = useState<number | null>(null);
    const [editedRating, setEditedRating] = useState<number | null>(null);
    const [editedComment, setEditedComment] = useState<string | null>(null);
    const [isSubmitting, setIsSubmitting] = useState<boolean>(false);
    const [editedBookId, setEditedBookId] = useState<number | null>(null);

    // stan do refetch
    const [shouldRefetch, setShouldRefetch] = useState<boolean>(true);


    useEffect(() => {
        const fetchReviews = async () => {
            try {
                setLoading(true);
                const reviews = await getMyReviews();
                setMyReviews(reviews);
                setIsError(false);
            }
            catch
            {
                setIsError(true);
            }
            finally {
                setLoading(false);
            }
        }
        fetchReviews();
    }, [shouldRefetch]);

    return <>
        {loading && <Typography variant="h6">Loading...</Typography>}
        {isError && <Typography variant="h6">Error</Typography>}
        {!isError && !loading && myReviews.length === 0 && <Typography variant="h6">You have not reviewed any books yet!</Typography>}

        <Stack
        direction="row">
            <Typography
                sx = {{width: 160, flexShrink: 0}}
                variant="h6">Author</Typography>
            <Typography
                sx = {{width: 360, flexShrink: 0}}
                variant="h6">Title</Typography>
            <Typography
                sx = {{width: 80, flexShrink: 0}}
                variant="h6">Rating</Typography>
            <Typography
                sx = {{width: 200, flexShrink: 0}}
                variant="h6">Review</Typography>
        </Stack>
        {myReviews.map( rev =>
            (

                <Stack
                    direction="row"
                    alignItems="center"
                    key={rev.id}
                    >
                    <Dialog
                        open={isEditDialogOpen}
                        onClose={() => {
                            setIsEditDialogOpen(false);
                            setEditedReviewId(null);
                            setEditedRating(null);
                            setEditedComment(null);
                            setEditedBookId(null);} }

                    >
                        <Typography>Edit review</Typography>
                        <InputLabel
                            id="rating-select-label">Rating</InputLabel>
                        <Select
                            disabled={isSubmitting}
                            required={true}
                            labelId="rating-select-label"
                            value={editedRating}
                            onChange={(e) => setEditedRating(Number(e.target.value))}
                        >
                            <MenuItem value={1}>1</MenuItem>
                            <MenuItem value={2}>2</MenuItem>
                            <MenuItem value={3}>3</MenuItem>
                            <MenuItem value={4}>4</MenuItem>
                            <MenuItem value={5}>5</MenuItem>
                            <MenuItem value={6}>6</MenuItem>
                            <MenuItem value={7}>7</MenuItem>
                            <MenuItem value={8}>8</MenuItem>
                            <MenuItem value={9}>9</MenuItem>
                            <MenuItem value={10}>10</MenuItem>
                        </Select>
                        <TextField
                            id="review-field"
                            label="Review"
                            multiline
                            rows={4}
                            value={editedComment}
                            onChange={(e) => setEditedComment(e.target.value)}
                        ></TextField>
                        <Stack
                            direction="row"
                        >
                            <Button
                                type="submit"
                                onClick={ async () =>
                                    {
                                        setIsSubmitting(true);
                                        await updateReview({rating: Number(editedRating), comment: editedComment, bookId: Number(editedBookId)}, editedReviewId!);
                                        setShouldRefetch(prev => !prev);
                                        setIsEditDialogOpen(false);
                                        setEditedReviewId(null);
                                        setEditedRating(null);
                                        setEditedComment(null);
                                        setEditedBookId(null);
                                        setIsSubmitting(false);
                                    }
                                }
                            >Edit Review!</Button>
                            <Button onClick={() => {setIsEditDialogOpen(false); setEditedReviewId(null); setEditedRating(null); setEditedComment(null); setEditedBookId(null);}}>Cancel</Button>
                        </Stack>
                    </Dialog>
                    <Dialog
                        open={selectedReview !== null}
                        onClose={() => setSelectedReview(null)}
                    ><Typography>{selectedReview?.comment}</Typography></Dialog>
                    <Typography
                        sx = {{width: 160, flexShrink: 0}}
                        variant="h6">{rev.authorSurname}</Typography>
                    <Typography
                        sx = {{width: 360, flexShrink: 0}}
                        variant="h6">{rev.bookTitle}</Typography>
                    <Typography
                        sx = {{width: 80, flexShrink: 0}}
                        variant="h6">{rev.rating}</Typography>
                    <Typography

                        onClick={() => setSelectedReview(rev)}
                        sx = {{width: 200, flexShrink: 0
                               , cursor: "pointer"
                                , "&:hover": {color: "primary.main"}
                    }}
                        variant="h6">{rev.comment.length-1 > COMMENT_PREVIEW_LENGTH ? rev.comment.slice(0, COMMENT_PREVIEW_LENGTH) + "..." : rev.comment}</Typography>
                    <Button
                        disabled={isSubmitting}
                        onClick={
                            () =>
                                {
                                    setIsEditDialogOpen(true);
                                    setEditedReviewId(rev.id);
                                    setEditedRating(rev.rating);
                                    setEditedComment(rev.comment);
                                    setEditedBookId(rev.bookId);
                                }
                        }
                    >Edit review</Button>
                    <Button>Delete review</Button>
                </Stack>
            )
        )}

    </>;

}