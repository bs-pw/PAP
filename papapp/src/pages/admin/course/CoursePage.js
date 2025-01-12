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

    useEffect(() => {
        getCourses();
    }, []);


    return (
        <>
            <Link to="/admin/courses/create" className='nav-link text-primary' style={{ fontSize: "1.2em" }}><i class="bi bi-plus-lg"></i> Nowy</Link >
            <CoursesList listName='Lista kursów' adminButtons={true} columnNames={['ID', 'Nazwa', 'Opis', 'Cena', 'Czas trwania']} data={users} error={error} />
        </>
    )
}

export default CoursePage