import React, { useEffect } from 'react'
import { useClient } from '../../../../components/ClientContext'
import { useParams, Outlet } from 'react-router-dom';

const CheckIsLocked = () => {
    const client = useClient();
    const { semesterId, courseId } = useParams();
    const isClosed = async () => {
        await client.isClosed(semesterId, courseId);
    }
    useEffect(() => {
        isClosed();
    }, [])
    return (
        <Outlet />
    )
}

export default CheckIsLocked