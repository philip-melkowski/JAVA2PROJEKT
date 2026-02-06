    import {apiFetch} from "./api.ts";

    export type BookDTO =
        {
            id: number;
            title: string;
            genre: string;
            publishYear: number;
            authorId: number;
            authorName: string;
            authorSurname: string;
            averageRating: number | null;
        };

    export type Page<T> = {
        content: T[];
        totalElements: number;
        totalPages: number;
        number: number; // aktualna strona
        size: number;
    };

    export type BookSearchParams =
        {
            page?: number;
            size?: number;
            sortBy?: string;
            order?: string;
            genre?: string;
            authorId?: number;
            title?: string;

        };



    export function getBooks(params: BookSearchParams) : Promise<Page<BookDTO>>{
        const query = new URLSearchParams();
        if(params.page != undefined) query.append("page", params.page.toString());
        if(params.size != undefined) query.append("size", params.size.toString());
        if(params.sortBy != undefined) query.append("sortBy", params.sortBy);
        if(params.order != undefined) query.append("order", params.order);
        if(params.genre != undefined) query.append("genre", params.genre);
        if(params.authorId != undefined) query.append("authorId", params.authorId.toString());
        if(params.title != undefined) query.append("title", params.title);

        return apiFetch(`api/books/search?${query.toString()}`);
    }

    export function getBooksAdmin(params: {
        page: number;
        size: number;
        sortBy: string;
        order: "asc" | "desc";
        genre?: string;
        authorId?: number;
        title?: string;
    }) : Promise<Page<BookDTO>> {
        const qs = new URLSearchParams();
        qs.set("page", String(params.page));
        qs.set("size", String(params.size));
        qs.set("sortBy", params.sortBy);
        qs.set("order", params.order);
        if (params.genre) qs.set("genre", params.genre);
        if (params.authorId !== undefined) qs.set("authorId", String(params.authorId));
        if (params.title) qs.set("title", params.title);

        return apiFetch(`api/books/admin/search?${qs.toString()}`);
    }

    export type CreateBookRequest = {
        title: string;
        genre: string;
        publishYear: number;
        authorId: number;
    };

    export type UpdateBookRequest = CreateBookRequest;

    export function createBook(params: CreateBookRequest): Promise<BookDTO> {
        return apiFetch("api/books", { method: "POST", body: JSON.stringify(params) });
    }

    export function updateBook(bookId: number, params: UpdateBookRequest): Promise<BookDTO> {
        return apiFetch(`api/books/${bookId}`, { method: "PUT", body: JSON.stringify(params) });
    }

    export function deleteBook(bookId: number): Promise<void> {
        return apiFetch(`api/books/${bookId}`, { method: "DELETE" });
    }