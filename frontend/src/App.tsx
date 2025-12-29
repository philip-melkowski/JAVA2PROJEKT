import {Routes, Route, Navigate} from "react-router-dom";
import LoginPage from "./pages/LoginPage";
import DashboardPage from "./pages/DashboardPage";
import ProtectedRoute from "./auth/ProtectedRoute";
import {useAuth} from "./auth/AuthContext.tsx";
import BooksPage from "./pages/BooksPage.tsx";


function App() {
  const {isAuthenticated} = useAuth();
    return (
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
          <Route path="*" element={
              <Navigate to="/login" />
          }
          />
      </Routes>
  );
}

export default App;