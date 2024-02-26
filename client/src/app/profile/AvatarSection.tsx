"use client";

import { ChangeEvent, useRef, useState } from "react";
import { useSelector } from "react-redux";

import { Button, Stack, useMediaQuery } from "@mui/material";

import CustomAvatar from "@/lib/components/custom-avatar";
import { theme } from "@/lib/theme";
import { RootState } from "@/lib/redux/store";

const AvatarSection = () => {
  const fileInputRef = useRef<HTMLInputElement>(null);

  const { userProfile } = useSelector((state: RootState) => state.user);

  const [avatarUrl, setAvatarUrl] = useState<string | undefined>("");

  const handleResetAvatar = () => {
    setAvatarUrl(userProfile?.avatarLink || "");
  };

  const handleUpdateAvatar = () => {};

  const handleOpenFileDialog = () => {
    if (fileInputRef.current) fileInputRef.current.click();
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
          src={avatarUrl}
          width={150}
          height={150}
          className="avatar-ring"
        />
        {Boolean(avatarUrl) !== Boolean(userProfile?.avatarLink) ? (
          <Stack direction="row" gap={2} width="100%" justifyContent="center">
            <Button
              variant="outlined"
              color="primary"
              sx={{
                width: `${isDesktop ? "100%" : "240px"}`,
                height: "40px",
              }}
              onClick={handleResetAvatar}
              fullWidth
            >
              Hủy
            </Button>
            <Button
              variant="contained"
              color="primary"
              sx={{
                width: `${isDesktop ? "100%" : "240px"}`,
                height: "40px",
              }}
              onClick={handleUpdateAvatar}
              fullWidth
            >
              Lưu
            </Button>
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
    </>
  );
};

export default AvatarSection;
