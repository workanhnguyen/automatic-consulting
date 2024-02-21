'use client';

import { createSlice } from '@reduxjs/toolkit';
import { AuthState } from '../module';
import { loginThunk } from '../actions/Auth';

const initialState: AuthState = {
  loadingLogin: false,
  successLogin: false,
  errorLogin: null,
  userInfo: null,
};

export const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    logOut: () => {
      return initialState;
    }
  },
  extraReducers: (builder) => {
    //log in
    builder.addCase(loginThunk.pending, (state) => {
      state.loadingLogin = true;
      state.successLogin = false;
      state.errorLogin = null;
    });
    builder.addCase(loginThunk.fulfilled, (state) => {
      state.loadingLogin = false;
      state.successLogin = true;
      state.errorLogin = null;
    });

    builder.addCase(loginThunk.rejected, (state, action) => {
      state.loadingLogin = false;
      state.successLogin = false;
      state.errorLogin = action.payload !== undefined ? action.payload : null;
    });
  },
});

export const { logOut } = authSlice.actions;
export default authSlice.reducer;
