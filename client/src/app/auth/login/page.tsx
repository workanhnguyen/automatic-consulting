'use client';

import { useEffect } from 'react';
import { useDispatch } from 'react-redux';
// @ts-ignore
import Cookies from 'js-cookie';

import {
  Avatar,
  Box,
  Button,
  Checkbox,
  Container,
  FormControlLabel,
  Grid,
  Stack,
  TextField,
  Typography,
} from '@mui/material';
import { Lock } from '@phosphor-icons/react';

import { loginThunk } from '@/lib/redux/actions/Auth';
import { UserLogin } from '@/lib/redux/module';
import { AppDispatch } from '@/lib/redux/store';
import './style.scss';

const LoginPage = () => {
  const dispatch = useDispatch<AppDispatch>();

  useEffect(() => {
    const account: UserLogin = {
      email: 'an@gmail.com',
      password: '1234',
    };
    const token = Cookies.get('token');
    !token && dispatch(loginThunk(account));
  }, []);

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);
    console.log({
      email: data.get('email'),
      password: data.get('password'),
    });
  };

  return (
    <>
      <Container className="login-container">
        <Box
          sx={{
            marginTop: 8,
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
          }}
        >
          <Avatar sx={{ margin: 1, backgroundColor: 'var(--primary)' }}>
            <Lock size={24} />
          </Avatar>
          <Typography component="h1" variant="h5">
            Đăng nhập
          </Typography>
          <Stack
            direction="column"
            gap={2}
            width="100%"
            component="form"
            onSubmit={handleSubmit}
            noValidate
            sx={{ mt: 1 }}
          >
            <Stack direction="column" width="100%" gap={2}>
              <TextField
                required
                fullWidth
                id="email"
                label="Địa chỉ email"
                name="email"
                autoComplete="email"
                autoFocus
              />
              <TextField
                required
                fullWidth
                name="password"
                label="Mật khẩu"
                type="password"
                id="password"
                autoComplete="current-password"
              />
              <FormControlLabel
                sx={{ gap: 1, ml: 0 }}
                control={<Checkbox />}
                label={<Typography variant="body2">Lưu đăng nhập</Typography>}
              />
            </Stack>
            <Button
              type="submit"
              fullWidth
              variant="contained"
              color="primary"
              disableElevation
            >
              Đăng nhập
            </Button>
            <Box className="flex-row">
              <Typography variant="body2">Chưa có tài khoản?&nbsp;</Typography>
              <Typography variant="body2" className="signup-switch">
                Đăng ký
              </Typography>
            </Box>
          </Stack>
        </Box>
      </Container>
    </>
  );
};

export default LoginPage;
