// src/index.js
import React from 'react';
import ReactDOM from 'react-dom/client'; // Update this import
import App from './App';
import { Elements } from '@stripe/react-stripe-js';
import { loadStripe } from '@stripe/stripe-js';
import { ThemeProvider } from '@mui/material/styles';
import theme from './theme';

const stripePromise = loadStripe(process.env.REACT_APP_STRIPE_PUBLISHABLE_KEY);

// Create a root.
const root = ReactDOM.createRoot(document.getElementById('root'));

// Initial render
root.render(
    <ThemeProvider theme={theme}>
        <Elements stripe={stripePromise}>
            <App />
        </Elements>
    </ThemeProvider>
);
