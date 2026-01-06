import {Button, Stack, Typography} from "@mui/material";
import {getMyReviews, type ReviewDTO} from "../api/reviewApi.ts";
import {useEffect, useState} from "react";


export default function MyReviewsPage() {

    const [myReviews, setMyReviews] = useState<ReviewDTO[]>([]);
    const [loading, setLoading] = useState<boolean>(false);
    const [isError, setIsError] = useState<boolean>(false);


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

        {myReviews.map( rev =>
            (
                <Stack
                    direction="row"
                    key={rev.id}
                    >
                    <Typography variant="h6">{rev.authorSurname}</Typography>
                    <Typography variant="h6">{rev.bookTitle}</Typography>
                    <Button>Change review</Button>
                    <Button>Delete review</Button>
                </Stack>
            )
        )}

    </>;

}