-- OK for: CH, UK
SELECT portfoliocode,
isocurrency
FROM portfolio
WHERE portfolioseedid = ?
