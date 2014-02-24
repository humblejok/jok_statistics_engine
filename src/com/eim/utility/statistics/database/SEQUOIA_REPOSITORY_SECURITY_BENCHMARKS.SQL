SELECT 	distinct perf_track.alias as bench_label,
	perf_track.alias as bench_id,
	CASE WHEN perf_track.alias='BERNARDOS FUND' THEN 'true' ELSE 'false' END AS bench_rfr,
	?                 AS bench_fake
FROM security_trackrawdata perf_track
WHERE 	perf_track.alias in ('SP500')