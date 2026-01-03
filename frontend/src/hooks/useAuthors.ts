import {useEffect, useState} from "react";
import {type AuthorDTO, findCaseInsensitive} from "../api/authorsApi.ts";



export function useAuthors() {

    const [authors, setAuthors] = useState<AuthorDTO[]>([]);
    const [authorInputValue, setAuthorInputValue] = useState<string>(""); // wartość do filtrowania listy autorów
    const [debounceAuthorInputValue, setDebounceAuthorInputValue] = useState<string>("");

    useEffect(() => {
        const fetchAuthors = async () =>
        {
            const authors = await findCaseInsensitive(
                {
                    fragment: debounceAuthorInputValue,
                }
            );
            setAuthors(authors);
        }
        fetchAuthors();
    }, [debounceAuthorInputValue]);

    useEffect(() => {
        const timeout = setTimeout(() => {
            setDebounceAuthorInputValue(authorInputValue);
        }, 500);
        return () => clearTimeout(timeout);
    }, [authorInputValue]);

    return {authors, setAuthorInputValue}
}