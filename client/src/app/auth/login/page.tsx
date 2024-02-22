'use client';

import { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { z } from 'zod';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { useRouter } from 'next/navigation';

import {
  Avatar,
  Box,
  Button,
  Checkbox,
  CircularProgress,
  Container,
  FormControlLabel,
  Stack,
  TextField,
  Typography,
} from '@mui/material';
import { Lock } from '@phosphor-icons/react';

import { loginThunk } from '@/lib/redux/actions/Auth';
import { AppDispatch, RootState } from '@/lib/redux/store';
import CustomToast from '@/lib/components/toast';
import './style.scss';

const loginUserSchema = z.object({
  email: z.string().min(1, 'Không được bỏ trống').email('Email không hợp lệ'),
  password: z.string().min(1, 'Không được bỏ trống'),
});

type UserLoginForm = z.infer<typeof loginUserSchema>;

const LoginPage = () => {
  const dispatch = useDispatch<AppDispatch>();
  const { loadingLogin, successLogin, errorLogin } = useSelector(
    (state: RootState) => state.auth
  );
  const router = useRouter();

  const [openToast, setOpenToast] = useState(false);

  const { register, handleSubmit, formState } = useForm<UserLoginForm>({
    resolver: zodResolver(loginUserSchema),
    mode: 'onChange',
  });

  const handleLogin = (data: UserLoginForm) => {
    dispatch(loginThunk(data));
  };

  useEffect(() => {
    successLogin && router.replace('/');
    errorLogin && setOpenToast(true);
  }, [successLogin, errorLogin]);

  return (
    <>
      <Container className="login-container">
        <Box className="login-wrapper">
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
            onSubmit={handleSubmit(handleLogin)}
            noValidate
            sx={{ mt: 1 }}
          >
            <Stack direction="column" width="100%" gap={2}>
              <TextField
                id="email"
                fullWidth
                label="Địa chỉ email"
                autoComplete="email"
                autoFocus
                error={!!formState.errors.email}
                helperText={formState.errors.email?.message}
                disabled={loadingLogin}
                {...register('email')}
              />
              <TextField
                id="password"
                fullWidth
                label="Mật khẩu"
                type="password"
                autoComplete="current-password"
                error={!!formState.errors.password}
                helperText={formState.errors.password?.message}
                disabled={loadingLogin}
                {...register('password')}
              />
              <FormControlLabel
                disabled={loadingLogin}
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
              disabled={loadingLogin}
            >
              {loadingLogin ? (
                <CircularProgress size={24} sx={{ color: 'var(--primary)' }} />
              ) : (
                <Typography variant="button2">Đăng nhập</Typography>
              )}
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
      <CustomToast
        open={openToast}
        title="Thất bại"
        handleClose={() => setOpenToast(false)}
        message={errorLogin}
        severity="error"
      />
    </>
  );
};

export default LoginPage;
