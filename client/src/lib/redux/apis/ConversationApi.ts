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
  sendQuery: (query: String) => {
    return axiosClient.post("/dialogflow/query", { query });
  }
};

export default ConversationApi;
