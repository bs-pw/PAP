import React, { useState, useEffect } from 'react';
import { useClient } from '../../../../../../components/ClientContext';
import { Link, useNavigate, useParams } from 'react-router-dom';
import List from '../../../../../../components/common/List';

const StudentsInGroupPage = () => {
    const [students, setStudents] = useState([]);
    const [error, setError] = useState('');
    const client = useClient();
    const navigate = useNavigate();
    const { semesterId, courseId, groupId } = useParams();

    const getStudentsInGroup = async () => {
        try {
            const data = await client.getStudentsInGroup(semesterId, courseId, groupId);
            setStudents(data);
            console.log(data)
        } catch (error) {
            setError(error.message || 'Błąd połączenia');
        }
    }

    const handleDeleteStudent = async (groupNumber) => {
        try {
            await client.deleteGroup(semesterId, courseId, groupNumber);
            getStudentsInGroup();
        } catch (error) {
            setError(error.message);
        }
    }

    useEffect(() => {
        getStudentsInGroup(semesterId, courseId, groupId);
    }, []);

    return (
        <>
            <Link to={`add`} className='nav-link text-primary' style={{ fontSize: "1.2em" }}><i className="bi bi-plus-lg"></i> Nowy</Link >
            <List listName={`Studenci w grupie ${groupId}, przedmiot ${courseId} w semestrze ${semesterId}`} data={students} columnNames={[`Imię`, `Nazwisko`]} adminButtons={[true, false]} handleDelete={handleDeleteStudent} />
        </>
    )
}

export default StudentsInGroupPage