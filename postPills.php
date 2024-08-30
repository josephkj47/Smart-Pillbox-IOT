<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['userid'])&&isset($_POST['box1'])&&isset($_POST['box2'])&&isset($_POST['box3'])&&isset($_POST['box4'])) {
    if ($db->dbConnect()) {
        $res = $db->addpillData("box_det", $_POST['userid'],$_POST['box1'],$_POST['box2'],$_POST['box3'],$_POST['box4']);
        if ($res == false) {
            echo "Failed";
        } else {
            echo $res;
        }
    } else echo "Failed";
} else echo "Failed";
?>
