"use client";

import { Box, Grid, Stack, useMediaQuery } from "@mui/material";

import CustomLayout from "@/lib/components/layouts";
import { theme } from "@/lib/theme";
import "./style.scss";
import AvatarSection from "./AvatarSection";
import UserInfoSection from "./UserInfoSection";
import UserPasswordSection from "./UserPasswordSection";
import { useEffect } from "react";
import { useDispatch } from "react-redux";
import { AppDispatch } from "@/lib/redux/store";
import { getUserProfileFromLocalStorage, resetUserState } from "@/lib/redux/features/userSlice";

const ProfilePage = () => {
  const dispatch = useDispatch<AppDispatch>();

  // Responsive
  const isDesktop = useMediaQuery(theme.breakpoints.up("desktop"));
  const isTablet = useMediaQuery(theme.breakpoints.up("tablet"));

  const padding = isDesktop ? "84px" : isTablet ? "64px" : "32px";

  useEffect(() => {
    dispatch(resetUserState());
    dispatch(getUserProfileFromLocalStorage());
  }, []);

  return (
    <CustomLayout>
      <Box className="profile-page-container">
        <Grid
          container
          padding={padding}
          justifyContent="space-between"
          gap={4}
        >
          <Grid item oversize={2.8} desktop={2.8} tablet={12} mobile={12}>
            <AvatarSection />
          </Grid>
          <Grid item oversize={8.6} desktop={8.4} tablet={12} mobile={12}>
            <Stack direction="column" gap={4}>
              <UserInfoSection />
              <UserPasswordSection />
            </Stack>
          </Grid>
        </Grid>
      </Box>
    </CustomLayout>
  );
};

export default ProfilePage;
