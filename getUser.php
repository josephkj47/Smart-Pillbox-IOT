<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['phone'])) {
    if ($db->dbConnect()) {
        $res = $db->getUserData("schedule", $_POST['phone']);
        if ($res == false) {
            echo "Failed";
        } else {
            echo $res;
        }
    } else echo "Failed";
} else echo "Failed";
?>
