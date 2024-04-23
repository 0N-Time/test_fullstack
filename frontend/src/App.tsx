import './App.css'
import {Route, Routes} from "react-router-dom";
import HomePage from "./containers/homepage/HomePage.tsx";
import LoginPage from "./containers/login/LoginPage.tsx";
import RegisterPage from "./containers/register/RegisterPage.tsx";

function App() {

  return (
    <div className="App">
        <Routes>
            <Route path={"/"} element={<HomePage/>}/>

            <Route path={"/login"} element={<LoginPage/>}/>

            <Route path={"/register"} element={<RegisterPage/>}/>
        </Routes>
    </div>
  )
}

export default App
