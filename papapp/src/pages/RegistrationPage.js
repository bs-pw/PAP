import React, { useState } from 'react'

const RegistrationPage = () => {
    const [formData, setFormData] = useState({
        name: "",
        surname: "",
        mail: "",
        password: "",
        status: "",
    });

    const [message, setMessage] = useState('');

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        console.log("Dane formularza:", formData);
        try {
            const url = new URL('http://localhost/api/user');

            const response = await fetch(url, {
                method: 'POST', credentials: 'include',
                body: JSON.stringify({
                    name: formData["name"],
                    surname: formData["surname"],
                    mail: formData["mail"],
                    password: formData["password"],
                    status: formData["status"]
                }),
                headers: { 'content-type': 'application/json' },
            });
            if (response.ok) {
                setMessage('Zarejestrowano!');
            } else {
                const errorData = await response.json();
                setMessage(errorData.message || 'Błąd!');
            }
        } catch (error) {
            setMessage('Wystąpił błąd! Spróbuj ponownie!');
            console.error(error)
        }
    };

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
                    <label htmlFor="text" className="form-label">Status</label>
                    <input
                        type="text"
                        className="form-control"
                        id="status"
                        name="status"
                        value={formData.status}
                        onChange={handleChange}
                        required
                    />
                </div>
                {message && <p>{message}</p>}
                <button type="submit" className="btn btn-primary">
                    <i className='bi bi-box-arrow-in-right'></i> Zarejestruj
                </button>
            </form>
        </div>
    );
}

export default RegistrationPage