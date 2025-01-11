import React, { useState, useEffect } from 'react'
import { useClient } from '../../../components/ClientContext'

const RegistrationPage = () => {
    const [formData, setFormData] = useState({
        name: "",
        surname: "",
        mail: "",
        password: "",
        user_type_id: "",
    });

    const [message, setMessage] = useState('');
    const [userTypes, setUserTypes] = useState([]);

    const client = useClient();

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleUserTypes = async () => {
        try {
            const data = await client.getUserTypes();
            console.log(data);
            setUserTypes(data);
        } catch (error) {
            console.log(error.message);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            await client.registerUser(formData);
            setMessage('Użytkownik został zarejestrowany');
        } catch (error) {
            setMessage('Błąd podczas rejestracji');
        }
    };

    useEffect(() => {
        handleUserTypes();
    }, []);

    return (
        <div className="container mt-5">
            <h2>Formularz Rejestracyjny</h2>
            <form onSubmit={handleSubmit}>
                <div className="mb-3">
                    <label htmlFor="name" className="form-label">Imię</label>
                    <input
                        type="text"
                        className="form-control"
                        id="name"
                        name="name"
                        value={formData.name}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className="mb-3">
                    <label htmlFor="surname" className="form-label">Nazwisko</label>
                    <input
                        type="text"
                        className="form-control"
                        id="surname"
                        name="surname"
                        value={formData.surname}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className="mb-3">
                    <label htmlFor="mail" className="form-label">Mail</label>
                    <input
                        type="mail"
                        className="form-control"
                        id="mail"
                        name="mail"
                        value={formData.mail}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className="mb-3">
                    <label htmlFor="password" className="form-label">Hasło</label>
                    <input
                        type="password"
                        className="form-control"
                        id="password"
                        name="password"
                        value={formData.password}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div className="mb-3">
                    <label htmlFor="text" className="form-label">Typ użytkownika</label>
                    <select className="form-select" name="user_type_id" value={formData.user_type_id} onChange={handleChange}>
                        {userTypes.map((userType) => (
                            <option key={userType.user_type_id} value={userType.user_type_id}>
                                {userType.type}
                            </option>
                        ))}
                    </select>
                </div>
                {message && <p>{message}</p>}
                <button type="submit" className="btn btn-primary">
                    <i className='bi bi-box-arrow-in-right'></i> Zarejestruj
                </button>
            </form>
        </div>
    );
}

export default RegistrationPage;