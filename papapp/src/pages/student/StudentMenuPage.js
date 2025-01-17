import React from 'react'
import { Link } from 'react-router-dom'

const StudentMenuPage = () => {
    return (
        <>
            <Link to={`final-grades`} className='nav-link'>Oceny końcowe</Link>
            <Link to={`grades`} className='nav-link'>Oceny</Link>
        </>
    )
}

export default StudentMenuPage