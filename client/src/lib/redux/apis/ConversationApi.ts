import { ConversationParams } from "../module";
import axiosClient from "./AxiosClient";

const ConversationApi = {
  getConversationMessages: (params?: ConversationParams) => {
    return axiosClient.get(
      `/conversations${
        params?.pageNumber ? `?pageNumber=${params?.pageNumber}` : ""
      }${params?.pageSize ? `&pageSize=${params?.pageSize}` : ""}`
    );
  },
};

export default ConversationApi;
