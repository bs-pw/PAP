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
            const data = await client.getCourseCoordinators(semesterId, courseId)
            setCoordinators(data);
        } catch (error) {
            setError(error.message);
        }
    }

    const handleDelete = async (id) => { }

    useEffect(() => {
        getCourseCoordinators();
    }, [])

    return (
        <>
            <Link to={`add`} className='nav-link text-primary' style={{ fontSize: "1.2em" }}><i className="bi bi-plus-lg"></i> Nowy</Link >
            <List listName={`Koordynatorzy przedmiotu ${courseId} w semestrze ${semesterId}`} columnNames={['Kod przedmotu']} data={coordinators} error={error} adminButtons={[true, false]} handleDelete={handleDelete} id="user_id" />
        </>
    )
}

export default CoordinatorsPage