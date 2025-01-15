import React, { useState, useEffect } from 'react';
import { useClient } from '../../components/ClientContext';
import { Link, useNavigate, useParams } from 'react-router-dom';
import List from '../../components/common/List';

const LecurerSemesterPage = () => {
    const [semesters, getSemesters] = useState([]);
    const [error, setError] = useState('');
    const client = useClient();
    const navigate = useNavigate();

    const getSemestersByLecturer = async () => {
        try {
            const data = await client.getSemesterByCoordinatorAndLecturer(client.userId);
            getSemesters(data);
        } catch (error) {
            await setError('Błąd podczas ładowania danych: ' + error.message);
        }
    };

    const handleViewCourses = (e) => {
        e.preventDefault();
        navigate(`${e.target.value}`);
    }

    useEffect(() => {
        getSemestersByLecturer();
    }, []);

    console.log(semesters);

    return (
        <List listName={`Semestry`} columnNames={['Kod semestru']} data={semesters} error={error} userButtons={true} handleViev={handleViewCourses} id="semester" />
    )
}

export default LecurerSemesterPage