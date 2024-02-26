import { createAsyncThunk } from "@reduxjs/toolkit";
import UserApi from "../apis/UserApi";
import { User, UserInfoUpdate, UserPasswordUpdate } from "../module";

export const handleAddOrUpdateUserToLocalStorage = (
    userAttributes: Partial<User>
  ) => {
    const userProfileStr = localStorage.getItem('userProfile');
  
    if (userProfileStr) {
      const userProfileJson = JSON.parse(userProfileStr);
  
      const updatedUser = {
        ...userProfileJson,
        ...userAttributes,
      };
  
      localStorage.setItem('userProfile', JSON.stringify(updatedUser));
    } else {
      localStorage.setItem('userProfile', JSON.stringify(userAttributes));
    }
  };

export const getProfileThunk = createAsyncThunk(
  "getProfile",
  async (_, { rejectWithValue }) => {
    try {
      return await UserApi.getProfile();
    } catch (error: any) {
      if (error.response && error.response.data.message) {
        return rejectWithValue(error.response.data.message);
      } else {
        return rejectWithValue(error.message);
      }
    }
  }
);

export const changeAvatarThunk = createAsyncThunk(
  "changeAvatar",
  async (avatarBase64: string, { rejectWithValue }) => {
    try {
      return await UserApi.changeAvatar(avatarBase64);
    } catch (error: any) {
      if (error.response && error.response.data.message) {
        return rejectWithValue(error.response.data.message);
      } else {
        return rejectWithValue(error.message);
      }
    }
  }
);

export const changePasswordThunk = createAsyncThunk(
  "changePassword",
  async (args: UserPasswordUpdate, { rejectWithValue }) => {
    try {
      return await UserApi.changePassword(args);
    } catch (error: any) {
      if (error.response && error.response.data.message) {
        return rejectWithValue(error.response.data.message);
      } else {
        return rejectWithValue(error.message);
      }
    }
  }
);

export const updateUserInfoThunk = createAsyncThunk(
  "updateUserInfo",
  async (args: UserInfoUpdate, { rejectWithValue }) => {
    try {
      return await UserApi.updateInfo(args);
    } catch (error: any) {
      if (error.response && error.response.data.message) {
        return rejectWithValue(error.response.data.message);
      } else {
        return rejectWithValue(error.message);
      }
    }
  }
);

