import {useState} from "react";
import type {BookDTO} from "../api/booksApi.ts";



export default function BooksPage() {
    const [booksList, setBooksList] = useState<BookDTO[]>([]);
    return (
    <div>Liczba książek: {booksList.length}</div>
    )
}