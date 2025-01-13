import { Box } from '@mui/material'
import React from 'react'
import Sidebar from '../common/Sidebar'
import Header from '../common/Header'
import { Link, Outlet } from 'react-router-dom'

const MainLayout = () => {
    return (
        <Box sx={{ height: "100vh", display: "flex", flexDirection: "column" }}>
            <Header />
            <Box sx={{ display: "flex", flexGrow: 1 }}>
                <Sidebar />
                <Box component="main" sx={{
                    flexGrow: 1,
                    p: 3,
                    width: "100%",
                    overflow: "auto"
                }}>
                    <Outlet />
                </Box>
            </Box>
        </Box>
    );
};


export default MainLayout