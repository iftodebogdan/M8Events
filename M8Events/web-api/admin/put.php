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
    		$q = mysql_query("SELECT *,DATE_FORMAT(event_time, '%H:%i') AS event_time FROM events_list WHERE event_id=".$_GET['eid']);
    		if($q)
    			$e = mysql_fetch_assoc($q);
    	}
    ?>
    
    <?php
    	if($_POST['event_name'] &&
	   $_POST['event_summary_description'] &&
	   $_POST['event_detailed_description'] &&
	   $_POST['event_date'] &&
	   $_POST['event_time'] &&
	   $_POST['event_location_name'] &&
	   $_POST['event_location_coords'])
	{
		if($_POST['event_id'])
			$query = 	"UPDATE events_list SET event_name='".
					str_replace("'", "\'", $_POST['event_name']).
	
					"',event_summary_description='".
					str_replace("'", "\'", $_POST['event_summary_description']).
	
					"',event_detailed_description='".
					str_replace("'", "\'", $_POST['event_detailed_description']).
	
					"',event_date=".
					str_replace("-", "", $_POST['event_date']).
	
					",event_time=".
					str_replace(":", "", $_POST['event_time'])."00".
	
					",event_location_name='".
					str_replace("'", "\'", $_POST['event_location_name']).
	
					"',event_location_coords='".
					str_replace("'", "\'", $_POST['event_location_coords']).
	
					"' WHERE event_id=".
					str_replace("'", "\'", $_POST['event_id']);
		else
			$query = 	"INSERT INTO events_list VALUES (NULL,'".
					str_replace("'", "\'", $_POST['event_name']).
			
					"','".
					str_replace("'", "\'", $_POST['event_summary_description']).
			
					"','".
					str_replace("'", "\'", $_POST['event_detailed_description']).
			
					"',".
					str_replace("-", "", $_POST['event_date']).
			
					",".
					str_replace(":", "", $_POST['event_time'])."00".
			
					",'".
					str_replace("'", "\'", $_POST['event_location_name']).
			
					"','".
					str_replace("'", "\'", $_POST['event_location_coords']).
		
					"')";
		
		$q = mysql_query($query);
		
		$ok = 0;
		
		if($q)
		{
			print '<div class="alert alert-success">Evenimentul a fost ';
			if($_POST['event_id'])
				print 'editat';
			else
				print 'adaugat';
			print ' cu succes!</div>';
			
			$allowedExts = array("jpg", "jpeg");
			$extension = end(explode(".", $_FILES["file"]["name"]));
			if ((	($_FILES["file"]["type"] == "image/jpeg")
					|| ($_FILES["file"]["type"] == "image/pjpeg"))
					&& ($_FILES["file"]["size"] < 256000)
					&& in_array($extension, $allowedExts))
			{
				if ($_FILES["file"]["error"] > 0)
				{
					print '<div class="alert alert-error">';
					echo "Cod de retur: " . $_FILES["file"]["error"] . "<br>";
					print '</div>';
				}
				else
				{
					print '<div class="alert alert-success">';
					echo "Fisier incarcat: " . $_FILES["file"]["name"] . "<br>";
					echo "Tip: " . $_FILES["file"]["type"] . "<br>";
					echo "Marime: " . ($_FILES["file"]["size"] / 1024) . " kB<br>";
					echo "Fisier temporar: " . $_FILES["file"]["tmp_name"] . "<br>";
				
					move_uploaded_file($_FILES["file"]["tmp_name"],
					"../img/" . ($_POST['event_id'] ? $_POST['event_id'] : mysql_insert_id()) . ".jpg");
					echo "Salvat in: " . "../img/" . ($_POST['event_id'] ? $_POST['event_id'] : mysql_insert_id()) . ".jpg";
					print '</div>';
					$ok = 1;
				}
			}
			else
				if($_FILES["file"]["size"] > 0)
				{
					print '<div class="alert alert-error">';
					echo "Fisierul este invalid. Imaginea trebuie sa fie in format JPEG si sa aiba sub 256kB.";
					print '</div>';
				}
				else
				{
					print '<div class="alert alert-warning">';
					echo "Nu a fost incarcata nicio imagine.";
					if($_POST['event_id'])
						print " Daca ati incarcat deja o imagine, aceasta a ramas nemodificata.";
					print '</div>';
					$ok = 1;
				}
		}
		else
		{
			print '<div class="alert alert-error">Evenimentul nu a putut fi ';
			if($_POST['event_id'])
				print 'editat <br/>';
			else
				print 'adaugat <br/>';
			print mysql_error();
			print '</div>';
		}
		
		if($q && $ok)
			print '<a href="index.php">
					<button class="btn" type="button">
						<i class="icon-circle-arrow-left"></i> Inapoi
					</button>
				  </a>';
		else
			print '<a href="put.php?eid='.$_POST['event_id'].'">
					<button class="btn" type="button">
						<i class="icon-circle-arrow-left"></i> Inapoi
					</button>
				  </a>';
	}
	else {
    ?>
    
    <form class="form-horizontal" action="put.php" method="post" enctype="multipart/form-data">
	  <input type="hidden" name="event_id" value="<?php print $e['event_id']; ?>">
	  <div class="control-group">
	    <label class="control-label">Nume eveniment<br/>(max. 64 char)</label>
	    <div class="controls">
	      <input type="text" name="event_name" maxlength="64" value="<?php print $e['event_name']; ?>">
	    </div>
	  </div>
	  <div class="control-group">
	    <label class="control-label">Descriere sumara<br/>(max 128 char)</label>
	    <div class="controls">
	      <input type="text" name="event_summary_description" maxlength="128" value="<?php print $e['event_summary_description']; ?>">
	    </div>
	  </div>
	  <div class="control-group">
	    <label class="control-label">Descriere detaliata</label>
	    <div class="controls">
	      <textarea rows="3" name="event_detailed_description"><?php print $e['event_detailed_description']; ?></textarea>
	    </div>
	  </div>
	  <div class="control-group">
	    <label class="control-label">Data evenimentului<br/>(yyyy-mm-dd)</label>
	    <div class="controls">
	      <input type="text" name="event_date" value="<?php print $e['event_date']; ?>">
	    </div>
	  </div>
	  <div class="control-group">
	    <label class="control-label">Ora evenimentului<br/>(hh:mm)</label>
	    <div class="controls">
	      <input type="text" name="event_time" value="<?php print $e['event_time']; ?>">
	    </div>
	  </div>
	  <div class="control-group">
	    <label class="control-label">Nume locatie<br/>(max. 64 char)</label>
	    <div class="controls">
	      <input type="text" name="event_location_name" maxlength="64" value="<?php print $e['event_location_name']; ?>">
	    </div>
	  </div>
	  <div class="control-group">
	    <label class="control-label">Coordonate/adresa locatie<br/>(max. 128 char)</label>
	    <div class="controls">
	      <input type="text" name="event_location_coords" maxlength="128" value="<?php print $e['event_location_coords']; ?>">
	    </div>
	  </div>
	  <div class="control-group">
	    <label class="control-label">Poster/imagine eveniment</label>
	    <div class="controls">
	      <input type="file" name="file" id="file">
	    </div>
	  </div>
	  <div class="control-group">
	    <div class="controls">
	      <button type="submit" class="btn"><?php if($e) print "Editeaza"; else print "Adauga"; ?></button>
	    </div>
	  </div>
	</form>
	<?php } ?>

    <script src="http://code.jquery.com/jquery-latest.js"></script>
    <script src="js/bootstrap.min.js"></script>
  </body>
</html>
<?php
  mysql_close();
?>