export const GENRES = [
    "FANTASY",
    "SCI_FI",
    "ROMANCE",
    "HISTORY",
    "HORROR",
    "BIOGRAPHY",
    "THRILLER",
    "ADVENTURE",
    "POETRY",
    "DRAMA",
] as const;

export type Genre = (typeof GENRES)[number];