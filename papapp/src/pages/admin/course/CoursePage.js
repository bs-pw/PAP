import React from 'react'
import { Link } from 'react-router-dom'
import CoursesList from '../../../components/common/CoursesList'

const CoursePage = () => {
    return (
        <>
            <Link to="/admin/courses/create" className='nav-link text-primary' style={{ fontSize: "1.2em" }}><i class="bi bi-plus-lg"></i> Nowy</Link >
            <CoursesList adminButtons={true} />
        </>
    )
}

export default CoursePage