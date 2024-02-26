import Image from "next/image";

import { Box } from "@mui/material";

import { images } from "@/lib/assets/img";
import { CustomAvatarProps } from "../module";
import "./style.scss";

const CustomAvatar = (props: CustomAvatarProps) => {
  const { width = 32, height = 32, src, alt = "avatar", sx, className } = props;

  return (
    <Box sx={sx}>
      {src ? (
        <img
          src={src}
          width={`${width}px`}
          height={`${height}px`}
          alt={alt}
          className={`custom-avatar-container ${className}`}
        />
      ) : (
        <Image
          src={images.defaultAvatar}
          alt={alt}
          className={`custom-avatar-container ${className}`}
          width={width}
          height={height}
        />
      )}
    </Box>
  );
};

export default CustomAvatar;
