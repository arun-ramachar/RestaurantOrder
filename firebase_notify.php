<?php

if(isset($_GET['send_notification'])){
   send_notification ();
}

function send_notification()
{
	echo 'Hello';
define( 'API_ACCESS_KEY', 'AAAAp-EYKD8:APA91bHFXZY1jIVKf4SURCJl-NW_TPoSG2uw-65nGI-TwwXMYfYaotYqxTy05naHxcPM7O_FQdZVCm18jNF9ysHsQ6XemH2uw7q1yaXXXxi3rQLjlehUQKlPe9CMFVFRouVhoILkYf1_');
 //   $registrationIds = ;
#prep the bundle

     $msg = array
          (
		'body' 	=> $_REQUEST['message'],
		'title'	=> 'Restaurant',
		'click_action'=> 'com.example.yourapplication_YOUR_NOTIFICATION_NAME',
             	
          );
	$fields = array
			(
				'to'		=> $_REQUEST['token'],
				'notification'	=> $msg,
				'data'=>$data,
		
			);
	
	
	$headers = array
			(
				'Authorization: key=' . API_ACCESS_KEY,
				'Content-Type: application/json'
			);
#Send Reponse To FireBase Server	
		$ch = curl_init();
		curl_setopt( $ch,CURLOPT_URL, 'https://fcm.googleapis.com/fcm/send' );
		curl_setopt( $ch,CURLOPT_POST, true );
		curl_setopt( $ch,CURLOPT_HTTPHEADER, $headers );
		curl_setopt( $ch,CURLOPT_RETURNTRANSFER, true );
		curl_setopt( $ch,CURLOPT_SSL_VERIFYPEER, false );
		curl_setopt( $ch,CURLOPT_POSTFIELDS, json_encode( $fields ) );
		$result = curl_exec($ch );
		echo $result;
		curl_close( $ch );
}
?>