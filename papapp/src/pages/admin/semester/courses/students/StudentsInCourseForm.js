import React, { useEffect, useState } from 'react'
import { useClient } from '../../../../../components/ClientContext'
import Form from '../../../../../components/common/Form';
import { useNavigate, useParams } from 'react-router-dom';

const CoordinatorsForm = () => {
    const [students, setStudents] = useState([]);
    const [selectedStudent, setSelectedStudent] = useState();
    const [message, setMessage] = useState("");
    const client = useClient();
    const { semesterId, courseId } = useParams();
    const navigate = useNavigate();

    const getAvStudents = async (e) => {
        const data = await client.getAvailableCourseStudents(semesterId, courseId);
        setStudents(data);
        //console.log(data)
    }

    const handleChange = (e) => {
        setSelectedStudent(e.target.value);
    }

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await client.addStudentToCourse(semesterId, courseId, selectedStudent);
            navigate(`..`);
        } catch (error) {
            await setMessage(error.message || "Błąd podczas dodawania kursu");
        }
    }

    useEffect(() => {
        getAvStudents();
    }, [])

    const selectData = [{
        name: "user_id", label: "Student", options: students.map(student => ({ value: student.user_id, label: `${student.name} ${student.surname}` })), defaultValue: selectedStudent, onChange: handleChange
    }]

    return (
        <Form selectData={selectData} buttonName="Dodaj" formName={`Dodaj studenta do przedmiotu ${courseId} w semestrze ${semesterId}`} onSubmit={handleSubmit} message={message} />
    )
}

export default CoordinatorsForm