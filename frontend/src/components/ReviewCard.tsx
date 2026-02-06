import {Card, CardContent, Typography, Box, Stack, Chip, Button} from "@mui/material";
import type {ReviewDTO} from "../api/reviewApi.ts";
import StarIcon from '@mui/icons-material/Star';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import PersonIcon from '@mui/icons-material/Person';
import MenuBookIcon from '@mui/icons-material/MenuBook';

type ReviewCardProps = {
    review: ReviewDTO;
    onEdit: () => void;
    onDelete: () => void;
    onViewComment: () => void;
    disabled?: boolean;
}

export default function ReviewCard({review, onEdit, onDelete, onViewComment, disabled}: ReviewCardProps) {
    const hasLongComment = review.comment.length > 100;

    return (
        <Card
            sx={{
                borderRadius: '16px',
                border: '1px solid #e0e0e0',
                minHeight: '220px',
                display: 'flex',
                flexDirection: 'column',
                transition: 'all 0.3s ease',
                '&:hover': {
                    transform: 'translateY(-4px)',
                    boxShadow: '0 8px 16px rgba(0, 0, 0, 0.1)',
                }
            }}
        >
            <CardContent sx={{
                p: 3,
                display: 'flex',
                flexDirection: 'column',
                flexGrow: 1,
            }}>
                <Stack spacing={2} sx={{ height: '100%' }}>
                    {/* Header: Book Title & Rating */}
                    <Stack direction="row" justifyContent="space-between" alignItems="flex-start">
                        <Box sx={{ flex: 1 }}>
                            <Stack direction="row" spacing={1} alignItems="center" sx={{ mb: 0.5 }}>
                                <MenuBookIcon sx={{ color: '#667eea', fontSize: 20 }} />
                                <Typography
                                    variant="h6"
                                    sx={{
                                        fontWeight: 700,
                                        color: '#333',
                                        overflow: 'hidden',
                                        textOverflow: 'ellipsis',
                                        display: '-webkit-box',
                                        WebkitLineClamp: 1,
                                        WebkitBoxOrient: 'vertical',
                                    }}
                                >
                                    {review.bookTitle}
                                </Typography>
                            </Stack>

                            <Stack direction="row" spacing={0.5} alignItems="center">
                                <PersonIcon sx={{ color: '#999', fontSize: 16 }} />
                                <Typography variant="body2" sx={{ color: '#666' }}>
                                    {review.authorName} {review.authorSurname}
                                </Typography>
                            </Stack>
                        </Box>

                        {/* Rating Badge */}
                        <Chip
                            icon={<StarIcon sx={{ fontSize: 18, color: '#ffd700 !important' }} />}
                            label={`${review.rating}/10`}
                            sx={{
                                background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                                color: '#fff',
                                fontWeight: 700,
                                fontSize: '1rem',
                                height: '36px',
                                '& .MuiChip-label': {
                                    px: 1.5,
                                }
                            }}
                        />
                    </Stack>

                    {/* Comment Preview */}
                    {review.comment && (
                        <Box
                            sx={{
                                p: 2,
                                borderRadius: '12px',
                                backgroundColor: '#f8f9fa',
                                border: '1px solid #e9ecef',
                                flexGrow: 1,
                                display: 'flex',
                                flexDirection: 'column',
                            }}
                        >
                            <Typography
                                variant="body2"
                                sx={{
                                    color: '#666',
                                    fontStyle: 'italic',
                                    overflow: 'hidden',
                                    textOverflow: 'ellipsis',
                                    display: '-webkit-box',
                                    WebkitLineClamp: 3,
                                    WebkitBoxOrient: 'vertical',
                                    cursor: hasLongComment ? 'pointer' : 'default',
                                    '&:hover': hasLongComment ? {
                                        color: '#667eea',
                                    } : {}
                                }}
                                onClick={hasLongComment ? onViewComment : undefined}
                            >
                                "{review.comment}"
                            </Typography>
                            {hasLongComment && (
                                <Typography
                                    variant="caption"
                                    sx={{
                                        color: '#667eea',
                                        fontWeight: 600,
                                        mt: 1,
                                        cursor: 'pointer',
                                        '&:hover': {
                                            textDecoration: 'underline',
                                        }
                                    }}
                                    onClick={onViewComment}
                                >
                                    Read more...
                                </Typography>
                            )}
                        </Box>
                    )}

                    {/* Action Buttons */}
                    <Stack direction="row" spacing={1} justifyContent="flex-end">
                        <Button
                            disabled={disabled}
                            variant="outlined"
                            startIcon={<EditIcon />}
                            onClick={onEdit}
                            size="small"
                            sx={{
                                textTransform: 'none',
                                borderColor: '#667eea',
                                color: '#667eea',
                                fontWeight: 500,
                                '&:hover': {
                                    borderColor: '#764ba2',
                                    backgroundColor: 'rgba(102, 126, 234, 0.05)',
                                }
                            }}
                        >
                            Edit
                        </Button>
                        <Button
                            disabled={disabled}
                            variant="outlined"
                            startIcon={<DeleteIcon />}
                            onClick={onDelete}
                            size="small"
                            sx={{
                                textTransform: 'none',
                                borderColor: '#f44336',
                                color: '#f44336',
                                fontWeight: 500,
                                '&:hover': {
                                    borderColor: '#d32f2f',
                                    backgroundColor: 'rgba(244, 67, 54, 0.05)',
                                }
                            }}
                        >
                            Delete
                        </Button>
                    </Stack>
                </Stack>
            </CardContent>
        </Card>
    );
}