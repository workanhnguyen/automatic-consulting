import { UserLogin, UserRegister } from '../module';
import AxiosClient from './AxiosClient';

const AuthApi = {
  login: (userAccount: UserLogin) => {
    return AxiosClient.post('/auth/signin', {
      email: userAccount.email,
      password: userAccount.password,
    });
  },
  register: (userAccount: UserRegister) => {
    return AxiosClient.post('/auth/signup', { ...userAccount });
  },
  logout: () => {
    return AxiosClient.post('/auth/logout');
  },
  refreshToken: (refreshToken: string) => {
    return AxiosClient.post('/auth/refreshToken', {
      refreshToken,
    });
  },
  getProfile: () => {
    return AxiosClient.get('/auth/profile');
  },
  updateProfile: (payload: any) => {
    return AxiosClient.patch('/auth/update', {
      payload,
    });
  },
  updatePassword: (oldPassword: string, newPassword: string) => {
    return AxiosClient.post('/auth/password', {
      oldPassword,
      newPassword,
    });
  },
};

export default AuthApi;
