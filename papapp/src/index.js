import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import { ClientProvider } from './components/ClientContext';
import { RouterProvider } from 'react-router-dom';
import { router } from './routes';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <ClientProvider>
      <RouterProvider router={router} />
    </ClientProvider>
  </React.StrictMode>
);

