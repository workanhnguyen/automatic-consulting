"use client";

import { useEffect, useState } from "react";
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
  Container,
  IconButton,
  InputAdornment,
  Stack,
  TextField,
  Typography,
} from "@mui/material";
import { Eye, EyeSlash, Lock } from "@phosphor-icons/react";

import { registerThunk } from "@/lib/redux/actions/Auth";
import { AppDispatch, RootState } from "@/lib/redux/store";
import CustomToast from "@/lib/components/toast";
import CustomLoadingButton from "@/lib/components/loading-button";
import { UserLogin, UserRegister } from "@/lib/redux/module";
import { resetRegisterStatus } from "@/lib/redux/features/authSlice";
import { ToastInformation } from "./module";
import "./style.scss";

const registerUserSchema = z
  .object({
    firstName: z.string().min(1, "Không được bỏ trống"),
    lastName: z.string().min(1, "Không được bỏ trống"),
    email: z.string().min(1, "Không được bỏ trống").email("Email không hợp lệ"),
    password: z.string().min(8, "Mật khẩu phải có ít nhất 8 kí tự"),
    confirmPassword: z.string().min(1, "Không được bỏ trống"),
  })
  .refine((data) => data.password === data.confirmPassword, {
    message: "Mật khẩu không trùng khớp",
    path: ["confirmPassword"],
  });

type UserRegisterForm = z.infer<typeof registerUserSchema>;

const RegisterPage = () => {
  const dispatch = useDispatch<AppDispatch>();
  const { loadingRegister, successRegister, errorRegister } = useSelector(
    (state: RootState) => state.auth
  );

  const router = useRouter();

  const [openToast, setOpenToast] = useState(false);
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [toastInfo, setToastInfo] = useState<ToastInformation>();

  const { register, handleSubmit, formState, getValues, reset } =
    useForm<UserRegisterForm>({
      resolver: zodResolver(registerUserSchema),
      mode: "onChange",
    });

  const handleClickShowPassword = () => setShowPassword((show) => !show);
  const handleClickShowConfirmPassword = () =>
    setShowConfirmPassword((show) => !show);

  const handleRegister = (data: UserRegisterForm) => {
    const registerUserInfo: UserRegister = {
      email: data.email,
      firstName: data.firstName,
      lastName: data.lastName,
      password: data.password,
    };
    dispatch(registerThunk(registerUserInfo));
  };

  const handleSaveRegisteredAccountToLocalStorage = () => {
    const userAccount: UserLogin = {
      email: getValues("email"),
      password: CryptoJS.AES.encrypt(
        getValues("password"),
        process.env.NEXT_PUBLIC_ENCRYPT_PASSWORD_KEY
      ).toString(),
    };

    localStorage.setItem("rememberedAccount", JSON.stringify(userAccount));
  };

  // Handle UI logic if register successfully
  useEffect(() => {
    if (successRegister) {
      setToastInfo({
        severity: "success",
        title: "Thành công",
        message:
          successRegister.message +
          "\nTự động chuyển sang đăng nhập sau 3 giây",
      });
      setOpenToast(true);

      handleSaveRegisteredAccountToLocalStorage();
      reset();
      dispatch(resetRegisterStatus());
      setTimeout(() => router.replace("/auth/login"), 3000);
    } else if (errorRegister) {
      setToastInfo({
        severity: "error",
        title: "Thất bại",
        message: errorRegister,
      });
      setOpenToast(true);
    }
  }, [successRegister, errorRegister]);

  return (
    <>
      <Container className="register-container">
        <Box className="register-wrapper">
          <Avatar sx={{ margin: 1, backgroundColor: "var(--primary)" }}>
            <Lock size={24} />
          </Avatar>
          <Typography component="h1" variant="h5">
            Đăng ký tài khoản
          </Typography>
          <Stack
            direction="column"
            gap={2}
            width="100%"
            component="form"
            onSubmit={handleSubmit(handleRegister)}
            noValidate
            sx={{ mt: 1 }}
          >
            <Stack direction="column" width="100%" gap={2}>
              <Stack direction="row" gap={1}>
                {/* Last name */}
                <TextField
                  id="last-name"
                  fullWidth
                  label="Họ và tên đệm"
                  autoFocus
                  error={!!formState.errors.lastName}
                  helperText={formState.errors.lastName?.message}
                  disabled={loadingRegister}
                  {...register("lastName")}
                />
                {/* First name */}
                <TextField
                  id="first-name"
                  fullWidth
                  label="Tên"
                  error={!!formState.errors.firstName}
                  helperText={formState.errors.firstName?.message}
                  disabled={loadingRegister}
                  {...register("firstName")}
                />
              </Stack>

              {/* Email */}
              <TextField
                id="email"
                fullWidth
                label="Địa chỉ email"
                autoComplete="email"
                autoFocus
                error={!!formState.errors.email}
                helperText={formState.errors.email?.message}
                disabled={loadingRegister}
                {...register("email")}
              />

              {/* Password */}
              <TextField
                id="password"
                fullWidth
                label="Mật khẩu"
                type={showPassword ? "text" : "password"}
                autoComplete="current-password"
                error={!!formState.errors.password}
                helperText={formState.errors.password?.message}
                disabled={loadingRegister}
                InputProps={{
                  endAdornment: (
                    <InputAdornment position="end">
                      <IconButton
                        aria-label="toggle password visibility"
                        onClick={handleClickShowPassword}
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

              {/* Confirm password */}
              <TextField
                id="confirm-password"
                fullWidth
                label="Nhập lại mật khẩu"
                type={showConfirmPassword ? "text" : "password"}
                error={!!formState.errors.confirmPassword}
                helperText={formState.errors.confirmPassword?.message}
                disabled={loadingRegister}
                InputProps={{
                  endAdornment: (
                    <InputAdornment position="end">
                      <IconButton
                        aria-label="toggle password visibility"
                        onClick={handleClickShowConfirmPassword}
                        edge="end"
                        sx={{ mr: 0 }}
                      >
                        {showConfirmPassword ? <EyeSlash /> : <Eye />}
                      </IconButton>
                    </InputAdornment>
                  ),
                }}
                {...register("confirmPassword")}
              />
            </Stack>

            {loadingRegister ? (
              <CustomLoadingButton fullWidth />
            ) : (
              <Button
                type="submit"
                fullWidth
                variant="contained"
                color="primary"
                disableElevation
                disabled={loadingRegister}
              >
                <Typography variant="button2">Đăng ký</Typography>
              </Button>
            )}

            <Box className="flex-row">
              <Typography variant="body2">Đã có tài khoản?&nbsp;</Typography>
              <Link href="/auth/login">
                <Typography variant="body2" className="signup-switch">
                  Đăng nhập
                </Typography>
              </Link>
            </Box>
          </Stack>
        </Box>
      </Container>
      <CustomToast
        open={openToast}
        title={toastInfo?.title || ""}
        handleClose={() => setOpenToast(false)}
        message={toastInfo?.message || ""}
        severity={toastInfo?.severity}
      />
    </>
  );
};

export default RegisterPage;
