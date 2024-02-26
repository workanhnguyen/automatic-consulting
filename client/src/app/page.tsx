"use client";

import { ChangeEvent, useState } from "react";

import { Box, Button, Stack, TextField, Typography } from "@mui/material";
import { PaperPlaneRight } from "@phosphor-icons/react";

import CustomLayout from "@/lib/components/layouts";
import SuggestedQuestionSection from "./SuggestedQuestionSection";
import "./style.scss";

const HomePage = () => {
  const [question, setQuestion] = useState("");

  const handleQuestionChange = (
    e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    setQuestion(e.target.value);
  };

  return (
    <CustomLayout>
      <Box className="home-page-container">
        <Stack
          direction="column"
          justifyContent="space-between"
          alignItems="center"
          className="home-page-conversation"
          gap={1}
        >
          <Box className="home-page-conversation-messages">
            <Box sx={{ width: "100%", height: "1000px" }}>hello</Box>
          </Box>
          <Box className="home-page-conversation-actions">
            <Box className="chat-input-field">
              <Typography
                className="chat-input-field-placeholder"
                display={question === "" ? "flex" : "none"}
              >
                Nhập câu hỏi của bạn tại đây...
              </Typography>
              <TextField
                type="text"
                multiline
                maxRows={4}
                variant="outlined"
                onChange={handleQuestionChange}
                value={question}
                InputProps={{
                  endAdornment: (
                    <Stack
                      direction="row"
                      gap={1}
                      height="100%"
                      alignItems="flex-end"
                      marginLeft={1}
                    >
                      <Button
                        variant="contained"
                        color="primary"
                        disabled={question === ""}
                      >
                        <PaperPlaneRight size={20} />
                      </Button>
                      <SuggestedQuestionSection setQuestion={setQuestion} />
                    </Stack>
                  ),
                }}
              />
            </Box>
          </Box>
        </Stack>
      </Box>
    </CustomLayout>
  );
};

export default HomePage;
