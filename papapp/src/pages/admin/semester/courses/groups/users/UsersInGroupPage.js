import React, { useState, useEffect } from 'react';
import { useClient } from '../../../../../../components/ClientContext';
import { Link, useNavigate, useParams } from 'react-router-dom';
import List from '../../../../../../components/common/List';

const UsersInGroupPage = ({ type }) => {
    const [users, setUsers] = useState([]);
    const [error, setError] = useState('');
    const [title, setTitle] = useState('');
    const client = useClient();
    const navigate = useNavigate();
    const { semesterId, courseId, groupId } = useParams();

    const getUsersInGroup = async () => {
        try {
            const data = await client.getUsersInGroup(semesterId, courseId, groupId, type);
            const result = data.map(({ user_id, name, surname }) => ({ user_id, name, surname }))
            setUsers(result);
            //console.log(result)
        } catch (error) {
            setError(error.message || 'Błąd połączenia');
        }
    }

    const handleDeleteUser = async (e) => {
        //console.log(e.target.value)
        try {
            await client.deleteUserInGroup(semesterId, courseId, groupId, e.target.value);
            getUsersInGroup();
        } catch (error) {
            setError(error.message);
        }
    }

    useEffect(() => {
        getUsersInGroup(semesterId, courseId, groupId);
        if (type == "students") {
            setTitle("Studenci");
        } else {
            setTitle("Prowadzący");
        }
    }, []);

    return (
        <>
            <Link to={`add`} className='nav-link text-primary' style={{ fontSize: "1.2em" }}><i className="bi bi-plus-lg"></i> Nowy</Link >
            <List listName={`${title} w grupie ${groupId}, przedmiot ${courseId} w semestrze ${semesterId}`} data={users} columnNames={[`Id`, `Imię`, `Nazwisko`]} adminButtons={[true, false]} handleDelete={handleDeleteUser} id="user_id" error={error} />
        </>
    )
}

export default UsersInGroupPage