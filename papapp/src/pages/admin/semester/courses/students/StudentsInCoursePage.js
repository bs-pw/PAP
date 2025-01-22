import React, { useState, useEffect } from 'react';
import { useClient } from '../../../../../components/ClientContext';
import { Link, useNavigate, useParams } from 'react-router-dom';
import List from '../../../../../components/common/List';

const CoordinatorsPage = () => {
    const [students, setStudents] = useState([]);
    const [error, setError] = useState('');
    const client = useClient();
    const navigate = useNavigate();
    const { semesterId, courseId } = useParams('');

    const getCourseStudents = async () => {
        try {
            const result = await client.getCourseStudents(semesterId, courseId)
            const data = result.map(({ user_id, name, surname }) => ({ user_id, name, surname }))
            //console.log(result)
            setStudents(data);
        } catch (error) {
            setError(error.message);
        }
    }

    const handleDelete = async (e) => {
        e.preventDefault();
        try {
            await client.deleteStudentFromCourse(semesterId, courseId, e.target.value);
            getCourseStudents();
        } catch (error) {
            setError(error.message);
        }
    }

    useEffect(() => {
        getCourseStudents();
    }, [])

    return (
        <>
            {!client.isLocked && <Link to={`add`} className='nav-link text-primary' style={{ fontSize: "1.2em" }}><i className="bi bi-plus-lg"></i> Nowy</Link >}
            <List listName={`Studenci przedmiotu ${courseId} w semestrze ${semesterId}`} columnNames={['Id', 'Imię', 'Nazwisko']} data={students} error={error} adminButtons={!client.isLocked && [true, false]} handleDelete={handleDelete} id="user_id" />
        </>
    )
}

export default CoordinatorsPage