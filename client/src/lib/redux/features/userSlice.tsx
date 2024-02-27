import { createSlice } from "@reduxjs/toolkit";
//@ts-ignore
import Cookies from "js-cookie";

import { UserState } from "../module";
import {
  changeAvatarThunk,
  changePasswordThunk,
  getProfileThunk,
  handleAddOrUpdateUserToLocalStorage,
  updateUserInfoThunk,
} from "../actions/User";

const initialState: UserState = {
  loadingUserProfile: false,
  userProfile: null,
  errorGetUserProfile: null,

  loadingChangeAvatar: false,
  newAvatarLink: null,
  errorChangeAvatar: null,

  loadingUpdateUserInfo: false,
  newUserInfo: null,
  errorUpdateUserInfo: null,

  loadingChangePassword: false,
  successChangePassword: null,
  errorChangePassword: null,
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
    getUserProfileFromLocalStorage: (state) => {
      const userProfileString = localStorage.getItem("userProfile");
      if (userProfileString) state.userProfile = JSON.parse(userProfileString);
    },
    logout: () => {
      Cookies.remove("token");
      Cookies.remove("refreshToken");
      localStorage.removeItem("userProfile");

      return initialState;
    },
    resetUserState: () => {
      return initialState;
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

    // change avatar
    builder.addCase(changeAvatarThunk.pending, (state) => {
      state.loadingChangeAvatar = true;
      state.newAvatarLink = false;
      state.errorChangeAvatar = null;
    });
    builder.addCase(changeAvatarThunk.fulfilled, (state, action) => {
      state.loadingChangeAvatar = false;
      state.newAvatarLink = action.payload.data.avatarLink;
      state.errorChangeAvatar = null;

      handleAddOrUpdateUserToLocalStorage(action.payload.data);
    });
    builder.addCase(changeAvatarThunk.rejected, (state, action) => {
      state.loadingChangeAvatar = false;
      state.newAvatarLink = false;
      state.errorChangeAvatar =
        action.payload !== undefined ? action.payload : null;
    });

    // update user information (first name, last name)
    builder.addCase(updateUserInfoThunk.pending, (state) => {
      state.loadingUpdateUserInfo = true;
      state.newUserInfo = null;
      state.errorUpdateUserInfo = null;
    });
    builder.addCase(updateUserInfoThunk.fulfilled, (state, action) => {
      state.loadingUpdateUserInfo = false;
      state.newUserInfo = action.payload.data;
      state.errorUpdateUserInfo = null;

      handleAddOrUpdateUserToLocalStorage(action.payload.data);
    });
    builder.addCase(updateUserInfoThunk.rejected, (state, action) => {
      state.loadingUpdateUserInfo = false;
      state.newUserInfo = null;
      state.errorUpdateUserInfo =
        action.payload !== undefined ? action.payload : null;
    });

    // change password
    builder.addCase(changePasswordThunk.pending, (state) => {
      state.loadingChangePassword = true;
      state.successChangePassword = null;
      state.errorChangePassword = null;
    });
    builder.addCase(changePasswordThunk.fulfilled, (state, action) => {
      state.loadingChangePassword = false;
      state.successChangePassword = action.payload.data.message;
      state.errorChangePassword = null;
    });
    builder.addCase(changePasswordThunk.rejected, (state, action) => {
      state.loadingChangePassword = false;
      state.successChangePassword = null;
      state.errorChangePassword =
        action.payload !== undefined ? action.payload : null;
    });
  },
});

export const {
  updateUserProfile,
  logout,
  getUserProfileFromLocalStorage,
  resetUserState,
} = userSlice.actions;
export default userSlice.reducer;
