import React, { useEffect, useState } from 'react';
import { useClient } from '../ClientContext';

const CoursesList = ({ adminButtons = false }) => {
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
        <div>CoursesList</div>
    )
}

export default CoursesList