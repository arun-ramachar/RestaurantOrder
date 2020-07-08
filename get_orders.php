<?php 
//importing required files 

require_once 'config.php';
$response = array(); 

if($_SERVER['REQUEST_METHOD']=='GET'){	
	
 
	



	
		//$con=mysqli_connect("localhost","root","","app_ktldb");
                $mysql_queery = "SELECT * FROM rest_tbl_orders ";
                
                $select_message = mysqli_query($con,$mysql_queery);

               $count = mysqli_num_rows($select_message );

               
  if($count>0)
{
 $temp_array = array();
                while($row=mysqli_fetch_assoc($select_message)){

                 $temp_array[] = $row;

                }

           echo json_encode(array("error"=>false,"message" =>$temp_array));

}
else
{
$response['error']=true;
	$response['message']='No Data';
}
	

}else{
	$response['error']=true;
	$response['message']='Invalid request';
}

//echo json_encode($response);