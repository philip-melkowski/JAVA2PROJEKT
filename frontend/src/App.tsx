import {Routes, Route, Navigate} from "react-router-dom";
import LoginPage from "./pages/LoginPage";
import DashboardPage from "./pages/DashboardPage";
import {useAuth} from "./auth/AuthContext";


function App() {
  const {isAuthenticated} = useAuth();
    return (
      <Routes>
        <Route path="/login" element={
            isAuthenticated ? <Navigate to="/dashboard" />
                :
            <LoginPage/>
        }/>
          <Route path="/dashboard" element={
              isAuthenticated ? <DashboardPage />
                :
                <Navigate to="/login" />
          } />

          <Route path="*" element={
              <Navigate to="/dashboard" />
          }
                 />
      </Routes>
  );
}

export default App;