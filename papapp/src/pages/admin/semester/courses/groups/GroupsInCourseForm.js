import React, { useState } from 'react'
import { useClient } from '../../../../../components/ClientContext'
import Form from '../../../../../components/common/Form';
import { useNavigate, useParams } from 'react-router-dom';

const GroupsInCourseForm = () => {
    const [formGroup, setFormGroup] = useState();
    const [message, setMessage] = useState("");
    const client = useClient();
    const { semesterId, courseId } = useParams();
    const navigate = useNavigate();

    const handleChange = (e) => {
        setFormGroup(e.target.value);
    }

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await client.addGroup(semesterId, courseId, formGroup);
            navigate(`..`);
        } catch (error) {
            await setMessage(error.message || "Błąd podczas dodawania kursu");
        }
    }

    const inputData = [{
        label: "Numer grupy", type: "number", name: "group_number", value: formGroup, onChange: handleChange, required: true
    }]


    return (
        <Form inputData={inputData} buttonName="Dodaj" formName={`Dodaj grupę do przemiotu ${courseId} w semestrze ${semesterId}`} onSubmit={handleSubmit} message={message} />
    )
}

export default GroupsInCourseForm