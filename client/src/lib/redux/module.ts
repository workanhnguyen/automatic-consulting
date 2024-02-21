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

export interface AuthState {
  loadingLogin: boolean;
  successLogin: boolean;
  errorLogin: any;
  userInfo: User | null;
}
