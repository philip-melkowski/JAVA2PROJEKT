import {Autocomplete, Button, MenuItem, Select, TextField} from "@mui/material";
import type {AuthorDTO} from "../api/authorsApi.ts";
import type {Genre} from "../types/Genre.ts";

export type filterArgs =
{
    loading: boolean;
    isError: boolean;

    // author
    authors: AuthorDTO[];
    authorFilter: AuthorDTO | null;
    authorInputValue: string;
    onAuthorChange: (author: AuthorDTO | null) => void;
    onAuthorInputChange: (value: string) => void;

    // genre
    genreFilter: Genre | null;
    genres: readonly Genre[];
    onGenreChange: (genre: Genre | null) => void;

    // title
    onTitleChange: (value: string | null) => void;

    // sortowanie
    sortBy: "title" | "publishYear" | "genre";
    order: "asc" | "desc";
    onSortChange: (value: "title" | "publishYear" | "genre") => void; // ustawienie wartości po której sortować
    onToggleChange: () => void; // zmiana przyciskiem z asc -> desc lub owdronie

    // paginacja
    pageSize: number;
    onPageSizeChange: (size: number) => void;
};
