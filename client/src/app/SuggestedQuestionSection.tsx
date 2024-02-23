"use client";

import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";

import {
  Button,
  CircularProgress,
  MenuItem,
  Popover,
  Stack,
  SxProps,
  Theme,
  Typography,
} from "@mui/material";
import { Sparkle } from "@phosphor-icons/react";

import { AppDispatch, RootState } from "@/lib/redux/store";
import { getSuggestedQuestionsThunk } from "@/lib/redux/actions/SuggestedQuestion";
import "./style.scss";

interface SuggestedQuestionSectionProps {
  setQuestion: any;
}

const SuggestedQuestionSection = (props: SuggestedQuestionSectionProps) => {
  const { setQuestion } = props;

  const dispatch = useDispatch<AppDispatch>();
  const { loading, suggestedQuestions, error } = useSelector(
    (state: RootState) => state.suggestedQuestion
  );

  const [anchorEl, setAnchorEl] = useState<HTMLButtonElement | null>(null);

  const handleFillSuggestedQuestionToInput = (_: any, content: string) => {
    setQuestion(content);
    handleClose();
  };

  const handleClick = (event: React.MouseEvent<HTMLButtonElement>) => {
    setAnchorEl(event.currentTarget);
  };

  const handleClose = () => {
    setAnchorEl(null);
  };

  const open = Boolean(anchorEl);
  const id = open ? "suggested-questions" : undefined;

  useEffect(() => {
    dispatch(getSuggestedQuestionsThunk({}));
  }, []);

  return (
    <>
      <Button
        id={id}
        onClick={handleClick}
        className="reset-btn-style"
        variant="contained"
        color="info"
      >
        <Sparkle size={20} />
      </Button>
      <Popover
        id={id}
        open={open}
        anchorEl={anchorEl}
        onClose={handleClose}
        anchorOrigin={{
          vertical: "top",
          horizontal: "right",
        }}
        transformOrigin={{
          vertical: "bottom",
          horizontal: "right",
        }}
        sx={popoverStyles}
      >
        {loading ? (
          <Stack
            direction="row"
            width="100%"
            height="100%"
            justifyContent="center"
            alignItems="center"
          >
            <CircularProgress size={24} sx={{ color: "var(--primary)" }} />
          </Stack>
        ) : (
          suggestedQuestions.map((item, index) => (
            <MenuItem
              key={index}
              onClick={(e) =>
                handleFillSuggestedQuestionToInput(e, item.content)
              }
            >
              <Typography variant="body2" style={truncatedStyles}>
                {item.content}hhh
              </Typography>
            </MenuItem>
          ))
        )}
      </Popover>
    </>
  );
};

export default SuggestedQuestionSection;

const popoverStyles: SxProps<Theme> = {
  "& .MuiPaper-root": {
    minWidth: "260px",
    maxWidth: "460px",
    padding: 1,
    marginTop: "-12px",
    boxShadow: "0px 2px 4px 0px rgba(30, 32, 32, 0.4)",
  },
};

const truncatedStyles = {
  overflow: "hidden",
  textOverflow: "ellipsis",
  whiteSpace: "nowrap",
};
