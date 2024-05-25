import React, {useState} from 'react';
import axios from 'axios';

function App() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [nickname, setNickname] = useState('');
    const [phone, setPhone] = useState('');
    const [gender, setGender] = useState('');
    const [healthStatus, setHealthStatus] = useState('');
    const [membershipGrade, setMembershipGrade] = useState('');
    const [image, setImage] = useState(null);

    const handleImageChange = (e) => {
        setImage(e.target.files[0]);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const formData = new FormData();
        formData.append('email', email);
        formData.append('password', password);
        formData.append('nickname', nickname);
        formData.append('phone', phone);
        formData.append('gender', gender);
        formData.append('healthStatus', healthStatus);
        formData.append('membershipGrade', membershipGrade);
        formData.append('photo', image);

        try {
            await axios.post('http://localhost:5000/login/joinProc', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            });
            alert('User registered successfully!');
        } catch (err) {
            console.error(err);
            alert('Error registering user');
        }
    };

    return (
        <div className="App">
            <form onSubmit={handleSubmit}>
                <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} placeholder="Email"
                       required/>
                <input type="password" value={password} onChange={(e) => setPassword(e.target.value)}
                       placeholder="Password" required/>
                <input type="text" value={nickname} onChange={(e) => setNickname(e.target.value)} placeholder="Nickname"
                       required/>
                <input type="text" value={phone} onChange={(e) => setPhone(e.target.value)} placeholder="Phone"
                       required/>
                <input type="text" value={gender} onChange={(e) => setGender(e.target.value)} placeholder="Gender"
                       required/>
                <input type="text" value={healthStatus} onChange={(e) => setHealthStatus(e.target.value)}
                       placeholder="Health Status" required/>
                <input type="text" value={membershipGrade} onChange={(e) => setMembershipGrade(e.target.value)}
                       placeholder="Membership Grade" required/>
                <input type="file" onChange={handleImageChange} required/>
                <button type="submit">Register</button>
            </form>
        </div>
    );
}

export default App;
