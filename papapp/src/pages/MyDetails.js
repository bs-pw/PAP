import React, { useEffect, useState } from 'react';
import { useClient } from '../components/ClientContext';
import Form from '../components/common/Form';

const MyDetails = () => {

    const [mail, setMail] = useState();
    const [password, setPassword] = useState();
    const [password2, setPassword2] = useState();
    const [message, setMessage] = useState(" ");
    const client = useClient();

    const handleChangeMail = (e) => {
        setMail(e.target.value);
    }

    const handleChangePassword = (e) => {
        setPassword(e.target.value);
    }

    const changeMail = async (e) => {
        e.preventDefault()
        // //console.log(e)
        try {
            await client.updateUser(client.userId, { mail: mail })
            setMessage("Zmieniono mail!")
        } catch (error) {
            setMessage(error.message || "Błąd podczas zmiany maila!")
        }
    }

    const changePassword = async (e) => {
        e.preventDefault()
        if (password != password2) return;
        try {
            await client.updateUser(client.userId, { password: password })
            setMessage("Zmieniono hasło!")
        } catch (error) {
            setMessage(error.message || "Błąd podczas zmiany hasła!")
        }
    }

    const handleChangePassword2 = (e) => {
        setPassword2(e.target.value);
        password != password2 ? setMessage('Hasła nie są identyczne') : setMessage(' ');
    }


    useEffect(() => {
        client.checkAuthStatus();
        setMail(client.mail)
        // setPassword(client.password)
    }, []);

    const mailForm = [{ name: 'mail', label: 'E-mail', type: 'email', value: mail, required: true, onChange: handleChangeMail }];
    const passwordForm = [
        { name: 'password', label: 'Hasło', type: 'password', value: password, required: true, onChange: handleChangePassword },
        { name: 'password2', label: 'Powtórz hasło', type: 'password', value: password2, required: true, onChange: handleChangePassword2 },
    ];

    return (
        <div>
            <h1>Moje dane</h1>
            <p>Imię: {client.name}</p>
            <p>Nazwisko: {client.surname}</p>
            {message && <p>{message}</p>}
            <Form inputData={mailForm} buttonName="Zmień" formName={"Zmień e-mail"} onSubmit={changeMail} />
            <Form inputData={passwordForm} buttonName="Zmień" formName={"Zmień hasło"} onSubmit={changePassword} />
        </div>
    )
}

export default MyDetails