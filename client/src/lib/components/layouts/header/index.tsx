"use client";

import { useState } from "react";
import Image from "next/image";

import {
  Box,
  Popover,
  Stack,
  SxProps,
  Theme,
  Typography,
  useMediaQuery,
} from "@mui/material";
import { Info } from "@phosphor-icons/react";

import { images } from "@/lib/assets/img";
import { theme } from "@/lib/theme";
import Profile from "./Profile";
import AddtionalInfo from "../../common/empty-data/AddtionalInfo";
import "./style.scss";

const CustomHeader = () => {
  const [anchorEl, setAnchorEl] = useState<HTMLButtonElement | null>(null);

  const handleClick = (event: React.MouseEvent<HTMLButtonElement>) => {
    setAnchorEl(event.currentTarget);
  };

  const handleClose = () => {
    setAnchorEl(null);
  };

  const open = Boolean(anchorEl);
  const id = open ? "suggested-questions" : undefined;

  const isOversize = useMediaQuery(theme.breakpoints.up("oversize"));
  const isDesktop = useMediaQuery(theme.breakpoints.up("desktop"));
  const isTablet = useMediaQuery(theme.breakpoints.up("tablet"));

  const responsiveMargin = isOversize
    ? "160px"
    : isDesktop
    ? "64px"
    : isTablet
    ? "32px"
    : "16px";

  return (
    <>
      <Stack
        direction="row"
        component="header"
        paddingY={0}
        paddingX={3}
        justifyContent="space-between"
        alignItems="center"
        className="header"
        marginX={responsiveMargin}
      >
        <Stack direction="row" gap={2} alignItems="center">
          <Image src={images.logoOU} alt="logo-ou" width={50} />
          <Box sx={{ position: "relative" }}>
            <Typography variant={isTablet ? "h5" : "h6"}>
              Tư vấn tuyển sinh tự động
            </Typography>
            <Box
              component="button"
              onMouseEnter={handleClick}
              sx={{
                position: "absolute",
                top: -10,
                right: -20,
                cursor: "pointer",
                outline: "none",
                border: "none",
                backgroundColor: 'transparent'
              }}
            >
              <Info size={16} />
            </Box>
          </Box>
        </Stack>
        <Profile />
      </Stack>
      <Popover
        id={id}
        open={open}
        anchorEl={anchorEl}
        onClose={handleClose}
        anchorOrigin={{
          vertical: "bottom",
          horizontal: "center",
        }}
        transformOrigin={{
          vertical: "top",
          horizontal: "center",
        }}
        sx={popoverStyles}
      >
        <AddtionalInfo />
      </Popover>
    </>
  );
};

export default CustomHeader;

const popoverStyles: SxProps<Theme> = {
  "& .MuiPaper-root": {
    width: "400px",
    borderRadius: "12px",
    boxShadow: "0px 2px 4px 0px rgba(30, 32, 32, 0.4)",
  },
};
