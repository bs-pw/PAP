import React, { useState, useEffect } from 'react';
import { useClient } from '../../components/ClientContext';
import { Link, useNavigate } from 'react-router-dom';
import List from '../../components/common/List';

const StudentSemesterPage = () => {
    const [semesters, setSemesters] = useState([]);
    const [error, setError] = useState('');
    const client = useClient();
    const navigate = useNavigate();

    const getSemesters = async () => {
        try {
            const data = await client.getSemesterByStudent(client.userId);
            setSemesters(data);
        } catch (error) {
            setError('Błąd podczas ładowania danych: ' + error.message);
        }
    };

    const handleView = (e) => {
        e.preventDefault();
        navigate(`${e.target.value}`);
    }

    useEffect(() => {
        getSemesters();
    }, []);

    //console.log(semesters);

    return (
        <List listName='Lista semestrów' columnNames={['Kod']} data={semesters} error={error} userButtons={true} handleView={handleView} id="semester" />
    )
}

export default StudentSemesterPage