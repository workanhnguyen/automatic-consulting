import { SuggestedQuestionParams } from "../module";
import axiosClient from "./AxiosClient";

const SuggestedQuestionApi = {
  getSuggestedQuestions: (params?: SuggestedQuestionParams) => {
    return axiosClient.get(
      `/suggestedQuestions${
        params?.pageNumber ? `?pageNumber=${params?.pageNumber}` : ""
      }${params?.pageSize ? `&pageSize=${params?.pageSize}` : ""}`
    );
  },
};

export default SuggestedQuestionApi;
