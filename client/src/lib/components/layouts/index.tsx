"use client";

import React from "react";

import { Box, useMediaQuery } from "@mui/material";

import { theme } from "@/lib/theme";
import CustomHeader from "./header";

const CustomLayout = ({ children }: { children: React.ReactNode }) => {
  const isOversize = useMediaQuery(theme.breakpoints.up("oversize"));
  const isDesktop = useMediaQuery(theme.breakpoints.up("desktop"));
  const isTablet = useMediaQuery(theme.breakpoints.up("tablet"));

  const responsivePadding = isOversize
    ? "160px"
    : isDesktop
    ? "64px"
    : isTablet
    ? "32px"
    : "16px";

  return (
    <Box paddingX={responsivePadding}>
      <CustomHeader />
      {children}
    </Box>
  );
};

export default CustomLayout;
