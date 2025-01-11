import { createBrowserRouter } from "react-router-dom";
import { useClient } from "../components/ClientContext";
import AppLayout from "../components/layout/AppLayout";
import LoginPage from "../pages/LoginPage"
import DashboardPage from "../pages/DashboardPage"
import MainLayout from "../components/layout/MainLayout";
import { Navigate, Outlet } from 'react-router-dom';
import { useState, useEffect } from 'react';
import LecturerPage from "../pages/LecturersPage";
import RegistrationPage from "../pages/admin/user/RegistrationPage";
import CreateCourse from "../pages/admin/course/CreateCourse";
import UsersPage from "../pages/admin/user/UsersPage";
//import useAuthStatus from './useAuthStatus';

const ProtectedRoute = ({ isAuthRequired = true }) => {
    const [loading, setLoading] = useState(true);
    const client = useClient();

    const checkAuthStatus = async () => {
        try {
            const data = await client.checkAuthStatus();
            if (data) {
                setLoading(false);
            }
        } catch (error) {
            console.error('Błąd podczas sprawdzania autoryzacji:', error);
        }
    };

    useEffect(() => {
        if (client.loggedIn === null) {
            checkAuthStatus();
        } else {
            setLoading(false);
        }
    }, [client.loggedIn]);

    if (loading) {
        return <div>Ładowanie...</div>;
    }

    console.log(isAuthRequired, client.loggedIn);
    if (isAuthRequired) {
        return client.loggedIn ? <Outlet /> : <Navigate to="/" />;
    } else {
        return !client.loggedIn ? <Outlet /> : <Navigate to="/dashboard" />;
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
                        path: "admin",
                        element: <MainLayout />,
                        children: [
                            {
                                path: "users",
                                children: [
                                    {
                                        index: true,
                                        element: <UsersPage />,
                                    },
                                    {
                                        path: "register",
                                        children: [
                                            {
                                                index: true,
                                                element: <RegistrationPage />,
                                            },
                                        ],
                                    },
                                ],
                            },
                            {
                                path: "create-course",
                                element: <MainLayout />,
                                children: [
                                    {
                                        index: true,
                                        element: <CreateCourse />,
                                    },
                                ],
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