import {Button} from "@mui/material";
import {Link} from "react-router-dom";

type Props = {
    to: string;
    label: string;
    onClick?: () => void;
};

export default function NavButton({to, label, onClick}: Props) {
    return (
        <Button
            component={Link}
            to={to}
            onClick={onClick}
        >
            {label}
        </Button>
    );
}