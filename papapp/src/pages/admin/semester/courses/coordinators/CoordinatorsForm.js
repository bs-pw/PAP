import React, { useEffect, useState } from 'react'
import { useClient } from '../../../../../components/ClientContext'
import Form from '../../../../../components/common/Form';
import { useNavigate, useParams } from 'react-router-dom';

const CoordinatorsForm = () => {
    const [coordinators, setCoordinators] = useState([]);
    const [selectedCoordinator, setSelectedCoordinator] = useState();
    const [message, setMessage] = useState("");
    const client = useClient();
    const { semesterId, courseId } = useParams();
    const navigate = useNavigate();

    const getAvCoordinators = async (e) => {
        const data = await client.getAvailableCourseCoordinators(semesterId, courseId);
        setCoordinators(data);
        //console.log(data)
    }

    const handleChange = (e) => {
        setSelectedCoordinator(e.target.value);
    }

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await client.addCoordinatorToCourse(semesterId, courseId, selectedCoordinator);
            navigate(`..`);
        } catch (error) {
            await setMessage(error.message || "Błąd podczas dodawania kursu");
        }
    }

    useEffect(() => {
        getAvCoordinators();
    }, [])

    const selectData = [{
        name: "user_id", label: "Koordynator", options: coordinators.map(coordinator => ({ value: coordinator.user_id, label: `${coordinator.name} ${coordinator.surname}` })), defaultValue: selectedCoordinator, onChange: handleChange
    }]

    return (
        <Form selectData={selectData} buttonName="Dodaj" formName={`Dodaj koordynatora przedmiotu ${courseId} w semestrze ${semesterId}`} onSubmit={handleSubmit} message={message} />
    )
}

export default CoordinatorsForm