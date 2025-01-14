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
            const result = data.map(({ user_id, name, surname }) => ({ user_id, name, surname }))
            setStudents(result);
            console.log(result)
        } catch (error) {
            setError(error.message || 'Błąd połączenia');
        }
    }

    const handleDeleteStudent = async (e) => {
        console.log(e.target.value)
        try {
            await client.deleteUserInGroup(semesterId, courseId, groupId, e.target.value);
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
            <List listName={`Studenci w grupie ${groupId}, przedmiot ${courseId} w semestrze ${semesterId}`} data={students} columnNames={[`Id`, `Imię`, `Nazwisko`]} adminButtons={[true, false]} handleDelete={handleDeleteStudent} id="user_id" />
        </>
    )
}

export default StudentsInGroupPage