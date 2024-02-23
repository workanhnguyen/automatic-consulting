export interface User {
  email: string;
  firstName: string;
  lastName: string;
  avatarLink: string;
  createdDate: number;
  lastModifiedDate: number;
  isEnaled: boolean;
}

export interface UserLogin {
  email: string;
  password: string;
}

export interface SuggestedQuestion {
  id: number;
  content: string;
  note: string;
  createdDate: number;
  lastModifiedDate: number;
}

export interface SuggestedQuestionParams {
  pageSize?: number;
  pageNumber?: number;
}

export interface UserRegister {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
}

export interface AuthState {
  // CASE: Login
  loadingLogin: boolean;
  successLogin: boolean;
  errorLogin: any;
  userInfo: User | null;

  // CASE: Login
  loadingRegister: boolean;
  successRegister: any;
  errorRegister: any;
}

export interface UserState {
  loadingUserProfile: boolean;
  userProfile: User | null;
  errorGetUserProfile: any;
}

export interface SuggestedQuestionState {
  loading: boolean;
  suggestedQuestions: SuggestedQuestion[];
  error: any;
}
