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
    
    <br/><br/><br/>
    <div style="float:right;margin-right:25px;"><a href="put.php"><button class="btn" type="button"><i class="icon-plus-sign"></i> Adauga un eveniment nou</button></a></div>
    <br/>
    <?php
    	$q = mysql_query("SELECT event_id, event_name, event_summary_description, event_date, DATE_FORMAT(event_time, '%H:%i') AS event_time, event_location_name FROM events_list ORDER BY event_date DESC");
    	while($e = mysql_fetch_assoc($q))
    		$output[] = $e;
    ?>
    
    <table class="table table-striped">
    	<thead>
  		<tr>
  			<th>Name</th>
               <th>Summary description</th>
               <th>Date</th>
               <th>Time</th>
               <th>Location</th>
               <th></th>
          </tr>
     </thead>
     <tbody>
     <?php
    	if($output)
		foreach($output as $e) { ?>
          <tr>
               <td><?php print $e['event_name']; ?></td>
               <td><?php print $e['event_summary_description']; ?></td>
               <td><?php print $e['event_date']; ?></td>
               <td><?php print $e['event_time']; ?></td>
               <td><?php print $e['event_location_name']; ?></td>
               <td><a href="put.php?eid=<?php print $e['event_id']; ?>"><button class="btn" type="button"><i class="icon-edit"></i> Editeaza</button></a></td>
               <td><a href="delete.php?eid=<?php print $e['event_id']; ?>"><button class="btn" type="button"><i class="icon-remove-sign"></i> Sterge</button></a></td>
          </tr>
     <?php } ?>
     </tbody>
    </table>

    <script src="http://code.jquery.com/jquery-latest.js"></script>
    <script src="js/bootstrap.min.js"></script>
  </body>
</html>
<?php
  mysql_close();
?>