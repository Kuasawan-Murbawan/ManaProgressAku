import axios from "axios";

const REST_API_BASE_URL = "http://localhost:8080/mana-progress-aku";

export const exerciseList = () => {
  return axios.get(REST_API_BASE_URL);
};
