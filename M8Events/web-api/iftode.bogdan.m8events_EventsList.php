<?php
	mysql_connect("host","user","pass");
	mysql_select_db("database");
	
	$q = mysql_query("SELECT event_id, event_name, event_summary_description FROM events_list WHERE event_date >= DATE(NOW()) ORDER BY event_date ASC");
	while($e = mysql_fetch_assoc($q))
		$output[] = $e;
	
	print "<EventsList>\n";
	if($output)
		foreach($output as $e) {
			print "  <Event>\n";
			print "    <EventID>".$e['event_id']."</EventID>\n";
			print "    <Name>".$e['event_name']."</Name>\n";
			print "    <Desc>".$e['event_summary_description']."</Desc>\n";
			print "  </Event>\n";
		}
	print '</EventsList>';
	
	mysql_close();
?>