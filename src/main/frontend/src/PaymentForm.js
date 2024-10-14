// src/PaymentForm.js

import React, { useState } from 'react';
import {
    CardElement,
    useStripe,
    useElements,
} from '@stripe/react-stripe-js';
import {
    Container,
    Typography,
    Box,
    Button,
    TextField,
    Alert,
    CircularProgress,
    Card,
    CardContent,
    CardHeader,
    Stack,
    Grow,
} from '@mui/material';
import SendIcon from '@mui/icons-material/Send';
import { useNavigate } from 'react-router-dom';

const PaymentForm = () => {
    const stripe = useStripe();
    const elements = useElements();
    const navigate = useNavigate();

    // State variables
    const [customerName, setCustomerName] = useState('');
    const [email, setEmail] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [isProcessing, setIsProcessing] = useState(false);

    const CARD_ELEMENT_OPTIONS = {
        style: {
            base: {
                color: '#424770',
                letterSpacing: '0.025em',
                fontFamily: 'Roboto, Helvetica, Arial, sans-serif',
                fontSize: '16px',
                '::placeholder': {
                    color: '#aab7c4',
                },
            },
            invalid: {
                color: '#c23d4b',
            },
        },
        hidePostalCode: true,
    };

    const handleCardChange = (event) => {
        if (event.error) {
            setErrorMessage(event.error.message);
        } else {
            setErrorMessage('');
        }
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        setIsProcessing(true);
        setErrorMessage('');

        if (!stripe || !elements) {
            setErrorMessage('Stripe.js has not loaded yet.');
            setIsProcessing(false);
            return;
        }

        try {
            // Create PaymentIntent on the server
            const response = await fetch('/api/payment/create-payment-intent', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ name: customerName, email }),
            });

            const responseData = await response.json();

            if (!response.ok) {
                throw new Error(responseData.error || 'Failed to create PaymentIntent.');
            }

            const clientSecret = responseData.clientSecret;

            if (!clientSecret) {
                throw new Error('Did not receive clientSecret from backend.');
            }

            // Confirm the card payment
            const { error, paymentIntent } = await stripe.confirmCardPayment(
                clientSecret,
                {
                    payment_method: {
                        card: elements.getElement(CardElement),
                        billing_details: {
                            name: customerName,
                            email,
                        },
                    },
                }
            );

            if (error) {
                setErrorMessage(error.message);
            } else if (paymentIntent && paymentIntent.status === 'succeeded') {
                // Redirect to success page
                navigate('/success');
            } else {
                setErrorMessage('Payment failed. Please try again.');
            }
        } catch (error) {
            setErrorMessage(error.message);
        } finally {
            setIsProcessing(false);
        }
    };

    return (
        <Container maxWidth="sm">
            <Card elevation={3} sx={{ mt: 5 }}>
                <CardHeader
                    title="Make a Payment"
                    subheader="Use the form below to make a test payment."
                    titleTypographyProps={{ align: 'center' }}
                    subheaderTypographyProps={{ align: 'center' }}
                    sx={{ backgroundColor: (theme) => theme.palette.grey[200] }}
                />
                <CardContent>
                    <form onSubmit={handleSubmit}>
                        <Stack spacing={2}>
                            <TextField
                                label="Name"
                                variant="outlined"
                                fullWidth
                                value={customerName}
                                onChange={(e) => setCustomerName(e.target.value)}
                                required
                            />
                            <TextField
                                label="Email"
                                variant="outlined"
                                type="email"
                                fullWidth
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                required
                            />
                            <Box>
                                <CardElement
                                    options={CARD_ELEMENT_OPTIONS}
                                    onChange={handleCardChange}
                                />
                            </Box>
                            <Button
                                variant="contained"
                                color="primary"
                                type="submit"
                                fullWidth
                                disabled={!stripe || isProcessing}
                                endIcon={
                                    isProcessing ? (
                                        <CircularProgress size={20} color="inherit" />
                                    ) : (
                                        <SendIcon />
                                    )
                                }
                            >
                                {isProcessing ? 'Processing...' : 'Pay $50.00'}
                            </Button>
                        </Stack>
                    </form>
                    {errorMessage && (
                        <Grow in>
                            <Box mt={2}>
                                <Alert severity="error">{errorMessage}</Alert>
                            </Box>
                        </Grow>
                    )}
                </CardContent>
            </Card>
        </Container>
    );
};

export default PaymentForm;
