CREATE TABLE
  customers ( customerName STRING(MAX),
    customerPan STRING(MAX),
    customerAddress STRING(MAX),
    customerDOB DATE,
    customerGender STRING(MAX),
    monthlyIncome FLOAT64,
    )
PRIMARY KEY
  (customerPan);