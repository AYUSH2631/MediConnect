import React, { useState } from "react";
import { navigateTo } from "../../services/navigateUtil";
import { register } from "../../services/service";

const Signup = () => {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  // Patient fields
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [phone, setPhone] = useState("");
  const [age, setAge] = useState("");

  const handleSubmit = async (e: any) => {
    e.preventDefault();

    const payload = {
      username,
      email,
      password,
      roles: null, // backend defaults to PATIENT
      patientRequest: {
        firstName,
        lastName,
        email,      // reuse same email
        phone,
        age: Number(age)
      }
    };

    try {
      await register(payload);
      alert("Registration successful. Please login.");
      navigateTo("/login");
    } catch (err) {
      alert("Signup failed");
    }
  };

  return (
    <div
      className="modal show d-block"
      style={{ backgroundColor: "rgba(18, 19, 19, 0.85)" }}
    >
      <div className="modal-dialog modal-dialog-centered">
        <div className="modal-content">
          <div className="modal-body">
            <h4 className="mb-3 text-center">SIGN UP</h4>

            <form onSubmit={handleSubmit}>
              <input className="form-control mb-2" placeholder="Username"
                value={username} onChange={(e) => setUsername(e.target.value)} required />

              <input className="form-control mb-2" placeholder="Email" type="email"
                value={email} onChange={(e) => setEmail(e.target.value)} required />

              <input className="form-control mb-2" placeholder="Password" type="password"
                value={password} onChange={(e) => setPassword(e.target.value)} required />

              <hr />

              <input className="form-control mb-2" placeholder="First Name"
                value={firstName} onChange={(e) => setFirstName(e.target.value)} required />

              <input className="form-control mb-2" placeholder="Last Name"
                value={lastName} onChange={(e) => setLastName(e.target.value)} required />

              <input className="form-control mb-2" placeholder="Phone"
                value={phone} onChange={(e) => setPhone(e.target.value)} required />

              <input className="form-control mb-3" placeholder="Age" type="number"
                value={age} onChange={(e) => setAge(e.target.value)} required />

              <button type="submit" className="btn btn-dark w-100">
                Register
              </button>

              <p className="text-center mt-3">
                Already have an account?{" "}
                <span
                  style={{ cursor: "pointer", color: "blue" }}
                  onClick={() => navigateTo("/login")}
                >
                  Login
                </span>
              </p>
            </form>

          </div>
        </div>
      </div>
    </div>
  );
};

export default Signup;
