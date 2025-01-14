import React, { useState, useEffect } from 'react';
import { useClient } from '../../../../../components/ClientContext';
import { Link, useNavigate, useParams } from 'react-router-dom';
import List from '../../../../../components/common/List';

const CoordinatorsPage = () => {
    const [coordinators, setCoordinators] = useState([]);
    const [error, setError] = useState('');
    const client = useClient();
    const navigate = useNavigate();
    const { semesterId, courseId } = useParams('');

    const getCourseCoordinators = async () => {
        try {
            const result = await client.getCourseCoordinators(semesterId, courseId)
            const data = result.map(({ user_id, name, surname }) => ({ user_id, name, surname }))
            setCoordinators(data);
        } catch (error) {
            setError(error.message);
        }
    }

    const handleDelete = async (e) => {
        e.preventDefault();
        try {
            await client.deleteCoordinatorFromCourse(semesterId, courseId, e.target.value);
            getCourseCoordinators();
        } catch (error) {
            setError(error.message);
        }
    }

    useEffect(() => {
        getCourseCoordinators();
    }, [])

    return (
        <>
            {client.userTypeId == 0 && <Link to={`add`} className='nav-link text-primary' style={{ fontSize: "1.2em" }}><i className="bi bi-plus-lg"></i> Nowy</Link >}
            <List listName={`Koordynatorzy przedmiotu ${courseId} w semestrze ${semesterId}`} columnNames={['Id', 'Imię', 'Nazwisko']} data={coordinators} error={error} adminButtons={client.userTypeId == 0 && [true, false]} handleDelete={handleDelete} id="user_id" />
        </>
    )
}

export default CoordinatorsPage