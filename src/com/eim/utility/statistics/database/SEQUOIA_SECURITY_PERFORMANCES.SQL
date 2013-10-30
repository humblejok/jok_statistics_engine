SELECT perf_track.quote_date,
       perf_track.security_id,
       perf_track.quote_status,
       perf_track.quote_value,
       perf_track.quote_frequency
FROM security_securityperfs perf_track
WHERE CAST(perf_track.security_id AS varchar) = ?
ORDER BY perf_track.quote_date