<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['fname']) && isset($_POST['lname']) && isset($_POST['email']) && isset($_POST['phone']) && isset($_POST['passwd']) && isset($_POST['deviceID'])) {
    if ($db->dbConnect()) {
        if ($db->signUp("users", $_POST['fname'], $_POST['lname'], $_POST['email'], $_POST['phone'], $_POST['passwd'], $_POST['deviceID'])) {
            echo "Sign Up Success";
        } else echo "Sign up Failed";
    } else echo "Error: Database connection";
} else echo "All fields are required";
?>
