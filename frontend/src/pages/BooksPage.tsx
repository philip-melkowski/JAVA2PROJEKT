import {useEffect, useState} from "react";
import {
    Box, Dialog, DialogTitle,
    Pagination,
    Stack,
    Typography,
} from "@mui/material";
import {type AuthorDTO} from "../api/authorsApi.ts";
import {type Genre, GENRES} from "../types/Genre.ts";
import {BooksFiltersBar} from "../components/BooksFiltersBar.tsx";
import {BooksListSection} from "../components/BooksListSection.tsx";
import {useBooks} from "../hooks/useBooks.ts";
import {useAuthors} from "../hooks/useAuthors.ts";


type SortField = "title" | "publishYear" | "genre";
type Order = "asc" | "desc";
type Title = string | null;





export default function BooksPage() {
    const [sortBy, setSortBy] = useState<SortField>("title"); // pole sortowania
    const [order, setOrder] = useState<Order>("desc"); // kolejność sortowania
    const [titleFilter, setTitleFilter] = useState<Title>(null); // tytuł, po którym filtrujemy
    const [debounceTitleFilter, setDebounceTitleFilter] = useState<Title>(null);
    const [genreFilter, setGenreFilter] = useState<Genre | null>(null); // gatunek, po którym filtrujemy
    const [authorFilter, setAuthorFilter] = useState<AuthorDTO | null>(null); // autor po którym filtrujemy
    const [selectedBookId, setSelectedBookId] = useState<number | null>(null); // id książki, którą będziemy oceniać


    const {
        booksList,
        currentPage,
        pageSize,
        totalPages,
        loading,
        isError,
        resetPage,
        retry,
        setCurrentPage,
        setPageSize
    } = useBooks({
        sortBy,
        order,
        genre: genreFilter ?? undefined,
        authorId: authorFilter?.id,
        title: debounceTitleFilter ?? undefined
    });

    const {authors, debounceAuthorInputValue, setAuthorInputValue} = useAuthors();


    useEffect(() => {
        const timeout = setTimeout(() => {
            setDebounceTitleFilter(titleFilter);
        }, 100);
        return () => clearTimeout(timeout);
    }, [titleFilter]);


    const handleAddReview = (bookId: number) => {
        setSelectedBookId(bookId);
        console.log("Review dla ksiazki o ID: ", bookId);
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
                <BooksFiltersBar loading={loading} isError={isError} authors={authors} authorFilter={authorFilter} authorInputValue={debounceAuthorInputValue} onAuthorChange={setAuthorFilter} onAuthorInputChange={setAuthorInputValue} genreFilter={genreFilter} genres={GENRES} onGenreChange={setGenreFilter} onTitleChange={setTitleFilter} sortBy={sortBy} order={order} onSortChange={setSortBy} onToggleChange={() => setOrder(prev => (prev === "asc" ? "desc" : "asc"))} pageSize={pageSize} onPageSizeChange={setPageSize} onPageReset={(resetPage)}></BooksFiltersBar>
            </Stack>
            <BooksListSection loading={loading} isError={isError} onRetry={retry} books={booksList} onAddReview={handleAddReview}></BooksListSection>
                <Dialog
                    open={selectedBookId !== null}
                    onClose={() => setSelectedBookId(null)}
                    sx={{mt: 2}}
            >
                    <DialogTitle> Dialog dla książki o ID: {selectedBookId} </DialogTitle>
                </Dialog>
        <Stack spacing={2}>
            <Pagination
                disabled={loading}
                count={totalPages}
                page={currentPage + 1}
                onChange={(_, value) => setCurrentPage(value - 1)}
                variant="outlined"
                color="primary"
            ></Pagination>
        </Stack>
        </Box>

    )
}