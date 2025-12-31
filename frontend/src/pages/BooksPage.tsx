import {useEffect, useState} from "react";
import {type BookDTO, getBooks} from "../api/booksApi.ts";
import {
    Autocomplete,
    Box,
    Button,
    Divider,
    MenuItem,
    Pagination,
    Select,
    Stack,
    TextField,
    Typography
} from "@mui/material";
import BookCard from "../components/BookCard.tsx";
import {type AuthorDTO, findCaseInsensitive} from "../api/authorsApi.ts";


type SortField = "title" | "publishYear" | "genre";
type Order = "asc" | "desc";
type Title = string | null;
type Author = AuthorDTO | null; // autor, po którym filtrujemy.

const GENRES = [
    "FANTASY",
    "SCI_FI",
    "ROMANCE",
    "HISTORY",
    "HORROR",
    "BIOGRAPHY",
    "THRILLER",
    "ADVENTURE",
    "POETRY",
    "DRAMA",
] as const;

type Genre = (typeof GENRES)[number] | null;

export default function BooksPage() {
    const [booksList, setBooksList] = useState<BookDTO[]>([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [pageSize, setPageSize] = useState(3);
    const [totalPages, setTotalPages] = useState<number>(0);
    const [sortBy, setSortBy] = useState<SortField>("title"); // pole sortowania
    const [order, setOrder] = useState<Order>("desc"); // kolejność sortowania
    const [titleFilter, setTitleFilter] = useState<Title>(null); // tytuł, po którym filtrujemy
    const [genreFilter, setGenreFilter] = useState<Genre>(null); // gatunek, po którym filtrujemy
    const [authorFilter, setAuthorFilter] = useState<Author>(null); // autor po którym filtrujemy
    const [authors, setAuthors] = useState<Author[]>([]); // lista autorów wszytkich
    const [authorInputValue, setAuthorInputValue] = useState<string>(""); // wartość do filtrowania listy autorów
    const [debounceAuthorInputValue, setDebounceAuthorInputValue] = useState<string>("");

    useEffect( () => {
        const fetchData = async () =>
        {
            const books = await getBooks({
                page: currentPage,
                size: pageSize,
                sortBy: sortBy,
                order: order,
                genre: genreFilter,
                authorId: authorFilter?.id,
                title: titleFilter,
            });
            setBooksList(books.content);
            setTotalPages(books.totalPages);

        }
        fetchData();
    }, [currentPage, pageSize, sortBy, order, genreFilter, authorFilter, titleFilter]);

    useEffect(() => {
        const timeout = setTimeout(() => {
            setDebounceAuthorInputValue(authorInputValue);
        }, 500);
        return () => clearTimeout(timeout);
    }, [authorInputValue]);

    useEffect(() => {
        const fetchData = async () =>
        {
            const authors = await findCaseInsensitive(
                {
                    fragment: debounceAuthorInputValue
                }
            );
            setAuthors(authors);
        }
        fetchData();
    }, [debounceAuthorInputValue]);

    return (
        <Box
            sx={{flexGrow: 1, maxWidth: 550, mx: "auto"}}
        >
            <Stack spacing={5}
                   direction="row">
                <Typography sx={{mt: 4, mb: 2}} variant="h6" component="div">
                    GoodReadsPL
                </Typography>
                <Autocomplete
                    id="autocomplete-author-fitler"
                    disablePortal
                    options={authors ?? []}
                    getOptionLabel={(option) => option.name + " " + option.surname}
                    isOptionEqualToValue={(option, value) => option.id === value.id}
                    sx={{ width: 350 }}
                    value={authorFilter}
                    onChange={(e, newValue) => {
                        setAuthorFilter(newValue);
                        setCurrentPage(0);
                    }
                }

                    inputValue={authorInputValue}
                    onInputChange={
                    (e, newInputValue) => setAuthorInputValue(newInputValue)}

                    renderInput={(params) => <TextField {...params} label="Author Filter" />}
                />
                <Select
                    value={genreFilter}
                    label="Genre Filter"
                    onChange={(e) => {
                        setGenreFilter(e.target.value as Genre);
                        setCurrentPage(0);
                    }}
                >
                    <MenuItem
                        value={null}
                    >All Genres</MenuItem>
                    {
                        GENRES.map(genre => (
                            <MenuItem key={genre} value={genre}>{genre}</MenuItem>
                        ))
                    }
                </Select>
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