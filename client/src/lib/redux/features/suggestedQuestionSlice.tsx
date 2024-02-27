import { createSlice } from "@reduxjs/toolkit";

import { SuggestedQuestionState } from "../module";
import { handleAddOrUpdateUserToLocalStorage } from "../actions/User";
import { getSuggestedQuestionsThunk } from "../actions/SuggestedQuestion";

const initialState: SuggestedQuestionState = {
  loading: false,
  suggestedQuestions: [],
  error: null,
};

const suggestedQuestionSlice = createSlice({
  name: "suggestedQuestion",
  initialState,
  reducers: {
    resetSuggestedQuestionState: () => {
      return initialState;
    },
  },
  extraReducers: (builder) => {
    // get suggested
    builder.addCase(getSuggestedQuestionsThunk.pending, (state) => {
      state.loading = true;
      state.suggestedQuestions = [];
      state.error = null;
    });
    builder.addCase(getSuggestedQuestionsThunk.fulfilled, (state, action) => {
      state.loading = false;
      state.suggestedQuestions = action.payload.data;
      state.error = null;

      handleAddOrUpdateUserToLocalStorage(action.payload.data);
    });

    builder.addCase(getSuggestedQuestionsThunk.rejected, (state, action) => {
      state.loading = false;
      state.suggestedQuestions = [];
      state.error = action.payload !== undefined ? action.payload : null;
    });
  },
});

export const { resetSuggestedQuestionState } = suggestedQuestionSlice.actions;
export default suggestedQuestionSlice.reducer;
