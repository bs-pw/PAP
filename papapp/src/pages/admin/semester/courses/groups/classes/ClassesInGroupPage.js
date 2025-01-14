import React, { useState, useEffect } from 'react';
import { useClient } from '../../../../../../components/ClientContext';
import { Link, useNavigate, useParams } from 'react-router-dom';
import List from '../../../../../../components/common/List';
import TimeConverter from '../../../../../../components/common/TimeConverter';

const ClassesInGroupPage = () => {
    const [classes, setClasses] = useState([]);
    const [error, setError] = useState('');
    const client = useClient();
    const navigate = useNavigate();
    const { semesterId, courseId, groupId } = useParams('');

    const getGroupClasses = async () => {
        try {
            const response = await client.getClassesForGroup(semesterId, courseId, groupId);
            const data = response.map(({ class_id_for_group, type, day, hour, length, where }) => ({ class_id_for_group, type, day, hour: <TimeConverter minutes={(hour)} />, length: <TimeConverter minutes={(hour + length)} />, where }))

            setClasses(data);
        } catch (error) {
            setError(error.message);
        }
    }

    const handleDelete = async (e) => {
        e.preventDefault();
        try {
            await client.cos2(semesterId, courseId, groupId, e.target.value);
            getGroupClasses();
        } catch (error) {
            setError(error.message);
        }
    }

    const handleEdit = (e) => {
        e.preventDefault();
        navigate(`${e.target.value}/edit`);
    }

    useEffect(() => {
        getGroupClasses();
    }, [])

    return (
        <>
            <Link to={`add`} className='nav-link text-primary' style={{ fontSize: "1.2em" }}><i className="bi bi-plus-lg"></i> Nowy</Link >
            <List listName={`Zajęcia dla grupy ${groupId} z ${courseId} w semestrze ${semesterId}`} columnNames={['Id', 'Typ', 'Dzień tygodnia', 'Godzina', 'Długość', 'Sala']} data={classes} error={error} adminButtons={true} handleDelete={handleDelete} handleEdit={handleEdit} id="class_id_for_group" />
        </>
    )
}

export default ClassesInGroupPage