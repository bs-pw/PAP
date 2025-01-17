import React, { useState, useEffect } from 'react';
import { useClient } from '../../../../../components/ClientContext';
import Form from '../../../../../components/common/Form';
import { useNavigate, useParams } from 'react-router-dom';

const GradeCategoriesForm = () => {
    const [formData, setFormData] = useState({
        description: "",
        max_grade: "",
    });

    const [message, setMessage] = useState('');
    const [formName, setFormName] = useState('Dodaj kategorię punktową');
    const [buttonName, setButtonName] = useState('Dodaj');

    const client = useClient();
    const { semesterId, courseId, gradeCategoryId } = useParams();
    const navigate = useNavigate();

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (gradeCategoryId) {
            try {
                await client.updateGradeCategory(semesterId, courseId, gradeCategoryId, formData);
                navigate("..")
            } catch (error) {
                setMessage('Błąd podczas aktualizacji');
            }
        } else {
            try {
                formData.semester = semesterId;
                formData.course_code = courseId;
                await client.insertGradeCategory(formData);
                navigate("..")
            } catch (error) {
                setMessage('Błąd podczas dodawania');
            }
        }
    };

    const getGradeCategory = async (gradeCategoryId) => {
        try {
            const data = await client.getGradeCategory(semesterId, courseId, gradeCategoryId);
            //console.log(data);
            setFormData(data)
            //console.log(formData)
        } catch (error) {
            navigate('..');
        }
    };

    useEffect(() => {
        if (gradeCategoryId) {
            setFormName('Edycja kategorii punktowej');
            setButtonName('Zapisz');
            getGradeCategory(gradeCategoryId);
        }
    }, []);

    const inputData = [
        { name: "description", type: "text", label: `Opis`, value: formData.description, onChange: handleChange, required: true },
        { name: "max_grade", type: "number", label: `Max punktów w kategorii`, value: formData.max_grade, onChange: handleChange, required: true },
    ];

    return (
        <Form formName={formName} onSubmit={handleSubmit} inputData={inputData} buttonName={buttonName} message={message} />
    )
}

export default GradeCategoriesForm