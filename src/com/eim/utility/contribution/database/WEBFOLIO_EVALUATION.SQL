-- OK for CH, UK, PT
SELECT pa.portfolio AS portfolio_id, -- Portfolio ID
p.portfoliocode, -- Portfolio code
p.isocurrency, -- Portfolio currency
pa.padate AS eval_date, -- Evaluation date
pa.internalcode AS eval_status, -- Evaluation status
pa.calculationdate AS eval_calculation_date, -- Evaluation calculation date
pa.orgarrest AS ref_eval_id, -- Reference evaluation ID
pa_ref.padate AS ref_eval_date, -- Reference evaluation date
pa_ref.aumnet AS ref_eval_aumnet, -- Reference evaluation net AUM
pa_ref.calculationdate AS ref_eval_calculation_date, -- Reference evaluation calculation date
cw.contributions AS eval_contributions, -- Contributions
cw.withdrawals AS eval_withdrawals -- Withdrawals
FROM portfolio p,
portfolioarrest pa,
portfolioarrest pa_ref,
(
    SELECT ia.objectkey, -- Portfolio ID
    ia.contributions, -- Contributions
    ia.withdrawals -- Withdrawals
    FROM indicatorarrest ia
    WHERE ia.portfolioarrest = ?
    -- Specific indicatorarrest (portfolio having intermediary = 0)
    AND ia.objectclassid = 1791956682
    AND ia.intermediary = 0
) cw
-- Joins
WHERE pa_ref.paseedid = pa.orgarrest
AND cw.objectkey(+) = pa.portfolio
AND p.portfolioseedid = pa.portfolio
-- Official or estimated
AND pa.internalcode IN ('EOM', 'EOD')
AND pa.paseedid = ?
