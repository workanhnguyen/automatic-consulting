import { Alert, AlertTitle, Box, Snackbar, Typography } from '@mui/material';
import {
  CheckCircle,
  Info,
  WarningCircle,
  X,
  XCircle,
} from '@phosphor-icons/react';

import { ToastProps } from '../module';
import './style.scss';

const CustomToast = (props: ToastProps) => {
  const {
    open,
    handleClose,
    title,
    message,
    severity,
    anchorOrigin = { vertical: 'top', horizontal: 'right' },
  } = props;

  return (
    <>
      <Snackbar
        open={open}
        autoHideDuration={3000}
        onClose={handleClose}
        anchorOrigin={anchorOrigin}
      >
        <Alert
          className="toast-alert"
          onClose={handleClose}
          severity={severity}
          iconMapping={{
            success: <CheckCircle size={32} color="var(--success)" />,
            info: <Info size={32} color="var(--information)" />,
            warning: <WarningCircle size={32} color="var(--warning)" />,
            error: <XCircle size={48} color="var(--alert)" />,
          }}
        >
          <AlertTitle>
            <Typography variant="h6">{title}</Typography>
          </AlertTitle>
          <Typography variant="body2" className="alert-message">
            {message}
          </Typography>
        </Alert>
      </Snackbar>
    </>
  );
};

export default CustomToast;
