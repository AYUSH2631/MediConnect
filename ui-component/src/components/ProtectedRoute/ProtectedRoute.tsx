import React from "react";
import { Navigate } from "react-router-dom";

const ProtectedRoute = ({ children, roles }) => {
  const isAuthenticated = localStorage.getItem("jwt");

  const role = localStorage.getItem("userRole");

  // If the user is not authenticated (no JWT token), redirect them to the login page
  if (!isAuthenticated) {
    return <Navigate to="/login" />;
  }

  // If the user is authenticated but their role doesn't match the required roles, redirect to unauthorized page
  if (roles && !roles.includes(role)) {
    return <Navigate to="/unauthorized" />;
  }

  // If the user is authenticated and has the required role(s), render the protected children components
  return children;
};

export default ProtectedRoute;
