import {Routes, Route, Navigate, useLocation} from "react-router-dom";
import LoginPage from "./pages/LoginPage";
import DashboardPage from "./pages/DashboardPage";
import ProtectedRoute from "./auth/ProtectedRoute";
import {useAuth} from "./auth/AuthContext.tsx";
import BooksPage from "./pages/BooksPage.tsx";
import MyReviewsPage from "./pages/MyReviewsPage.tsx";
import Navbar from "./components/Navbar.tsx";


function App() {
  const {isAuthenticated} = useAuth();
  const location = useLocation();

  const hideNavbar = location.pathname === "/login";

    return (
        <>
            {!hideNavbar && <Navbar />}
      <Routes>
        <Route path="/login" element={
            isAuthenticated ? <Navigate to="/dashboard" replace/>
                :
            <LoginPage/>
        }
        />
          <Route path="/dashboard" element={
              <ProtectedRoute>
                  <DashboardPage></DashboardPage>
              </ProtectedRoute>
          }
          />
            <Route path="/books" element={
                <ProtectedRoute>
                    <BooksPage></BooksPage>
                </ProtectedRoute>
            }
            />
          <Route path="/my-reviews" element={
              <ProtectedRoute>
                  <MyReviewsPage></MyReviewsPage>
              </ProtectedRoute>
          }></Route>
          <Route path="*" element={
              <Navigate to="/login" />
          }
          />
      </Routes>
        </>
  );
}

export default App;