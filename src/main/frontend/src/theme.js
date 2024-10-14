// src/theme.js
import { createTheme } from '@mui/material/styles';

const theme = createTheme({
    palette: {
        primary: {
            main: '#6200ea', // A vibrant purple
        },
        secondary: {
            main: '#03dac6', // A complementary teal
        },
    },
    typography: {
        fontFamily: 'Roboto, Arial',
        h4: {
            fontWeight: 700,
        },
        subtitle1: {
            fontStyle: 'italic',
        },
    },
});

export default theme;