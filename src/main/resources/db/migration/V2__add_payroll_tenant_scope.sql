ALTER TABLE payroll ADD COLUMN IF NOT EXISTS company_id VARCHAR(100);

CREATE INDEX IF NOT EXISTS ix_payroll_tenant ON payroll(company_id);
