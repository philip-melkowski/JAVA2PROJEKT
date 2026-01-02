import {Autocomplete, Button, MenuItem, Select, TextField} from "@mui/material";
import type {AuthorDTO} from "../api/authorsApi.ts";
import {type Genre, GENRES} from "../types/Genre.ts";

export type FilterBarProps =
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
    onPageReset: () => void;
};

export function BooksFiltersBar(props: FilterBarProps)
{
    return (
            <>

                <Autocomplete
                    disabled={props.loading || props.isError}
                    id="autocomplete-author-fitler"
                    disablePortal
                    options={props.authors ?? []}
                    getOptionLabel={(option) => option.name + " " + option.surname}
                    isOptionEqualToValue={(option, value) => option.id === value.id}
                    sx={{ width: 350 }}
                    value={props.authorFilter}
                    onChange={(e, newValue) => {
                        props.onAuthorChange(newValue);
                        props.onPageReset();
                    }
                    }

                    inputValue={props.authorInputValue}
                    onInputChange={
                        (e, newInputValue) => props.onAuthorInputChange(newInputValue)}

                    renderInput={(params) => <TextField {...params} label="Author Filter" />}
                />
                <Select
                    disabled={props.loading || props.isError}
                    value={props.genreFilter ?? ""}
                    label="Genre Filter"
                    onChange={(e) => {
                        props.onGenreChange(e.target.value === "" ? null : e.target.value as Genre);
                        props.onPageReset();
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
                    disabled={props.loading || props.isError}
                    id="title-filter-field"
                    label="Filter by Title"
                    type="search"
                    variant="outlined"
                    onChange={(e) =>
                    {
                        props.onTitleChange(e.target.value === "" ? null : e.target.value);
                        props.onPageReset();
                    }
                    }
                />
                <Select
                    disabled={props.loading || props.isError}
                    value={props.sortBy}
                    label="Sort by"
                    onChange={(e) => {
                        props.onSortChange(e.target.value);
                        props.onPageReset();
                    }}
                >
                    <MenuItem value="title">Title</MenuItem>
                    <MenuItem value="publishYear">Publish Year</MenuItem>
                    <MenuItem value="genre">Genre</MenuItem>
                </Select>

                <Button
                    disabled={props.loading || props.isError}
                    variant="contained"
                    onClick={() => props.onToggleChange()}
                >
                    {props.order === "asc" ? "Sort ascending" : "Sort descending"}
                </Button>

                <Select
                    disabled={props.loading || props.isError}
                    value={props.pageSize}
                    label="Books per page"
                    onChange={(e) => {
                        props.onPageSizeChange(Number(e.target.value));
                        props.onPageReset();
                    }}
                >
                    <MenuItem value={3}>3</MenuItem>
                    <MenuItem value={5}>5</MenuItem>
                    <MenuItem value={10}>10</MenuItem>
                </Select>
            </>
    )
}