SELECT perf_track.quote_date,
       perf_track.security_id,
       perf_track.quote_status,
       perf_track.quote_value,
       perf_track.quote_frequency
FROM security_securityperfs perf_track
WHERE CAST(perf_track.security_id as varchar) = ?
AND perf_track.quote_date BETWEEN ? AND ?
ORDER BY perf_track.quote_date