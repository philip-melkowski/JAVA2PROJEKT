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
    Typography,
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
    const [currentPage, setCurrentPage] = useState<number>(0);
    const [pageSize, setPageSize] = useState(3);
    const [totalPages, setTotalPages] = useState<number>(0);
    const [sortBy, setSortBy] = useState<SortField>("title"); // pole sortowania
    const [order, setOrder] = useState<Order>("desc"); // kolejność sortowania
    const [titleFilter, setTitleFilter] = useState<Title>(null); // tytuł, po którym filtrujemy
    const [debounceTitleFilter, setDebounceTitleFilter] = useState<Title>(null);
    const [genreFilter, setGenreFilter] = useState<Genre>(null); // gatunek, po którym filtrujemy
    const [authorFilter, setAuthorFilter] = useState<Author>(null); // autor po którym filtrujemy
    const [authors, setAuthors] = useState<Author[]>([]); // lista autorów wszytkich
    const [authorInputValue, setAuthorInputValue] = useState<string>(""); // wartość do filtrowania listy autorów
    const [debounceAuthorInputValue, setDebounceAuthorInputValue] = useState<string>("");
    const [loading, setLoading] = useState<boolean>(false);
    const [isError, setIsError] = useState<boolean>(false);

    const fetchBooks = async () =>
    {
        try {
            setLoading(true);
            const books = await getBooks({
                page: currentPage,
                size: pageSize,
                sortBy: sortBy,
                order: order,
                genre: genreFilter,
                authorId: authorFilter?.id,
                title: debounceTitleFilter,
            });
            setBooksList(books.content);
            setTotalPages(books.totalPages);
            setIsError(false);
        }
        catch (e)
        {
            setIsError(true);
        }
        finally {
            setLoading(false);
        }


    }


    useEffect( () => {
        fetchBooks();
    }, [currentPage, pageSize, sortBy, order, genreFilter, authorFilter, debounceTitleFilter]);

    useEffect(() => {
        const timeout = setTimeout(() => {
            setDebounceAuthorInputValue(authorInputValue);
        }, 500);
        return () => clearTimeout(timeout);
    }, [authorInputValue]);

    useEffect(() => {
        const timeout = setTimeout(() => {
            setDebounceTitleFilter(titleFilter);
        }, 500);
        return () => clearTimeout(timeout);
    }, [titleFilter]);

    useEffect(() => {
        const fetchAuthors = async () =>
        {
            const authors = await findCaseInsensitive(
                {
                    fragment: debounceAuthorInputValue
                }
            );
            setAuthors(authors);
        }
        fetchAuthors();
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
                    disabled={loading || isError}
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
                    disabled={loading || isError}
                    value={genreFilter ?? ""}
                    label="Genre Filter"
                    onChange={(e) => {
                        setGenreFilter(e.target.value === "" ? null : e.target.value as Genre);
                        setCurrentPage(0);
                    }}
                >
                    <MenuItem
                        value={""}
                    >All Genres</MenuItem>
                    {
                        GENRES.map(genre => (
                            <MenuItem key={genre} value={genre}>{genre}</MenuItem>
                        ))
                    }
                </Select>
                <TextField
                    disabled={loading || isError}
                    id="title-filter-field"
                    label="Filter by Title"
                    type="search"
                    variant="outlined"
                    onChange={(e) =>
                    {
                        setTitleFilter(e.target.value === "" ? null : e.target.value);
                        setCurrentPage(0);
                    }
                }
                />
                <Select
                    disabled={loading || isError}
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
                    disabled={loading || isError}
                    variant="contained"
                    onClick={() => setOrder(order === "asc" ? "desc" : "asc")}
                >
                    {order === "asc" ? "Sort ascending" : "Sort descending"}
                </Button>

                <Select
                    disabled={loading || isError}
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
            {loading && !isError && <Typography variant="h3">Loading...</Typography>}
            {!loading && !isError && booksList.length === 0 && <Typography variant="h3">No Books to list. Change your filters.</Typography>}
            {!loading && isError &&
                <Button
                    onClick={(e) =>
                    {
                        setIsError(false);
                        fetchBooks();
                    }
                    }
                >Retry loading</Button>
            }
            {!loading && !isError && booksList.length > 0 && <Stack spacing={2}
                       divider={<Divider orientation="horizontal" flexItem/>}
        >
                {booksList.map(book => (
                <BookCard key={book.id} book={book}/>
            ))}
        </Stack> }
        <Stack spacing={2}>
            <Pagination
                disabled={loading}
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