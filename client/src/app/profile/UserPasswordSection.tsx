"use client";

import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";

import {
  Button,
  Grid,
  Stack,
  TextField,
  Typography,
} from "@mui/material";

const userPasswordSchema = z
  .object({
    oldPassword: z.string().min(1, "Không được bỏ trống"),
    newPassword: z.string().min(8, "Mật khẩu phải có ít nhất 8 kí tự"),
    confirmPassword: z.string().min(1, "Không được bỏ trống"),
  })
  .refine((data) => data.newPassword === data.confirmPassword, {
    message: "Mật khẩu không trùng khớp",
    path: ["confirmPassword"],
  });

type UserPasswordForm = z.infer<typeof userPasswordSchema>;

const UserPasswordSection = () => {
  // Form handling for user password
  const { register, handleSubmit, formState, getValues, reset } =
    useForm<UserPasswordForm>({
      resolver: zodResolver(userPasswordSchema),
      mode: "onChange",
    });

  const handleChangePassword = (data: UserPasswordForm) => {
    console.log(data);
  };

  return (
    <Stack
      component="form"
      onSubmit={handleSubmit(handleChangePassword)}
      direction="column"
      gap={2}
    >
      <Typography variant="label1">Mật khẩu</Typography>
      <Grid container direction="row" spacing={2}>
        <Grid item oversize={6} desktop={6} tablet={12} mobile={12}>
          <TextField
            type="password"
            variant="outlined"
            label="Mật khẩu cũ"
            error={!!formState.errors.oldPassword}
            helperText={formState.errors.oldPassword?.message}
            {...register("oldPassword")}
          />
        </Grid>
        <Grid item oversize={6} desktop={6} tablet={12} mobile={12}>
          <Stack direction="column" gap={2}>
            <TextField
              type="password"
              variant="outlined"
              label="Mật khẩu mới"
              error={!!formState.errors.newPassword}
              helperText={formState.errors.newPassword?.message}
              {...register("newPassword")}
            />
            <TextField
              type="password"
              variant="outlined"
              label="Nhập lại mật khẩu mới"
              error={!!formState.errors.confirmPassword}
              helperText={formState.errors.confirmPassword?.message}
              {...register("confirmPassword")}
            />
          </Stack>
        </Grid>
      </Grid>
      <Button
        type="submit"
        variant="contained"
        color="primary"
        sx={{ width: "fit-content", alignSelf: "flex-end" }}
        size="medium"
      >
        Đổi mật khẩu
      </Button>
    </Stack>
  );
};

export default UserPasswordSection;
