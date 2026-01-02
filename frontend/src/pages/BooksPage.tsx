import {useEffect, useState} from "react";
import {type BookDTO, getBooks} from "../api/booksApi.ts";
import {
    Box,
    Button,
    Divider,
    Pagination,
    Stack,
    Typography,
} from "@mui/material";
import BookCard from "../components/BookCard.tsx";
import {type AuthorDTO, findCaseInsensitive} from "../api/authorsApi.ts";
import {type Genre, GENRES} from "../types/Genre.ts";
import {BooksFiltersBar} from "../components/BooksFiltersBar.tsx";

type SortField = "title" | "publishYear" | "genre";
type Order = "asc" | "desc";
type Title = string | null;
type Author = AuthorDTO | null; // autor, po którym filtrujemy.




export default function BooksPage() {
    const [booksList, setBooksList] = useState<BookDTO[]>([]);
    const [currentPage, setCurrentPage] = useState<number>(0);
    const [pageSize, setPageSize] = useState(3);
    const [totalPages, setTotalPages] = useState<number>(0);
    const [sortBy, setSortBy] = useState<SortField>("title"); // pole sortowania
    const [order, setOrder] = useState<Order>("desc"); // kolejność sortowania
    const [titleFilter, setTitleFilter] = useState<Title>(null); // tytuł, po którym filtrujemy
    const [debounceTitleFilter, setDebounceTitleFilter] = useState<Title>(null);
    const [genreFilter, setGenreFilter] = useState<Genre | null>(null); // gatunek, po którym filtrujemy
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

    function resetPage(): void
    {
        setCurrentPage(0);
    }
    return (
        <Box
            sx={{flexGrow: 1, maxWidth: 550, mx: "auto"}}
        >
            <Stack spacing={5}
                   direction="row">
                <Typography sx={{mt: 4, mb: 2}} variant="h6" component="div">
                    GoodReadsPL
                </Typography>
                <BooksFiltersBar loading={loading} isError={isError} authors={authors} authorFilter={authorFilter} authorInputValue={authorInputValue} onAuthorChange={setAuthorFilter} onAuthorInputChange={setAuthorInputValue} genreFilter={genreFilter} genres={GENRES} onGenreChange={setGenreFilter} onTitleChange={setTitleFilter} sortBy={sortBy} order={order} onSortChange={setSortBy} onToggleChange={() => setOrder(prev => (prev === "asc" ? "desc" : "asc"))} pageSize={pageSize} onPageSizeChange={setPageSize} onPageReset={(resetPage)}></BooksFiltersBar>
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