import { createAsyncThunk } from "@reduxjs/toolkit";
import SuggestedQuestionApi from "../apis/SuggestedQuestionApi";
import { SuggestedQuestionParams } from "../module";

export const getSuggestedQuestionsThunk = createAsyncThunk(
  "getSuggestedQuestions",
  async (params: SuggestedQuestionParams, { rejectWithValue }) => {
    try {
      return await SuggestedQuestionApi.getSuggestedQuestions(params);
    } catch (error: any) {
      if (error.response && error.response.data.message) {
        return rejectWithValue(error.response.data.message);
      } else {
        return rejectWithValue(error.message);
      }
    }
  }
);
