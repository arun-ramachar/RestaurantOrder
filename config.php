	<?php
	$db_name="restaurantOrder";
	$mysql_user="root";
	$mysql_pass="";
	$server_name="localhost";

	$con=mysqli_connect($server_name,$mysql_user,$mysql_pass,$db_name);
		if($con){
		//	echo "<h3>Success</h3>";
		}else
		{
			die("Error in connection".mysqli_connect_error());
		//	echo "Error".mysqli_connect_error();
		}
	?>
