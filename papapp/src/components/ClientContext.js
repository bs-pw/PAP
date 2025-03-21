import React, { createContext, useContext, useState } from 'react';
import Client from './Client';
const ClientContext = createContext();

export const ClientProvider = ({ children }) => {
    const [client] = useState(new Client("https://usos.pro/api"));

    return (
        <ClientContext.Provider value={client}>
            {children}
        </ClientContext.Provider>
    );
};

export const useClient = () => {
    return useContext(ClientContext);
};