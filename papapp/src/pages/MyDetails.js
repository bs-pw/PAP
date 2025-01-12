import React, { useEffect, useState } from 'react';
import { useClient } from '../components/ClientContext';
import Form from '../components/common/Form';

const MyDetails = () => {

    const [mail, setMail] = useState();
    const [password, setPassword] = useState();
    const [message, setMessage] = useState();
    const client = useClient();

    const handleChangeMail = (e) => {
        setMail(e.target.value);
    }

    const handleChangePassword = (e) => {
        setPassword(e.target.value);
    }

    const changeMail = async (e) => {
        e.preventDefault()
        // console.log(e)
        try {
            client.updateUser(client.userId, { "mail": mail })
        } catch (error) {

        }
    }

    const changePassword = async (e) => {
        e.preventDefault()
        try {
            client.updateUserPassword(client.userId, password)
        } catch (error) {
            setMessage(error.message || "Zmieniono hasło!")
        }
    }


    useEffect(() => {
        client.checkAuthStatus();
        setMail(client.mail)
        setPassword(client.password)
    }, []);

    const mailForm = [{ name: 'mail', label: 'E-mail', type: 'email', value: mail, required: true, onChange: handleChangeMail }];
    const passwordForm = [{ name: 'password', label: 'Hasło', type: 'password', value: password, required: true, onChange: handleChangePassword }];

    return (
        <div>
            <h1>Moje dane</h1>
            <p>Imię: {client.name}</p>
            <p>Nazwisko: {client.surname}</p>
            {message && <p style={{ color: 'green' }}>{message}</p>}
            <Form inputData={mailForm} buttonName="Zmień" formName={"Zmień e-mail"} onSubmit={changeMail} />
            <Form inputData={passwordForm} buttonName="Zmień" formName={"Zmień hasło"} onSubmit={changePassword} />
        </div>
    )
}

export default MyDetails