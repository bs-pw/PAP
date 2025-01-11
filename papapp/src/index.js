import React, { useState } from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import { ClientProvider } from './components/ClientContext';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <ClientProvider>
      <App />
    </ClientProvider>
  </React.StrictMode>
);

