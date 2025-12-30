import {useEffect, useState} from "react";
import {type BookDTO, getBooks} from "../api/booksApi.ts";
import {Box, Button, Divider, MenuItem, Pagination, Select, Stack, Typography} from "@mui/material";
import BookCard from "../components/BookCard.tsx";


type SortField = "title" | "publishYear" | "genre";
type Order = "asc" | "desc";

export default function BooksPage() {
    const [booksList, setBooksList] = useState<BookDTO[]>([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [pageSize, setPageSize] = useState(3);
    const [totalPages, setTotalPages] = useState<number>(0);
    const [sortBy, setSortBy] = useState<SortField>("title");
    const [order, setOrder] = useState<Order>("desc")
    useEffect( () => {
        const fetchData = async () =>
        {
            const books = await getBooks({
                page: currentPage,
                size: pageSize,
                sortBy: sortBy,
                order: order
            });
            console.log(books);
            setBooksList(books.content);
            setTotalPages(books.totalPages);

        }
        fetchData();
    }, [currentPage, pageSize, sortBy, order]);

    return (
        <Box
            sx={{flexGrow: 1, maxWidth: 550, mx: "auto"}}
        >
            <Stack spacing={5}
                   direction="row">
                <Typography sx={{mt: 4, mb: 2}} variant="h3" component="div">
                    GoodReadsPL
                </Typography>
                <Select
                    value={sortBy}
                    label="Sort by"
                    onChange={(e) => {
                        setSortBy(e.target.value);
                        setCurrentPage(0);
                    }}
                >
                    <MenuItem value="title">Title</MenuItem>
                    <MenuItem value="publishYear">Publish Year</MenuItem>
                    <MenuItem value="genre">Genre</MenuItem>
                </Select>

                <Button
                    variant="contained"
                    onClick={() => setOrder(order === "asc" ? "desc" : "asc")}
                >
                    {order === "asc" ? "Sort ascending" : "Sort descending"}
                </Button>

                <Select
                    value={pageSize}
                    label="Books per page"
                    onChange={(e) => {
                        setPageSize(Number(e.target.value));
                        setCurrentPage(0);
                    }}
                >
                    <MenuItem value={3}>3</MenuItem>
                    <MenuItem value={5}>5</MenuItem>
                    <MenuItem value={10}>10</MenuItem>
                </Select>
            </Stack>
            <Stack spacing={2}
                       divider={<Divider orientation="horizontal" flexItem/>}
        >
            {booksList.map(book => (
                <BookCard key={book.id} book={book}/>
            ))}
        </Stack>
        <Stack spacing={2}>
            <Pagination
                count={totalPages}
                page={currentPage + 1}
                onChange={(e, value) => setCurrentPage(value - 1)}
                variant="outlined"
                color="primary"
            ></Pagination>
        </Stack>
        </Box>

    )
}