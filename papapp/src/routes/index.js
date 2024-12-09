import { createBrowserRouter } from "react-router-dom";
import AppLayout from "../components/layout/AppLayout";
import LoginPage from "../pages/LoginPage"
import DashboardPage from "../pages/DashboardPage"
import MainLayout from "../components/layout/MainLayout";
import { Navigate, Outlet } from 'react-router-dom';
import { useState, useEffect } from 'react';
//import useAuthStatus from './useAuthStatus';

const ProtectedRoute = () => {
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const checkAuthStatus = async () => {
            try {
                const response = await fetch('/api/auth/status', {
                    credentials: 'include',
                });
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

    return isAuthenticated ? <Outlet /> : <Navigate to="/" />;
};

export const router = createBrowserRouter([
    {
        path: "/",
        element: <AppLayout />,
        children: [
            {
                index: true,
                element: <LoginPage />,
            },
            {
                path: "dashboard",
                element: <ProtectedRoute />, // Chroniona trasa
                children: [
                    {
                        element: <MainLayout />,
                        children: [
                            {
                                index: true,
                                element: <DashboardPage />,
                            },
                        ],
                    },
                ],
            },
        ],
    },
]);