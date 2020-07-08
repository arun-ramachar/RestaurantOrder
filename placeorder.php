    <?php
    require "config.php";
   
    $response = array();
    $userid=$_POST["id"];
    $username = $_POST["username"];
    $orderdetails = $_POST["order"];
    $status = $_POST["status"];


     /*  $sql = mysqli_query($con,"select username from tbl_users where `username`='$username'");
        $count = mysqli_num_rows($sql);
           if($count) {
              // echo "already";
                $response['error'] = true;
		$response['message'] = "username already in use";
               } else{*/
                  $sql_query="INSERT INTO `rest_tbl_orders`( `username`, `userId`, `orderDetails`, `status`) VALUES ('$username','$userid','$orderdetails','$status')";
                  if(mysqli_query($con,$sql_query)){
                      //  echo "pass";
                        $response['error'] = false;
			                  $response['message'] = " Order Placed Successfully";
                    } else {
                     $response['error'] = true;
                     $response['message'] = "Error Occured while placing your order".mysqli_error($con);
                    //  echo "fail".mysqli_error($con);
                    }
              
              echo json_encode($response);
    ?>
