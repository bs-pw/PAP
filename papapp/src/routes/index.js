import { createBrowserRouter } from "react-router-dom";
import { useClient } from "../components/ClientContext";
import AppLayout from "../components/layout/AppLayout";
import LoginPage from "../pages/LoginPage"
import DashboardPage from "../pages/DashboardPage"
import MainLayout from "../components/layout/MainLayout";
import { Navigate, Outlet } from 'react-router-dom';
import { useState, useEffect } from 'react';
import LecturerPage from "../pages/LecturersPage";
import UserForm from "../pages/admin/user/UserForm";
import CourseForm from "../pages/admin/course/CourseForm";
import UsersPage from "../pages/admin/user/UsersPage";
import CoursePage from "../pages/admin/course/CoursePage";
import SemesterPage from "../pages/admin/semester/SemesterPage";
import SemesterForm from "../pages/admin/semester/SemesterForm";
import StudentCourses from "../pages/student/StudentCourses";
import MyDetails from "../pages/MyDetails";
import CourseInSemesterPage from "../pages/admin/semester/courses/CourseInSemesterPage";
import CourseInSemesterForm from "../pages/admin/semester/courses/CourseInSemesterForm";
import GroupsInCoursePage from "../pages/admin/semester/courses/groups/GroupsInCoursePage";
import GroupsInCourseForm from "../pages/admin/semester/courses/groups/GroupsInCourseForm";
import StudentsInGroupPage from "../pages/admin/semester/courses/groups/students/StudentsInGroupPage";
import StudentsInGroupForm from "../pages/admin/semester/courses/groups/students/StudentsInGroupForm";
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
                        path: "settings",
                        element: <MainLayout />,
                        children: [
                            {
                                index: true,
                                element: <MyDetails />,
                            },
                        ],
                    },
                    {
                        path: "admin",
                        element: <MainLayout />,
                        children: [
                            {
                                index: true,
                                element: <UsersPage />,
                            },
                            {
                                path: "users",
                                children: [
                                    {
                                        index: true,
                                        element: <UsersPage />,
                                    },
                                    {
                                        path: "register",
                                        element: <UserForm />,
                                    },
                                    {
                                        path: "edit/:id",
                                        element: <UserForm />,
                                    },
                                ],
                            },
                            {
                                path: "courses",
                                children: [
                                    {
                                        index: true,
                                        element: <CoursePage />,
                                    },
                                    {
                                        path: "create",
                                        element: <CourseForm />,

                                    },
                                    {
                                        path: "edit/:id",
                                        element: <CourseForm />,
                                    },
                                ],
                            },
                            {
                                path: "semesters",
                                children: [
                                    {
                                        index: true,
                                        element: <SemesterPage />,
                                    },
                                    {
                                        path: "create",
                                        element: <SemesterForm />,
                                    },
                                    {
                                        path: "edit/:id",
                                        element: <SemesterForm />,
                                    },
                                    {
                                        path: ":semesterId",
                                        children: [
                                            {
                                                index: true,
                                                element: <CourseInSemesterPage />,
                                            },
                                            {
                                                path: "courses",
                                                children: [
                                                    {
                                                        index: true,
                                                        element: <CourseInSemesterPage />,
                                                    },
                                                    {
                                                        path: "add",
                                                        element: <CourseInSemesterForm />,
                                                    },
                                                    {
                                                        path: ":courseId",
                                                        children: [
                                                            {
                                                                index: true,
                                                                element: <GroupsInCoursePage />
                                                            },
                                                            {
                                                                path: "groups",
                                                                children: [
                                                                    {
                                                                        index: true,
                                                                        element: <GroupsInCoursePage />
                                                                    },
                                                                    {
                                                                        path: "add",
                                                                        element: <GroupsInCourseForm />,
                                                                    },
                                                                    {
                                                                        path: ":groupId",
                                                                        children: [
                                                                            {
                                                                                index: true,
                                                                                element: <StudentsInGroupPage />
                                                                            },
                                                                            {
                                                                                path: "students",
                                                                                children: [
                                                                                    {
                                                                                        index: true,
                                                                                        element: <StudentsInGroupPage />
                                                                                    },
                                                                                    {
                                                                                        path: "add",
                                                                                        element: <StudentsInGroupForm />
                                                                                    }
                                                                                ]
                                                                            }
                                                                        ]
                                                                    }
                                                                ]
                                                            }
                                                        ]
                                                    }
                                                ]
                                            },

                                        ],
                                    },
                                ],
                            },
                        ],
                    },
                    {
                        path: "student",
                        element: <MainLayout />,
                        children: [
                            {
                                path: "courses",
                                children: [
                                    {
                                        index: true,
                                        element: <StudentCourses />,
                                    },
                                ],
                            },
                            {
                                path: "semesters",
                                children: [
                                    {
                                        index: true,
                                        element: <SemesterPage />,
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