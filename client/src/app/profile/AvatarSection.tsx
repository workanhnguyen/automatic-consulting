"use client";

import { ChangeEvent, useEffect, useRef, useState } from "react";
import { useDispatch, useSelector } from "react-redux";

import { Button, Stack, useMediaQuery } from "@mui/material";

import CustomAvatar from "@/lib/components/custom-avatar";
import { theme } from "@/lib/theme";
import { AppDispatch, RootState } from "@/lib/redux/store";
import { changeAvatarThunk } from "@/lib/redux/actions/User";
import CustomToast from "@/lib/components/toast";
import CustomLoadingButton from "@/lib/components/loading-button";
import { updateUserProfile } from "@/lib/redux/features/userSlice";
import { ToastInformation } from "../auth/module";

const AvatarSection = () => {
  const dispatch = useDispatch<AppDispatch>();
  const { loadingChangeAvatar, newAvatarLink, errorChangeAvatar } = useSelector(
    (state: RootState) => state.user
  );

  const fileInputRef = useRef<HTMLInputElement>(null);

  const { userProfile } = useSelector((state: RootState) => state.user);

  const [avatarUrl, setAvatarUrl] = useState<string | undefined>("");
  const [isAllowToOpenAvatarAction, setIsAllowToOpenAvatarAction] =
    useState(false);
  const [openToast, setOpenToast] = useState(false);
  const [toastInfo, setToastInfo] = useState<ToastInformation>();

  const handleResetAvatar = () => {
    setAvatarUrl("");
    setIsAllowToOpenAvatarAction(false);
  };

  const handleUpdateAvatar = () => {
    avatarUrl && dispatch(changeAvatarThunk(avatarUrl));
  };

  const handleOpenFileDialog = () => {
    if (fileInputRef.current) {
      fileInputRef.current.click();
      setIsAllowToOpenAvatarAction(true);
    }
  };

  const handleFileChange = (e: ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = () => {
        if (typeof reader.result === "string") {
          setAvatarUrl(reader.result);
        }
      };
      reader.readAsDataURL(file);
    }
  };

  useEffect(() => {
    if (newAvatarLink) {
      dispatch(updateUserProfile({ avatarLink: newAvatarLink }));
      handleResetAvatar();
      setOpenToast(true);
      setToastInfo({
        title: "Thành công",
        message: "Thay đổi ảnh đại diện thành công",
        severity: "success",
      });
    } else if (errorChangeAvatar) {
      setOpenToast(true);
      setToastInfo({
        title: "Thất bại",
        message: errorChangeAvatar,
        severity: "error",
      });
    }
  }, [newAvatarLink, errorChangeAvatar]);

  // Responsive
  const isDesktop = useMediaQuery(theme.breakpoints.up("desktop"));

  return (
    <>
      <Stack
        direction="column"
        gap={2}
        justifyContent="center"
        alignItems="center"
      >
        <CustomAvatar
          src={avatarUrl !== "" ? avatarUrl : userProfile?.avatarLink}
          width={150}
          height={150}
          className="avatar-ring"
        />
        {isAllowToOpenAvatarAction ? (
          <Stack direction="row" gap={2} width="100%" justifyContent="center">
            <Button
              variant="outlined"
              color="primary"
              sx={{
                width: `${isDesktop ? "100%" : "240px"}`,
                height: "40px",
              }}
              onClick={handleResetAvatar}
              disabled={loadingChangeAvatar}
            >
              Hủy
            </Button>
            {loadingChangeAvatar ? (
              <CustomLoadingButton
                sx={{
                  width: `${isDesktop ? "100%" : "240px"}`,
                  height: "40px",
                }}
              />
            ) : (
              <Button
                variant="contained"
                color="primary"
                onClick={handleUpdateAvatar}
                sx={{
                  width: `${isDesktop ? "100%" : "240px"}`,
                  height: "40px",
                }}
              >
                Lưu
              </Button>
            )}
          </Stack>
        ) : (
          <Button
            variant="contained"
            color="primary"
            onClick={handleOpenFileDialog}
            sx={{ width: `${isDesktop ? "100%" : "240px"}` }}
          >
            Chọn ảnh
          </Button>
        )}
      </Stack>
      <input
        type="file"
        hidden={true}
        ref={fileInputRef}
        onChange={handleFileChange}
        accept=".png, .jpg, .jpeg"
      />
      {(Boolean(newAvatarLink) || Boolean(errorChangeAvatar)) && (
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

export default AvatarSection;
