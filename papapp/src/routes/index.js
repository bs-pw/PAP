import { createBrowserRouter } from "react-router-dom";
import AppLayout from "../components/layout/AppLayout";
import LoginPage from "../pages/LoginPage"
import DashboardPage from "../pages/DashboardPage"
import MainLayout from "../components/layout/MainLayout";
import { Navigate, Outlet } from 'react-router-dom';
import { useState, useEffect } from 'react';
import LecturerPage from "../pages/LecturersPage";
import RegisterPage from "../pages/RegisterPage";
//import useAuthStatus from './useAuthStatus';

const ProtectedRoute = ({ isAuthRequired = true }) => {
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const checkAuthStatus = async () => {
            try {
                const response = await fetch('http://localhost:80/api/auth/status', { method: 'GET', credentials: 'include' });
                const data = await response.json();
                setIsAuthenticated(data.loggedIn);
            } catch (error) {
                console.error('Błąd sprawdzania statusu logowania', error);
            } finally {
                setLoading(false);
            }
        };

        checkAuthStatus();
    }, []);

    if (loading) {
        return <div>Ładowanie...</div>;
    }

    if (isAuthRequired) {
        return isAuthenticated ? <Outlet /> : <Navigate to="/" />;
    } else {
        return !isAuthenticated ? <Outlet /> : <Navigate to="/dashboard" />;
    }
};


export const router = createBrowserRouter([
    {
        path: "/",
        element: <AppLayout />,
        children: [
            {
                element: <ProtectedRoute isAuthRequired={false} />,
                children: [
                    {
                        index: true,
                        element: <LoginPage />
                    },
                ],
            },
            {
                element: <ProtectedRoute />,
                children: [
                    {
                        path: "dashboard",
                        element: <MainLayout />,
                        children: [
                            {
                                index: true,
                                element: <DashboardPage />,
                            },
                        ],
                    },
                    {
                        path: "lecturer",
                        element: <MainLayout />,
                        children: [
                            {
                                index: true,
                                element: <LecturerPage />,
                            },
                        ],
                    },
                    {
                        path: "register",
                        element: <MainLayout />,
                        children: [
                            {
                                index: true,
                                element: <RegisterPage />,
                            },
                        ],
                    },
                ],
            },
        ],
    },
    {
        path: "*",
        element: <Navigate to="/" replace />,
    }
]);