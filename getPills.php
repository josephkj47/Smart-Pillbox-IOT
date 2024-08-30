<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['userid'])) {
    if ($db->dbConnect()) {
        $res = $db->getpillData("box_det", $_POST['userid']);
        if ($res == false) {
            echo "Failed";
        } else {
            echo $res;
        }
    } else echo "Failed";
} else echo "Failed";
?>
