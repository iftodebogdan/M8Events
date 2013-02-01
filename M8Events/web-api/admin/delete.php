<?php
  include 'auth.inc';
  auth('password'); // Set the password here

  mysql_connect("host","user","password");
  mysql_select_db("database");
?>
<!DOCTYPE html>
<html>
  <head>
    <title>M8 Events admin</title>
    <!-- Bootstrap -->
    <link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
  </head>
  <body>
    <div class="navbar navbar-inverse navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container">
          <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </a>
          <a class="brand" href="index.php">M8 Events admin</a>
          <div class="nav-collapse collapse">
            <ul class="nav">
            </ul>
          </div><!--/.nav-collapse -->
        </div>
      </div>
    </div>
    <br/><br/><br/><br/>
    <?php
    	if($_GET['eid'])
    	{
    		$q = mysql_query("SELECT event_name, event_summary_description, event_date, event_time, event_location_name FROM events_list WHERE event_id=".$_GET['eid']);
    		if($q)
    			$e = mysql_fetch_assoc($q);
    		if(!$e)
    			print '<div class="alert alert-error">
    					Nu exista un eveniment cu IDul '.$_GET['eid'].'
    				  </div><br/>
    				  <a href="index.php">
					<button class="btn" type="button">
						<i class="icon-circle-arrow-left"></i> Inapoi
					</button>
				  </a>';
    		else
    			if(!$_GET['answer'])
	    			print '<div class="alert alert-error">
	            			Nume: '.$e['event_name'].'<br/>
	            			Descriere: '.$e['event_summary_description'].'<br/>
	            			Data: '.$e['event_date'].'<br/>
	            			Ora: '.$e['event_time'].'<br/>
	            			Locatia: '.$e['event_location_name'].'<br/><br/>
	    					Sunteti sigur ca vreti sa stergeti evenimentul?<br/>
	            			<a style="margin-right:25px" href="delete.php?eid='.$_GET['eid'].'&answer=yes">
							<button class="btn" type="button">
								<i class="icon-ok"></i> DA
							</button>
					  	</a>
	            			<a href="index.php">
							<button class="btn" type="button">
								<i class="icon-remove"></i> NU
							</button>
					  	</a>
	    				  </div>';
    			else
    			{
    				$q = mysql_query("DELETE FROM events_list WHERE event_id=".$_GET['eid']);
    				if($q)
    				{
    					unlink("../img/" . $_GET['eid'] . ".jpg");
	    				print '<div class="alert alert-success">Evenimentul a fost sters cu succes</div>';
    				}
    				else
    				{
    					print '<div class="alert alert-error">Evenimentul nu a putut fi sters<br/>';
    					print mysql_error();
    					print '</div>';
    				}
    				print '<a href="index.php">
						<button class="btn" type="button">
							<i class="icon-circle-arrow-left"></i> Inapoi
						</button>
					  </a>';
}
    	}
    ?>
    
    <script src="http://code.jquery.com/jquery-latest.js"></script>
    <script src="js/bootstrap.min.js"></script>
  </body>
</html>
<?php
  mysql_close();
?>