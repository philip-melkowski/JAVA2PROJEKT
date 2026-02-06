import {Autocomplete, Button, MenuItem, Select, TextField, Box, Stack, Paper, InputLabel, FormControl, Chip} from "@mui/material";
import type {AuthorDTO} from "../api/authorsApi.ts";
import {type Genre} from "../types/Genre.ts";
import FilterListIcon from '@mui/icons-material/FilterList';
import SortIcon from '@mui/icons-material/Sort';
import SwapVertIcon from '@mui/icons-material/SwapVert';
import TitleFilterInput from "./TitleFilterInput.tsx";

type GenreSelectValue = Genre | "";

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
        titleFilter: string | null;
        onTitleChange: (value: string | null) => void;

        // sortowanie
        sortBy: "title" | "publishYear" | "genre";
        order: "asc" | "desc";
        onSortChange: (value: "title" | "publishYear" | "genre") => void;
        onToggleChange: () => void;

        // paginacja
        pageSize: number;
        onPageSizeChange: (size: number) => void;
        onPageReset: () => void;
    };

export function BooksFiltersBar(props: FilterBarProps)
{
    return (
        <Paper
            elevation={0}
            sx={{
                p: 3,
                borderRadius: '16px',
                border: '1px solid #e0e0e0',
                background: '#fff',
            }}
        >
            <Stack spacing={3}>
                {/* Sekcja filtrów */}
                <Box>
                    <Stack direction="row" spacing={1} alignItems="center" sx={{ mb: 2 }}>
                        <FilterListIcon sx={{ color: '#667eea' }} />
                        <InputLabel sx={{ fontWeight: 600, color: '#333', fontSize: '1rem' }}>
                            Filters
                        </InputLabel>
                    </Stack>

                    <Stack direction="row" spacing={2} flexWrap="wrap" useFlexGap>
                        {/* Search by title */}
                        <TitleFilterInput
                            value={props.titleFilter}
                            onChange={props.onTitleChange}
                            disabled={props.isError}
                        />

                        {/* Author filter */}
                        <Autocomplete
                            disabled={props.isError}
                            disablePortal
                            options={props.authors ?? []}
                            getOptionLabel={(option) => option.name + " " + option.surname}
                            isOptionEqualToValue={(option, value) => option.id === value.id}
                            size="small"
                            sx={{ minWidth: '250px', flex: 1 }}
                            value={props.authorFilter}
                            onChange={(_, newValue) => {
                                props.onAuthorChange(newValue);
                                props.onPageReset();
                            }}
                            inputValue={props.authorInputValue}
                            onInputChange={(_, newInputValue) => props.onAuthorInputChange(newInputValue)}
                            renderInput={(params) => <TextField {...params} label="Filter by author" />}
                        />

                        {/* Genre filter */}
                        <FormControl size="small" sx={{ minWidth: '180px' }}>
                            <InputLabel>Genre</InputLabel>
                            <Select
                                disabled={props.isError}
                                value={props.genreFilter ?? ""}
                                label="Genre"
                                onChange={(e) => {
                                    const genreValue = e.target.value as GenreSelectValue;
                                    props.onGenreChange(genreValue === "" ? null : e.target.value as Genre);
                                    props.onPageReset();
                                }}
                            >
                                <MenuItem value="">All Genres</MenuItem>
                                {props.genres.map(genre => (
                                    <MenuItem key={genre} value={genre}>{genre}</MenuItem>
                                ))}
                            </Select>
                        </FormControl>
                    </Stack>
                </Box>

                {/* Sekcja sortowania i paginacji */}
                <Box sx={{ borderTop: '1px solid #e0e0e0', pt: 2 }}>
                    <Stack direction="row" spacing={2} alignItems="center" flexWrap="wrap" useFlexGap>
                        <Stack direction="row" spacing={1} alignItems="center">
                            <SortIcon sx={{ color: '#667eea', fontSize: 20 }} />
                            <InputLabel sx={{ fontWeight: 600, color: '#333', fontSize: '0.9rem', mr: 1 }}>
                                Sort:
                            </InputLabel>
                        </Stack>

                        <FormControl size="small" sx={{ minWidth: '140px' }}>
                            <Select
                                disabled={props.isError}
                                value={props.sortBy}
                                onChange={(e) => {
                                    props.onSortChange(e.target.value);
                                    props.onPageReset();
                                }}
                            >
                                <MenuItem value="title">Title</MenuItem>
                                <MenuItem value="publishYear">Year</MenuItem>
                                <MenuItem value="genre">Genre</MenuItem>
                            </Select>
                        </FormControl>

                        <Button
                            disabled={props.isError}
                            variant="outlined"
                            size="small"
                            startIcon={<SwapVertIcon />}
                            onClick={() => props.onToggleChange()}
                            sx={{
                                textTransform: 'none',
                                borderColor: '#667eea',
                                color: '#667eea',
                                fontWeight: 500,
                                '&:hover': {
                                    borderColor: '#764ba2',
                                    backgroundColor: 'rgba(102, 126, 234, 0.05)',
                                }
                            }}
                        >
                            {props.order === "asc" ? "Ascending" : "Descending"}
                        </Button>

                        <Box sx={{ flexGrow: 1 }} />

                        <Stack direction="row" spacing={1} alignItems="center">
                            <InputLabel sx={{ fontWeight: 500, color: '#666', fontSize: '0.9rem' }}>
                                Per page:
                            </InputLabel>
                            <FormControl size="small" sx={{ minWidth: '80px' }}>
                                <Select
                                    disabled={props.isError}
                                    value={props.pageSize}
                                    onChange={(e) => {
                                        props.onPageSizeChange(Number(e.target.value));
                                        props.onPageReset();
                                    }}
                                >
                                    <MenuItem value={3}>3</MenuItem>
                                    <MenuItem value={5}>5</MenuItem>
                                    <MenuItem value={10}>10</MenuItem>
                                    <MenuItem value={20}>20</MenuItem>
                                </Select>
                            </FormControl>
                        </Stack>
                    </Stack>
                </Box>

                {/* Active filters chips */}
                {(props.authorFilter || props.genreFilter || props.titleFilter) && (
                    <Stack direction="row" spacing={1} sx={{ pt: 1 }} flexWrap="wrap" useFlexGap>
                        {props.titleFilter && (
                            <Chip
                                label={`Title: "${props.titleFilter}"`}
                                onDelete={() => {
                                    props.onTitleChange(null);
                                    props.onPageReset();
                                }}
                                size="small"
                                sx={{
                                    backgroundColor: 'rgba(102, 126, 234, 0.1)',
                                    color: '#667eea',
                                    fontWeight: 500,
                                }}
                            />
                        )}
                        {props.authorFilter && (
                            <Chip
                                label={`Author: ${props.authorFilter.name} ${props.authorFilter.surname}`}
                                onDelete={() => {
                                    props.onAuthorChange(null);
                                    props.onPageReset();
                                }}
                                size="small"
                                sx={{
                                    backgroundColor: 'rgba(102, 126, 234, 0.1)',
                                    color: '#667eea',
                                    fontWeight: 500,
                                }}
                            />
                        )}
                        {props.genreFilter && (
                            <Chip
                                label={`Genre: ${props.genreFilter}`}
                                onDelete={() => {
                                    props.onGenreChange(null);
                                    props.onPageReset();
                                }}
                                size="small"
                                sx={{
                                    backgroundColor: 'rgba(102, 126, 234, 0.1)',
                                    color: '#667eea',
                                    fontWeight: 500,
                                }}
                            />
                        )}
                    </Stack>
                )}
            </Stack>
        </Paper>
    )
}