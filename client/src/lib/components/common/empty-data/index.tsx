import Image from "next/image";

import { Stack, Typography } from "@mui/material";

import { images } from "@/lib/assets/img";
import AddtionalInfo from "./AddtionalInfo";

const EmptyDataPlaceholder = () => {
  return (
    <Stack
      direction="column"
      justifyContent="center"
      alignItems="center"
      sx={{ width: "400px", height: "100%", alignSelf: 'center' }}
      gap={2}
    >
      <Image src={images.robotAvatar} alt="logo-ou" width={100} />
      <Typography variant="body1">Tôi có thể giúp gì cho bạn?</Typography>
      <AddtionalInfo />
    </Stack>
  );
};

export default EmptyDataPlaceholder;
