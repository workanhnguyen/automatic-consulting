import { createAsyncThunk } from '@reduxjs/toolkit';
//@ts-ignore
import Cookies from 'js-cookie';

import { UserLogin, UserRegister } from '../module';
import AuthApi from '../apis/AuthApi';

export const loginThunk = createAsyncThunk(
  'login',
  async (userAccount: UserLogin, { rejectWithValue }) => {
    try {
      const response = await AuthApi.login(userAccount);

      const token = response.data.token;
      const refreshToken = response.data.refreshToken;

      Cookies.set('token', token);
      Cookies.set('refreshToken', refreshToken);

      return response;
    } catch (error: any) {
      if (error.response && error.response.data.message) {
        return rejectWithValue(error.response.data.message);
      } else {
        return rejectWithValue(error.message);
      }
    }
  }
);

export const registerThunk = createAsyncThunk(
  'register',
  async (userAccount: UserRegister, { rejectWithValue }) => {
    try {
      return await AuthApi.register(userAccount);
    } catch (error: any) {
      if (error.response && error.response.data.message) {
        return rejectWithValue(error.response.data.message);
      } else {
        return rejectWithValue(error.message);
      }
    }
  }
);
