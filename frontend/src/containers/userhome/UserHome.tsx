import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {logout} from "../../components/LogOut.tsx";

function UserHome() {
    const navigate = useNavigate();
    const [name, setName] = useState("");
    const [medals, setMedals] = useState("");

    const handleChangeSettings = () => {
        navigate("/user-home/settings")
    }
    const handleLogout = () => {
        logout();
        navigate("/login");
    };

    useEffect(() => {
        const fetchData = async () => {
            const token = localStorage.getItem("jwt");
            const config = {
                headers: {
                    Authorization: `Bearer ` + token,
                },
            };

            try {
                const response = await fetch("/api/user/get-name", config);
                console.log(response);
                const data = await response.text();
                console.log(data);
                setName(data);

                const response2 = await fetch("/api/user/get-medals", config);
                const data2 = await response2.text();
                console.log(data2);
                setMedals(data2);
            } catch (error) {
                console.error(error);
            }
        };

        fetchData();
    }, []);

    return (
        <div>
            <h1>Welcome, {name}</h1>
            <span>Medals: {medals}</span>
            <button onClick={handleChangeSettings}>Settings</button>
            <button onClick={handleLogout}>Logout</button>
        </div>
    );
}

export default UserHome;