import { createBrowserRouter, Link } from "react-router-dom";
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
import UsersInGroupPage from "../pages/admin/semester/courses/groups/users/UsersInGroupPage";
import UsersInGroupForm from "../pages/admin/semester/courses/groups/users/UsersInGroupForm";
import CoordinatorsPage from "../pages/admin/semester/courses/coordinators/CoordinatorsPage";
import CoordinatorsForm from "../pages/admin/semester/courses/coordinators/CoordinatorsForm";
import StudentsInCoursePage from "../pages/admin/semester/courses/students/StudentsInCoursePage";
import StudentsInCourseForm from "../pages/admin/semester/courses/students/StudentsInCourseForm";
import ClassesInGroupForm from "../pages/admin/semester/courses/groups/classes/ClassesInGroupForm";
import ClassesInGroupPage from "../pages/admin/semester/courses/groups/classes/ClassesInGroupPage";
import GradeCategoriesPage from "../pages/admin/semester/courses/gradeCategories/GradeCategoriesPage";
import GradeCategoriesForm from "../pages/admin/semester/courses/gradeCategories/GradeCategoriesForm";
import LecurerSemesterPage from "../pages/lecturer/LecurerSemesterPage";
import LecturerCoursePage from "../pages/lecturer/LecturerCoursePage";
import LecturerGradeMenuPage from "../pages/lecturer/LecturerGradeMenuPage";
import LecturerGradePage from "../pages/lecturer/grades/LecturerGradePage";
import LecturerGradesListPage from "../pages/lecturer/grades/LecturerGradesListPage";
import LecturerFinalGradesPage from "../pages/lecturer/grades/LecturerFinalGradesPage";
import StudentSemesterPage from "../pages/student/StudentSemesterPage";
import StudentMenuPage from "../pages/student/StudentMenuPage";
import StudentFinalGradesInSem from "../pages/student/StudentFinalGradesInSem";
import StudentCoursesWithGrades from "../pages/student/grades/StudentCoursesWithGrades";
import StudentGrades from "../pages/student/grades/StudentGrades";

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

    //console.log(isAuthRequired, client.loggedIn);
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
                                element: <LecurerSemesterPage />
                            },
                            {
                                path: ":semesterId",
                                children: [
                                    {
                                        index: true,
                                        element: <LecturerCoursePage />,
                                    },
                                    {
                                        path: ":courseId",
                                        children: [
                                            {
                                                index: true,
                                                element: <LecturerGradeMenuPage />,
                                            },
                                            {
                                                path: "grade-by-student",
                                                children: [
                                                    {
                                                        index: true,
                                                        element: <LecturerGradePage type="student" />
                                                    },
                                                    {
                                                        path: ":searchId",
                                                        element: <LecturerGradesListPage type="student" />
                                                    }
                                                ]
                                            },
                                            {
                                                path: "grade-by-category",
                                                children: [
                                                    {
                                                        index: true,
                                                        element: <LecturerGradePage type="category" />
                                                    },
                                                    {
                                                        path: ":searchId",
                                                        element: <LecturerGradesListPage type="category" />
                                                    }
                                                ]
                                            },
                                            {
                                                path: "final-grade",
                                                element: <LecturerFinalGradesPage />
                                            }
                                        ]
                                    },
                                ]
                            }
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
                                                                element: <><Link to={`coordinators`} className="nav-link">Koordynatorzy</Link><Link to={`groups`} className="nav-link">Grupy</Link><Link to={`students`} className="nav-link">Studenci</Link><Link to={`grade-categories`} className="nav-link">Punktacja przedmiotu</Link></>
                                                            },
                                                            {
                                                                path: "coordinators",
                                                                children: [
                                                                    {
                                                                        index: true,
                                                                        element: <CoordinatorsPage />
                                                                    },
                                                                    {
                                                                        path: "add",
                                                                        element: <CoordinatorsForm />
                                                                    }
                                                                ]
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
                                                                                element: <><Link to={`lecturers`} className="nav-link">Prowadzący</Link><Link to={`students`} className="nav-link">Studenci</Link><Link to={`classes`} className="nav-link">Zajęcia</Link></>
                                                                            },
                                                                            {
                                                                                path: "students",
                                                                                children: [
                                                                                    {
                                                                                        index: true,
                                                                                        element: <UsersInGroupPage type="students" />
                                                                                    },
                                                                                    {
                                                                                        path: "add",
                                                                                        element: <UsersInGroupForm type="students" />
                                                                                    }
                                                                                ]
                                                                            },
                                                                            {
                                                                                path: "lecturers",
                                                                                children: [
                                                                                    {
                                                                                        index: true,
                                                                                        element: <UsersInGroupPage type="lecturers" />
                                                                                    },
                                                                                    {
                                                                                        path: "add",
                                                                                        element: <UsersInGroupForm type="lecturers" />
                                                                                    }
                                                                                ]
                                                                            },
                                                                            {
                                                                                path: "classes",
                                                                                children: [
                                                                                    {
                                                                                        index: true,
                                                                                        element: <ClassesInGroupPage />
                                                                                    },
                                                                                    {
                                                                                        path: "add",
                                                                                        element: <ClassesInGroupForm />
                                                                                    },
                                                                                    {
                                                                                        path: ":classId",
                                                                                        element: <Navigate to=".." />
                                                                                    },
                                                                                    {
                                                                                        path: ":classId/edit",
                                                                                        element: <ClassesInGroupForm />
                                                                                    }
                                                                                ]
                                                                            }
                                                                        ]
                                                                    }
                                                                ]
                                                            },
                                                            {
                                                                path: "students",
                                                                children: [
                                                                    {
                                                                        index: true,
                                                                        element: <StudentsInCoursePage />
                                                                    },
                                                                    {
                                                                        path: "add",
                                                                        element: <StudentsInCourseForm />
                                                                    }
                                                                ]
                                                            },
                                                            {
                                                                path: "grade-categories",
                                                                children: [
                                                                    {
                                                                        index: true,
                                                                        element: <GradeCategoriesPage />
                                                                    },
                                                                    {
                                                                        path: "add",
                                                                        element: <GradeCategoriesForm />
                                                                    },
                                                                    {
                                                                        path: ":gradeCategoryId",
                                                                        element: <Navigate to=".." />
                                                                    },
                                                                    {
                                                                        path: ":gradeCategoryId/edit",
                                                                        element: <GradeCategoriesForm />
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
                                path: "semester",
                                children: [
                                    {
                                        index: true,
                                        element: <StudentSemesterPage />,
                                    },
                                    {
                                        path: ":semesterId",
                                        children: [
                                            {
                                                index: true,
                                                element: <StudentMenuPage />,
                                            },
                                            {
                                                path: "final-grades",
                                                element: <StudentFinalGradesInSem />
                                            },
                                            {
                                                path: "grades",
                                                children: [
                                                    {
                                                        index: true,
                                                        element: <StudentCoursesWithGrades />
                                                    },
                                                    {
                                                        path: ":courseId",
                                                        element: <StudentGrades />
                                                    }
                                                ]
                                            }
                                        ],
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