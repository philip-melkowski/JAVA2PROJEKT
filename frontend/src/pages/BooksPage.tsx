import {useEffect, useState} from "react";
import {
    Box, Button, Dialog, DialogContent, DialogContentText, DialogTitle, InputLabel, MenuItem,
    Pagination, Select,
    Stack, TextField,
    Typography,
} from "@mui/material";
import {type AuthorDTO} from "../api/authorsApi.ts";
import {type Genre, GENRES} from "../types/Genre.ts";
import {BooksFiltersBar} from "../components/BooksFiltersBar.tsx";
import {BooksListSection} from "../components/BooksListSection.tsx";
import {useBooks} from "../hooks/useBooks.ts";
import {useAuthors} from "../hooks/useAuthors.ts";
import {createReview} from "../api/reviewApi.ts";


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
    const [rating, setRating] = useState<number | "">("");
    const [comment, setComment] = useState<string>("");
    const [reviewError, setReviewError] = useState<string | null>(null);


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
                    <DialogTitle> Add Review of a book You read! </DialogTitle>
                    <DialogContent>
                        <DialogContentText>Choose a rating and optionally add a written review.</DialogContentText>
                        <Typography color="error">{reviewError}</Typography>
                        <form
                            onSubmit={ async (e) => {

                                e.preventDefault(); // nie przeładowuje strony

                                try{
                                await createReview({ rating: Number(rating), comment, bookId: selectedBookId! });
                                resetPage();
                                retry();
                                setSelectedBookId(null);
                                setRating("");
                                setComment("");
                                setReviewError(null);
                            }

                            catch (err) {
                                    if (err instanceof Error) {
                                        setReviewError(err.message);
                                    } else {
                                        setReviewError("Unexpected error");
                                    }
                                }
                        }}
                            id="add-review-form"
                        >
                            <InputLabel
                            id="rating-select-label">Rating</InputLabel>
                         <Select
                             required={true}
                             labelId="rating-select-label"
                             id="rating-select"
                             value={rating}
                             onChange={(e) => setRating(Number(e.target.value))}
                         >
                             <MenuItem value={1}>1</MenuItem>
                             <MenuItem value={2}>2</MenuItem>
                             <MenuItem value={3}>3</MenuItem>
                             <MenuItem value={4}>4</MenuItem>
                             <MenuItem value={5}>5</MenuItem>
                             <MenuItem value={6}>6</MenuItem>
                             <MenuItem value={7}>7</MenuItem>
                             <MenuItem value={8}>8</MenuItem>
                             <MenuItem value={9}>9</MenuItem>
                             <MenuItem value={10}>10</MenuItem>
                         </Select>

                            <TextField
                                id="review-field"
                                label="Review"
                                multiline
                                rows={4}
                                value={comment}
                                onChange={(e) => setComment(e.target.value)}

                            >

                            </TextField>
                        </form>
                    </DialogContent>
                    <Button onClick = {() => {setSelectedBookId(null); setRating("");setReviewError(null);}}>Cancel</Button>
                    <Button form="add-review-form" type="submit">Submit</Button>
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