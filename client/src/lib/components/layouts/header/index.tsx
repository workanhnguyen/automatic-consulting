"use client";

import Image from "next/image";

import { Stack, Typography, useMediaQuery } from "@mui/material";

import { images } from "@/lib/assets/img";
import { theme } from "@/lib/theme";
import Profile from "./Profile";
import "./style.scss";

const CustomHeader = () => {
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
        <Typography variant={isTablet ? 'h5' : 'h6'}>Tư vấn tuyển sinh tự động</Typography>
      </Stack>

      <Profile />
    </Stack>
  );
};

export default CustomHeader;
