import React from "react";
import { Navigate, useLocation } from "react-router-dom";
import { useAuthStore } from "../store/auth";

const ProtectedRoute = ({ children }) => {
  // const token = localStorage.getItem("token");
  const token = useAuthStore((state) => state.token);
  // we  havent switch to zustand, previously we used localStorage
  const location = useLocation();

  // If no token, redirect to login
  if (!token) {
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  return children;
};

export default ProtectedRoute;
