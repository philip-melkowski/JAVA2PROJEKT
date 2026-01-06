import {Button, Stack, Typography} from "@mui/material";
import {getMyReviews, type ReviewDTO} from "../api/reviewApi.ts";
import {useEffect, useState} from "react";


export default function MyReviewsPage() {

    const [myReviews, setMyReviews] = useState<ReviewDTO[]>([]);
    const [loading, setLoading] = useState<boolean>(false);
    const [isError, setIsError] = useState<boolean>(false);


    useEffect(() => {
        const reviews = getMyReviews();
        setMyReviews(reviews);
    }, []);

    return <>
        {myReviews.map( rev =>
            (
                <Stack
                    direction="row"
                    key={rev.id}
                >   <Typography variant="h6">{rev.bookId}</Typography>
                    <Button>Change review</Button>
                    <Button>Delete review</Button>
                </Stack>
            )
        )}

    </>;

}