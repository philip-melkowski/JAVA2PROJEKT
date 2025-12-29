import {useEffect, useState} from "react";
import {type BookDTO, getBooks} from "../api/booksApi.ts";
import {Box, Divider, Stack, Typography} from "@mui/material";
import BookCard from "../components/BookCard.tsx";




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
            sx = {{flexGrow: 1, maxWidth: 550, mx: "auto"}}
        >
        <Typography sx = {{mt: 4, mb:2 }} variant="h3" component="div">
            Książki ilosc: {booksList.length}
        </Typography>
            <Stack spacing={2}
                   divider = {<Divider orientation="horizontal" flexItem />}
            >


            {booksList.map(book =>
                (
                    <BookCard key={book.id} book={book} />
                ))}
            </Stack>
        </Box>

    )
}