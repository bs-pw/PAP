import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'

const LoginPage = () => {
    const [mail, setMail] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();

        try {
            const url = new URL('http://localhost/api/auth/login');
            url.searchParams.append('mail', mail);
            url.searchParams.append('password', password);

            const response = await fetch(url, { method: 'GET' });
            if (response.ok) {
                navigate('/dashboard');
            } else {
                const errorData = await response.json();
                setErrorMessage(errorData.message || 'Błąd logowania!');
            }
        } catch (error) {
            setErrorMessage('Wystąpił błąd! Spróbuj ponownie!');
            console.error(error)
        }
    };

    return (
        <div className='d-flex align-items-center justify-content-center vh-100'>
            <div className='p-5 bg-light rounded'>
                <h1 className='text-center'>PAP App</h1>
                <form className='d-flex flex-column gap-3' onSubmit={handleLogin}>
                    <div className="form-group">
                        <label htmlFor="exampleInputEmail1">Email address</label>
                        <input
                            type="email"
                            className="form-control"
                            id="exampleInputEmail1"
                            aria-describedby="emailHelp"
                            placeholder="Enter email"
                            value={mail}
                            onChange={(e) => setMail(e.target.value)}
                            required
                        />

                    </div>
                    <div className="form-group">
                        <label htmlFor="exampleInputPassword1">Password</label>
                        <input
                            type="password"
                            className="form-control"
                            id="exampleInputPassword1"
                            placeholder="Password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </div>
                    {errorMessage && <p>{errorMessage}</p>}
                    <button type="submit" className="btn btn-primary">
                        <i className='bi bi-box-arrow-in-right'></i> Zaloguj
                    </button>
                </form>
            </div>
        </div >
    )
}

export default LoginPage