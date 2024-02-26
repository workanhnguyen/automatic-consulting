import { Box, Stack, Typography } from "@mui/material";
import { Info } from "@phosphor-icons/react";

const AddtionalInfo = () => {
  return (
    <Stack
      direction="row"
      gap={1.5}
      sx={{
        borderRadius: "12px",
        border: "1px solid var(--primary)",
        padding: "12px",
      }}
    >
      <Box>
        <Info size={26} />
      </Box>
      <Typography variant="body3">
        Dữ liệu hiện tại chỉ có thể trả lời được các câu hỏi thuộc phạm vi tuyển
        sinh của Khoa Công nghệ thông tin - Trường Đại học Mở TP.HCM
      </Typography>
    </Stack>
  );
};

export default AddtionalInfo;
