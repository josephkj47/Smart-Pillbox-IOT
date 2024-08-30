<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['phone']) && isset($_POST['schedules'])) {
    if ($db->dbConnect()) {
        if ($db->addSch("schedule", $_POST['phone'], $_POST['schedules'])) {
            echo "Done";
        } else echo "Username or Password wrong";
    } else echo "Error: Database connection";
} else echo "All fields are required";
?>
