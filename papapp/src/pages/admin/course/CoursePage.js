import React from 'react'
import { Link } from 'react-router-dom'
import CoursesList from '../../../components/common/List'
import { useClient } from '../../../components/ClientContext'
import { useState, useEffect } from 'react'

const CoursePage = () => {
    const [users, setUsers] = useState([]);
    const [error, setError] = useState(null);
    const client = useClient();

    const getCourses = async () => {
        try {
            const data = await client.getCourses();
            console.log(data);
            setUsers(data);
        } catch (error) {
            setError('Błąd podczas ładowania danych: ' + error.message);
        }
    }

    const handleDelete = async (e) => {
        e.preventDefault();
        try {
            if (await client.deleteCourse(e.target.value)) {
                await getCourses();
            }
        } catch (error) {
            setError('Błąd podczas usuwania kursu: ' + error.message);
        }
    }

    useEffect(() => {
        getCourses();
    }, []);


    return (
        <>
            <Link to="/admin/courses/create" className='nav-link text-primary' style={{ fontSize: "1.2em" }}><i className="bi bi-plus-lg"></i> Nowy</Link >
            <CoursesList listName='Lista kursów' adminButtons={true} columnNames={['Skrót', 'Nazwa']} data={users} handleDelete={handleDelete} error={error} id="course_code" />
        </>
    )
}

export default CoursePage