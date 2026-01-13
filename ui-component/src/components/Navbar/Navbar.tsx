import React, { useContext, useEffect, useState } from "react";
import "./Navbar.css";
import { Link } from "react-router-dom";
import { AuthContext } from "../../auth/AuthContext";

// Navbar Component: Displays navigation links and user info based on role
const Navbar = () => {
  const [currentRole, setCurrentRole] = useState<any>("");

  const { user, logout } = useContext<any>(AuthContext);

  useEffect(() => {
    const userRole = localStorage.getItem("userRole");
    setCurrentRole(userRole);
  }, []);
  return (
    <div className="navbar-wrapper d-flex justify-content-between align-items-center bg-dark px-3">
      <div>
        <h3 className="text-white">Smart Healthcare System</h3>
      </div>
      <div>
        {/* Home link */}
        <Link to="/" className="text-decoration-none text-white mx-2">
          Home
        </Link>

        {/* Show New Doctor link for admin role */}
        {currentRole === "ROLE_ADMIN" && (
          <Link
            to="/newdoctor"
            className="text-decoration-none text-white mx-2"
          >
            New Doctor
          </Link>
        )}

        {/* Show New Appointment link for patient role */}
        {currentRole === "ROLE_PATIENT" && (
          <Link
            to="/newappointment"
            className="text-decoration-none text-white mx-2"
          >
            New Appointment
          </Link>
        )}

        {/* Display greeting based on user role */}
        {currentRole === "ROLE_ADMIN" && (
          <span className="text-white">Hi, Admin</span>
        )}
        {currentRole !== "ROLE_ADMIN" && (
          <span className="text-white">Hi, {user?.firstName}</span>
        )}

        {/* Logout button */}
        <button className="bg-white text-black p-2 btn mx-4" onClick={logout}>
          Logout
        </button>
      </div>
    </div>
  );
};

export default Navbar;
