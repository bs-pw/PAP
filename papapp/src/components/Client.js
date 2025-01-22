class Client {
    loggedIn = null;
    userId = null;
    name = null;
    surname = null;
    mail = null;
    userTypeId = null;
    isLocked = false;

    constructor(baseUrl) {
        this.baseUrl = baseUrl;
        this.headers = {
            'Content-Type': 'application/json',
        };
        this.credentials = 'include';
        if (this.loggedIn === null) {
            this.checkAuthStatus();
        }
    }

    async checkAuthStatus() {
        return fetch(`${this.baseUrl}/auth/status`, {
            method: 'GET',
            credentials: this.credentials,
        }).then(response => {
            return response.json();
        }).then(data => {
            this.loggedIn = data.loggedIn;
            this.userId = data.user_id;
            this.name = data.name;
            this.surname = data.surname;
            this.mail = data.mail;
            this.userTypeId = data.userTypeId;
            return true;
        }).catch(error => {
            throw new Error(error.message);
        });
    }

    async isClosed(semesterId, courseId) {
        return fetch(`${this.baseUrl}/courseinsemester/${semesterId}/${courseId}/is-closed`, {
            method: 'GET',
            credentials: this.credentials,
        }).then(response => {
            return response.json();
        }).then(data => {
            this.isLocked = data.isLocked;
            return true;
        }).catch(error => {
            throw new Error(error.message);
        });
    }

    async login(mail, password) {
        return fetch(`${this.baseUrl}/auth/login`, {
            method: 'POST',
            headers: this.headers,
            credentials: this.credentials,
            body: JSON.stringify({ mail, password }),
        }).then(async response => {
            let data = await response.json()
            //console.log(data);
            if (response.ok) {
                this.loggedIn = data.loggedIn;
                this.userId = data.user_id;
                this.name = data.name;
                this.surname = data.surname;
                this.mail = data.mail;
                this.userTypeId = data.userTypeId;
                //console.log("client logged in: " + data.loggedIn);
            }
            return data;
        }).catch(error => {
            throw new Error(error.message);
        });
    }

    async logout() {
        return fetch(`${this.baseUrl}/auth/logout`, {
            method: 'DELETE',
            credentials: this.credentials,
        }).then(response => {
            if (response.ok) {
                this.loggedIn = false;
                this.name = null;
                this.surname = null;
                this.userTypeId = null;
            } else {
                throw new Error('Error logging out');
            }
        }).catch(error => {
            throw new Error(error.message);
        });
    }

    async getUser(userId) {
        return fetch(`${this.baseUrl}/user/${userId}`, {
            method: 'GET',
            headers: this.headers,
            credentials: this.credentials,
        }).then(response => {
            if (response.ok) {
                return response.json();
            }
            throw new Error(`Error fetching user: ${response.statusText}`);
        }).catch(error => {
            throw new Error(error.message);
        });
    }

    async getUsers() {
        return fetch(`${this.baseUrl}/user/all`, {
            method: 'GET',
            headers: this.headers,
            credentials: this.credentials,
        }).then(response => {
            if (response.ok) {
                return response.json();
            }
            throw new Error(`Error fetching users: ${response.statusText}`);
        }).catch(error => {
            throw new Error(error.message);
        });
    }

    async deleteUser(userId) {
        return fetch(`${this.baseUrl}/user/${userId}`, {
            method: 'DELETE',
            headers: this.headers,
            credentials: this.credentials,
        }).then(response => {
            if (response.ok) {
                return true;
            }
            throw new Error(`Error deleting user: ${response.statusText}`);
        }).catch(error => {
            throw new Error(error.message);
        });
    }

    async updateUser(userId, data) {
        return fetch(`${this.baseUrl}/user/${userId}`, {
            method: 'PUT',
            headers: this.headers,
            credentials: this.credentials,
            body: JSON.stringify(data)
        }).then(response => {
            if (response.ok) {
                return true;
            }
            throw new Error(`Error updating user: ${response.statusText}`);
        }).catch(error => {
            throw new Error(error.message);
        });
    }

    async getLecturers() {
        return fetch(`${this.baseUrl}/lecturer`, {
            method: 'GET',
            headers: this.headers,
            credentials: this.credentials,
        }).then(response => {
            if (response.ok) {
                return response.json();
            }
            throw new Error(`Error fetching lecturers: ${response.statusText}`);
        }).catch(error => {
            throw new Error(error.message);
        });
    }

    async getUserTypes() {
        return fetch(`${this.baseUrl}/usertypes`, {
            method: 'GET',
            headers: this.headers,
            credentials: this.credentials,
        }).then(response => {
            if (response.ok) {
                return response.json();
            }
            throw new Error(`Error fetching user types: ${response.statusText}`);
        }).catch(error => {
            throw new Error(error.message);
        });
    }

    async registerUser(data) {
        return fetch(`${this.baseUrl}/user`, {
            method: 'POST',
            headers: this.headers,
            credentials: this.credentials,
            body: JSON.stringify(data),
        }).then(async response => {
            let data = await response.json();
            if (response.ok) {
                return data;
            } else {
                throw new Error(`Error registering user`);
            }
        }).catch(error => {
            throw new Error(error.message);
        });
    }

    async getCourses() {
        return fetch(`${this.baseUrl}/course`, {
            method: 'GET',
            headers: this.headers,
            credentials: this.credentials,
        }).then(response => {
            if (response.ok) {
                return response.json();
            }
            throw new Error(`Error fetching courses: ${response.statusText}`);
        }).catch(error => {
            throw new Error(error.message);
        });
    }

    async getCourse(id) {
        return fetch(`${this.baseUrl}/course/${id}`, {
            method: 'GET',
            headers: this.headers,
            credentials: this.credentials,
        }).then(response => {
            if (response.ok) {
                return response.json();
            }
            throw new Error(`Error fetching course: ${response.statusText}`);
        }).catch(error => {
            throw new Error(error.message);
        });
    }

    async createCourse(data) {
        return fetch(`${this.baseUrl}/course`, {
            method: 'POST',
            headers: this.headers,
            credentials: this.credentials,
            body: JSON.stringify(data),
        }).then(response => {
            if (response.ok) {
                return response.json();
            }
            throw new Error(`Error registering course: ${response.statusText}`);
        }).catch(error => {
            throw new Error(error.message);
        });
    }

    async deleteCourse(id) {
        return fetch(`${this.baseUrl}/course/${id}`, {
            method: 'DELETE',
            headers: this.headers,
            credentials: this.credentials,
        }).then(response => {
            if (response.ok) {
                return true;
            }
            throw new Error(`Error deleting course: ${response.statusText}`);
        }).catch(error => {
            throw new Error(error.message);
        });

    }


    async getSemesters() {
        return fetch(`${this.baseUrl}/semester`, {
            method: 'GET',
            headers: this.headers,
            credentials: this.credentials,
        }).then(response => {
            if (response.ok) {
                return response.json();
            }
            throw new Error(`Error fetching semesters: ${response.statusText}`);
        }).catch(error => {
            throw new Error(error.message);
        });
    }

    async getSemester(semesterId) {
        return fetch(`${this.baseUrl}/semester/${semesterId}`, {
            method: 'GET',
            headers: this.headers,
            credentials: this.credentials,
        }).then(response => {
            if (response.ok) {
                return response.json();
            }
            throw new Error(`Error fetching semester: ${response.statusText}`);
        }).catch(error => {
            throw new Error(error.message);
        });
    }

    async createSemester(data) {
        return fetch(`${this.baseUrl}/semester`, {
            method: 'POST',
            headers: this.headers,
            credentials: this.credentials,
            body: JSON.stringify(data),
        }).then(async response => {
            let data = await response.json();
            if (response.ok) {
                return data;
            } else {
                throw new Error(`Error creating semester`);
            }
        }).catch(error => {
            throw new Error(error.message);
        });
    }

    async deleteSemester(id) {
        return fetch(`${this.baseUrl}/semester?semesterCode=${id}`, {
            method: 'DELETE',
            headers: this.headers,
            credentials: this.credentials,
        }).then(response => {
            if (response.ok) {
                return true;
            }
            throw new Error(`Error deleting semester: ${response.statusText}`);
        }).catch(error => {
            throw new Error(error.message);
        });
    }

    async updateSemester(id, data) {
        return fetch(`${this.baseUrl}/semester`, {
            method: 'PUT',
            headers: this.headers,
            credentials: this.credentials,
            body: JSON.stringify(data),
        }).then(async response => {
            let data = await response.json();
            if (response.ok) {
                return data;
            } else {
                throw new Error(`Error updating semester`);
            }
        }).catch(error => {
            throw new Error(error.message);
        });
    }

    async getStudentCourses(userId) {
        return fetch(`${this.baseUrl}/classes/${userId}`, {
            method: 'GET',
            headers: this.headers,
            credentials: this.credentials,
        }).then(response => {
            if (response.ok) {
                return response.json();
            }
            throw new Error(`Error fetching student courses: ${response.statusText}`);
        }).catch(error => {
            throw new Error(error.message);
        });
    }

    async getCoursesInSemesterBySemester(semesterId) {
        return fetch(`${this.baseUrl}/courseinsemester/bysemester/${semesterId}`, {
            method: 'GET',
            headers: this.headers,
            credentials: this.credentials,
        }).then(response => {
            if (response.ok) {
                return response.json();
            }
            throw new Error(`Error fetching courses in semester: ${response.statusText}`);
        }).catch(error => {
            throw new Error(error.message);
        });
    }

    async addCourseInSemester(semester, course_code) {
        return fetch(`${this.baseUrl}/courseinsemester`, {
            method: 'POST',
            headers: this.headers,
            credentials: this.credentials,
            body: JSON.stringify({ semester, course_code }),
        }).then(async response => {
            let data = await response.json();
            if (response.ok) {
                return data;
            }
            throw new Error(`Error adding course in semester: ${data.message}`);
        }).catch(error => {
            throw new Error(error.message);
        });
    }

    async deleteCourseInSemester(semesterId, courseId) {
        return fetch(`${this.baseUrl}/courseinsemester`, {
            method: 'DELETE',
            headers: this.headers,
            credentials: this.credentials,
            body: JSON.stringify({ semester: semesterId, course_code: courseId }),
        }).then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error(`Error deleting course in semester: ${response.statusText}`);
            }
        }).catch(error => {
            throw new Error(error.message);
        });
    }

    async getGroupsInSemesterInCourse(semesterId, courseId) {
        return fetch(`${this.baseUrl}/group/${semesterId}/${courseId}`,
            {
                method: 'GET',
                headers: this.headers,
                credentials: this.credentials,
            }
        ).then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error(`Error getting groups in semester in course: ${response.json().message}`);
            }
        }).catch(error => {
            throw new Error(error.message);
        });
    }

    async addGroup(semester, course_code, group_number) {
        return fetch(`${this.baseUrl}/group`, {
            method: 'POST',
            headers: this.headers,
            credentials: this.credentials,
            body: JSON.stringify({
                semester, course_code, group_number
            }),
        }).then(response => {
            if (response.ok) {
                return true;
            } else {
                throw new Error(`Error adding group: ${response.json().message}`);
            }
        }).catch(error => {
            throw new Error(error.message);
        });

    }

    async deleteGroup(semester, course_code, group_number) {
        return fetch(`${this.baseUrl}/group`, {
            method: 'DELETE',
            headers: this.headers,
            credentials: this.credentials,
            body: JSON.stringify({
                semester, course_code, group_number
            }),
        }).then(response => {
            if (response.ok) {
                return true;
            } else {
                throw new Error(`Error deleting group: ${response.json().message}`);
            }
        }).catch(error => {
            throw new Error(error.message);
        });
    }

    async getUsersInGroup(semesterId, courseCode, groupNumber, type) {
        return fetch(`${this.baseUrl}/usersingroups/${semesterId}/${courseCode}/${groupNumber}/${type}`,
            {
                method: 'GET',
                headers: this.headers,
                credentials: this.credentials
            }
        )
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new Error(`Error getting students in group: ${response.json().message}`);
                }
            })
            .catch(error => {
                throw new Error(error.message);
            });
    }

    async addUserInGroup(semester, course_code, group_number, user_id, type) {
        return fetch(`${this.baseUrl}/usersingroups/${type}`, {
            method: 'POST',
            headers: this.headers,
            credentials: this.credentials,
            body: JSON.stringify({
                semester, course_code, group_number, user_id
            }),
        }).then(response => {
            if (response.ok) {
                return true;
            } else {
                throw new Error(`Error adding user to group: ${response.json().message}`);

            }
        })
    }

    async deleteUserInGroup(semester, course_code, group_number, user_id) {
        return fetch(`${this.baseUrl}/usersingroups/${semester}/${course_code}/${group_number}/${user_id}`,
            {
                method: 'DELETE',
                headers: this.headers,
                credentials: this.credentials
            }
        )
            .then(response => {
                if (response.ok) {
                    return true;
                } else {
                    throw new Error(`Error deleting user from group: ${response.json().message}`);
                }
            })
            .catch(error => {
                throw new Error(error.message);
            });
    }

    async getAvailableUsersToAddToGroup(semester, course_code, group_number, type) {
        return fetch(`${this.baseUrl}/usersingroups/${semester}/${course_code}/${group_number}/available/${type}`,
            {
                method: 'GET',
                headers: this.headers,
                credentials: this.credentials
            }
        )
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new Error(`Error getting available students to add to group: ${response.json().message}`);
                }
            })
            .catch(error => {
                throw new Error(error.message);
            });
    }

    async getCourseCoordinators(semester, course_code) {
        return fetch(`${this.baseUrl}/coordinators/${semester}/${course_code}`,
            {
                method: 'GET',
                headers: this.headers,
                credentials: this.credentials
            }
        )
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new Error(`Error getting course coordinators: ${response.json().message}`);
                }
            })
            .catch(error => {
                throw new Error(error.message);
            });
    }
    async getAvailableCourseCoordinators(semester, course_code) {
        return fetch(`${this.baseUrl}/coordinators/${semester}/${course_code}/available`,
            {
                method: 'GET',
                headers: this.headers,
                credentials: this.credentials
            }
        )
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new Error(`Error getting course available coordinators: ${response.json().message}`);
                }
            })
            .catch(error => {
                throw new Error(error.message);
            });
    }
    async getCourseStudents(semester, course_code) {
        return fetch(`${this.baseUrl}/courseinsemester/${semester}/${course_code}`,
            {
                method: 'GET',
                headers: this.headers,
                credentials: this.credentials
            }
        )
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new Error(`Error getting course students: ${response.json().message}`);
                }
            })
            .catch(error => {
                throw new Error(error.message);
            });
    }
    async getCourseFinalGrades(semester, course_code) {
        return fetch(`${this.baseUrl}/finalgrades/${semester}/course/${course_code}`,
            {
                method: 'GET',
                headers: this.headers,
                credentials: this.credentials
            }
        )
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new Error(`Error getting course students: ${response.json().message}`);
                }
            })
            .catch(error => {
                throw new Error(error.message);
            });
    }
    async getAvailableCourseStudents(semester, course_code) {
        return fetch(`${this.baseUrl}/finalgrades/${semester}/${course_code}/available`,
            {
                method: 'GET',
                headers: this.headers,
                credentials: this.credentials
            }
        )
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new Error(`Error getting course available students: ${response.json().message}`);
                }
            })
            .catch(error => {
                throw new Error(error.message);
            });
    }
    async addCoordinatorToCourse(semester, course_code, user_id) {
        return fetch(`${this.baseUrl}/coordinators`, {
            method: 'POST',
            headers: this.headers,
            credentials: this.credentials,
            body: JSON.stringify({
                semester, course_code, user_id
            }),
        }).then(response => {
            if (response.ok) {
                return true;
            } else {
                throw new Error(`Error adding coordinator to course: ${response.json().message}`);

            }
        })
    }
    async deleteCoordinatorFromCourse(semester, course_code, user_id) {
        return fetch(`${this.baseUrl}/coordinators/${semester}/${course_code}/${user_id}`,
            {
                method: 'DELETE',
                headers: this.headers,
                credentials: this.credentials
            }
        )
            .then(response => {
                if (response.ok) {
                    return true;
                } else {
                    throw new Error(`Error deleting coordinator from course: ${response.json().message}`);
                }
            })
            .catch(error => {
                throw new Error(error.message);
            });
    }

    async addStudentToCourse(semester, course_code, user_id) {
        return fetch(`${this.baseUrl}/finalgrades`, {
            method: 'POST',
            headers: this.headers,
            credentials: this.credentials,
            body: JSON.stringify({
                semester, course_code, user_id
            }),
        }).then(response => {
            if (response.ok) {
                return true;
            } else {
                throw new Error(`Error adding student to course: ${response.json().message}`);

            }
        })
    }

    async deleteStudentFromCourse(semester, course_code, user_id) {
        return fetch(`${this.baseUrl}/finalgrades`,
            {
                method: 'DELETE',
                headers: this.headers,
                credentials: this.credentials,
                body: JSON.stringify({
                    semester, course_code, user_id
                }),
            }
        )
            .then(response => {
                if (response.ok) {
                    return true;
                } else {
                    throw new Error(`Error deleting student from course: ${response.json().message}`);
                }
            })
            .catch(error => {
                throw new Error(error.message);
            });
    }
    async getUserClasses(userId) {
        return fetch(`${this.baseUrl}/classes/${userId}`,
            {
                method: 'GET',
                headers: this.headers,
                credentials: this.credentials
            }
        )
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new Error(`Error getting users classes: ${response.json().message}`);
                }
            })
            .catch(error => {
                throw new Error(error.message);
            });
    }
    async getClassesForGroup(semester, course_code, group_number) {
        return fetch(`${this.baseUrl}/classes/${semester}/${course_code}/${group_number}`,
            {
                method: 'GET',
                headers: this.headers,
                credentials: this.credentials
            }
        )
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new Error(`Error getting groups classes: ${response.json().message}`);
                }
            })
            .catch(error => {
                throw new Error(error.message);
            });
    }


    async getClassesForCourse(semester, course_code) {
        return fetch(`${this.baseUrl}/classes/${semester}/${course_code}`,
            {
                method: 'GET',
                headers: this.headers,
                credentials: this.credentials
            }
        )
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new Error(`Error getting classes: ${response.json().message}`);
                }
            })
            .catch(error => {
                throw new Error(error.message);
            });
    }
    async getClass(semester, course_code, group_number, class_id_for_group) {
        return fetch(`${this.baseUrl}/classes/${semester}/${course_code}/${group_number}/${class_id_for_group}`,
            {
                method: 'GET',
                headers: this.headers,
                credentials: this.credentials
            }
        )
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new Error(`Error getting class: ${response.json().message}`);
                }
            })
            .catch(error => {
                throw new Error(error.message);
            });
    }

    async insertClassInGroup(data) {
        return fetch(`${this.baseUrl}/classes`, {
            method: 'POST',
            headers: this.headers,
            credentials: this.credentials,
            body: JSON.stringify(data),
        }).then(response => {
            if (response.ok) {
                return true;
            } else {
                throw new Error(`Error inserting classes: ${response.json().message}`);

            }
        })
    }

    async deleteClasses(semester, course_code, group_number, class_id_for_group) {
        return fetch(`${this.baseUrl}/classes/${semester}/${course_code}/${group_number}/${class_id_for_group}`,
            {
                method: 'DELETE',
                headers: this.headers,
                credentials: this.credentials

            }
        )
            .then(response => {
                if (response.ok) {
                    return true;
                } else {
                    throw new Error(`Error deleting classes: ${response.json().message}`);
                }
            })
            .catch(error => {
                throw new Error(error.message);
            });
    }
    async updateClass(semester, course_code, group_number, class_id_for_group, data) {
        return fetch(`${this.baseUrl}/classes/${semester}/${course_code}/${group_number}/${class_id_for_group}`, {
            method: 'PUT',
            headers: this.headers,
            credentials: this.credentials,
            body: JSON.stringify(data)
        }).then(response => {
            if (response.ok) {
                return true;
            }
            throw new Error(`Error updating class: ${response.statusText}`);
        }).catch(error => {
            throw new Error(error.message);
        });
    }
    async getClassTypes() {
        return fetch(`${this.baseUrl}/classtypes`,
            {
                method: 'GET',
                headers: this.headers,
                credentials: this.credentials
            }
        )
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new Error(`Error getting classtypes: ${response.json().message}`);
                }
            })
            .catch(error => {
                throw new Error(error.message);
            });
    }

    async getCoursesInSemesterByCoordinator(semesterId, userId) {
        return fetch(`${this.baseUrl}/courseinsemester/bycoordinator/${semesterId}/${userId}`, {
            method: 'GET',
            headers: this.headers,
            credentials: this.credentials,
        }).then(response => {
            if (response.ok) {
                return response.json();
            }
            throw new Error(`Error fetching courses in semester: ${response.json().message}`);
        }).catch(error => {
            throw new Error(error.message);
        });
    }
    async getCoursesInSemesterByLecturerAndCoordinator(semesterId, userId) {
        return fetch(`${this.baseUrl}/courseinsemester/bylecturer/${semesterId}/${userId}`, {
            method: 'GET',
            headers: this.headers,
            credentials: this.credentials,
        }).then(response => {
            if (response.ok) {
                return response.json();
            }
            throw new Error(`Error fetching courses in semester: ${response.json().message}`);
        }).catch(error => {
            throw new Error(error.message);
        });
    }

    async getGradeCategoriesInCourse(semester, coursesCode) {
        return fetch(`${this.baseUrl}/gradecategories/${semester}/${coursesCode}`, {
            method: 'GET',
            headers: this.headers,
            credentials: this.credentials,
        }).then(response => {
            if (response.ok) {
                return response.json();
            }
            throw new Error(`Error fetching grade categories: ${response.json().message}`);
        }).catch(error => {
            throw new Error(error.message);
        });
    }
    async insertGradeCategory(data) {
        return fetch(`${this.baseUrl}/gradecategories`, {
            method: 'POST',
            headers: this.headers,
            credentials: this.credentials,
            body: JSON.stringify(data),
        }).then(response => {
            if (response.ok) {
                return true;
            } else {
                throw new Error(`Error inserting grade categories: ${response.json().message}`);

            }
        })
    }
    async updateGradeCategory(semester, course_code, categoryId, data) {
        return fetch(`${this.baseUrl}/gradecategories/${semester}/${course_code}/${categoryId}`, {
            method: 'PUT',
            headers: this.headers,
            credentials: this.credentials,
            body: JSON.stringify(data)
        }).then(response => {
            if (response.ok) {
                return true;
            }
            throw new Error(`Error updating grade category: ${response.statusText}`);
        }).catch(error => {
            throw new Error(error.message);
        });
    }
    async deleteGradeCategory(semester, course_code, categoryId) {
        return fetch(`${this.baseUrl}/gradecategories/${semester}/${course_code}/${categoryId}`,
            {
                method: 'DELETE',
                headers: this.headers,
                credentials: this.credentials

            }
        )
            .then(response => {
                if (response.ok) {
                    return true;
                } else {
                    throw new Error(`Error deleting grade categories: ${response.json().message}`);
                }
            })
            .catch(error => {
                throw new Error(error.message);
            });
    }

    async getGradeCategory(semester, course_code, categoryId) {
        return fetch(`${this.baseUrl}/gradecategories/${semester}/${course_code}/${categoryId}`, {
            method: 'GET',
            headers: this.headers,
            credentials: this.credentials
        })
            .then(response => {
                if (response.ok) {
                    return response.json();
                }
                throw new Error(`Error getting grade category: ${response.json().message}`);
            })
            .catch(error => {
                throw new Error(error.message);
            });
    }
    async getSemesterByCoordinatorAndLecturer(userId) {
        return fetch(`${this.baseUrl}/semester/bylecturer/${userId}`, {
            method: 'GET',
            headers: this.headers,
            credentials: this.credentials
        })
            .then(response => {
                if (response.ok) {
                    return response.json();
                }
                throw new Error(`Error getting semester: ${response.json().message}`);
            })
            .catch(error => {
                throw new Error(error.message);
            });
    }
    async getSemesterByCoordinator(userId) {
        return fetch(`${this.baseUrl}/semester/bycoordinator/${userId}`, {
            method: 'GET',
            headers: this.headers,
            credentials: this.credentials
        })
            .then(response => {
                if (response.ok) {
                    return response.json();
                }
                throw new Error(`Error getting semester: ${response.json().message}`);
            })
            .catch(error => {
                throw new Error(error.message);
            });

    }
    async getSemesterByStudent(userId) {
        return fetch(`${this.baseUrl}/semester/bystudent/${userId}`, {
            method: 'GET',
            headers: this.headers,
            credentials: this.credentials
        })
            .then(response => {
                if (response.ok) {
                    return response.json();
                }
                throw new Error(`Error getting semester: ${response.json().message}`);
            })
            .catch(error => {
                throw new Error(error.message);
            });
    }

    async getSemesterByLecturer(userId) {
        return fetch(`${this.baseUrl}/semesters/bylecturer/${userId}`, {
            method: 'GET',
            headers: this.headers,
            credentials: this.credentials
        })
            .then(response => {
                if (response.ok) {
                    return response.json();
                }
                throw new Error(`Error getting semesters: ${response.json().message}`);
            })
            .catch(error => {
                throw new Error(error.message);
            });
    }
    async checkIfIsCoordinatorOfCourse(semester, courseCode, userId) {
        return fetch(`${this.baseUrl}/coordinators/${semester}/${courseCode}/amicoordinator/${userId}`, {
            method: 'GET',
            headers: this.headers,
            credentials: this.credentials
        })
            .then(response => {
                if (response.ok) {
                    return response.json();
                }
                throw new Error(`Error checking if is coordinator: ${response.json().message}`);
            })
            .catch(error => {
                throw new Error(error.message);
            });
    }

    async getGradesByCategory(semesters, courseCode, categoryId) {
        return fetch(`${this.baseUrl}/grades/${semesters}/${courseCode}/${categoryId}/category`, {
            method: 'GET',
            headers: this.headers,
            credentials: this.credentials
        })
            .then(response => {
                if (response.ok) {
                    return response.json();
                }
                throw new Error(`Error getting grades: ${response.json().message}`);
            })
            .catch(error => {
                throw new Error(error.message);
            });
    }
    async getAllGradesOfCourseForUser(semesters, courseCode, userId) {
        return fetch(`${this.baseUrl}/grades/${semesters}/${courseCode}/${userId}/user`, {
            method: 'GET',
            headers: this.headers,
            credentials: this.credentials
        })
            .then(response => {
                if (response.ok) {
                    return response.json();
                }
                throw new Error(`Error getting grades: ${response.json().message}`);
            })
            .catch(error => {
                throw new Error(error.message);
            });
    }
    // async insertGrade(data) {
    //     return fetch(`${this.baseUrl}/grades`, {
    //         method: 'POST',
    //         headers: this.headers,
    //         credentials: this.credentials,
    //         body: JSON.stringify(data),
    //     }).then(response => {
    //         if (response.ok) {
    //             return true;
    //         } else {
    //             throw new Error(`Error inserting grade: ${response.json().message}`);

    //         }
    //     })
    // }
    // async updateGrade(semester, course_code, categoryId, userId, data) {
    //     return fetch(`${this.baseUrl}/grades/${semester}/${course_code}/${categoryId}/${userId}`, {
    //         method: 'PUT',
    //         headers: this.headers,
    //         credentials: this.credentials,
    //         body: JSON.stringify(data)
    //     }).then(response => {
    //         if (response.ok) {
    //             return true;
    //         }
    //         throw new Error(`Error updating grade: ${response.statusText}`);
    //     }).catch(error => {
    //         throw new Error(error.message);
    //     });
    // }
    async insertGrade(semesterId, courseId, data) {
        return fetch(`${this.baseUrl}/grades/${semesterId}/${courseId}`, {
            method: 'POST',
            headers: this.headers,
            credentials: this.credentials,
            body: JSON.stringify(data),
        }).then(response => {
            if (response.ok) {
                return true;
            } else {
                throw new Error(`Error inserting grade: ${response.json().message}`);

            }
        })
    }
    async deleteGrade(semester, course_code, categoryId, userId) {
        return fetch(`${this.baseUrl}/grades/${semester}/${course_code}/${categoryId}/${userId}`,
            {
                method: 'DELETE',
                headers: this.headers,
                credentials: this.credentials

            }
        )
            .then(response => {
                if (response.ok) {
                    return true;
                } else {
                    throw new Error(`Error deleting grade: ${response.json().message}`);
                }
            })
            .catch(error => {
                throw new Error(error.message);
            });
    }
    async updateCourse(courseCode, data) {
        return fetch(`${this.baseUrl}/course/${courseCode}`, {
            method: 'PUT',
            headers: this.headers,
            credentials: this.credentials,
            body: JSON.stringify(data)
        }).then(response => {
            if (response.ok) {
                return true;
            }
            throw new Error(`Error updating course: ${response.statusText}`);
        }).catch(error => {
            throw new Error(error.message);
        });
    }
    async updateFinalGrades(semester, courseCode, data) {
        return fetch(`${this.baseUrl}/finalgrades/${semester}/${courseCode}`, {
            method: 'PUT',
            headers: this.headers,
            credentials: this.credentials,
            body: JSON.stringify(data)
        }).then(response => {
            if (response.ok) {
                return true;
            }
            throw new Error(`Error updating final grades: ${response.statusText}`);
        }).catch(error => {
            throw new Error(error.message);
        });
    }
    async updateFinalGradeNullsToTwosInSemester(semester) {
        return fetch(`${this.baseUrl}/finalgrades/${semester}`, {
            method: 'PUT',
            headers: this.headers,
            credentials: this.credentials
        }).then(response => {
            if (response.ok) {
                return true;
            }
            throw new Error(`Error updating final grades: ${response.statusText}`);
        }).catch(error => {
            throw new Error(error.message);
        });
    }
    async getStudentsFinalGrades(semester, userId) {
        return fetch(`${this.baseUrl}/finalgrades/${semester}/student/${userId}`,
            {
                method: 'GET',
                headers: this.headers,
                credentials: this.credentials
            }
        )
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new Error(`Error getting students final grades: ${response.json().message}`);
                }
            })
            .catch(error => {
                throw new Error(error.message);
            });
    }
    async getMaxPointsToGetInCourse(semester, courseCode) {
        return fetch(`${this.baseUrl}/gradecategories/${semester}/${courseCode}/sum`,
            {
                method: 'GET',
                headers: this.headers,
                credentials: this.credentials
            }
        )
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new Error(`Error getting sum of grade categories' max grade in course: ${response.json().message}`);
                }
            })
            .catch(error => {
                throw new Error(error.message);
            });
    }

}


export default Client;
