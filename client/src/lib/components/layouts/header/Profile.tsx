"use client";

import { useSelector } from "react-redux";
import { useState } from "react";
import Link from "next/link";

import {
  Avatar,
  Box,
  MenuItem,
  Popover,
  Stack,
  SxProps,
  Theme,
  Typography,
} from "@mui/material";
import { SignOut, UserCircle } from "@phosphor-icons/react";

import { RootState } from "@/lib/redux/store";

const Profile = () => {
  const { userProfile } = useSelector((state: RootState) => state.user);

  const [anchorEl, setAnchorEl] = useState<HTMLButtonElement | null>(null);

  const handleClick = (event: React.MouseEvent<HTMLButtonElement>) => {
    setAnchorEl(event.currentTarget);
  };

  const handleClose = () => {
    setAnchorEl(null);
  };

  const open = Boolean(anchorEl);
  const id = open ? "profile-actions" : undefined;

  return (
    <>
      <Box
        id={id}
        component="button"
        onClick={handleClick}
        className="reset-btn-style"
      >
        <Avatar
          src={userProfile?.avatarLink}
          alt="user-avt"
          className="avatar-ring"
          sx={{ width: "42px", height: "42px" }}
        />
      </Box>
      <Popover
        id={id}
        open={open}
        anchorEl={anchorEl}
        onClose={handleClose}
        anchorOrigin={{
          vertical: "bottom",
          horizontal: "right",
        }}
        transformOrigin={{
          vertical: "top",
          horizontal: "right",
        }}
        sx={popoverStyles}
      >
        <Link href="/profile">
          <MenuItem onClick={handleClose}>
            <Stack direction="row" alignItems="center" gap={1}>
              <UserCircle size={24} />
              <Typography variant="body2">Trang cá nhân</Typography>
            </Stack>
          </MenuItem>
        </Link>
        <MenuItem onClick={handleClose}>
          <Stack
            direction="row"
            alignItems="center"
            gap={1}
            sx={{ color: "var(--alert)" }}
          >
            <SignOut size={24} />
            <Typography variant="body2">Đăng xuất</Typography>
          </Stack>
        </MenuItem>
      </Popover>
    </>
  );
};

export default Profile;

const popoverStyles: SxProps<Theme> = {
  "& .MuiPaper-root": {
    minWidth: "260px",
    padding: 1,
    marginTop: '12px',
    boxShadow: "0px 2px 4px 0px rgba(30, 32, 32, 0.4)",
  },
};
