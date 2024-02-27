import { createSlice } from "@reduxjs/toolkit";

import { ConversationState } from "../module";
import {
  getConversationMessagesThunk,
  sendQueryThunk,
} from "../actions/Conversation";

const initialState: ConversationState = {
  loadingMessages: false,
  messages: null,
  totalMessages: [],
  errorGetMessages: null,

  loadingSendQuery: false,
  returnedResult: null,
  errorSendQuery: false,
};

const conversationSlice = createSlice({
  name: "conversation",
  initialState,
  reducers: {
    resetConversationState: () => {
      return initialState;
    },
  },
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
      state.errorGetMessages =
        action.payload !== undefined ? action.payload : null;
    });

    // send query
    builder.addCase(sendQueryThunk.pending, (state) => {
      state.loadingSendQuery = true;
      state.returnedResult = null;
      state.errorSendQuery = null;
    });
    builder.addCase(sendQueryThunk.fulfilled, (state, action) => {
      state.loadingSendQuery = false;
      state.returnedResult = action.payload.data;
      state.errorSendQuery = null;
    });
    builder.addCase(sendQueryThunk.rejected, (state, action) => {
      state.loadingSendQuery = false;
      state.returnedResult = null;
      state.errorSendQuery =
        action.payload !== undefined ? action.payload : null;
    });
  },
});

export const { resetConversationState } = conversationSlice.actions;
export default conversationSlice.reducer;
