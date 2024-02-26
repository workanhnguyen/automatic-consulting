import { createSlice } from "@reduxjs/toolkit";

import { ConversationState } from "../module";
import { getConversationMessagesThunk } from "../actions/Conversation";

const initialState: ConversationState = {
  loadingMessages: false,
  messages: null,
  totalMessages: [],
  errorGetMessages: null,
};

const conversationSlice = createSlice({
  name: "conversation",
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    // get messages
    builder.addCase(getConversationMessagesThunk.pending, (state) => {
      state.loadingMessages = true;
      state.messages = null;
      state.errorGetMessages = null;
    });
    builder.addCase(getConversationMessagesThunk.fulfilled, (state, action) => {
      state.loadingMessages = false;
      state.messages = action.payload.data;
      state.errorGetMessages = null;
    });
    builder.addCase(getConversationMessagesThunk.rejected, (state, action) => {
      state.loadingMessages = false;
      state.messages = null;
      state.errorGetMessages = action.payload !== undefined ? action.payload : null;
    });
  },
});

export const {} = conversationSlice.actions;
export default conversationSlice.reducer;
