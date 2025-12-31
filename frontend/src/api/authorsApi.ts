import {apiFetch} from "./api.ts";

export type AuthorDTO = {
    id: number;
    name: string;
    surname: string;
}

export type AuthorSearchParams =
{
    fragment: string;
}

// to można chyba usunąć i po prostu wywołać findCaseInsensitive z pustym fragmentem
export function getAllAuthors() : Promise<AuthorDTO[]>
{
    return apiFetch("api/authors");
}

export function findCaseInsensitive(params: AuthorSearchParams) : Promise<AuthorDTO[]>{
    const query = new URLSearchParams();
    if(params.fragment != undefined) query.append("fragment", params.fragment);
    return apiFetch(`api/authors/search?${query.toString()}`);
}


