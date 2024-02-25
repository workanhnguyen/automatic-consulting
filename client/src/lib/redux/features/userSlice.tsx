import { createSlice } from "@reduxjs/toolkit";
//@ts-ignore
import Cookies from "js-cookie";

import { UserState } from "../module";
import {
  getProfileThunk,
  handleAddOrUpdateUserToLocalStorage,
} from "../actions/User";

const initialState: UserState = {
  loadingUserProfile: false,
  userProfile: JSON.parse(localStorage.getItem("userProfile")!),
  errorGetUserProfile: null,
};

const userSlice = createSlice({
  name: "user",
  initialState,
  reducers: {
    updateUserProfile: (state: UserState, action) => {
      state.userProfile &&
        (state.userProfile = { ...state.userProfile, ...action.payload });
      handleAddOrUpdateUserToLocalStorage({
        ...state.userProfile,
        ...action.payload,
      });
    },
    logout: (state) => {
      state.userProfile = null;

      Cookies.remove("token");
      Cookies.remove("refreshToken");
      localStorage.removeItem("userProfile");
    },
  },
  extraReducers: (builder) => {
    // get profile
    builder.addCase(getProfileThunk.pending, (state) => {
      state.loadingUserProfile = true;
      state.userProfile = null;
      state.errorGetUserProfile = null;
    });
    builder.addCase(getProfileThunk.fulfilled, (state, action) => {
      state.loadingUserProfile = false;
      state.userProfile = action.payload.data;
      state.errorGetUserProfile = null;

      handleAddOrUpdateUserToLocalStorage(action.payload.data);
    });

    builder.addCase(getProfileThunk.rejected, (state, action) => {
      state.loadingUserProfile = false;
      state.userProfile = null;
      state.errorGetUserProfile =
        action.payload !== undefined ? action.payload : null;
    });
  },
});

export const { updateUserProfile, logout } = userSlice.actions;
export default userSlice.reducer;
