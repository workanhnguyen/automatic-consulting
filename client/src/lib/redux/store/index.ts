import { configureStore } from "@reduxjs/toolkit";

import authReducer from "../features/authSlice";
import userReducer from "../features/userSlice";
import suggestedQuestionReducer from "../features/suggestedQuestionSlice";

export const store = configureStore({
  reducer: {
    auth: authReducer,
    user: userReducer,
    suggestedQuestion: suggestedQuestionReducer,
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({ serializableCheck: false }),
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
