import { Button, CircularProgress } from '@mui/material';

import { CustomLoadingButtonProps } from '../module';

const CustomLoadingButton = (props: CustomLoadingButtonProps) => {
  const { fullWidth, variant = 'contained', color = 'primary', sx } = props;
  return (
    <Button
      fullWidth={fullWidth}
      variant={variant}
      color={color}
      disableElevation
      disabled
      sx={sx}
    >
      <CircularProgress size={24} sx={{ color: 'var(--primary)' }} />
    </Button>
  );
};

export default CustomLoadingButton;
