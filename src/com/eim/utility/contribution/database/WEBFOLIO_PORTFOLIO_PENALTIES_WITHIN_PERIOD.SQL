-- OK for CH, UK, PT
-- Select penalties
SELECT SUM(t.localamount) AS total_pen_amount,
o.security AS security_id
FROM transaction_ t,
operation o
-- Joins
WHERE o.operationseedid = t.operation
-- Redemption
AND o.type_ = 'RED'
-- Normal or No Capital
AND o.subtype = 'NO'
-- Not cancelled
AND o.status != 'CAN'
-- Penalty fees
AND t.type_ = 'FEES'
AND t.subtype IN ('PF', 'XFPNF')
-- Not cancelled
AND t.status != 'CAN'
-- Given portfolio
AND t.portfolio = ?
-- Given time period
--AND t.effectivedate > ?
--AND t.effectivedate <= ?
AND (
    -- Effective date is between reference evaluation date and current evaluation date
    (
        t.effectivedate > ?
        AND t.effectivedate <= ?
    )
    -- Or effective date is before reference evaluation date and operation creation date is between reference evaluation date and current evaluation date
    OR
    (
        t.effectivedate <= ?
        AND t.creationdate > ?
        AND t.creationdate <= ?
    )
)
GROUP BY o.security
