import axiosClient from "./AxiosClient";

const UserApi = {
  getProfile: () => {
    return axiosClient.get("/users/profile");
  },
  changeAvatar: (avatarBase64: string) => {
    return axiosClient.post("/users/changeAvatar", { avatarBase64 });
  }
};

export default UserApi;
