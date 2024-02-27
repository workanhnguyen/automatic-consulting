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

export interface ConversationParams extends SuggestedQuestionParams {}

export interface Message {
  createdDate: number;
  question: string;
  answer: string;
}

export interface ConversationResponse {
  data: Message[];
  hasNext: boolean;
}

export interface UserRegister {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
}

export interface UserInfoUpdate {
  firstName: string;
  lastName: string;
}

export interface UserPasswordUpdate {
  oldPassword: string;
  newPassword: string;
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

  loadingChangeAvatar: boolean;
  newAvatarLink: any;
  errorChangeAvatar: any;

  loadingUpdateUserInfo: boolean;
  newUserInfo: User | null;
  errorUpdateUserInfo: any;

  loadingChangePassword: boolean;
  successChangePassword: any;
  errorChangePassword: any;
}

export interface SuggestedQuestionState {
  loading: boolean;
  suggestedQuestions: SuggestedQuestion[];
  error: any;
}

export interface ConversationState {
  loadingMessages: boolean;
  messages: ConversationResponse | null;
  totalMessages: {
    createdDate: number;
    question: string;
    answer: string;
  }[];
  errorGetMessages: any;

  loadingSendQuery: boolean;
  returnedResult: Message | null;
  errorSendQuery: any;
}
