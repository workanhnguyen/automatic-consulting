import { createAsyncThunk } from "@reduxjs/toolkit";
import UserApi from "../apis/UserApi";
import { User } from "../module";

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
