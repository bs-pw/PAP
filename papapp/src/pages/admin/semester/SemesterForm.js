import React, { useEffect, useState } from 'react';
import { useClient } from '../../../components/ClientContext';
import { useNavigate, useParams } from 'react-router-dom';
import Form from '../../../components/common/Form';

const SemesterForm = () => {
    const [formData, setFormData] = useState({
        semester_code: "",
        start_date: "",
        end_date: "",
    });

    const [formName, setFormName] = useState('Utwórz semestr');
    const [buttonName, setButtonName] = useState('Utwórz');

    const [message, setMessage] = useState('');
    const client = useClient();
    const { id } = useParams();
    const navigate = useNavigate();

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (id) {
            try {
                await client.updateSemester(id, formData);
                setMessage('Semestr został zaktualizowany');
            } catch (error) {
                setMessage(error.message || "Błąd podczas aktualizacji semestru");
            }
        } else {
            try {
                await client.createSemester(formData);
                // setMessage('Semestr został utworzony');
                navigate('/admin/semesters');
            } catch (error) {
                setMessage('Błąd podczas tworzenia semestru');
            }
        }
    };

    const handleGetSemester = async (semesterId) => {
        try {
            const data = await client.getSemester(semesterId);
            //console.log(data);
            setFormData(data);
        } catch (error) {
            navigate('/admin/semesters');
        }
    };

    useEffect(() => {
        if (id) {
            setFormName('Edytuj semestr');
            setButtonName('Zapisz');
            handleGetSemester(id);
            //console.log(formData);
        }
    }, []);

    const inputData = [
        { label: 'Kod', name: 'semester_code', type: 'text', value: formData.semester_code, onChange: handleChange, required: true },
        { label: 'Data początku', name: 'start_date', type: 'date', value: formData.start_date, onChange: handleChange, required: true },
        { label: 'Data końca', name: 'end_date', type: 'date', value: formData.end_date, onChange: handleChange, required: true },
    ];

    return (
        <Form formName={formName} inputData={inputData} buttonName={buttonName} onSubmit={handleSubmit} message={message} />
    );
}

export default SemesterForm