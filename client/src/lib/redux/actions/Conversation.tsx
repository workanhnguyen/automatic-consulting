import { createAsyncThunk } from "@reduxjs/toolkit";

import { ConversationParams } from "../module";
import ConversationApi from "../apis/ConversationApi";

export const getConversationMessagesThunk = createAsyncThunk(
  "getConversationMessages",
  async (params: ConversationParams, { rejectWithValue }) => {
    try {
      return await ConversationApi.getConversationMessages(params);
    } catch (error: any) {
      if (error.response && error.response.data.message) {
        return rejectWithValue(error.response.data.message);
      } else {
        return rejectWithValue(error.message);
      }
    }
  }
);

export const sendQueryThunk = createAsyncThunk(
  "sendQuery",
  async (query: String, { rejectWithValue }) => {
    try {
      return await ConversationApi.sendQuery(query);
    } catch (error: any) {
      if (error.response && error.response.data.message) {
        return rejectWithValue(error.response.data.message);
      } else {
        return rejectWithValue(error.message);
      }
    }
  }
);
