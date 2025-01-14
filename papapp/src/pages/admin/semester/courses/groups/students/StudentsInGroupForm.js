import React, { useEffect, useState } from 'react'
import { useClient } from '../../../../../../components/ClientContext'
import Form from '../../../../../../components/common/Form';
import { useNavigate, useParams } from 'react-router-dom';

const StudentsInGroupForm = () => {
    const [students, setStudents] = useState([]);
    const [selectedStudent, setSelectedStudent] = useState();
    const [message, setMessage] = useState("");
    const client = useClient();
    const { semesterId, courseId, groupId } = useParams();
    const navigate = useNavigate();

    const getStudents = async (e) => {
        try {
            const data = await client.getAvailableStudentsToAddToGroup(semesterId, courseId, groupId, "student");
            setStudents(data);
            console.log(students)
        } catch (error) {
            console.log(error);
            setMessage(error.message);
        }
    }

    const handleChange = (e) => {
        setSelectedStudent(e.target.value);
    }

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await client.addStudentInGroup
        } catch (error) {
            setMessage(error.message);
        }
    }

    useEffect(() => {
        getStudents();
    })

    const selectData = [{
        name: "user_id", label: "Wybierz studenta", options: students.map(student => ({ value: student.user_id, label: `${student.name} ${student.surname}` })), defaultValue: selectedStudent, onChange: handleChange
    }]

    return (
        <Form selectData={selectData} buttonName="Dodaj" formName={`Dodaj studenta do grupy ${groupId}, przedmiot ${courseId} w semestrze ${semesterId}`} onSubmit={handleSubmit} message={message} />
    )
}

export default StudentsInGroupForm