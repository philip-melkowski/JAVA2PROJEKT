import {apiFetch} from "./api.ts";

export type ReviewDTO = {
    id: number;
    rating: number;
    comment: string;
    bookId: number;
    userId: number;
}

export type CreateReviewRequest = {

    rating: number;
    comment?: string;
    bookId: number;

}

export function createReview(params: CreateReviewRequest) : Promise<ReviewDTO>
{
    return apiFetch("api/reviews", {method: "POST", body: JSON.stringify(params)});
}