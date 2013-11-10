SELECT 	perf_track.date,
	perf_track.alias,
	'FINAL',
	perf_track.value,
	perf_track.frequency
FROM security_trackrawdata perf_track
WHERE 	perf_track.frequency = 'MONTHLY'
	AND perf_track.type='MPERF'
	AND perf_track.alias = ?
	AND perf_track.date BETWEEN ? AND ?
ORDER BY perf_track.date