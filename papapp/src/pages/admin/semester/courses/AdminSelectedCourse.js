import React from 'react'
import { Link } from 'react-router-dom';

const AdminSelectedCourse = () => {
    return (
        <>
            <Link to={`coordinators`} className="nav-link">Koordynatorzy</Link>
            <Link to={`groups`} className="nav-link">Grupy</Link>
            <Link to={`students`} className="nav-link">Studenci</Link>
            <Link to={`grade-categories`} className="nav-link">Punktacja przedmiotu</Link>
        </>
    )
}

export default AdminSelectedCourse