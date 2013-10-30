SELECT *
FROM stats_contributions
-- Defined portfolio
WHERE portfolioid = ?
-- Defined evaluation date
AND applicationdate BETWEEN ? AND ?
