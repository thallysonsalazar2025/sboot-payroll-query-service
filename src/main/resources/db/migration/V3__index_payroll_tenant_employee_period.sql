CREATE INDEX IF NOT EXISTS ix_payroll_tenant_employee_period
    ON payroll(company_id, employee_id, payroll_date);
