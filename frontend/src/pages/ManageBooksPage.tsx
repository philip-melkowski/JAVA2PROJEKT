import {
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    InputLabel,
    MenuItem,
    Select,
    Stack,
    TextField,
    Typography
} from "@mui/material";
import {useEffect, useMemo, useState} from "react";
import {
    createBook,
    deleteBook,
    getBooksAdmin,
    type BookDTO,
    type BookSearchParams,
    updateBook
} from "../api/booksApi.ts";

export default function ManageBooksPage() {

    const [books, setBooks] = useState<BookDTO[]>([]);
    const [totalPages, setTotalPages] = useState<number>(0);
    const [totalElements, setTotalElements] = useState<number>(0);

    const [loading, setLoading] = useState<boolean>(false);
    const [isError, setIsError] = useState<boolean>(false);

    const [page, setPage] = useState<number>(0);
    const [size, setSize] = useState<number>(10);
    const [sortBy, setSortBy] = useState<string>("title");
    const [order, setOrder] = useState<"asc" | "desc">("asc");

    const [shouldRefetch, setShouldRefetch] = useState<boolean>(true);

    const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState<boolean>(false);
    const [bookIdToDelete, setBookIdToDelete] = useState<number | null>(null);
    const [isDeleting, setIsDeleting] = useState<boolean>(false);
    const [errorDeleting, setErrorDeleting] = useState<string | null>(null);

    const [isUpsertDialogOpen, setIsUpsertDialogOpen] = useState<boolean>(false);
    const [isEditing, setIsEditing] = useState<boolean>(false);
    const [editingBookId, setEditingBookId] = useState<number | null>(null);

    const [formTitle, setFormTitle] = useState<string>("");
    const [formGenre, setFormGenre] = useState<string>("");
    const [formPublishYear, setFormPublishYear] = useState<number | null>(null);
    const [formAuthorId, setFormAuthorId] = useState<number | null>(null);

    const [isSaving, setIsSaving] = useState<boolean>(false);
    const [errorSaving, setErrorSaving] = useState<string | null>(null);

    // MUSI pasować do backend enum Genre
    const GENRES = useMemo(() => ([
        "FANTASY",
        "SCI_FI",
        "ROMANCE",
        "HISTORY",
        "HORROR",
        "BIOGRAPHY",
        "THRILLER",
        "ADVENTURE",
        "POETRY",
        "DRAMA"
    ]), []);

    const resetUpsertState = () => {
        setIsUpsertDialogOpen(false);
        setIsEditing(false);
        setEditingBookId(null);
        setFormTitle("");
        setFormGenre("");
        setFormPublishYear(null);
        setFormAuthorId(null);
        setErrorSaving(null);
    };

    const openAddDialog = () => {
        setIsEditing(false);
        setEditingBookId(null);
        setFormTitle("");
        setFormGenre("");
        setFormPublishYear(null);
        setFormAuthorId(null);
        setErrorSaving(null);
        setIsUpsertDialogOpen(true);
    };

    const openEditDialog = (b: BookDTO) => {
        setIsEditing(true);
        setEditingBookId(b.id);
        setFormTitle(b.title ?? "");
        setFormGenre(b.genre ?? "");

        // publishYear może przyjść jako string (np. "2020"), więc parsujemy
        const rawYear: any = (b as any).publishYear;
        const yearNum =
            rawYear === null || rawYear === undefined || rawYear === ""
                ? null
                : (typeof rawYear === "number" ? rawYear : Number(rawYear));

        setFormPublishYear(Number.isNaN(yearNum as number) ? null : yearNum);
        setFormAuthorId(typeof b.authorId === "number" ? b.authorId : null);

        setErrorSaving(null);
        setIsUpsertDialogOpen(true);
    };

    useEffect(() => {
        const fetchBooks = async () => {
            try {
                setLoading(true);
                const params: BookSearchParams = {
                    page,
                    size,
                    sortBy,
                    order
                };
                const result = await getBooksAdmin(params);
                setBooks(result.content);
                setTotalPages(result.totalPages);
                setTotalElements(result.totalElements);
                setIsError(false);
            } catch {
                setIsError(true);
            } finally {
                setLoading(false);
            }
        };
        fetchBooks();
    }, [page, size, sortBy, order, shouldRefetch]);

    const isTitleMissing = formTitle.trim().length === 0;
    const isGenreMissing = formGenre.trim().length === 0;
    const isAuthorMissing = formAuthorId === null;

    // publishYear NIE jest wymagane, ale jak podane, to walidujemy zakres
    const isYearInvalid =
        formPublishYear !== null && (Number.isNaN(formPublishYear) || formPublishYear < 0 || formPublishYear > 3000);

    const isFormInvalid =
        isTitleMissing ||
        isGenreMissing ||
        isAuthorMissing ||
        isYearInvalid;

    return <>
        <Stack direction="row" justifyContent="space-between" alignItems="center" sx={{mb: 2}}>
            <Typography variant="h5">Manage books</Typography>
            <Button disabled={loading || isSaving || isDeleting} onClick={openAddDialog}>
                Add book
            </Button>
        </Stack>

        {loading && <Typography variant="h6">Loading...</Typography>}
        {isError && <Typography variant="h6">Error</Typography>}
        {!isError && !loading && books.length === 0 && (
            <Typography variant="h6">No books found.</Typography>
        )}

        {!isError && !loading && books.length > 0 && (
            <>
                <Stack direction="row" sx={{mb: 1}}>
                    <Typography sx={{width: 360, flexShrink: 0}} variant="h6">Title</Typography>
                    <Typography sx={{width: 220, flexShrink: 0}} variant="h6">Author</Typography>
                    <Typography sx={{width: 140, flexShrink: 0}} variant="h6">Genre</Typography>
                    <Typography sx={{width: 90, flexShrink: 0}} variant="h6">Year</Typography>
                    <Typography sx={{width: 140, flexShrink: 0}} variant="h6">Rating</Typography>
                </Stack>

                {books.map(b => (
                    <Stack
                        key={b.id}
                        direction="row"
                        alignItems="center"
                        sx={{py: 0.5}}
                    >
                        <Typography sx={{width: 360, flexShrink: 0}} variant="body1">
                            {b.title}
                        </Typography>
                        <Typography sx={{width: 220, flexShrink: 0}} variant="body1">
                            {b.authorName} {b.authorSurname}
                        </Typography>
                        <Typography sx={{width: 140, flexShrink: 0}} variant="body1">
                            {b.genre}
                        </Typography>
                        <Typography sx={{width: 90, flexShrink: 0}} variant="body1">
                            {(b as any).publishYear ?? "-"}
                        </Typography>
                        <Typography sx={{width: 140, flexShrink: 0}} variant="body1">
                            {b.averageRating === null ? "-" : b.averageRating}
                        </Typography>

                        <Button
                            disabled={isDeleting || isSaving}
                            onClick={() => openEditDialog(b)}
                        >
                            Edit
                        </Button>

                        <Button
                            disabled={isDeleting || isSaving}
                            onClick={() => {
                                setIsDeleteDialogOpen(true);
                                setBookIdToDelete(b.id);
                                setErrorDeleting(null);
                            }}
                        >
                            Delete
                        </Button>
                    </Stack>
                ))}

                <Stack direction="row" spacing={1} alignItems="center" sx={{mt: 2}}>
                    <Button
                        disabled={loading || isSaving || isDeleting || page <= 0}
                        onClick={() => setPage(p => Math.max(0, p - 1))}
                    >
                        Prev
                    </Button>

                    <Typography variant="body2">
                        Page {page + 1} / {Math.max(1, totalPages)} (total: {totalElements})
                    </Typography>

                    <Button
                        disabled={loading || isSaving || isDeleting || totalPages === 0 || page >= totalPages - 1}
                        onClick={() => setPage(p => p + 1)}
                    >
                        Next
                    </Button>

                    <Button
                        disabled={loading || isSaving || isDeleting}
                        onClick={() => setOrder(o => (o === "asc" ? "desc" : "asc"))}
                    >
                        Order: {order}
                    </Button>

                    <Button
                        disabled={loading || isSaving || isDeleting}
                        onClick={() => setSortBy("title")}
                    >
                        Sort: title
                    </Button>

                    <Button
                        disabled={loading || isSaving || isDeleting}
                        onClick={() => setSortBy("publishYear")}
                    >
                        Sort: year
                    </Button>

                    <Select
                        value={String(size)}
                        disabled={loading || isSaving || isDeleting}
                        onChange={(e) => {
                            setPage(0);
                            setSize(Number(e.target.value));
                        }}
                        sx={{ml: 1, width: 120}}
                    >
                        {[5, 10, 20, 50].map(v => (
                            <MenuItem key={v} value={String(v)}>{v} / page</MenuItem>
                        ))}
                    </Select>
                </Stack>
            </>
        )}

        <Dialog
            open={isUpsertDialogOpen}
            onClose={() => {
                if (isSaving || isDeleting) return;
                resetUpsertState();
            }}
            fullWidth
        >
            <DialogTitle>{isEditing ? "Edit book" : "Add book"}</DialogTitle>
            <DialogContent>
                <TextField
                    label="Title"
                    value={formTitle}
                    onChange={(e) => setFormTitle(e.target.value)}
                    fullWidth
                    margin="normal"
                    disabled={isSaving || isDeleting}
                />
                {isTitleMissing && (
                    <Typography color="error">Title is required</Typography>
                )}

                <InputLabel id="genre-select-label" sx={{mt: 2}}>Genre</InputLabel>
                <Select
                    labelId="genre-select-label"
                    value={formGenre}
                    onChange={(e) => setFormGenre(String(e.target.value))}
                    fullWidth
                    disabled={isSaving || isDeleting}
                >
                    {GENRES.map(g => (
                        <MenuItem key={g} value={g}>{g}</MenuItem>
                    ))}
                </Select>
                {isGenreMissing && (
                    <Typography color="error">Genre is required</Typography>
                )}

                <TextField
                    label="Publish year (optional)"
                    type="number"
                    value={formPublishYear ?? ""}
                    onChange={(e) => setFormPublishYear(e.target.value === "" ? null : Number(e.target.value))}
                    fullWidth
                    margin="normal"
                    disabled={isSaving || isDeleting}
                />
                {isYearInvalid && (
                    <Typography color="error">
                        Publish year must be valid
                    </Typography>
                )}

                <TextField
                    label="Author ID"
                    type="number"
                    value={formAuthorId ?? ""}
                    onChange={(e) => setFormAuthorId(e.target.value === "" ? null : Number(e.target.value))}
                    fullWidth
                    margin="normal"
                    disabled={isSaving || isDeleting}
                />
                {isAuthorMissing && (
                    <Typography color="error">Author ID is required</Typography>
                )}

                {errorSaving && (
                    <Typography color="error" sx={{mt: 2}}>
                        {errorSaving}
                    </Typography>
                )}
            </DialogContent>

            <DialogActions>
                <Button
                    disabled={isSaving || isDeleting}
                    onClick={resetUpsertState}
                >
                    Cancel
                </Button>

                <Button
                    disabled={isSaving || isDeleting || isFormInvalid || (isEditing && editingBookId === null)}
                    onClick={async () => {
                        setIsSaving(true);
                        try {
                            const payload: any = {
                                title: formTitle.trim(),
                                genre: formGenre,
                                publishYear: formPublishYear === null ? null : formPublishYear,
                                authorId: Number(formAuthorId)
                            };

                            if (isEditing) {
                                await updateBook(editingBookId!, payload);
                            } else {
                                await createBook(payload);
                            }

                            setShouldRefetch(prev => !prev);
                            resetUpsertState();
                        } catch (err: any) {
                            // apiFetch potrafi rzucać obiektem z polem message
                            const msg = err?.message ? String(err.message) : "Unexpected error";
                            setErrorSaving(msg);
                        } finally {
                            setIsSaving(false);
                        }
                    }}
                >
                    {isSaving ? "Saving..." : (isEditing ? "Save changes" : "Create")}
                </Button>
            </DialogActions>
        </Dialog>

        <Dialog
            open={isDeleteDialogOpen}
            onClose={() => {
                if (isDeleting || isSaving) return;
                setIsDeleteDialogOpen(false);
                setBookIdToDelete(null);
                setErrorDeleting(null);
            }}
        >
            <DialogTitle>Delete book</DialogTitle>
            <DialogContent>
                <Typography>Are you sure you want to delete this book?</Typography>
                {errorDeleting && (
                    <Typography color="error" sx={{mt: 1}}>
                        {errorDeleting}
                    </Typography>
                )}
            </DialogContent>
            <DialogActions>
                <Button
                    disabled={isDeleting || isSaving}
                    onClick={() => {
                        setIsDeleteDialogOpen(false);
                        setBookIdToDelete(null);
                        setErrorDeleting(null);
                    }}
                >
                    Cancel
                </Button>
                <Button
                    disabled={isDeleting || isSaving || bookIdToDelete === null}
                    onClick={async () => {
                        if (bookIdToDelete === null) return;
                        setIsDeleting(true);
                        try {
                            await deleteBook(bookIdToDelete);
                            setShouldRefetch(prev => !prev);
                            setIsDeleteDialogOpen(false);
                            setBookIdToDelete(null);
                            setErrorDeleting(null);
                        } catch (err: any) {
                            const msg = err?.message ? String(err.message) : "Unexpected error";
                            setErrorDeleting(msg);
                        } finally {
                            setIsDeleting(false);
                        }
                    }}
                >
                    {isDeleting ? "Deleting..." : "Delete"}
                </Button>
            </DialogActions>
        </Dialog>
    </>;
}