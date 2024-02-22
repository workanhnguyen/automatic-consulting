import { createSlice } from "@reduxjs/toolkit";
import { AuthState } from "../module";
import { loginThunk, registerThunk } from "../actions/Auth";

const initialState: AuthState = {
  loadingLogin: false,
  successLogin: false,
  errorLogin: null,
  userInfo: null,

  loadingRegister: false,
  successRegister: null,
  errorRegister: null,
};

export const authSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {
    logOut: () => {
      return initialState;
    },
    resetLoginStatus: (state) => {
      return {
        ...state,
        loadingLogin: false,
        successLogin: false,
        errorLogin: null,
      };
    },
    resetRegisterStatus: (state) => {
      return {
        ...state,
        loadingRegister: false,
        successRegister: null,
        errorRegister: null,
      };
    },
  },
  extraReducers: (builder) => {
    // login
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

    // register
    builder.addCase(registerThunk.pending, (state) => {
      state.loadingRegister = true;
      state.successRegister = false;
      state.errorRegister = null;
    });
    builder.addCase(registerThunk.fulfilled, (state, action) => {
      state.loadingRegister = false;
      state.successRegister = action.payload.data;
      state.errorRegister = null;
    });

    builder.addCase(registerThunk.rejected, (state, action) => {
      state.loadingRegister = false;
      state.successRegister = false;
      state.errorRegister =
        action.payload !== undefined ? action.payload : null;
    });
  },
});

export const { logOut, resetLoginStatus, resetRegisterStatus } =
  authSlice.actions;
export default authSlice.reducer;
