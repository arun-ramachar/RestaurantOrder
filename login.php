  <?php
  require "config.php";
  $username=$_POST["username"];
  $password=$_POST["password"];

  $response = array();
                    

      $sql=mysqli_query($con,"SELECT `id`, `username`,`token`,`role` FROM `tbl_users` WHERE `username`='$username' AND `password`='$password'");
      $count = mysqli_num_rows($sql);

        if($count >0) {
            $result = array();
            while($row=mysqli_fetch_assoc($sql)){
            $result['data'] = $row;

            $response['error'] = false;

            $response['id'] = $row['id'];;
            $response['username'] = $row['username'];
            $response['token'] = $row['token'];
$response['role'] = $row['role'];

 


     // $count_r = mysqli_num_rows($sql);

         }
        }else {
          $response['error'] = true;
          $response['message'] = "Invalid username or password";
        }
          
        echo json_encode($response);
?>
