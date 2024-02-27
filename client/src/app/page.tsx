"use client";

import { ChangeEvent, useEffect, useRef, useState } from "react";

import {
  Box,
  Button,
  CircularProgress,
  Stack,
  TextField,
  Typography,
} from "@mui/material";
import { PaperPlaneRight } from "@phosphor-icons/react";

import CustomLayout from "@/lib/components/layouts";
import { useDispatch, useSelector } from "react-redux";
import { AppDispatch, RootState } from "@/lib/redux/store";
import EmptyDataPlaceholder from "@/lib/components/common/empty-data";
import { Message } from "@/lib/redux/module";
import {
  getConversationMessagesThunk,
  sendQueryThunk,
} from "../lib/redux/actions/Conversation";
import SuggestedQuestionSection from "./SuggestedQuestionSection";
import MessageCard from "./MessageCard";
import { CONVERSATION_PAGE_SIZE } from "../lib/constants/index";
import "./style.scss";
import CustomToast from "@/lib/components/toast";
import { ToastInformation } from "./auth/module";

const HomePage = () => {
  const dispatch = useDispatch<AppDispatch>();
  const {
    loadingMessages,
    messages,
    errorGetMessages,
    returnedResult,
    errorSendQuery,
  } = useSelector((state: RootState) => state.conversation);

  const scrollEndRef = useRef<HTMLDivElement>(null);

  const [question, setQuestion] = useState("");
  const [pageNumber, setPageNumber] = useState(1);
  const [data, setData] = useState<Message[]>([]);
  const [openToast, setOpenToast] = useState(false);
  const [toastInfo, setToastInfo] = useState<ToastInformation>();

  const handleQuestionChange = (
    e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    setQuestion(e.target.value);
  };

  const handleLoadMoreMessage = () => {
    if (messages?.hasNext) {
      setPageNumber((prev) => prev + 1);
      dispatch(
        getConversationMessagesThunk({
          pageNumber: pageNumber + 1,
          pageSize: CONVERSATION_PAGE_SIZE,
        })
      );
      if (scrollEndRef.current) {
        scrollEndRef.current.scrollIntoView({ behavior: "smooth" });
      }
    }
  };

  const handleSendQuery = () => {
    setData((prev) => [
      ...prev,
      { question, answer: "...", createdDate: Date.now() },
    ]);
    setQuestion("");
    dispatch(sendQueryThunk(question));
  };

  // Fetch initial messages
  useEffect(() => {
    dispatch(
      getConversationMessagesThunk({
        pageNumber,
        pageSize: CONVERSATION_PAGE_SIZE,
      })
    );
  }, []);

  useEffect(() => {
    if (messages?.data && messages?.data.length > 0) {
      setData((prev) => {
        const newMessages = messages.data.filter(
          (message) =>
            !prev.some(
              (prevMessage) => prevMessage.createdDate === message.createdDate
            )
        );
        return [...newMessages.reverse(), ...prev];
      });
    } else if (errorGetMessages) {
      setOpenToast(true);
      setToastInfo({
        title: "Thất bại",
        message: errorGetMessages,
        severity: "error",
      });
    }
  }, [messages, errorGetMessages]);

  useEffect(() => {
    if (scrollEndRef.current) {
      scrollEndRef.current.scrollIntoView({ behavior: "smooth" });
    }
  }, [data]);

  useEffect(() => {
    if (returnedResult) {
      data.pop();
      setData((prev) => [...prev, returnedResult]);
    } else if (errorSendQuery) {
      setOpenToast(true);
      setToastInfo({
        title: "Thất bại",
        message: errorSendQuery,
        severity: "error",
      });
    }
  }, [returnedResult, errorSendQuery]);

  return (
    <>
      <CustomLayout>
        <Box className="home-page-container">
          <Stack
            direction="column"
            justifyContent="space-between"
            alignItems="center"
            className="home-page-conversation"
            gap={1}
          >
            <Stack
              direction="column"
              className="home-page-conversation-messages"
            >
              {loadingMessages ? (
                <Stack
                  justifyContent="center"
                  alignItems="center"
                  sx={{ width: "100%", height: "100%" }}
                >
                  <CircularProgress sx={{ color: "var(--primary)" }} />
                </Stack>
              ) : data.length === 0 ? (
                <EmptyDataPlaceholder />
              ) : (
                <Stack
                  direction="column"
                  gap={2}
                  sx={{ width: "100%", height: "100%" }}
                >
                  {data.map((message, index) => {
                    if (index === 0)
                      return (
                        <Stack
                          direction="column"
                          width="100%"
                          height="inherit"
                          gap={3}
                          key={index}
                        >
                          {messages?.hasNext && (
                            <Button
                              variant="outlined"
                              color="primary"
                              onClick={handleLoadMoreMessage}
                              sx={{
                                width: "fit-content",
                                height: "32px",
                                alignSelf: "center",
                              }}
                            >
                              Xem thêm lịch sử
                            </Button>
                          )}
                          <Stack direction="column" width="100%" gap={2}>
                            <MessageCard
                              type="question"
                              content={message.question}
                            />
                            <MessageCard
                              type="answer"
                              content={message.answer}
                            />
                          </Stack>
                        </Stack>
                      );
                    else if (index === data.length - 1)
                      return (
                        <Stack
                          direction="column"
                          width="100%"
                          gap={2}
                          key={index}
                        >
                          <MessageCard
                            type="question"
                            content={message.question}
                          />
                          <MessageCard type="answer" content={message.answer} />
                          <div ref={scrollEndRef} />
                        </Stack>
                      );
                    else
                      return (
                        <Stack
                          direction="column"
                          width="100%"
                          gap={2}
                          key={index}
                        >
                          <MessageCard
                            type="question"
                            content={message.question}
                          />
                          <MessageCard type="answer" content={message.answer} />
                        </Stack>
                      );
                  })}
                </Stack>
              )}
            </Stack>

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
                          onClick={handleSendQuery}
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
      <CustomToast
        open={openToast}
        title={toastInfo?.title || ''}
        handleClose={() => setOpenToast(false)}
        message={toastInfo?.message || ''}
        severity={toastInfo?.severity}
      />
    </>
  );
};

export default HomePage;
