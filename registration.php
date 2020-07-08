    <?php
    require "config.php";
    //$id=1;//$_POST["name"];
    $response = array();
    $username = $_POST["username"];
    $email = $_POST["email"];
$role = $_POST["role"];
$token = $_POST["token"];
    $password = $_POST["password"];

       $sql = mysqli_query($con,"select username from tbl_users where `username`='$username'");
        $count = mysqli_num_rows($sql);
           if($count) {
              // echo "already";
                $response['error'] = true;
		$response['message'] = "username already in use";
               } else{
                  $sql_query="INSERT INTO `tbl_users`(`username`, `email`, `password`,`token`,`role`) VALUES ('$username','$email','$password','$token','$role')";
                  if(mysqli_query($con,$sql_query)){
                      //  echo "pass";
                        $response['error'] = false;
			                  $response['message'] = $_POST["username"]." registered successfully";
                    } else {
                    //  echo "fail".mysqli_error($con);
                    }
              }
              echo json_encode($response);
    ?>
