import { AlertProps, SnackbarProps } from '@mui/material';

export interface ToastProps {
  open: boolean;
  handleClose?: any;
  title: string;
  message: string;
  severity: AlertProps['severity'];
  anchorOrigin?: SnackbarProps['anchorOrigin'];
}

export interface CustomLoadingButtonProps {
  fullWidth?: boolean;
  variant?: "text" | "contained" | "outlined";
  color?: "success" | "info" | "warning" | "error" | "inherit" | "primary" | "secondary";
}