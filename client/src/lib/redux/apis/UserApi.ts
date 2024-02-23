import axiosClient from "./AxiosClient";

const UserApi = {
  getProfile: () => {
    return axiosClient.get("/users/profile");
  },
};

export default UserApi;
