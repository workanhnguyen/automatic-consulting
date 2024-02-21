import { createAsyncThunk } from '@reduxjs/toolkit';
//@ts-ignore
import Cookies from 'js-cookie';

import { AuthState, User, UserLogin } from '../module';
import AuthApi from '../apis/AuthApi';

export const handleAddOrUpdateLocalUser = (
  updatedAttributes: Partial<User>
) => {
  const userJson = localStorage.getItem('userInfo');

  if (userJson) {
    const localUserInfo = JSON.parse(userJson);

    const updatedUser = {
      ...localUserInfo,
      ...updatedAttributes,
    };

    localStorage.setItem('userInfo', JSON.stringify(updatedUser));
  } else {
    localStorage.setItem('userInfo', JSON.stringify(updatedAttributes));
  }
};

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
