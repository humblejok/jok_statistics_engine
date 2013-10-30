-- OK for CH, UK, PT
-- Get sum of operations for given portfolio within time period
SELECT SUM(op_amount) AS total_op_amount,
security AS security_id
FROM (
    -- Redemptions
    SELECT (op.finalqty * op.finalprice) AS op_amount,
    op.security
    FROM operation op
    -- Redemption
    WHERE op.type_ = 'RED'
    -- Not cancelled
    AND op.status != 'CAN'
    -- Normal or No Capital
    AND op.subtype IN ('NO', 'NC')
    -- Given portfolio
    AND op.portfolio = ?
    -- Given time period
    AND (
        -- Effective date is between reference evaluation date and current evaluation date
        (
            op.effectivedate > ?
            AND op.effectivedate <= ?
        )
        -- Or effective date is before reference evaluation date and operation creation date is between reference evaluation date and current evaluation date
        OR
        (
            op.effectivedate <= ?
            AND op.creationdate > ?
            AND op.creationdate <= ?
        )
    )
    -- Not switch or equalization factor
    AND NOT EXISTS (
        SELECT 1
        FROM opgroup opg,
        opgroupline opgl
        WHERE opgl.opgroup = opg.groupseedid
        AND opgl.operation = op.operationseedid
        AND opg.type_ IN ('S', 'H')
    )
    -- Subscriptions
    UNION
    SELECT (-op.finalqty * op.finalprice) AS op_amount,
    op.security
    FROM operation op
    -- Subscription
    WHERE op.type_ = 'SUB'
    -- Not cancelled
    AND op.status != 'CAN'
    -- Normal or No Capital
    AND op.subtype IN ('NO', 'NC')
    -- Given portfolio
    AND op.portfolio = ?
    -- Given time period
    AND (
        -- Effective date is between reference evaluation date and current evaluation date
        (
            op.effectivedate > ?
            AND op.effectivedate <= ?
        )
        -- Or effective date is before reference evaluation date and operation creation date is between reference evaluation date and current evaluation date
        OR
        (
            op.effectivedate <= ?
            AND op.creationdate > ?
            AND op.creationdate <= ?
        )
    )
    -- Not switch or equalization factor
    AND NOT EXISTS (
        SELECT 1
        FROM opgroup opg,
        opgroupline opgl
        WHERE opgl.opgroup = opg.groupseedid
        AND opgl.operation = op.operationseedid
        AND opg.type_ IN ('S', 'H')
    )
)
GROUP BY security