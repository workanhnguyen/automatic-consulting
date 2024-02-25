"use client";

import { SyntheticEvent, useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { z } from "zod";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useRouter } from "next/navigation";
import Link from "next/link";
// @ts-ignore
import CryptoJS from "crypto-js";

import {
  Avatar,
  Box,
  Button,
  Checkbox,
  Container,
  FormControlLabel,
  IconButton,
  InputAdornment,
  Stack,
  TextField,
  Typography,
} from "@mui/material";
import { Eye, EyeSlash, Lock } from "@phosphor-icons/react";

import { loginThunk } from "@/lib/redux/actions/Auth";
import { AppDispatch, RootState } from "@/lib/redux/store";
import CustomToast from "@/lib/components/toast";
import { resetLoginStatus } from "@/lib/redux/features/authSlice";
import CustomLoadingButton from "@/lib/components/loading-button";
import { getProfileThunk } from "@/lib/redux/actions/User";
import "./style.scss";

const loginUserSchema = z.object({
  email: z.string().min(1, "Không được bỏ trống").email("Email không hợp lệ"),
  password: z.string().min(1, "Không được bỏ trống"),
});

type UserLoginForm = z.infer<typeof loginUserSchema>;

const LoginPage = () => {
  const dispatch = useDispatch<AppDispatch>();
  const { loadingLogin, successLogin, errorLogin } = useSelector(
    (state: RootState) => state.auth
  );
  const { userProfile, errorGetUserProfile } = useSelector(
    (state: RootState) => state.user
  );

  const router = useRouter();

  const [openToast, setOpenToast] = useState(false);
  const [showPassword, setShowPassword] = useState(false);
  const [rememberLogin, setRememberLogin] = useState(false);

  const { register, handleSubmit, formState, getValues, reset } =
    useForm<UserLoginForm>({
      resolver: zodResolver(loginUserSchema),
      mode: "onChange",
    });

  const handleClickShowPassword = () => setShowPassword((show) => !show);

  const handleMouseDownPassword = (
    event: React.MouseEvent<HTMLButtonElement>
  ) => {
    event.preventDefault();
  };

  const handleLogin = (data: UserLoginForm) => {
    dispatch(loginThunk(data));
  };

  const handleRememberLoginChange = (
    _: SyntheticEvent<Element, Event>,
    checked: boolean
  ) => {
    setRememberLogin(checked);
    if (checked) {
      const userAccount: UserLoginForm = {
        email: getValues("email"),
        password: CryptoJS.AES.encrypt(
          getValues("password"),
          process.env.NEXT_PUBLIC_ENCRYPT_PASSWORD_KEY
        ).toString(),
      };

      localStorage.setItem("rememberedAccount", JSON.stringify(userAccount));
    } else {
      localStorage.removeItem("rememberedAccount");
    }
  };

  // Load remembered account if it exists
  useEffect(() => {
    const rememberedAccount = localStorage.getItem("rememberedAccount");

    if (rememberedAccount) {
      const accountJson: UserLoginForm = JSON.parse(rememberedAccount);
      const decryptedPassword = CryptoJS.AES.decrypt(
        accountJson.password,
        process.env.NEXT_PUBLIC_ENCRYPT_PASSWORD_KEY
      ).toString(CryptoJS.enc.Utf8);

      accountJson.password = decryptedPassword;

      reset({ ...accountJson });
      setRememberLogin(true);
    }
  }, []);

  // Handle naviagate to home page if authenticated
  useEffect(() => {
    if (successLogin) {
      dispatch(getProfileThunk());
      dispatch(resetLoginStatus());
    }
    errorLogin && setOpenToast(true);
  }, [successLogin, errorLogin]);

  useEffect(() => {
    userProfile && router.replace("/");
    errorGetUserProfile && setOpenToast(true);
  }, [userProfile, errorGetUserProfile]);

  return (
    <>
      <Container className="login-container">
        <Box className="login-wrapper">
          <Avatar sx={{ margin: 1, backgroundColor: "var(--primary)" }}>
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
                {...register("email")}
              />
              <TextField
                id="password"
                fullWidth
                label="Mật khẩu"
                type={showPassword ? "text" : "password"}
                autoComplete="current-password"
                error={!!formState.errors.password}
                helperText={formState.errors.password?.message}
                disabled={loadingLogin}
                InputProps={{
                  endAdornment: (
                    <InputAdornment position="end">
                      <IconButton
                        aria-label="toggle password visibility"
                        onClick={handleClickShowPassword}
                        onMouseDown={handleMouseDownPassword}
                        edge="end"
                        sx={{ mr: 0 }}
                      >
                        {showPassword ? <EyeSlash /> : <Eye />}
                      </IconButton>
                    </InputAdornment>
                  ),
                }}
                {...register("password")}
              />
              <FormControlLabel
                disabled={loadingLogin}
                sx={{ gap: 1, ml: 0 }}
                control={<Checkbox checked={rememberLogin} />}
                onChange={(e, checked) => handleRememberLoginChange(e, checked)}
                label={<Typography variant="body2">Lưu đăng nhập</Typography>}
              />
            </Stack>

            {loadingLogin ? (
              <CustomLoadingButton fullWidth />
            ) : (
              <Button
                type="submit"
                fullWidth
                variant="contained"
                color="primary"
                disableElevation
                disabled={loadingLogin}
              >
                <Typography variant="button2">Đăng nhập</Typography>
              </Button>
            )}
            <Box className="flex-row">
              <Typography variant="body2">Chưa có tài khoản?&nbsp;</Typography>
              <Link href="/auth/register">
                <Typography variant="body2" className="signup-switch">
                  Đăng ký
                </Typography>
              </Link>
            </Box>
          </Stack>
        </Box>
      </Container>
      <CustomToast
        open={openToast}
        title="Thất bại"
        handleClose={() => setOpenToast(false)}
        message={errorLogin || errorGetUserProfile}
        severity="error"
      />
    </>
  );
};

export default LoginPage;
