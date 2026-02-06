
import {Button} from "@mui/material";
import {Link, useLocation} from "react-router-dom";

type Props = {
    to: string;
    label: string;
    onClick?: () => void;
};

export default function NavButton({to, label, onClick}: Props) {
    const location = useLocation();
    const isActive = location.pathname === to;

    return (
        <Button
            component={Link}
            to={to}
            onClick={onClick}
            sx={{
                color: isActive ? '#fff' : 'rgba(255, 255, 255, 0.85)',
                backgroundColor: isActive ? 'rgba(255, 255, 255, 0.15)' : 'transparent',
                fontWeight: isActive ? 600 : 500,
                fontSize: '0.95rem',
                textTransform: 'none',
                px: 3,
                py: 1,
                borderRadius: '8px',
                position: 'relative',
                overflow: 'hidden',
                transition: 'all 0.3s ease',
                '&:hover': {
                    backgroundColor: isActive ? 'rgba(255, 255, 255, 0.2)' : 'rgba(255, 255, 255, 0.1)',
                    color: '#fff',
                    transform: 'translateY(-2px)',
                },
                '&::before': {
                    content: '""',
                    position: 'absolute',
                    bottom: 0,
                    left: '50%',
                    width: isActive ? '80%' : '0%',
                    height: '3px',
                    backgroundColor: '#fff',
                    transform: 'translateX(-50%)',
                    transition: 'width 0.3s ease',
                    borderRadius: '2px 2px 0 0',
                },
                '&:hover::before': {
                    width: '80%',
                }
            }}
        >
            {label}
        </Button>
    );
}