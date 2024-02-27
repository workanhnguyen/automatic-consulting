"use client";

import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useDispatch, useSelector } from "react-redux";
import { z } from "zod";

import {
  Button,
  Stack,
  TextField,
  Typography,
  useMediaQuery,
} from "@mui/material";

import { theme } from "@/lib/theme";
import { AppDispatch, RootState } from "@/lib/redux/store";
import { updateUserInfoThunk } from "@/lib/redux/actions/User";
import CustomLoadingButton from "@/lib/components/loading-button";
import CustomToast from "@/lib/components/toast";
import {
  updateUserProfile,
} from "@/lib/redux/features/userSlice";
import { ToastInformation } from "../auth/module";

const userInfoSchema = z.object({
  firstName: z.string().min(1, "Không được bỏ trống").max(20, "Tên quá dài"),
  lastName: z
    .string()
    .min(1, "Không được bỏ trống")
    .max(30, "Họ và tên đệm quá dài"),
});

type UserInfoForm = z.infer<typeof userInfoSchema>;

const UserInfoSection = () => {
  const dispatch = useDispatch<AppDispatch>();
  const {
    userProfile,
    loadingUpdateUserInfo,
    newUserInfo,
    errorUpdateUserInfo,
  } = useSelector((state: RootState) => state.user);

  const [isAllowToEditUserInfo, setIsAllowToEditUserInfo] = useState(false);
  const [openToast, setOpenToast] = useState(false);
  const [toastInfo, setToastInfo] = useState<ToastInformation>();

  // Form handling for user info
  const { register, handleSubmit, formState, getValues, reset } =
    useForm<UserInfoForm>({
      resolver: zodResolver(userInfoSchema),
      mode: "onChange",
    });

  const handleToggleEditUserInfo = () => {
    setIsAllowToEditUserInfo((prev) => !prev);
  };

  const handleUpdateUserInfo = (data: UserInfoForm) => {
    dispatch(updateUserInfoThunk(data));
    handleToggleEditUserInfo();
  };

  const handleResetUserInfo = () => {
    reset();
    handleToggleEditUserInfo();
  };

  useEffect(() => {
    userProfile &&
      reset({
        firstName: userProfile.firstName,
        lastName: userProfile.lastName,
      });
  }, [userProfile]);

  useEffect(() => {
    if (newUserInfo) {
      reset({ ...newUserInfo });
      dispatch(updateUserProfile({ ...newUserInfo }));
      setIsAllowToEditUserInfo(false);
      setOpenToast(true);
      setToastInfo({
        title: "Thành công",
        message: "Cập nhật thông tin thành công!",
        severity: "success",
      });
    } else if (errorUpdateUserInfo) {
      setOpenToast(true);
      setToastInfo({
        title: "Thất bại",
        message: "Cập nhật thông tin thất bại. Vui lòng thử lại sau",
        severity: "error",
      });
    }
  }, [newUserInfo, errorUpdateUserInfo]);

  // Responsive
  const isTablet = useMediaQuery(theme.breakpoints.up("tablet"));

  return (
    <>
      <Stack direction="column" gap={2}>
        <Typography variant="label1">Thông tin người dùng</Typography>
        <Stack direction="column" gap={2}>
          <TextField
            type="text"
            variant="outlined"
            label="Email / Tên đăng nhập"
            value={userProfile?.email}
            disabled
            fullWidth
          />
          <Stack
            component="form"
            onSubmit={handleSubmit(handleUpdateUserInfo)}
            direction={isTablet ? "row" : "column"}
            gap={2}
          >
            <TextField
              type="text"
              variant="outlined"
              label="Họ và tên đệm"
              error={!!formState.errors.lastName}
              helperText={formState.errors.lastName?.message}
              disabled={!isAllowToEditUserInfo}
              {...register("lastName")}
            />

            <TextField
              type="text"
              variant="outlined"
              label="Tên"
              error={!!formState.errors.firstName}
              helperText={formState.errors.firstName?.message}
              disabled={!isAllowToEditUserInfo}
              {...register("firstName")}
            />

            {isAllowToEditUserInfo ? (
              <Stack direction="row" gap={2} width="100%">
                <Button
                  variant="outlined"
                  color="primary"
                  sx={{
                    height: "40px",
                    marginTop: `${isTablet ? "28px" : "6px"}`,
                  }}
                  onClick={handleResetUserInfo}
                  fullWidth
                  disabled={loadingUpdateUserInfo}
                >
                  Hủy
                </Button>
                {loadingUpdateUserInfo ? (
                  <CustomLoadingButton
                    sx={{
                      height: "40px",
                      marginTop: `${isTablet ? "28px" : "6px"}`,
                    }}
                  />
                ) : (
                  <Button
                    type="submit"
                    variant="contained"
                    color="primary"
                    sx={{
                      height: "40px",
                      marginTop: `${isTablet ? "28px" : "6px"}`,
                    }}
                    fullWidth
                    disabled={
                      getValues("firstName") === "" ||
                      getValues("lastName") === ""
                    }
                  >
                    Lưu
                  </Button>
                )}
              </Stack>
            ) : (
              <Button
                variant="contained"
                color="primary"
                sx={{
                  width: `${isTablet ? "100%" : "fit-content"}`,
                  alignSelf: "flex-end",
                  height: "40px",
                  marginTop: `${isTablet ? "28px" : "6px"}`,
                }}
                onClick={handleToggleEditUserInfo}
              >
                Chỉnh sửa
              </Button>
            )}
          </Stack>
        </Stack>
      </Stack>
      {(Boolean(newUserInfo) || Boolean(errorUpdateUserInfo)) && (
        <CustomToast
          open={openToast}
          title={toastInfo?.title || ""}
          handleClose={() => setOpenToast(false)}
          message={toastInfo?.message || ""}
          severity={toastInfo?.severity}
        />
      )}
    </>
  );
};

export default UserInfoSection;
