SELECT perf_track.arrest_date,
       perf_track.portfolio_id,
       perf_track.arrest_status,
       perf_track.arrest_mtd,
       'MONTHLY'
FROM security_portfolioarrests perf_track
WHERE CAST(perf_track.portfolio_id AS varchar) = ?
ORDER BY perf_track.arrest_date