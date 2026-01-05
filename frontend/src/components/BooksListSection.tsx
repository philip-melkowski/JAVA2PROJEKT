import type {BookDTO} from "../api/booksApi.ts";
import BookCard from "./BookCard.tsx";
import {Button, Divider, Stack, Typography} from "@mui/material";

export type BooksListProps = {
    loading: boolean;
    isError: boolean;
    onRetry: () => void;
    books: BookDTO[];
}

export function BooksListSection(props: BooksListProps) {
    return (<>
        {(props.loading) && !(props.isError) && <Typography variant="h3">Loading...</Typography>}
    {!props.loading && !props.isError && props.books.length === 0 && <Typography variant="h3">No Books to list. Change your filters.</Typography>}
    {!props.loading && props.isError &&
    <Button
        onClick={() =>
        {   props.onRetry(); }
        }
    >Retry loading</Button>
    }
    {!props.loading && !props.isError && props.books.length > 0 &&
        <Stack spacing={2}
               divider={<Divider orientation="horizontal" flexItem/>
                }
    >
        {props.books.map(book => (<Stack
            direction="row">
            <Stack sx={{ width: 420 }}><BookCard key={book.id} book={book}/>
            </Stack>
                <Button
                sx={{ width: 80, height: 40, alignSelf: "center" }}
            variant="contained"
            onClick={() => console.log("ADDED REVIEW for book ID: ", book.id)}
            ></Button></Stack>
        ))}
    </Stack> }
    </>
    );
}