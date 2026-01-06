import {Typography} from "@mui/material";
import {getMyReviews, type ReviewDTO} from "../api/reviewApi.ts";
import {useEffect, useState} from "react";


export default function MyReviewsPage() {

    const [myReviews, setMyReviews] = useState<ReviewDTO[]>([]);
    const [loading, setLoading] = useState<boolean>(false);
    const [isError, setIsError] = useState<boolean>(false);


    useEffect(() => {
        getMyReviews()
    }, []);

    return <>
      <Typography>hello</Typography>

    </>;

}