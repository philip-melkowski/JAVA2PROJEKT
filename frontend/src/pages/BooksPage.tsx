import {useEffect, useState} from "react";
import {type BookDTO, getBooks} from "../api/booksApi.ts";




export default function BooksPage() {
    const [booksList, setBooksList] = useState<BookDTO[]>([]);
    useEffect( () => {
        const fetchData = async () =>
        {
            const books = await getBooks({
                page: 0,
                size: 10,
                sortBy: "title",
                order: "asc"
            });
            console.log(books);
            setBooksList(books.content);

        }
        fetchData();
    }, []);
    return (
    <div>Liczba książek: {booksList.length}</div>
    )
}