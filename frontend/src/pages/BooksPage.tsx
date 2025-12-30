import {useEffect, useState} from "react";
import {type BookDTO, getBooks} from "../api/booksApi.ts";
import {Box, Divider, MenuItem, Pagination, Select, Stack, Typography} from "@mui/material";
import BookCard from "../components/BookCard.tsx";




export default function BooksPage() {
    const [booksList, setBooksList] = useState<BookDTO[]>([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [pageSize, setPageSize] = useState(3);
    const [totalPages, setTotalPages] = useState<number>(0);
    useEffect( () => {
        const fetchData = async () =>
        {
            const books = await getBooks({
                page: currentPage,
                size: pageSize,
                sortBy: "title",
                order: "asc"
            });
            console.log(books);
            setBooksList(books.content);
            setTotalPages(books.totalPages);

        }
        fetchData();
    }, [currentPage, pageSize]);

    return (

        <Box
            sx = {{flexGrow: 1, maxWidth: 550, mx: "auto"}}
        >
            <Stack spacing={44}
            direction="row">
        <Typography sx = {{mt: 4, mb:2 }} variant="h3" component="div">
            Książki
        </Typography>
                <Select
                    value={pageSize}
                    label="Books per page"
                    onChange={(e) =>
                    {
                        setPageSize(Number(e.target.value));
                        setCurrentPage(0);
                    }
                    }
                >
                    <MenuItem value={3}>3</MenuItem>
                    <MenuItem value={5}>5</MenuItem>
                    <MenuItem value={10}>10</MenuItem>
                </Select>
            </Stack>
            <Stack spacing={2}
                   divider = {<Divider orientation="horizontal" flexItem />}
            >
            {booksList.map(book =>
                (
                    <BookCard key={book.id} book={book} />
                ))}
            </Stack>
            <Stack spacing={2}>
            <Pagination
                count={totalPages}
                page= {currentPage + 1}
                onChange={(e, value) => setCurrentPage(value-1)}
                variant="outlined"
                color="primary"
            ></Pagination>
            </Stack>
        </Box>

    )
}