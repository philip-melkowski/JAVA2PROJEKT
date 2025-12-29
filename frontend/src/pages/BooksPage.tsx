import {useEffect, useState} from "react";
import {type BookDTO, getBooks} from "../api/booksApi.ts";
import {Box, Card, Divider, Stack, Typography, CardContent} from "@mui/material";




export default function BooksPage() {
    const [booksList, setBooksList] = useState<BookDTO[]>([]);
    useEffect( () => {
        const fetchData = async () =>
        {
            const books = await getBooks({
                page: 0,
                size: 10,
                sortBy: "title",
                order: "asc"
            });
            console.log(books);
            setBooksList(books.content);

        }
        fetchData();
    }, []);

    return (

        <Box
            sx = {{flexGrow: 1, maxWidth: 550}}
        >
        <Typography sx = {{mt: 4, mb:2 }} variant="h3" component="div">
            Książki
        </Typography>
            <Stack spacing={2}
                   divider = {<Divider orientation="horizontal" flexItem />}
            >


            {booksList.map(book =>
                (
                    <Card
                        variant="outlined">
                        <CardContent>
                        <Typography
                            variant="h5">
                            Title: {book.title}
                        </Typography>
                        <Typography
                            variant="h6">
                            Author: {book.authorName} {book.authorSurname}
                        </Typography>
                        <Typography
                            variant="h6">
                            Average rating: {book.averageRating}
                        </Typography>
                        </CardContent>
                    </Card>
                ))}
            </Stack>
        </Box>

    )
}