SELECT evaluation_date AS EVALUATION_DATE,
  portfolio_code AS PORTFOLIO_CODE,
  type AS STATUS,
  aumgross AS AUM_GROSS,
  aumnetcorr AS AUM_NET,
  aumgross_usd AS AUM_GROSS_USD,
  aumnet_usd AS AUM_NET_USD
FROM wh_evaluations
WHERE portfolio_code= ?
AND type <> 'Init'
AND evaluation_date BETWEEN ? AND ?
ORDER BY evaluation_date ASC