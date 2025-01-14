import React, { useState, useEffect } from 'react';
import { useClient } from '../../../../../components/ClientContext';
import { Link, useNavigate, useParams } from 'react-router-dom';
import List from '../../../../../components/common/List';

const GroupsInCoursePage = () => {
    const [groups, setGroups] = useState([]);
    const [error, setError] = useState('');
    const client = useClient();
    const navigate = useNavigate();
    const { semesterId, courseId } = useParams();

    const getGroupsInSemesterInCourse = async () => {
        try {
            const data = await client.getGroupsInSemesterInCourse(semesterId, courseId);
            const result = data.map(({ group_number }) => ({ group_number }))
            setGroups(result);
            console.log(data)
        } catch (error) {
            setError(error.message);
        }
    }

    const handleDeleteGroup = async (groupNumber) => {
        try {
            await client.deleteGroup(semesterId, courseId, groupNumber);
            getGroupsInSemesterInCourse();
        } catch (error) {
            setError(error.message);
        }
    }

    const handleViewStudents = (e) => {
        e.preventDefault();
        navigate(`${e.target.value}/students`);
    }

    useEffect(() => {
        getGroupsInSemesterInCourse();
    }, [])

    return (
        <>
            <Link to={`add`} className='nav-link text-primary' style={{ fontSize: "1.2em" }}><i className="bi bi-plus-lg"></i> Nowy</Link >
            <List listName={`Grupy przedmiotu ${courseId} w semestrze ${semesterId}`} data={groups} columnNames={[`Kurs`]} adminButtons={[true, false]} userButtons={true} handleDelete={handleDeleteGroup} handleViev={handleViewStudents} id="group_number" />
        </>
    )
}

export default GroupsInCoursePage