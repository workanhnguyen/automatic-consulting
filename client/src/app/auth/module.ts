import { AlertProps } from "@mui/material";

export interface ToastInformation {
    severity: AlertProps['severity'];
    title: string;
    message: string;
}