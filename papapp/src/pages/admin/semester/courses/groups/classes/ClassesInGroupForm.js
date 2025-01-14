import React, { useState, useEffect } from 'react'
import { useClient } from '../../../../../../components/ClientContext'
import Form from '../../../../../../components/common/Form';
import { useNavigate, useParams } from 'react-router-dom';
import { DayConverter } from '../../../../../../components/common/TimeConverter';

const ClassesInGroupForm = () => {
    const [formData, setFormData] = useState({
        class_id_for_group: "",
        class_type_id: "",
        day: "",
        hour: "",
        length: "",
        where: ""
    });

    const [message, setMessage] = useState('');
    const [classTypes, setClassTypes] = useState([]);
    const [formName, setFormName] = useState('Dodaj zajęcia');
    const [buttonName, setButtonName] = useState('Dodaj');
    const days = [0, 1, 2, 3, 4, 5, 6];

    const client = useClient();
    const { semesterId, courseId, groupId, classId } = useParams();
    const navigate = useNavigate();

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleClassTypes = async () => {
        try {
            const data = await client.getClassTypes();
            console.log(data);
            setClassTypes(data);
        } catch (error) {
            console.log(error.message);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (classId) {
            try {
                await client.updateClass(semesterId, courseId, groupId, classId, formData);
                // setMessage('Użytkownik został zaktualizowany!');
                navigate("..")
            } catch (error) {
                setMessage('Błąd podczas aktualizacji');
            }
        } else {
            try {
                formData.semester = semesterId;
                formData.course_code = courseId;
                formData.group_number = groupId;
                await client.insertClassInGroup(formData);
                // setMessage('Użytkownik został zarejestrowany');
                navigate("..")
            } catch (error) {
                setMessage('Błąd podczas dodawanie');
            }
        }
    };

    const getClass = async (classId) => {
        try {
            const data = await client.getClass(semesterId, courseId, groupId, classId);
            console.log(data);
            setFormData(data)
            console.log(formData)
        } catch (error) {
            // navigate('..');
            setMessage('Błąd podczas pobierania danych');
        }
    };

    useEffect(() => {
        handleClassTypes();
        if (classId) {
            setFormName('Edycja zajęcia');
            setButtonName('Zapisz');
            getClass(classId);
        }
    }, []);

    const inputData = [
        { name: "hour", type: "number", label: "Godzina rozpoczęcia(w minutach)", value: formData.hour, onChange: handleChange, required: true },
        { name: "length", type: "number", label: "Czas trwania(w minutach)", value: formData.length, onChange: handleChange, required: true },
        { name: "where", type: "text", label: "Sala", value: formData.where, onChange: handleChange, required: true },
    ];

    const selectData = [
        { name: "class_type_id", label: "Typ zajęć:", options: classTypes.map(classType => ({ value: classType.class_type_id, label: classType.type })), defaultValue: formData.class_type_id, onChange: handleChange },
        {
            name: "day", label: "Dzień", options: days.map(day => ({ value: day, label: <DayConverter dayCode={day} /> })), defaultValue: formData.day, onChange: handleChange
        }
    ];

    return (
        <Form formName={formName} onSubmit={handleSubmit} inputData={inputData} selectData={selectData} buttonName={buttonName} message={message} />
    )
}

export default ClassesInGroupForm