import {TextField} from "@mui/material";
import {useState, useEffect} from "react";

type TitleFilterInputProps = {
    value: string | null;
    onChange: (value: string | null) => void;
    disabled?: boolean;
};

export default function TitleFilterInput({value, onChange, disabled}: TitleFilterInputProps) {
    // Lokalny state dla inputa - nie zależy od zewnętrznych zmian
    const [localValue, setLocalValue] = useState(value ?? "");

    // Synchronizuj tylko gdy zewnętrzna wartość się wyczyści (np. przez chip)
    useEffect(() => {
        if (value === null && localValue !== "") {
            setLocalValue("");
        }
    }, [value]);

    return (
        <TextField
            disabled={disabled}
            label="Search by title"
            type="search"
            variant="outlined"
            size="small"
            sx={{ minWidth: '250px', flex: 1 }}
            value={localValue}
            onChange={(e) => {
                setLocalValue(e.target.value);
                onChange(e.target.value === "" ? null : e.target.value);
            }}
        />
    );
}