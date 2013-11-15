<?php
	//phpinfo();
	//include_once('lib/KMySQL.php');
	include_once('config.php');
	//$db = KMySQL::getInstance($db_host,$db_user,$db_pass,$db_database);
	//$db->query_directly_withoutResult('insert into survey(result) values({\"1\":\"1\"})');
	//$db->close_conn();
	$con = mysql_connect($db_host,$db_user,$db_pass);
	if(!$con)
	{
	    die('Could not connect: ' . mysql_error());
	}
	@mysql_select_db($db_database,$con) or die('select db fail !');
	mysql_query('INSERT INTO survey(result) values("'.addslashes($_POST['r']).'")');
	mysql_close($con);
	echo 'done';
?>