import React, { useState, useEffect } from 'react'
import { useClient } from '../../../components/ClientContext'
import Form from '../../../components/common/Form';
import { useParams } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';

const UserForm = () => {
    const [formData, setFormData] = useState({
        name: "",
        surname: "",
        mail: "",
        password: "",
        password2: "",
        user_type_id: "0",
    });
    const [password2, setPassword2] = useState('');
    const [message, setMessage] = useState('');
    const [userTypes, setUserTypes] = useState([]);
    const [formName, setFormName] = useState('Rejestracja');
    const [buttonName, setButtonName] = useState('Zarejestruj');

    const client = useClient();
    const { id } = useParams();
    const navigate = useNavigate();

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleChangePassword2 = (e) => {
        setPassword2(e.target.value);
        if (formData.password != password2) {
            setMessage('Hasła nie są identyczne');
        } else {
            setMessage(' ');
        }
    }


    const handleUserTypes = async () => {
        try {
            const data = await client.getUserTypes();
            //console.log(data);
            setUserTypes(data);
        } catch (error) {
            //console.log(error.message);
        }
    };

    const handleGetUser = async (userId) => {
        try {
            const data = await client.getUser(userId);
            //console.log(data);
            setFormData(data)
            // formData.password2 = '';
            // console.log(formData)
        } catch (error) {
            navigate('/admin/users');
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        // console.log(formData)
        // console.log(formData.password)
        // console.log(password2);
        if (formData.password != password2) return;
        if (id) {
            try {
                if (formData.password == '') {
                    formData.password = null;
                }
                await client.updateUser(id, formData);
                setMessage('Użytkownik został zaktualizowany!');
                navigate("..")
            } catch (error) {
                setMessage('Błąd podczas aktualizacji');
            }
        } else {
            try {
                await client.registerUser(formData);
                setMessage('Użytkownik został zarejestrowany');
                navigate("..")
            } catch (error) {
                setMessage('Błąd podczas rejestracji');
            }
        }
    };

    useEffect(() => {
        handleUserTypes();
        if (id) {
            setFormName('Edycja użytkownika');
            setButtonName('Zapisz');
            handleGetUser(id);
        }
    }, []);

    //console.log(id);

    const inputData = [
        { name: "name", type: "text", label: "Imię", value: formData.name, onChange: handleChange, required: true },
        { name: "surname", type: "text", label: "Nazwisko", value: formData.surname, onChange: handleChange, required: true },
        { name: "mail", type: "email", label: "Email", value: formData.mail, onChange: handleChange, required: true },
        { name: "password", type: "password", label: "Hasło", value: formData.password, onChange: handleChange, required: true },
        { name: "password2", type: "password", label: "Powtórz hasło", value: password2, onChange: handleChangePassword2, required: true },
    ];

    if (id) { inputData[3].label += "(zostaw puste, jeśli nie zmieniasz hasła)"; inputData[3].required = false; inputData[4].required = false }

    const selectData = [
        { name: "user_type_id", label: "Typ użytkownika", options: userTypes.map(userType => ({ value: userType.user_type_id, label: userType.type })), defaultValue: formData.user_type_id, onChange: handleChange },
    ];

    return (
        <Form formName={formName} onSubmit={handleSubmit} inputData={inputData} selectData={selectData} buttonName={buttonName} message={message} />
    );
}

export default UserForm;