package com.restaurant.restaurantorder;

public class Contants {


 //these are the url's for accessing web services i.e api's to get and send data to database
 private static final String ROOT_URL = "http://ktldb.com/restaurantOrder/";
 //send registration details to database to create new user
 public static final String URL_REGISTER = ROOT_URL + "registration.php";
 //send order details to database like take and dine in details.
 public static final String URL_PLACE_ORDER = ROOT_URL + "placeorder.php";
 //verify login credentials from database
 public static final String URL_LOGIN =ROOT_URL+"login.php";
 //get admin firebase registration token from database in order to notify admin.
 public static final String URL_GET_ADMIN_TOKEN = ROOT_URL + "get_admin_token.php";
}

