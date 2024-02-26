"use client";

import Image from "next/image";
import { useSelector } from "react-redux";

import { Avatar, Box, Stack, Typography, useMediaQuery } from "@mui/material";

import { images } from "@/lib/assets/img";
import { RootState } from "@/lib/redux/store";
import { theme } from "@/lib/theme";
import { MessageCardProps } from "./module";

const MessageCard = (props: MessageCardProps) => {
  const { type, content } = props;

  const { userProfile } = useSelector((state: RootState) => state.user);

  const isTablet = useMediaQuery(theme.breakpoints.up("tablet"));

  return (
    <>
      {type === "answer" ? (
        <Stack direction="row" gap={2} maxWidth="70%" alignSelf="flex-start">
          <Avatar>
            <Image src={images.robotAvatar} alt="avatar" width={40} />
          </Avatar>
          <Box
            sx={{
              padding: "12px",
              backgroundColor: "var(--grey-neutral-60)",
              borderRadius: "12px",
            }}
          >
            <Typography>
              {content.split("\n").map((line, index) => (
                <Typography variant={isTablet ? "body2" : "body3"} key={index}>
                  {line}
                </Typography>
              ))}
            </Typography>
          </Box>
        </Stack>
      ) : (
        <Stack direction="row" gap={2} maxWidth="70%" alignSelf="flex-end">
          <Box
            sx={{
              padding: "12px",
              backgroundColor: "var(--primary)",
              color: "var(--white)",
              borderRadius: "12px",
            }}
          >
            <Typography>
              {content.split("\n").map((line, index) => (
                <Typography variant={isTablet ? "body2" : "body3"} key={index}>
                  {line}
                </Typography>
              ))}
            </Typography>
          </Box>
          <Avatar src={userProfile?.avatarLink} alt="avatar" />
        </Stack>
      )}
    </>
  );
};

export default MessageCard;
