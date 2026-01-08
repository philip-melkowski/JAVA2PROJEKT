import {apiFetch} from "./api.ts";

export type ReviewDTO = {
    id: number;
    rating: number;
    comment: string;
    bookId: number;
    userId: number;
    bookTitle?: string;
    authorName?: string;
    authorSurname?: string;

}

export type CreateReviewRequest = {

    rating: number;
    comment: string | null;
    bookId: number;

}

export type UpdateReviewRequest = {
    rating: number;
    comment: string | null;
    reviewId: number;
}

export function createReview(params: CreateReviewRequest) : Promise<ReviewDTO>
{
    return apiFetch("api/reviews", {method: "POST",
        body: JSON.stringify(params)});
}

export function getMyReviews() : Promise<ReviewDTO[]>
{
    return apiFetch("api/reviews/me");
}

export function updateReview(params: UpdateReviewRequest) : Promise<ReviewDTO>
{
    return apiFetch(`api/reviews`, {method: "PUT",
        body: JSON.stringify(params)});
}