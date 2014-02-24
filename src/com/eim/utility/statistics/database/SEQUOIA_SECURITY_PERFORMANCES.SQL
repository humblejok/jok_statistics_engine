SELECT perf_track.quote_date,
       perf_track.security_id,
       perf_track.quote_status,
       perf_track.quote_value,
       perf_track.quote_frequency
FROM security_securityperfs perf_track, security_security ss
WHERE CAST(perf_track.security_id AS varchar) = ?
AND ss.security_id=perf_track.security_id
AND perf_track.quote_frequency = 'MONTHLY'
AND perf_track.quote_source = 'ADMINISTRATOR'
AND perf_track.quote_date>CASE WHEN ss.inception_date IS NULL THEN '1972-01-01' ELSE ss.inception_date END
ORDER BY perf_track.quote_date