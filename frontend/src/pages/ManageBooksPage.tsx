import {useBooks} from "../hooks/useBooks.ts";



export default function ManageBooksPage() {


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
}