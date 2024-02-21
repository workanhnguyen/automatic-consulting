import Link from 'next/link';

import { Box, Button, Typography } from '@mui/material';

const HomePage = () => {
  return (
    <Box padding={2}>
      <Typography variant="body1" sx={{ color: 'var(--primary)' }}>
        He thong tu van tuyen sinh tu dong
      </Typography>
      <Link href="/profile">
        <Button variant="contained" color="error" disableElevation>
          Profile
        </Button>
      </Link>
    </Box>
  );
};

export default HomePage;
