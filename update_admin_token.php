    <?php
    require "config.php";
   
    $response = array();
    
    $token = $_POST["token"];


     /*  $sql = mysqli_query($con,"select username from tbl_users where `username`='$username'");
        $count = mysqli_num_rows($sql);
           if($count) {
              // echo "already";
                $response['error'] = true;
		$response['message'] = "username already in use";
               } else{*/
                  $sql_query=" UPDATE   `tbl_users` SET token='$token' WHERE role='admin' ";
                  if(mysqli_query($con,$sql_query)){
                      //  echo "pass";
                        $response['error'] = false;
			                  $response['message'] = "Success";
                    } else {
                     $response['error'] = true;
                     $response['message'] = "Error Occured".mysqli_error($con);
                    //  echo "fail".mysqli_error($con);
                    }
              
              echo json_encode($response);
    ?>
