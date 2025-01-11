import React from 'react'
import { Link } from 'react-router-dom'
import UsersList from '../../../components/common/UsersList'

const UsersPage = () => {
    return (
        <>
            <Link to="/admin/users/register" className='nav-link text-primary' style={{ fontSize: "1.2em" }}><i class="bi bi-plus-lg"></i> Nowy</Link >
            <UsersList adminButtons={true} />
        </>
    )
}

export default UsersPage