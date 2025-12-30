import {Card, CardContent, Typography} from "@mui/material";
import type {BookDTO} from "../api/booksApi.ts";

type BookCardProps = {
    book: BookDTO
}

export default function BookCard({book}: BookCardProps) {
    return ( <Card
        variant="outlined">
        <CardContent>
            <Typography
                variant="h5">
                Title: {book.title}
            </Typography>
            <Typography
                variant="subtitle1">
                Author: {book.authorName} {book.authorSurname}
            </Typography>
            <Typography
                variant="body1">
                Publish Year: {book.publishYear}
            </Typography>
            <Typography
                variant="body1">
                Genre: {book.genre}
            </Typography>
            <Typography
                variant="body2">
                Average rating: {book.averageRating}
            </Typography>
        </CardContent>
    </Card> );
}