<?php
require "DataBaseConfig.php";

class DataBase
{
    public $connect;
    public $data;
    private $sql;
    protected $servername;
    protected $username;
    protected $password;
    protected $databasename;

    public function __construct()
    {
        $this->connect = null;
        $this->data = null;
        $this->sql = null;
        $dbc = new DataBaseConfig();
        $this->servername = $dbc->servername;
        $this->username = $dbc->username;
        $this->password = $dbc->password;
        $this->databasename = $dbc->databasename;
    }

    function dbConnect()
    {
        $this->connect = mysqli_connect($this->servername, $this->username, $this->password, $this->databasename);
        return $this->connect;
    }

    function prepareData($data)
    {
        return mysqli_real_escape_string($this->connect, stripslashes(htmlspecialchars($data)));
    }

    function logIn($table, $username, $password)
    {
        $username = $this->prepareData($username);
        $password = $this->prepareData($password);
        $this->sql = "select * from " . $table . " where phone = '" . $username . "'";
        $result = mysqli_query($this->connect, $this->sql);
        $row = mysqli_fetch_assoc($result);
        if (mysqli_num_rows($result) != 0) {
            $dbusername = $row['phone'];
            $dbpassword = $row['passwd'];
            $curpass = md5($password);
            if ($dbusername == $username && $dbpassword == $curpass) {
                $login = true;
            } else $login = false;
        } else $login = false;

        return $login;
    }

    function addSch($table, $phone, $schedules)
    {
        $phone = $this->prepareData($phone);
        $schedules = $this->prepareData($schedules);
        $this->sql =  "REPLACE INTO " . $table . " (phone, schedules) VALUES ('" . $phone . "','". $schedules ."')";
        if (mysqli_query($this->connect, $this->sql)) {
            return true;
        } else return false;
    }

    function getUserData($table, $phone)
    {
        $phone = $this->prepareData($phone);
        $this->sql = "select * from " . $table . " where phone = '" . $phone . "'";
        $result = mysqli_query($this->connect, $this->sql);
        $row = mysqli_fetch_assoc($result);
        if (mysqli_num_rows($result) != 0) {
                return $row['schedules'];
        } else
            return false;
    }

    function signUp($table, $fname, $lname, $email, $phone, $passwd, $deviceID)
    {
        $fname = $this->prepareData($fname);
        $lname = $this->prepareData($lname);
        $email = $this->prepareData($email);
        $phone = $this->prepareData($phone);
        $password = $this->prepareData($passwd);
        $password = md5($password);
        $deviceID = $this->prepareData($deviceID);
        
        $this->sql =
            "INSERT INTO " . $table . " (fname, lname, email, phone, passwd, deviceID) VALUES ('" . $fname . "','" . $lname . "','" . $email . "','" . $phone ."','" . $password . "','" . $deviceID ."')";
        if (mysqli_query($this->connect, $this->sql)) {
            return true;
        } else return false;
    }
    function getpillData($table, $userid)
    {
        $userid = $this->prepareData($userid);
        $this->sql = "select * from " . $table . " where userid = '" . $userid . "'";
        $result = mysqli_query($this->connect, $this->sql);
        $row = mysqli_fetch_assoc($result);
        if (mysqli_num_rows($result) != 0) {
            return $row['box1'] . ":" . $row['box2'] . ":" . $row['box3'] . ":" . $row['box4'];
        } else
            return false;
    }
    function addpillData($table, $userid, $box1, $box2, $box3, $box4)
    {
        $userid = $this->prepareData($userid);
        $box1 = $this->prepareData($box1);
        $box2 = $this->prepareData($box2);
        $box3 = $this->prepareData($box3);
        $box4 = $this->prepareData($box4);
        
        $this->sql =
            "REPLACE INTO " . $table . " (userid, box1, box2, box3, box4) VALUES ('" . $userid . "','" . $box1 . "','" . $box2 ."','" . $box3 . "','" . $box4 ."')";
        if (mysqli_query($this->connect, $this->sql)) {
            return true;
        } else return false;
    }

}

?>
