import { AlertProps, SnackbarProps, SxProps, Theme } from '@mui/material';
import { StaticImageData } from 'next/image';

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

export interface CustomAvatarProps {
  width?: number;
  height?: number;
  src: string | undefined;
  alt?: string;
  sx?: SxProps<Theme>
  className?: string;
}