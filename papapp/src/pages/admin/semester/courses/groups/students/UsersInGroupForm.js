import React, { useEffect, useState } from 'react'
import { useClient } from '../../../../../../components/ClientContext'
import Form from '../../../../../../components/common/Form';
import { useNavigate, useParams } from 'react-router-dom';

const UsersInGroupForm = ({ type }) => {
    const [users, setUsers] = useState([]);
    const [selectedUser, setSelectedUser] = useState();
    const [title, setTitle] = useState();
    const [message, setMessage] = useState("");
    const client = useClient();
    const { semesterId, courseId, groupId } = useParams();
    const navigate = useNavigate();

    const getAvailableUsers = async (e) => {
        try {
            const data = await client.getAvailableStudentsToAddToGroup(semesterId, courseId, groupId, type);
            setUsers(data);
            console.log(users)
        } catch (error) {
            console.log(error);
            setMessage(error.message);
        }
    }

    const handleChange = (e) => {
        setSelectedUser(e.target.value);
    }

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await client.addUserInGroup(semesterId, courseId, groupId, selectedUser, type);
            navigate(`..`);
        } catch (error) {
            setMessage(error.message);
        }
    }

    useEffect(() => {
        getAvailableUsers();
        if (type == "students") {
            setTitle("studenta");
        } else {
            setTitle("prowadzącego");
        }
    })

    const selectData = [{
        name: "user_id", label: `Wybierz ${title}`, options: users.map(student => ({ value: student.user_id, label: `${student.name} ${student.surname}` })), defaultValue: selectedUser, onChange: handleChange
    }]

    return (
        <Form selectData={selectData} buttonName="Dodaj" formName={`Dodaj ${title} do grupy ${groupId}, przedmiot ${courseId} w semestrze ${semesterId}`} onSubmit={handleSubmit} message={message} />
    )
}

export default UsersInGroupForm