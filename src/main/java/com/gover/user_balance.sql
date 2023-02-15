DROP TABLE IF EXISTS ec_userInfo;
CREATE TABLE IF NOT EXISTS ec_userInfo(
      id           INT PRIMARY KEY AUTO_INCREMENT,
      user_id      INT          NOT NULL COMMENT '用户id',
      user_name    VARCHAR(255) NOT NULL COMMENT '用户名',
      user_allBalance DECIMAL(10,2) DEFAULT 0.00 COMMENT '用户钱包余额'
);

DROP TABLE IF EXISTS ec_balanceInfo;
CREATE TABLE IF NOT EXISTS ec_balanceInfo(
     id INT PRIMARY KEY ,
     user_id INT NOT NULL ,
     balance_id INT NOT NULL COMMENT '账单号',
     balance_bail DECIMAL(10,2) NOT NULL COMMENT '账单',
     balance_typeId INT NOT NULL COMMENT '账单类型',
     balance_createTime TIMESTAMP NOT NULL COMMENT '余额变动时间'
);

DROP TABLE IF EXISTS ec_balanceType;
CREATE TABLE IF NOT EXISTS ec_balanceType(
     id INT PRIMARY KEY,
     balance_typeId INT NOT NULL COMMENT '类型编号',
     balance_type VARCHAR(10) NOT NULL COMMENT '1-4:充值/消费/退款/提现'
);