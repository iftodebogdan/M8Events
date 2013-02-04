<?php
	mysql_connect("host","user","pass");
	mysql_select_db("database");
	
	if(!$_GET['eid'])
	{
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
		print "</EventsList>";
	}
	else
	{
		$q = mysql_query("SELECT event_name, event_summary_description, event_detailed_description, event_date, event_time, event_location_name, event_location_coords FROM events_list WHERE event_id=".$_GET['eid']);
		while($e = mysql_fetch_assoc($q))
			$output[] = $e;
		
		print "<Event>\n";
		if($output)
			foreach($output as $e) {
			print "  <Name>".$e['event_name']."</Name>\n";
			print "  <SumDesc>".$e['event_summary_description']."</SumDesc>\n";
			print "  <DetDesc>".$e['event_detailed_description']."</DetDesc>\n";
			print "  <Date>".$e['event_date']."</Date>\n";
			print "  <Time>".$e['event_time']."</Time>\n";
			print "  <LocName>".$e['event_location_name']."</LocName>\n";
			print "  <LocCoords>".$e['event_location_coords']."</LocCoords>\n";
		}
		print "</Event>";
	}
	
	mysql_close();
?>