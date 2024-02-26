import { UserInfoUpdate } from "../module";
import axiosClient from "./AxiosClient";

const UserApi = {
  getProfile: () => {
    return axiosClient.get("/users/profile");
  },
  changeAvatar: (avatarBase64: string) => {
    return axiosClient.post("/users/changeAvatar", { avatarBase64 });
  },
  updateInfo: (args: UserInfoUpdate) => {
    return axiosClient.post("/users/updateInfo", { ...args });
  }
};

export default UserApi;
