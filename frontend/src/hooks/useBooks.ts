import {useCallback, useEffect, useState} from "react";
import {type BookDTO, getBooks} from "../api/booksApi.ts";

export type GetBooksProps = {

    sortBy: string;
    order: string;
    genre?: string;
    authorId?: number;
    title?: string;

}


export function useBooks(props: GetBooksProps) {

    const [booksList, setBooksList] = useState<BookDTO[]>([]);
    const [currentPage, setCurrentPage] = useState<number>(0);
    const [pageSize, setPageSize] = useState(3);
    const [totalPages, setTotalPages] = useState<number>(0);
    const [loading, setLoading] = useState<boolean>(false);
    const [isError, setIsError] = useState<boolean>(false);




    const fetchBooks = useCallback(async () => {

            try {
                setLoading(true);
                const books = await getBooks({
                    page: currentPage,
                    size: pageSize,
                    sortBy: props.sortBy,
                    order: props.order,
                    authorId: props.authorId,
                    ...(props.genre ? {genre: props.genre} : {}),
                    ...(props.title ? {title: props.title} : {})
                });
                setBooksList(books.content);
                setTotalPages(books.totalPages);
                setIsError(false);
            }
            catch
            {
                setIsError(true);
            }
            finally {
                setLoading(false);
            }

        }
        ,[
        props.sortBy,
        props.order,
        props.genre,
        props.authorId,
        props.title,
        currentPage,
        pageSize
            ]);

    useEffect( () => {
        fetchBooks();
    }, [fetchBooks]);

    function resetPage() : void
    {
        setCurrentPage(0);
    }

    function retry() : void
    {
        setIsError(false);
        fetchBooks();
    }

    return {
        booksList, currentPage, pageSize, totalPages, loading, isError, fetchBooks, resetPage, retry, setPageSize, setCurrentPage
    }
}