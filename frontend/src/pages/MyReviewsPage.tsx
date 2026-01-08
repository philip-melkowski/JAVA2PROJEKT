import {Button, Dialog, Stack, Typography} from "@mui/material";
import {getMyReviews, type ReviewDTO} from "../api/reviewApi.ts";
import {useEffect, useState} from "react";


export default function MyReviewsPage() {

    const [myReviews, setMyReviews] = useState<ReviewDTO[]>([]);
    const [loading, setLoading] = useState<boolean>(false);
    const [isError, setIsError] = useState<boolean>(false);
    const [selectedReview, setSelectedReview] = useState<ReviewDTO | null>(null);

    const COMMENT_PREVIEW_LENGTH = 15; // długość tekstu recenzji widocznego w tabeli


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
    }, []);

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
                    <Button>Change review</Button>
                    <Button>Delete review</Button>
                </Stack>
            )
        )}

    </>;

}