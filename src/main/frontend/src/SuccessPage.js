// src/SuccessPage.js
import React from 'react';
import { Container, Typography, Box, Button } from '@mui/material';
import { Link as RouterLink } from 'react-router-dom';

const SuccessPage = () => {
    return (
        <Container maxWidth="sm">
            <Box mt={5} p={3} textAlign="center">
                <Typography variant="h4" gutterBottom>
                    Payment Successful!
                </Typography>
                <Typography variant="body1" gutterBottom>
                    Thank you for your payment.
                </Typography>
                <Button
                    variant="contained"
                    color="primary"
                    component={RouterLink}
                    to="/"
                    sx={{ mt: 3 }}
                >
                    Make Another Payment
                </Button>
            </Box>
        </Container>
    );
};

export default SuccessPage;
