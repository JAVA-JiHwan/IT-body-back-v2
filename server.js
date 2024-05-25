const express = require('express');
const mysql = require('mysql');
const multer = require('multer');
const path = require('path');
const cors = require('cors');

const app = express();

// Middleware
app.use(cors());
app.use(express.json());
app.use('/uploads', express.static(path.join(__dirname, 'uploads')));

// MySQL database connection
const db = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: 'admin1234',
    database: 'it-body'
});

db.connect((err) => {
    if (err) {
        throw err;
    }
    console.log('MySQL Connected...');
});

// Multer configuration for file uploads
const storage = multer.diskStorage({
    destination: (req, file, cb) => {
        cb(null, path.join(__dirname, 'uploads'));
    },
    filename: (req, file, cb) => {
        cb(null, Date.now() + path.extname(file.originalname));
    }
});

const upload = multer({storage});

// API to handle user registration
app.post('/login/joinProc', upload.single('photo'), (req, res) => {
    const {email, password, nickname, phone, gender, healthStatus, membershipGrade} = req.body;
    const image = req.file ? req.file.path : null;

    const query = 'INSERT INTO members (email, password, name, selphone, gender, health_status, Membership_Grade, photo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)';
    db.query(query, [email, password, nickname, phone, gender, healthStatus, membershipGrade, image], (err, result) => {
        if (err) {
            console.error(err);
            return res.status(500).send('Server error');
        }
        res.status(201).send('User registered successfully');
    });
});

// API to fetch user information
app.get('/api/users', (req, res) => {
    const query = 'SELECT * FROM members';
    db.query(query, (err, results) => {
        if (err) {
            console.error(err);
            return res.status(500).send('Server error');
        }
        res.status(200).json(results);
    });
});

const PORT = process.env.PORT || 5000;
app.listen(PORT, () => {
    console.log(`Server running on port ${PORT}`);
});
